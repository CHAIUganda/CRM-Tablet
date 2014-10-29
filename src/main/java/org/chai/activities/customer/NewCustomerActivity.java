package org.chai.activities.customer;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.chai.R;
import org.chai.adapter.SubcountyArrayAdapter;
import org.chai.model.*;
import org.chai.util.GPSTracker;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 10/20/14.
 */
public class NewCustomerActivity extends Activity {
    private Spinner subcountySpinner;
    private SubcountyArrayAdapter adapter;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;
    private SubcountyDao subcountyDao;

    private Subcounty[] subcounties;
    private GPSTracker gpsTracker;
    private double capturedLatitude;
    private double capturedLongitude;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_form);
        initialiseGreenDao();
        try{
            List<Subcounty> subcountyData = subcountyDao.loadAll();
            subcounties = subcountyData.toArray(new Subcounty[subcountyData.size()]);
            subcountySpinner = (Spinner)findViewById(R.id.details_subcounty);

            adapter = new SubcountyArrayAdapter(this,android.R.layout.simple_spinner_item,subcounties);
            subcountySpinner.setAdapter(adapter);

            subcountySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                            Subcounty subcounty = adapter.getItem(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            Button addNewCustomerBtn = (Button)findViewById(R.id.menu_add_new_customer);
            addNewCustomerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getApplicationContext(), "New Customer"+((Spinner) findViewById(R.id.details_building_structure)).getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                    boolean isSaved = saveNewCustomer();
                    if(isSaved){
                        Toast.makeText(getApplicationContext(), "New Customer has been  successfully added!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "New Customer"+((Spinner) findViewById(R.id.details_building_structure)).getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception ex){
            //
        }
        Button showGps = (Button) findViewById(R.id.new_customer_capture_gps);
        showGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpsTracker = new GPSTracker(NewCustomerActivity.this);
                if (gpsTracker.canGetLocation()) {
                    capturedLatitude = gpsTracker.getLatitude();
                    capturedLongitude = gpsTracker.getLongitude();
                    EditText detailsGps = (EditText) findViewById(R.id.details_gps);
                    detailsGps.setText(capturedLatitude + "," + capturedLongitude);
                } else {
                    gpsTracker.showSettingsAlert();
                }
            }
        });
    }

    private void initialiseGreenDao(){
        try{
            DaoMaster.DevOpenHelper helper =new  DaoMaster.DevOpenHelper(this,"chai-crm-db",null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
            subcountyDao = daoSession.getSubcountyDao();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_customer_form_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.menu_add_new_customer:
                boolean isSaved = saveNewCustomer();
                if(isSaved){
                    Intent intent = new Intent(getApplicationContext(),CustomersMainActivity.class);
                    startActivity(intent);
                }
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private boolean saveNewCustomer() {
        try {
            Customer customer = new Customer(null);
            customer.setSysid(UUID.randomUUID().toString());
            customer.setOutletName(((EditText) findViewById(R.id.detailsname)).getText().toString());
            customer.setTenureLengthYears(Integer.parseInt(((EditText) findViewById(R.id.details_tenure_length_years)).getText().toString()));
            customer.setTenureLengthMonths(Integer.parseInt(((EditText) findViewById(R.id.details_tenure_length_months)).getText().toString()));
            customer.setOutletType(((Spinner) findViewById(R.id.details_outlet_type)).getSelectedItem().toString());
            customer.setOutletSize(((Spinner) findViewById(R.id.details_size)).getSelectedItem().toString());
            customer.setSplit(((Spinner) findViewById(R.id.details_split)).getSelectedItem().toString());
            customer.setOpeningHours(((Spinner) findViewById(R.id.details_opening_hrs)).getSelectedItem().toString());

            customer.setMajoritySourceOfSupply(((EditText) findViewById(R.id.details_sources_of_supply)).getText().toString());
            customer.setKeyWholeSalerName(((EditText) findViewById(R.id.details_key_wholesaler_name)).getText().toString());
            customer.setKeyWholeSalerContact(((EditText) findViewById(R.id.details_key_wholesaler_contact)).getText().toString());

            customer.setBuildingStructure(((Spinner) findViewById(R.id.details_building_structure)).getSelectedItem().toString());

            customer.setEquipment(((EditText) findViewById(R.id.details_equipment)).getText().toString());
            customer.setDescriptionOfOutletLocation(((EditText) findViewById(R.id.details_desc_location)).getText().toString());
            customer.setNumberOfEmployees(Integer.parseInt(((EditText) findViewById(R.id.details_number_of_employees)).getText().toString()));
            customer.setNumberOfBranches(Integer.parseInt(((EditText) findViewById(R.id.details_number_of_branches)).getText().toString()));
            customer.setNumberOfCustomersPerDay(Integer.parseInt(((EditText) findViewById(R.id.details_num_customers_per_day)).getText().toString()));
            customer.setNumberOfProducts(Integer.parseInt(((EditText) findViewById(R.id.details_num_products)).getText().toString()));
            customer.setRestockFrequency(Integer.parseInt(((EditText) findViewById(R.id.details_restock_frequency)).getText().toString()));
            customer.setTurnOver(Double.parseDouble(((EditText) findViewById(R.id.details_turn_over)).getText().toString()));
            customer.setLongitude(capturedLongitude);
            customer.setLatitude(capturedLatitude);
            customer.setSubcounty(((Subcounty) subcountySpinner.getSelectedItem()));
            customer.setVillage(((EditText) findViewById(R.id.details_village)).getText().toString());
            customer.setParish(((EditText) findViewById(R.id.details_parish)).getText().toString());
            customerDao.insert(customer);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;

    }
}