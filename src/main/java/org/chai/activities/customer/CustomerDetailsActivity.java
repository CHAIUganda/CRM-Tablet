package org.chai.activities.customer;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.chai.R;
import org.chai.activities.HomeActivity;
import org.chai.model.Customer;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;

/**
 * Created by victor on 10/17/14.
 */
public class CustomerDetailsActivity extends Activity {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;

    private long customerId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_details_activity);
        initialiseGreenDao();
        Bundle bundle = getIntent().getExtras();
        customerId = bundle.getLong("id");
        if (customerId != 0) {
            Customer customer = customerDao.load(customerId);
            if (customer != null) {
                loadCustomerDetails(customer);
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
                bundle.putLong("id",customerId);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.customer_details_inactive:
                Toast.makeText(getApplicationContext(), "Error initialising Database:" + customerId, Toast.LENGTH_LONG).show();
                return true;
            case R.id.details_home:
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void loadCustomerDetails(Customer customer) {
        ((TextView) findViewById(R.id.customer_profile_name)).setText(customer.getOutletName());
        ((TextView) findViewById(R.id.customer_profile_address)).setText(customer.getKeyWholeSalerContact());
        ((TextView) findViewById(R.id.customer_profile_date_outlet_opened)).setText(customer.getDateOutletOpened()+"");
        ((TextView) findViewById(R.id.customer_profile_outlet_type)).setText(customer.getOutletType());
        ((TextView) findViewById(R.id.customer_profile_size)).setText(customer.getOutletSize());
        ((TextView) findViewById(R.id.customer_profile_split)).setText(customer.getSplit());
        ((TextView) findViewById(R.id.customer_profile_opening_hrs)).setText(customer.getOpeningHours());
        ((TextView) findViewById(R.id.customer_profile_sources_of_supply)).setText(customer.getMajoritySourceOfSupply());
        ((TextView) findViewById(R.id.customer_profile_key_wholesaler_name)).setText(customer.getKeyWholeSalerName());
        ((TextView) findViewById(R.id.customer_profile_key_wholesaler_contact)).setText(customer.getKeyWholeSalerContact());
        ((TextView) findViewById(R.id.customer_profile_building_structure)).setText(customer.getBuildingStructure());
        ((TextView) findViewById(R.id.customer_profile_desc_location)).setText(customer.getDescriptionOfOutletLocation());
        ((TextView) findViewById(R.id.profile_number_of_employees)).setText(customer.getNumberOfEmployees() + "");
        ((TextView) findViewById(R.id.customer_profile_number_of_branches)).setText(customer.getHasSisterBranch() + "");
        ((TextView) findViewById(R.id.customer_profile_num_customers_per_day)).setText(customer.getNumberOfCustomersPerDay() + "");
        ((TextView) findViewById(R.id.customer_profile_num_products)).setText(customer.getNumberOfProducts() + "");
        ((TextView) findViewById(R.id.customer_profile_restock_frequency)).setText(customer.getRestockFrequency() + "");
        ((TextView) findViewById(R.id.customer_profile_turn_over)).setText(customer.getTurnOver() + "");
        ((TextView) findViewById(R.id.customer_profile_subcounty)).setText(customer.getVillage().getName());

    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}