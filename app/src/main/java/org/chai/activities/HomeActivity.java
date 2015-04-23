package org.chai.activities;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.astuetz.PagerSlidingTabStrip;

import org.chai.R;
import org.chai.activities.tasks.TaskByLocationFragment;
import org.chai.activities.tasks.TaskCalenderFragment;
import org.chai.activities.tasks.TaskViewOnMapFragment;
import org.chai.adapter.NavDrawerListAdapter;
import org.chai.util.NavDrawerItem;

import java.util.ArrayList;

/**
 * Created by victor on 10/15/14.
 */
public class HomeActivity extends BaseActivity{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    Toolbar toolbar;
    AQuery aq;

    ViewPager mViewPager;

    String[] titles = new String[]{"CALENDAR", "VIEW BY LOCATION", "MAP"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main_layout);

        aq = new AQuery(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter());

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        super.setUpDrawer(toolbar);

        /*
        removeAnyFragmentsOnStack();
        mTitle = mDrawerTitle = getTitle();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0],navMenuIcons.getResourceId(0,-1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1],navMenuIcons.getResourceId(1,-1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2],navMenuIcons.getResourceId(2,-1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3],navMenuIcons.getResourceId(3,-1)));
        if (RestClient.role.equalsIgnoreCase(User.ROLE_SALES)) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[4],navMenuIcons.getResourceId(3,-1)));
        }else{
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[5],navMenuIcons.getResourceId(2,-1)));
        }
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6],navMenuIcons.getResourceId(4,-1)));

        navMenuIcons.recycle();
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.drawable.ic_drawer,R.string.app_name,R.string.app_name){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
//      mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if(savedInstanceState == null){
            displayView(0);
        }*/
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

    /*private class SlideMenuClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()){
            case R.id.action_sync:
                final ProgressDialog progressDialog  = new ProgressDialog(HomeActivity.this);
                progressDialog.setMessage("Syncronising with Server...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setIndeterminate(false);
                progressDialog.setMax(100);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CHAISynchroniser chaiSynchroniser = new CHAISynchroniser(HomeActivity.this,progressDialog);
                        chaiSynchroniser.startSyncronisationProcess();
                        progressDialog.dismiss();
                    }
                }).start();
                return true;
            case R.id.action_logout:
                logout(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_form_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getItemId() == R.id.action_save){
            Toast.makeText(this, "Form details saved", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.START|Gravity.LEFT)){
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }


    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_sync).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    /*private void displayView(int position){
       Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new TaskMainFragment();
                break;
            case 1:
                fragment = new CustomersMainFragment();
                break;
            case 2:
                fragment = new HistoryMainFragment();
                break;
            case 3:
                fragment = new TakeOrderFragment();
                break;
            case 4:
                if (RestClient.role.equalsIgnoreCase(User.ROLE_SALES)) {
                    fragment = new AdhockSaleFragment();
                }else{
                    fragment = new AdhockDetailerFrgment();
                }
                break;
            case 5:
                fragment = new ReportViewFragment();
                break;
            default:
                break;
        }
        if(fragment !=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, fragment).commit();
            mDrawerList.setItemChecked(position,true);
            mDrawerList.setSelection(position);
            if ((position == 4 && RestClient.role.equalsIgnoreCase(User.ROLE_DETAILER))||position>4) {
                setTitle(navMenuTitles[position+1]);
            }else{
                setTitle(navMenuTitles[position]);
            }
            mDrawerLayout.closeDrawer(mDrawerList);
        }else{
            Log.e("HomeActivity", "Error in creating fragment");
        }
    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public void onBackPressed(){
        if(!BaseContainerFragment.handleBackPressed(getSupportFragmentManager())){
            super.onBackPressed();
        }
    }

    private void removeAnyFragmentsOnStack(){
        try{
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void logout(Activity activity){
        Intent login = new Intent(activity.getApplicationContext(), LoginActivity.class);
        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(login);
        activity.finish();
    }*/

}