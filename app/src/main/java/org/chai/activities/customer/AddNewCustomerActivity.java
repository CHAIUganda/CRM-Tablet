package org.chai.activities.customer;

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
import org.chai.model.Customer;
import org.chai.model.CustomerContactDao;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.UUID;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Zed on 4/23/2015.
 */
public class AddNewCustomerActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;

    int NUM_PAGES = 3;
    ViewPager pager;
    CircleIndicator indicator;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;
    private CustomerContactDao customerContactDao;

    public Customer customer;

    private CustomerBasicsFormFragment basicFragment;
    private CustomerCommercialFormFragment commercialFragment;
    private CustomerContactsFormFragment contactFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_CUSTOMERS;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_customer_activity);

        aq = new AQuery(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        basicFragment = new CustomerBasicsFormFragment();
        commercialFragment = new CustomerCommercialFormFragment();
        contactFragment = new CustomerContactsFormFragment();

        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        pager.setAdapter(new FormPagerAdapter(getSupportFragmentManager()));

        indicator.setViewPager(pager);

        initialiseGreenDao();

        super.setUpDrawer(toolbar);
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
            int index = -1;
            boolean validForm = basicFragment.saveFields();
            if(!validForm){
                index = 0;
            }

            if(validForm){
                validForm = commercialFragment.saveFields();
                if(!validForm){
                    index = 1;
                }
            }

            if(validForm){
                validForm = contactFragment.saveFields();
                if(!validForm){
                    index = 2;
                }
            }

            if(validForm){
                customer.setIsDirty(true);
                customer.setIsActive(true);

                if(customer.getUuid() == null){
                    Utils.log("Inserting new customer");
                    customer.setUuid(UUID.randomUUID().toString());
                    customerDao.insert(customer);
                }else{
                    customerDao.insert(customer);
                }

                finish();
                Toast.makeText(this, "Customer has been saved", Toast.LENGTH_LONG).show();
            }else{
                pager.setCurrentItem(index);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
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
                    fragment = basicFragment;
                    break;
                case 1:
                    fragment = commercialFragment;
                    break;
                case 2:
                    fragment = contactFragment;
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
            customerDao = daoSession.getCustomerDao();
            customerContactDao = daoSession.getCustomerContactDao();
        } catch (Exception ex) {
            Utils.log("Error initializing green DAO");
            Toast.makeText(this, "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
