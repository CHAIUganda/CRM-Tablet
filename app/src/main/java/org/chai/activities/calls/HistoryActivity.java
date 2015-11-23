package org.chai.activities.calls;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.astuetz.PagerSlidingTabStrip;

import org.chai.R;
import org.chai.activities.BaseActivity;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.User;
import org.chai.rest.RestClient;
import org.chai.util.MyApplication;
import org.chai.util.migration.UpgradeOpenHelper;

/**
 * Created by Zed on 4/23/2015.
 */
public class HistoryActivity extends BaseActivity {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;

    Toolbar toolbar;
    AQuery aq;

    ViewPager mViewPager;

    String[] titles;
    int PAGES = 0;
    int currentTab = -1;
    String customerId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_HISTORY;

        initialiseGreenDao();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        currentTab = getIntent().getIntExtra("tab", -1);
        customerId = getIntent().getStringExtra("customer_id");

        aq = new AQuery(this);

        if(RestClient.getRole().equalsIgnoreCase(User.ROLE_DETAILER)){
            titles = new String[]{"MALARIA FORMS", "DIARRHEA FORMS"};
            PAGES = 2;
        }else{
            titles = new String[]{"SALES FORMS", "UNSCHEDULED CALLS", "ORDERS"};
            PAGES = 3;
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter());

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        super.setUpDrawer(toolbar);

        if(customerId != null){
            setTitle("History | " + customerDao.load(customerId).getOutletName());
        }

        if(currentTab != -1){
            mViewPager.setCurrentItem(currentTab);
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle b = new Bundle();
            b.putString("customer_id", customerId);
            Fragment target = null;
            if(RestClient.getRole().equalsIgnoreCase(User.ROLE_DETAILER)){
                switch(position){
                    case 0:
                        target = new MalariaHistoryFragment();
                        break;
                    case 1:
                        target = new DiarrheaHistoryFragment();
                        break;
                }
            }else{
                switch(position){
                    case 0:
                        target = new SalesHistoryFragment();
                        break;
                    case 1:
                        target = new AdhockSalesHistoryFragment();
                        break;
                    case 2:
                        target = new OrdersHistoryFragment();
                        break;
                }
            }
            target.setArguments(b);
            return target;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
        } catch (Exception ex) {
            Toast.makeText(this, "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
