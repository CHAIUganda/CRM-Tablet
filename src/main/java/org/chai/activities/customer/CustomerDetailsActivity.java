package org.chai.activities.customer;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.chai.R;
import org.chai.activities.HomeActivity;
import org.chai.model.*;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.customwidget.ContactDetailsView;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.List;

/**
 * Created by victor on 10/17/14.
 */
public class CustomerDetailsActivity extends Activity {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;

    private String customerId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_details_activity);
        initialiseGreenDao();
        Bundle bundle = getIntent().getExtras();
        customerId = bundle.getString("id");
        if (customerId != null) {
            Customer customer = customerDao.load(customerId);
            if (customer != null) {
                loadCustomerDetails(customer);
                addContactsToLayout(customer.getCustomerContacts());
            }
        }

    }


    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.customer_details_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.customer_details_edit:
                Intent intent = new Intent(getApplicationContext(),CustomerForm.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",customerId);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.customer_details_inactive:
                try{
                   Customer customer = customerDao.load(customerId);
                    customer.setIsActive(false);
                    customer.setIsDirty(true);
                    customerDao.update(customer);
                    Utils.showError(CustomerDetailsActivity.this,"Info:",  "Customer :" + customer.getOutletName()+" has been inactivated");
                    finish();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                return true;
            case R.id.details_home:
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                return true;
            case R.id.details_logout:
                HomeActivity.logout(this);
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void loadCustomerDetails(Customer customer) {
        try{
            ((TextView) findViewById(R.id.customer_profile_name)).setText(customer.getOutletName());
            ((TextView) findViewById(R.id.customer_profile_outlet_type)).setText(customer.getOutletType());
            ((TextView) findViewById(R.id.customer_profile_size)).setText(customer.getOutletSize());
            ((TextView) findViewById(R.id.customer_profile_district)).setText(customer.getSubcounty().getDistrict().getName());
            ((TextView) findViewById(R.id.customer_profile_subcounty)).setText(customer.getSubcounty().getName());
            ((TextView) findViewById(R.id.customer_profile_licence_visible)).setText((customer.getLicenceVisible()!=null?customer.getLicenceVisible():"false")+"");
            ((TextView) findViewById(R.id.customer_profile_licence_type)).setText(customer.getTypeOfLicence());
            ((TextView) findViewById(R.id.customer_profile_split)).setText(customer.getSplit());
            ((TextView) findViewById(R.id.customer_profile_trading_center)).setText(customer.getTradingCenter());
            ((TextView) findViewById(R.id.customer_profile_desc_location)).setText(customer.getDescriptionOfOutletLocation());
            ((TextView) findViewById(R.id.customer_profile_segment)).setText(customer.getSegment());
            ((TextView) findViewById(R.id.customer_profile_how_long_opened)).setText(customer.getLengthOpen());
            ((TextView) findViewById(R.id.profile_number_of_employees)).setText(customer.getNumberOfEmployees() == 0 ? "" : customer.getNumberOfEmployees() + "");
            ((TextView) findViewById(R.id.customer_profile_num_customers_per_day)).setText(customer.getNumberOfCustomersPerDay() + "");
            ((TextView) findViewById(R.id.customer_profile_sources_of_supply)).setText(customer.getMajoritySourceOfSupply());
            ((TextView) findViewById(R.id.customer_profile_key_wholesaler_name)).setText(customer.getKeyWholeSalerName());
            ((TextView) findViewById(R.id.customer_profile_key_wholesaler_contact)).setText(customer.getKeyWholeSalerContact());
            ((TextView) findViewById(R.id.customer_profile_restock_frequency)).setText(customer.getRestockFrequency() + "");
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void initialiseGreenDao() {
        try {
             UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void addContactsToLayout(List<CustomerContact> customerContactList) {
        //add to parent form
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.details_contact_layout);
        for (CustomerContact customerContact : customerContactList) {
            ContactDetailsView contactDetailsView = new ContactDetailsView(CustomerDetailsActivity.this,customerContact);
            linearLayout.addView(contactDetailsView);
        }
    }
}