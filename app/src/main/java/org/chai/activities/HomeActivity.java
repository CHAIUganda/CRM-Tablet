package org.chai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.androidquery.AQuery;
import com.astuetz.PagerSlidingTabStrip;

import org.chai.R;
import org.chai.activities.tasks.TaskByLocationFragment;
import org.chai.activities.tasks.TaskCalenderFragment;
import org.chai.activities.tasks.TaskViewOnMapFragment;
import org.chai.sync.CHAISynchroniser;
import org.chai.util.Utils;

/**
 * Created by victor on 10/15/14.
 */
public class HomeActivity extends BaseActivity{
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

        if(!CHAISynchroniser.isSyncing && CHAISynchroniser.getLastSynced(this) == -1){ //Start service only when we've never and we are not currently syncing
            Utils.log("Service has never run before - starting it");
            startService(new Intent(this, CHAISynchroniser.class));
        }else{
            Utils.log("Cannot start service - has been started before");
        }
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