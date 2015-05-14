package org.chai.activities.customer;

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

        customerId = getIntent().getStringExtra("customer_id");
        if(customerId != null){
            customer = customerDao.load(customerId);
            if(customer != null){
                getSupportActionBar().setTitle("Edit Customer");
            }
        }

        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        pager.setAdapter(new FormPagerAdapter(getSupportFragmentManager()));

        indicator.setViewPager(pager);

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
            saveForm();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveForm(){
        if(basicFragment == null){
            pager.setCurrentItem(0);
            return;
        }
        if(!basicFragment.saveFields()){
            pager.setCurrentItem(0);
            return;
        }

        if(commercialFragment == null){
            pager.setCurrentItem(1);
            return;
        }
        if(!commercialFragment.saveFields()){
            pager.setCurrentItem(1);
            return;
        }

        if(contactFragment == null){
            pager.setCurrentItem(2);
            return;
        }
        if(!contactFragment.saveFields()){
            pager.setCurrentItem(2);
            return;
        }

        customer.setIsDirty(true);

        if(customer.getUuid() == null){
            customer.setUuid(UUID.randomUUID().toString());
            customer.setIsActive(true);
            customerDao.insert(customer);
        }else{
            customerDao.update(customer);
        }

        //First remove all customer contacts
        for(CustomerContact contact : customer.getCustomerContacts()){
            customerContactDao.delete(contact);
        }

        //Re-ad customer contacts
        for(CustomerContact c: contactFragment.contacts){
            if(c.getUuid() == null){
                c.setUuid(UUID.randomUUID().toString());
            }

            c.setCustomerId(customer.getUuid());
            c.setCustomer(customer);
            customerContactDao.insert(c);
        }

        Toast.makeText(this, "Customer has been saved", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, CustomersActivity.class));
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
                    basicFragment = new CustomerBasicsFormFragment();
                    fragment = basicFragment;
                    break;
                case 1:
                    commercialFragment = new CustomerCommercialFormFragment();
                    fragment = commercialFragment;
                    break;
                case 2:
                    contactFragment = new CustomerContactsFormFragment();
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
