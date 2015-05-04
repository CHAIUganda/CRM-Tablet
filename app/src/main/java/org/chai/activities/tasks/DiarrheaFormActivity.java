package org.chai.activities.tasks;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.BaseActivity;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.DetailerCall;
import org.chai.model.DetailerCallDao;
import org.chai.model.DetailerStock;
import org.chai.model.DetailerStockDao;
import org.chai.model.Task;
import org.chai.model.TaskDao;
import org.chai.model.VillageDao;
import org.chai.util.MyApplication;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.Date;
import java.util.UUID;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Zed on 5/2/2015.
 */
public class DiarrheaFormActivity extends BaseActivity {
    public final static String STATUS_NEW = "new", STATUS_COMPLETE = "complete",STATUS_CANCELLED = "cancelled";

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

    protected SQLiteDatabase db;
    protected DaoMaster daoMaster;
    protected DaoSession daoSession;
    protected VillageDao villageDao;
    protected TaskDao taskDao;
    protected DetailerCallDao detailerCallDao;
    protected CustomerDao customerDao;
    protected DetailerStockDao detailerStockDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_DIARRHEA_DETAILING;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.diarrhea_form_activity);

        initialiseGreenDao();

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

        if(zincFragment == null){ //Make sure the detailer has atleast been to the Zinc Screen
            pager.setCurrentItem(2);
            return;
        }else{
            if(!zincFragment.saveFields()){
                pager.setCurrentItem(2);
                return;
            }
        }

        if(orsFragment == null){ //Make sure the detailer has atleast been to the ORS Screen
            pager.setCurrentItem(3);
            return;
        }else{
            if(!orsFragment.saveFields()){
                pager.setCurrentItem(3);
                return;
            }
        }

        if(recommendationFragment == null){
            pager.setCurrentItem(4);
            return;
        }else{
            if(!recommendationFragment.saveFields()){
                pager.setCurrentItem(4);
                return;
            }
        }

        task.setUuid(UUID.randomUUID().toString());
        task.setStatus(STATUS_COMPLETE);
        task.setType("detailer");
        task.setIsAdhock(true);
        task.setCompletionDate(new Date());
        task.setIsDirty(true);

        taskDao.insert(task);

        call.setIsDirty(true);
        call.setUuid(UUID.randomUUID().toString());
        call.setDateOfSurvey(new Date());
        call.setTaskId(task.getUuid());
        call.setTask(task);

        //Save stocks
        for(DetailerStock stock: zincFragment.stocks){
            stock.setUuid(UUID.randomUUID().toString());
            stock.setIsDirty(true);
            stock.setDateCreated(new Date());
            stock.setDetailerId(call.getUuid());
            stock.setDetailerCall(call);
            stock.setCategory("zinc");

            detailerStockDao.insert(stock);
        }

        for(DetailerStock stock: orsFragment.stocks){
            stock.setUuid(UUID.randomUUID().toString());
            stock.setIsDirty(true);
            stock.setDateCreated(new Date());
            stock.setDetailerId(call.getUuid());
            stock.setDetailerCall(call);
            stock.setCategory("ors");

            detailerStockDao.insert(stock);
        }

        detailerCallDao.insert(call);
    }

    protected void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            taskDao = daoSession.getTaskDao();
            detailerCallDao = daoSession.getDetailerCallDao();
            detailerStockDao = daoSession.getDetailerStockDao();
        } catch (Exception ex) {
            Toast.makeText(this, "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
