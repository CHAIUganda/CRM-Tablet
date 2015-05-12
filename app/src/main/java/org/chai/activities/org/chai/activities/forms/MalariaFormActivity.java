package org.chai.activities.org.chai.activities.forms;

import android.content.Intent;
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
import org.chai.activities.calls.HistoryActivity;
import org.chai.activities.tasks.DiarrheaFormActivity;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.DetailerStock;
import org.chai.model.DetailerStockDao;
import org.chai.model.MalariaDetail;
import org.chai.model.MalariaDetailDao;
import org.chai.model.Task;
import org.chai.model.TaskDao;
import org.chai.util.MyApplication;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.Date;
import java.util.UUID;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Zed on 4/9/2015.
 */
public class MalariaFormActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;
    int NUM_PAGES = 5;
    ViewPager pager;
    CircleIndicator indicator;

    MalariaFormFragment1 customerFragment;
    MalariaFormFragment2 educationFragment;
    MalariaFormFragment3 antimalarialFragment;
    MalariaFormFragment4 recommendationFragment;
    MalariaFormFragment5 rdtFragment;

    public Task task;
    public MalariaDetail call;

    protected SQLiteDatabase db;
    protected DaoMaster daoMaster;
    protected DaoSession daoSession;
    protected TaskDao taskDao;
    protected MalariaDetailDao malariaDetailDao;
    protected CustomerDao customerDao;
    protected DetailerStockDao detailerStockDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_MALARIA_DETAILING;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.malaria_form_activity);
        initialiseGreenDao();

        aq = new AQuery(this);

        task = new Task();
        call = new MalariaDetail();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    customerFragment = new MalariaFormFragment1();
                    fragment = customerFragment;
                    break;
                case 1:
                    educationFragment = new MalariaFormFragment2();
                    fragment = educationFragment;
                    break;
                case 2:
                    antimalarialFragment = new MalariaFormFragment3();
                    fragment = antimalarialFragment;
                    break;
                case 3:
                    rdtFragment = new MalariaFormFragment5();
                    fragment = rdtFragment;
                    break;
                case 4:
                    recommendationFragment = new MalariaFormFragment4();
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
        if(customerFragment == null){
            pager.setCurrentItem(0);
        }else if(!customerFragment.saveFields()){
            pager.setCurrentItem(0);
            return;
        }

        if(educationFragment == null){
            pager.setCurrentItem(1);
        }else if(!educationFragment.saveFields()){
            pager.setCurrentItem(1);
            return;
        }

        if(antimalarialFragment == null){
            pager.setCurrentItem(2);
        }else if(!antimalarialFragment.saveFields()){
            pager.setCurrentItem(2);
            return;
        }

        if(rdtFragment == null){
            pager.setCurrentItem(3);
        }else if(!rdtFragment.saveFields()){
            pager.setCurrentItem(3);
            return;
        }

        if(recommendationFragment == null){
            pager.setCurrentItem(4);
        }else if(!recommendationFragment.saveFields()){
            pager.setCurrentItem(4);
            return;
        }

        task.setUuid(UUID.randomUUID().toString());
        task.setStatus(DiarrheaFormActivity.STATUS_COMPLETE);
        task.setType("malaria");
        task.setIsAdhock(true);
        task.setCompletionDate(new Date());
        task.setDateCreated(new Date());
        task.setIsDirty(true);

        taskDao.insert(task);

        call.setIsDirty(true);
        call.setUuid(UUID.randomUUID().toString());
        call.setDateOfSurvey(new Date());
        call.setTaskId(task.getUuid());
        call.setTask(task);

        //Save stocks
        for(DetailerStock stock: antimalarialFragment.stocks){
            stock.setUuid(UUID.randomUUID().toString());
            stock.setIsDirty(true);
            stock.setDateCreated(new Date());
            stock.setMalariadetailId(call.getUuid());
            stock.setMalariaDetail(call);
            stock.setDetailerId("");
            stock.setCategory("antimalarial");

            detailerStockDao.insert(stock);
        }

        for(DetailerStock stock: rdtFragment.stocks){
            stock.setUuid(UUID.randomUUID().toString());
            stock.setIsDirty(true);
            stock.setDateCreated(new Date());
            stock.setMalariadetailId(call.getUuid());
            stock.setMalariaDetail(call);
            stock.setDetailerId("");
            stock.setCategory("rdt");

            detailerStockDao.insert(stock);
        }

        malariaDetailDao.insert(call);

        Toast.makeText(this, "Diarrhea form has been saved", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, HistoryActivity.class);
        startActivity(i);
    }

    protected void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            taskDao = daoSession.getTaskDao();
            malariaDetailDao = daoSession.getMalariaDetailDao();
            detailerStockDao = daoSession.getDetailerStockDao();
        } catch (Exception ex) {
            Toast.makeText(this, "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
