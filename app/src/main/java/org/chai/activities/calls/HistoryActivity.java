package org.chai.activities.calls;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.androidquery.AQuery;
import com.astuetz.PagerSlidingTabStrip;

import org.chai.R;
import org.chai.activities.BaseActivity;

/**
 * Created by Zed on 4/23/2015.
 */
public class HistoryActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;

    ViewPager mViewPager;

    String[] titles = new String[]{"CALL DATA", "ORDERS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_HISTORY;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        aq = new AQuery(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter());

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        super.setUpDrawer(toolbar);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;

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
                    target = new CallMainFragment();
                    break;
                case 1:
                    target = new OrdersMainFragment();
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
