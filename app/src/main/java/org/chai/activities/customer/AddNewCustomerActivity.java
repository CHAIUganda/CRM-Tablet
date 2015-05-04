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
import org.chai.model.CustomerContact;
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

    String customerId;
    public Customer customer;

    private CustomerBasicsFormFragment basicFragment;
    private CustomerCommercialFormFragment commercialFragment;
    private CustomerContactsFormFragment contactFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_CUSTOMERS;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_customer_activity);

        initialiseGreenDao();

        aq = new AQuery(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        customerId = "59afe896-39fa-4f0d-bb18-fadeeb274a7a";//getIntent().getStringExtra("id");
        if(customerId != null){
            customer = customerDao.load(customerId);
            if(customer != null){
                getSupportActionBar().setTitle("Edit Customer");
            }
        }

        basicFragment = new CustomerBasicsFormFragment();
        commercialFragment = new CustomerCommercialFormFragment();
        contactFragment = new CustomerContactsFormFragment();

        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        pager.setAdapter(new FormPagerAdapter(getSupportFragmentManager()));

        indicator.setViewPager(pager);

        super.setUpDrawer(toolbar);

        pager.setCurrentItem(2);
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
                validForm = contactFragment.saveFields(); //This also sets the contacts which we'll get later and save
                if(!validForm){
                    index = 2;
                }
            }

            if(validForm){
                customer.setIsDirty(true);

                if(customer.getUuid() == null){
                    customer.setUuid(UUID.randomUUID().toString());
                    customer.setIsActive(true);
                    customerDao.insert(customer);
                }else{
                    customerDao.update(customer);
                }

                for(CustomerContact c: contactFragment.contacts){
                    if(c.getUuid() == null){
                        c.setUuid(UUID.randomUUID().toString());
                        c.setCustomerId(customer.getUuid());
                        c.setCustomer(customer);
                        customerContactDao.insert(c);
                    }else{
                        customerContactDao.update(c);
                    }
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
