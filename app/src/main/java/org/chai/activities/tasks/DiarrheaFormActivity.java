package org.chai.activities.tasks;

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
import org.chai.activities.calls.HistoryActivity;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.DetailerCall;
import org.chai.model.DetailerCallDao;
import org.chai.model.DetailerStock;
import org.chai.model.DetailerStockDao;
import org.chai.model.Task;
import org.chai.model.TaskDao;
import org.chai.util.MyApplication;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.WhereCondition;
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
    private String detailId;
    private String taskId;
    private String customerId;

    List<DetailerStock> stocks;
    List<DetailerStock> zincStocks;
    List<DetailerStock> orsStocks;

    protected SQLiteDatabase db;
    protected DaoMaster daoMaster;
    protected DaoSession daoSession;
    protected TaskDao taskDao;
    protected DetailerCallDao detailerCallDao;
    protected DetailerStockDao detailerStockDao;

    public boolean inferedCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_DIARRHEA_DETAILING;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.diarrhea_form_activity);

        initialiseGreenDao();

        aq = new AQuery(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        detailId = getIntent().getStringExtra("detail_id");
        taskId = getIntent().getStringExtra("task_id");

        if(detailId != null){
            call = detailerCallDao.load(detailId);
            if(call != null){
                task = call.getTask();
                toolbar.setTitle("Edit Diarrhea Details");
            }
        }else{
            if(taskId != null){
                task = taskDao.load(taskId);
            }
        }

        if(task == null){
            task = new Task();
        }else{
            customerId = task.getCustomerId();
        }

        if(customerId == null){
            //Try to get it directly from intent
            customerId = getIntent().getStringExtra("customer_id");
        }

        if(customerId != null && call == null){
            //Try to get the last known call
            call = getLastDetailerInfo(customerId);
            if(call != null){
                inferedCall = true;
            }
        }

        if(call == null){
            call = new DetailerCall();
            stocks = new ArrayList<DetailerStock>();
        }else{
            stocks = call.getDetailerStocks();
        }

        zincStocks = new ArrayList<DetailerStock>();
        orsStocks = new ArrayList<DetailerStock>();

        for(DetailerStock stock: stocks){
            if(stock.getCategory() != null && stock.getCategory().equalsIgnoreCase(DiarrheaFormZincFragment.STOCK_TYPE)){
                zincStocks.add(stock);
            }
            if(stock.getCategory() != null && stock.getCategory().equalsIgnoreCase(DiarrheaOrsFragment.STOCK_TYPE)){
                orsStocks.add(stock);
            }
        }

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
        if(customerFragment == null || !customerFragment.saveFields()){
            pager.setCurrentItem(0);
            return;
        }

        if(educationFragment == null || !educationFragment.saveFields()){
            pager.setCurrentItem(1);
            return;
        }

        if(zincFragment == null || !zincFragment.saveFields()){ //Make sure the detailer has atleast been to the Zinc Screen
            pager.setCurrentItem(2);
            return;
        }

        if(orsFragment == null || !orsFragment.saveFields()){ //Make sure the detailer has atleast been to the ORS Screen
            pager.setCurrentItem(3);
            return;
        }

        if(recommendationFragment == null || !recommendationFragment.saveFields()){
            pager.setCurrentItem(4);
            return;
        }

        task.setStatus(HomeActivity.STATUS_COMPLETE);
        task.setType("detailer");
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

        if(call.getUuid() == null || inferedCall){
            call.setUuid(UUID.randomUUID().toString());
            detailerCallDao.insert(call);
        }else{
            detailerCallDao.update(call);
        }

        //Clear all stocks first
        detailerStockDao.deleteInTx(call.getDetailerStocks());

        //Save stocks
        stocks = new ArrayList<DetailerStock>();
        stocks.addAll(zincStocks);
        stocks.addAll(orsStocks);

        for(DetailerStock stock: stocks){
            stock.setIsDirty(true);
            stock.setMalariadetailId("");
            stock.setDetailerId(call.getUuid());
            stock.setDetailerCall(call);

            if(stock.getUuid() == null){
                stock.setUuid(UUID.randomUUID().toString());
                stock.setDateCreated(new Date());
            }
            detailerStockDao.insert(stock);
        }

        Toast.makeText(this, "Diarrhea form has been saved", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, HistoryActivity.class);
        i.putExtra("tab", 1);
        startActivity(i);
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

    private DetailerCall getLastDetailerInfo(String customerUuid){
        if(customerUuid == null){
            return null;
        }
        try{
            Query query = detailerCallDao.queryBuilder().where(new WhereCondition.StringCondition(" T.'" + DetailerCallDao.Properties.
                    TaskId.columnName + "' IN " + "(SELECT " + TaskDao.Properties.Uuid.columnName + " FROM " + TaskDao.TABLENAME + " C WHERE C.'" + TaskDao.Properties.CustomerId.columnName + "' = '" + customerUuid + "')")).build();
            List<DetailerCall> detailerCallList = query.list();
            if (!detailerCallList.isEmpty()) {
                DetailerCall d = detailerCallList.get(0);
                //d.setUuid(null); //to set it as a new call
                return d;
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
