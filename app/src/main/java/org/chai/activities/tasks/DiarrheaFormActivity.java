package org.chai.activities.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.BaseActivity;
import org.chai.model.DetailerCall;
import org.chai.model.Task;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Zed on 5/2/2015.
 */
public class DiarrheaFormActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;
    int NUM_PAGES = 5;
    public ViewPager pager;
    CircleIndicator indicator;

    DiarrheaFormCustomerFragment customerFragment;
    DiarrheaFormEducationFragment educationFragment;
    DiarrheaFormZincFragment zincFragment;
    DiarrheaOrsFragment orsFragment;
    DiarrheaFormRecommendationFragment recommendationFragment;

    public Task task;
    public DetailerCall call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_DIARRHEA_DETAILING;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.diarrhea_form_activity);

        aq = new AQuery(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        task = new Task();
        call = new DetailerCall();

        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        pager.setAdapter(new FormPagerAdapter(getSupportFragmentManager()));
        indicator.setViewPager(pager);

        super.setUpDrawer(toolbar);

        pager.setCurrentItem(1);
    }

    private class FormPagerAdapter extends FragmentPagerAdapter {
        public FormPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    customerFragment = new DiarrheaFormCustomerFragment();
                    fragment = customerFragment;
                    break;
                case 1:
                    educationFragment = new DiarrheaFormEducationFragment();
                    fragment = educationFragment;
                    break;
                case 2:
                    zincFragment = new DiarrheaFormZincFragment();
                    fragment = zincFragment;
                    break;
                case 3:
                    orsFragment = new DiarrheaOrsFragment();
                    fragment = orsFragment;
                    break;
                case 4:
                    recommendationFragment = new DiarrheaFormRecommendationFragment();
                    fragment = recommendationFragment;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

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
            saveForm();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveForm(){
        if(!customerFragment.saveFields()){
            pager.setCurrentItem(0);
            return;
        }

        if(!educationFragment.saveFields()){
            pager.setCurrentItem(1);
            return;
        }
    }
}
