package org.chai.activities.forms;

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
import org.chai.activities.HomeActivity;
import org.chai.activities.tasks.DiarrheaFormActivity;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.DetailerStock;
import org.chai.model.DetailerStockDao;
import org.chai.model.MalariaDetail;
import org.chai.model.MalariaDetailDao;
import org.chai.model.Task;
import org.chai.model.TaskDao;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Zed on 4/9/2015.
 */
public class MalariaFormActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;
    int NUM_PAGES = 5;
    public ViewPager pager;
    CircleIndicator indicator;

    MalariaFormCustomerFragment customerFragment;
    MalariaFormEducationFragment educationFragment;
    MalariaFormAntiMalarialFragment antimalarialFragment;
    MalariaFormNextStepsFragment recommendationFragment;
    MalariaFormRdtFragment rdtFragment;

    public Task task;
    public MalariaDetail call;
    private String taskId;
    private String detailId;

    protected SQLiteDatabase db;
    protected DaoMaster daoMaster;
    protected DaoSession daoSession;
    protected TaskDao taskDao;
    protected MalariaDetailDao malariaDetailDao;
    protected DetailerStockDao detailerStockDao;

    List<DetailerStock> stocks;
    List<DetailerStock> antimalarials;
    List<DetailerStock> rdts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.log("onCreate() Main Activity");
        CURRENT_SCREEN = SCREEN_MALARIA_DETAILING;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.malaria_form_activity);

        initialiseGreenDao();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        pager.setAdapter(new FormPagerAdapter(getSupportFragmentManager()));
        indicator.setViewPager(pager);

        aq = new AQuery(this);

        detailId = getIntent().getStringExtra("detail_id");
        taskId = getIntent().getStringExtra("task_id");

        if(detailId != null){
            call = malariaDetailDao.load(detailId);
            if(call != null){
                task = call.getTask();
                toolbar.setTitle("Edit Malaria Details");
            }
        }else{
            if(taskId != null){
                task = taskDao.load(taskId);
            }
        }

        if(call == null){
            call = new MalariaDetail();
            stocks = new ArrayList<DetailerStock>();
        }else{
            stocks = call.getDetailerMalariaStocks();
        }

        if(task == null){
            task = new Task();
        }

        antimalarials = new ArrayList<DetailerStock>();
        rdts = new ArrayList<DetailerStock>();

        for(DetailerStock stock: stocks){
            if(stock.getCategory() != null && stock.getCategory().equalsIgnoreCase(MalariaFormAntiMalarialFragment.STOCK_TYPE)){
                antimalarials.add(stock);
            }
            if(stock.getCategory() != null && stock.getCategory().equalsIgnoreCase(MalariaFormRdtFragment.STOCK_TYPE)){
                rdts.add(stock);
            }
        }

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
                    customerFragment = new MalariaFormCustomerFragment();
                    fragment = customerFragment;
                    break;
                case 1:
                    educationFragment = new MalariaFormEducationFragment();
                    fragment = educationFragment;
                    break;
                case 2:
                    antimalarialFragment = new MalariaFormAntiMalarialFragment();
                    fragment = antimalarialFragment;
                    break;
                case 3:
                    rdtFragment = new MalariaFormRdtFragment();
                    fragment = rdtFragment;
                    break;
                case 4:
                    recommendationFragment = new MalariaFormNextStepsFragment();
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
        if(customerFragment == null || !customerFragment.saveFields()){
            pager.setCurrentItem(0);
            return;
        }

        if(educationFragment == null || !educationFragment.saveFields()){
            pager.setCurrentItem(1);
            return;
        }

        if(antimalarialFragment == null || !antimalarialFragment.saveFields()){
            pager.setCurrentItem(2);
            return;
        }

        if(rdtFragment == null || !rdtFragment.saveFields()){
            pager.setCurrentItem(3);
            return;
        }

        if(recommendationFragment == null || !recommendationFragment.saveFields()){
            pager.setCurrentItem(4);
            return;
        }

        task.setStatus(HomeActivity.STATUS_COMPLETE);
        task.setType("malaria");
        task.setCompletionDate(new Date());
        task.setIsDirty(true);

        if(task.getUuid() == null){
            task.setUuid(UUID.randomUUID().toString());
            taskDao.insert(task);
        }else{
            task.setIsAdhock(true);
            task.setDateCreated(new Date());
            taskDao.update(task);
        }

        call.setTaskId(task.getUuid());
        call.setTask(task);
        call.setDateOfSurvey(new Date());
        call.setIsDirty(true);

        if(call.getUuid() == null){
            call.setUuid(UUID.randomUUID().toString());
            malariaDetailDao.insert(call);
        }else{
            malariaDetailDao.update(call);
        }

        //Clear all stocks first
        detailerStockDao.deleteInTx(call.getDetailerMalariaStocks());

        //Save stocks
        stocks = new ArrayList<DetailerStock>();
        stocks.addAll(antimalarials);
        stocks.addAll(rdts);

        for(DetailerStock stock: stocks){
            Utils.log("Saving stock -> " + stock.getCategory() + " : " + stock.getBrand());
            stock.setIsDirty(true);
            stock.setMalariadetailId(call.getUuid());
            stock.setMalariaDetail(call);
            stock.setDetailerId("");

            if(stock.getUuid() == null){
                stock.setUuid(UUID.randomUUID().toString());
                stock.setDateCreated(new Date());
            }
            detailerStockDao.insert(stock);
        }

        Toast.makeText(this, "Malaria form has been saved. Please complete Diarrhea details too.", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, DiarrheaFormActivity.class);
        i.putExtra("customer_id", task.getCustomerId());
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
