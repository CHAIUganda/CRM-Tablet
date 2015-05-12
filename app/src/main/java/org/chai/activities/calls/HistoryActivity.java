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
import org.chai.model.User;
import org.chai.rest.RestClient;

/**
 * Created by Zed on 4/23/2015.
 */
public class HistoryActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;

    ViewPager mViewPager;

    String[] titles;
    int PAGES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_HISTORY;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        aq = new AQuery(this);

        if(RestClient.role.equalsIgnoreCase(User.ROLE_DETAILER)){
            titles = new String[]{"MALARIA FORMS", "DIARRHEA FORMS", "ORDERS"};
            PAGES = 3;
        }else{
            titles = new String[]{"SALES FORMS", "ORDERS"};
            PAGES = 2;
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter());

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        super.setUpDrawer(toolbar);
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
            Fragment target = null;
            if(RestClient.role.equalsIgnoreCase(User.ROLE_DETAILER)){
                switch(position){
                    case 0:
                        target = new MalariaHistoryFragment();
                        break;
                    case 1:
                        target = new DiarrheaHistoryFragment();
                        break;
                    case 2:
                        target = new OrdersHistoryFragment();
                        break;
                }
            }else{
                switch(position){
                    case 0:
                        target = new SalesHistoryFragment();
                        break;
                    case 1:
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
}
