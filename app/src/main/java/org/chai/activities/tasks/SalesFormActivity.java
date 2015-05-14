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
import org.chai.activities.calls.HistoryActivity;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.ProductDao;
import org.chai.model.Sale;
import org.chai.model.SaleDao;
import org.chai.model.SaleDataDao;
import org.chai.model.StokeDataDao;
import org.chai.model.Task;
import org.chai.model.TaskDao;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Zed on 5/3/2015.
 */
public class SalesFormActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;
    int NUM_PAGES = 4;
    ViewPager pager;
    CircleIndicator indicator;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TaskDao taskDao;
    private SaleDao saleDao;
    private SaleDataDao saleDataDao;
    private StokeDataDao stokeDataDao;
    private ProductDao productDao;

    public Task task;
    public Sale sale;

    SalesFormCustomerFragment customerFragment;
    SalesFormStockFragment stockFragment;
    SalesFormSaleFragment salesFragment;
    SalesFormNextStepsFragment nextStepsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_SALES;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_form_activity);
        initialiseGreenDao();

        task = new Task();
        sale = new Sale();

        aq = new AQuery(this);

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
                    customerFragment = new SalesFormCustomerFragment();
                    fragment = customerFragment;
                    break;
                case 1:
                    stockFragment = new SalesFormStockFragment();
                    fragment = stockFragment;
                    break;
                case 2:
                    salesFragment = new SalesFormSaleFragment();
                    fragment = salesFragment;
                    break;
                case 3:
                    nextStepsFragment = new SalesFormNextStepsFragment();
                    fragment = nextStepsFragment;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            taskDao = daoSession.getTaskDao();
            saleDao = daoSession.getSaleDao();
            saleDataDao = daoSession.getSaleDataDao();
            productDao = daoSession.getProductDao();
            stokeDataDao = daoSession.getStokeDataDao();
        } catch (Exception ex) {
            Toast.makeText(this, "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
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
        Utils.log("Saving sales form");
        if(customerFragment == null || !customerFragment.saveFields()){
            pager.setCurrentItem(0);
            return;
        }

        if(stockFragment == null || !stockFragment.saveFields()){
            pager.setCurrentItem(1);
            return;
        }

        if(salesFragment == null || !salesFragment.saveFields()){
            pager.setCurrentItem(2);
            return;
        }

        Toast.makeText(this, "Sale details have been saved", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, HistoryActivity.class);
        startActivity(i);
    }
}
