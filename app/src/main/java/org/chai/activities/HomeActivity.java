package org.chai.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.androidquery.AQuery;
import com.astuetz.PagerSlidingTabStrip;
import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.model.Market;

import org.chai.R;
import org.chai.activities.tasks.TaskByLocationFragment;
import org.chai.activities.tasks.TaskCalenderFragment;
import org.chai.activities.tasks.TaskViewOnMapFragment;
import org.chai.model.MalariaDetailDao;
import org.chai.sync.CHAISynchroniser;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.MigrationHelper3;

import java.util.Locale;

/**
 * Created by victor on 10/15/14.
 */
public class HomeActivity extends BaseActivity{
    Toolbar toolbar;
    AQuery aq;

    String VERSION_PREFS = "version_preferences";
    String PLAYSTORE_VERSION = "play_store_version";
    String VERSION_LAST_CHECKED = "version_last_checked";

    ViewPager mViewPager;

    String[] titles = new String[]{"CALENDAR", "VIEW BY LOCATION", "MAP"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Redirect to update screen once an update is available
        try{
            if(getPlayStoreVersion() > getPackageManager().getPackageInfo(getPackageName(), 0).versionCode){
                Intent i = new Intent(HomeActivity.this, UpdateActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        }catch (Exception ex){
            Utils.log("Error getting package name -> " + ex.getMessage());
        }

        setContentView(R.layout.home_main_layout);

        aq = new AQuery(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter());

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        super.setUpDrawer(toolbar);

        if(!CHAISynchroniser.isSyncing && CHAISynchroniser.getLastSynced(this) == -1){ //Start service only when we've never and we are not currently syncing
            Utils.log("Service has never run before - starting it");
            startService(new Intent(this, CHAISynchroniser.class));
        }else{
            Utils.log("Cannot start service - has been started before");
        }

        //Manually Upgrade DB - NEVER leave this in production
        if(!Utils.dbTableExists(MalariaDetailDao.TABLENAME, new MyApplication().getDbOpenHelper().getWritableDatabase())){
            new MigrationHelper3().onUpgrade(new MyApplication().getDbOpenHelper().getWritableDatabase());
            Utils.log(MalariaDetailDao.TABLENAME + " does not exist");
        }else{
            Utils.log(MalariaDetailDao.TABLENAME + " already exists");
        }

        //Check version after every 6 hours
        if(getVersionLastChecked() == -1 || System.currentTimeMillis() - getVersionLastChecked() > (6 * 60 * 60 * 1000)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        checkPlayStoreVersion();
                    }catch (Exception ex){
                        Utils.log("Error getting permissions for checking version -> " + ex.getMessage());
                    }
                }
            }).start();
        }
    }

    private void checkPlayStoreVersion(){
        String android_id = getGtalkAndroidId();

        MarketSession session = new MarketSession();
        session.setAuthSubToken(updateToken(false));
        session.getContext().setAndroidId(android_id);
        session.setLocale(Locale.getDefault());

        String query = "pname:" + getPackageName();
        Market.AppsRequest appsRequest = Market.AppsRequest.newBuilder()
                .setQuery(query)
                .setStartIndex(0).setEntriesCount(10)
                .setWithExtendedInfo(true)
                .build();

        session.append(appsRequest, new MarketSession.Callback<Market.AppsResponse>() {
            @Override
            public void onResult(Market.ResponseContext context, Market.AppsResponse response) {
                try{
                    Utils.log("Got apps -> " + response.getAppCount());
                    for(Market.App app : response.getAppList()){
                        if(app.getPackageName().equalsIgnoreCase(getPackageName())){
                            savePlayStoreVersion(app.getVersionCode());
                            saveLastCheckedVersion(System.currentTimeMillis());
                        }
                        break;
                    }
                }catch (Exception ex){
                    Utils.log("Failed to get version from play store -> " + ex.getMessage());
                }
            }
        });
        session.flush();
    }

    public String getGtalkAndroidId() {
        Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
        String ID_KEY = "android_id";
        String params[] = {ID_KEY};
        Cursor c = this.getContentResolver().query(URI, null, null, params, null);
        if (!c.moveToFirst() || c.getColumnCount() < 2)
            return null;
        try {
            return Long.toHexString(Long.parseLong(c.getString(1)));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String updateToken(boolean invalidateToken) {
        Utils.log("Updating token");
        String authToken = "null";
        try {
            AccountManager am = AccountManager.get(this);
            Account[] accounts = am.getAccountsByType("com.google");
            AccountManagerFuture<Bundle> accountManagerFuture;
            accountManagerFuture = am.getAuthToken(accounts[0], "android", null, this, null, null);
            Bundle authTokenBundle = accountManagerFuture.getResult();
            authToken = authTokenBundle.getString(AccountManager.KEY_AUTHTOKEN).toString();
            if(invalidateToken) {
                am.invalidateAuthToken("com.google", authToken);
                authToken = updateToken(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authToken;
    }

    private void savePlayStoreVersion(int version){
        SharedPreferences.Editor editor = getSharedPreferences(VERSION_PREFS, 0).edit();
        editor.putInt(PLAYSTORE_VERSION, version);
        editor.commit();
    }

    private int getPlayStoreVersion(){
        SharedPreferences prefs = getSharedPreferences(VERSION_PREFS, 0);
        return prefs.getInt(PLAYSTORE_VERSION, -1);
    }

    private void saveLastCheckedVersion(long milis){
        SharedPreferences.Editor editor = getSharedPreferences(VERSION_PREFS, 0).edit();
        editor.putLong(VERSION_LAST_CHECKED, milis);
        editor.commit();
    }

    private long getVersionLastChecked(){
        SharedPreferences prefs = getSharedPreferences(VERSION_PREFS, 0);
        return prefs.getLong(VERSION_LAST_CHECKED, -1);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;

        public ViewPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle b = new Bundle();
            Fragment target = null;
            switch(position){
                case 0:
                    target = new TaskCalenderFragment();
                    break;
                case 1:
                    target = new TaskByLocationFragment();
                    break;
                case 2:
                    target = new TaskViewOnMapFragment();
                    break;
            }
            target.setArguments(b);
            return target;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}