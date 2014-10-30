package org.chai.activities.customer;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.chai.R;
import org.chai.adapter.SubcountyArrayAdapter;
import org.chai.model.*;
import org.chai.util.GPSTracker;

import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 10/29/14.
 */
public class CustomerForm extends Activity {

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

    private Customer customerInstance;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_form);
        initialiseGreenDao();
        Bundle bundle = getIntent().getExtras();
        Long customerId = bundle.getLong("id");
        setCustomerInstance(customerId);
        try {
            List<Subcounty> subcountyData = subcountyDao.loadAll();
            subcounties = subcountyData.toArray(new Subcounty[subcountyData.size()]);
            subcountySpinner = (Spinner) findViewById(R.id.details_subcounty);

            adapter = new SubcountyArrayAdapter(this, android.R.layout.simple_spinner_item, subcounties);
            subcountySpinner.setAdapter(adapter);
            Button saveCustomerBtn = (Button) findViewById(R.id.menu_add_new_customer);
            setSaveButtonText(saveCustomerBtn);
            saveCustomerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isSaved = saveCustomer();
                    if (isSaved) {
                        Toast.makeText(getApplicationContext(), "New Customer has been  successfully added!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "A problem Occured while saving a new Customer,please ensure that data is entered correctly", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception ex) {
            //
        }
        Button showGps = (Button) findViewById(R.id.new_customer_capture_gps);
        showGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpsTracker = new GPSTracker(CustomerForm.this);
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
        bindCustomerToUI();
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
            subcountyDao = daoSession.getSubcountyDao();
        } catch (Exception ex) {
            Log.d("Error=====================================",ex.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_customer_form_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_add_new_customer:
                boolean isSaved = saveCustomer();
                if (isSaved) {
                    Intent intent = new Intent(getApplicationContext(), CustomersMainActivity.class);
                    startActivity(intent);
                }
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void getCustomerFormValues() {
        try {
            customerInstance.setSysid(UUID.randomUUID().toString());
            customerInstance.setOutletName(((EditText) findViewById(R.id.detailsname)).getText().toString());
            customerInstance.setTenureLengthYears(Integer.parseInt(((EditText) findViewById(R.id.details_tenure_length_years)).getText().toString()));
            customerInstance.setTenureLengthMonths(Integer.parseInt(((EditText) findViewById(R.id.details_tenure_length_months)).getText().toString()));
            customerInstance.setOutletType(((Spinner) findViewById(R.id.details_outlet_type)).getSelectedItem().toString());
            customerInstance.setOutletSize(((Spinner) findViewById(R.id.details_size)).getSelectedItem().toString());
            customerInstance.setSplit(((Spinner) findViewById(R.id.details_split)).getSelectedItem().toString());
            customerInstance.setOpeningHours(((Spinner) findViewById(R.id.details_opening_hrs)).getSelectedItem().toString());

            customerInstance.setMajoritySourceOfSupply(((EditText) findViewById(R.id.details_sources_of_supply)).getText().toString());
            customerInstance.setKeyWholeSalerName(((EditText) findViewById(R.id.details_key_wholesaler_name)).getText().toString());
            customerInstance.setKeyWholeSalerContact(((EditText) findViewById(R.id.details_key_wholesaler_contact)).getText().toString());

            customerInstance.setBuildingStructure(((Spinner) findViewById(R.id.details_building_structure)).getSelectedItem().toString());

            customerInstance.setEquipment(((EditText) findViewById(R.id.details_equipment)).getText().toString());
            customerInstance.setDescriptionOfOutletLocation(((EditText) findViewById(R.id.details_desc_location)).getText().toString());
            customerInstance.setNumberOfEmployees(Integer.parseInt(((EditText) findViewById(R.id.details_number_of_employees)).getText().toString()));
            customerInstance.setNumberOfBranches(Integer.parseInt(((EditText) findViewById(R.id.details_number_of_branches)).getText().toString()));
            customerInstance.setNumberOfCustomersPerDay(Integer.parseInt(((EditText) findViewById(R.id.details_num_customers_per_day)).getText().toString()));
            customerInstance.setNumberOfProducts(Integer.parseInt(((EditText) findViewById(R.id.details_num_products)).getText().toString()));
            customerInstance.setRestockFrequency(Integer.parseInt(((EditText) findViewById(R.id.details_restock_frequency)).getText().toString()));
            customerInstance.setTurnOver(Double.parseDouble(((EditText) findViewById(R.id.details_turn_over)).getText().toString()));
            customerInstance.setLongitude(capturedLongitude);
            customerInstance.setLatitude(capturedLatitude);
            customerInstance.setSubcounty(((Subcounty) subcountySpinner.getSelectedItem()));
            customerInstance.setVillage(((EditText) findViewById(R.id.details_village)).getText().toString());
            customerInstance.setParish(((EditText) findViewById(R.id.details_parish)).getText().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void bindCustomerToUI() {
        if (!isNewCustomer()) {
            ((EditText) findViewById(R.id.detailsname)).setText(customerInstance.getOutletName() == null ? "" : customerInstance.getOutletName());
            ((EditText) findViewById(R.id.details_tenure_length_years)).setText(customerInstance.getTenureLengthYears() == null ? "" : customerInstance.getTenureLengthYears() + "");
            ((EditText) findViewById(R.id.details_tenure_length_months)).setText(customerInstance.getTenureLengthMonths() == null ? "" : customerInstance.getTenureLengthMonths() + "");

            ((EditText) findViewById(R.id.details_sources_of_supply)).setText(customerInstance.getMajoritySourceOfSupply() == null ? "" : customerInstance.getMajoritySourceOfSupply());
            ((EditText) findViewById(R.id.details_key_wholesaler_name)).setText(customerInstance.getKeyWholeSalerName() == null ? "" : customerInstance.getKeyWholeSalerName());
            ((EditText) findViewById(R.id.details_key_wholesaler_contact)).setText(customerInstance.getKeyWholeSalerContact() == null ? "" : customerInstance.getKeyWholeSalerContact());


            ((EditText) findViewById(R.id.details_equipment)).setText(customerInstance.getEquipment() == null ? "" : customerInstance.getEquipment());
            ((EditText) findViewById(R.id.details_desc_location)).setText(customerInstance.getDescriptionOfOutletLocation() == null ? "" : customerInstance.getDescriptionOfOutletLocation());
            ((EditText) findViewById(R.id.details_number_of_employees)).setText(customerInstance.getNumberOfEmployees() == null ? "" : customerInstance.getNumberOfEmployees() + "");
            ((EditText) findViewById(R.id.details_number_of_branches)).setText(customerInstance.getNumberOfBranches() == null ? "" : customerInstance.getNumberOfBranches() + "");
            ((EditText) findViewById(R.id.details_num_customers_per_day)).setText(customerInstance.getNumberOfCustomersPerDay() == null ? "" : customerInstance.getNumberOfCustomersPerDay() + "");
            ((EditText) findViewById(R.id.details_num_products)).setText(customerInstance.getNumberOfProducts() == null ? "" : customerInstance.getNumberOfProducts() + "");
            ((EditText) findViewById(R.id.details_restock_frequency)).setText(customerInstance.getRestockFrequency() == null ? "" : customerInstance.getRestockFrequency() + "");
            ((EditText) findViewById(R.id.details_turn_over)).setText(customerInstance.getTurnOver() == null ? "" : customerInstance.getTurnOver() + "");
            ((EditText) findViewById(R.id.details_village)).setText(customerInstance.getVillage() == null ? "" : customerInstance.getVillage());
            ((EditText) findViewById(R.id.details_parish)).setText(customerInstance.getParish() == null ? "" : customerInstance.getParish());


            Spinner outletTypeSpinner = (Spinner) findViewById(R.id.details_outlet_type);
            setSpinnerSelection(outletTypeSpinner, customerInstance.getOutletType());

            Spinner outletSizeSpinner = (Spinner) findViewById(R.id.details_size);
            setSpinnerSelection(outletSizeSpinner, customerInstance.getOutletSize());

            Spinner splitSpinner = (Spinner) findViewById(R.id.details_split);
            setSpinnerSelection(splitSpinner, customerInstance.getSplit());

            Spinner openingHrsSpinner = (Spinner) findViewById(R.id.details_opening_hrs);
            setSpinnerSelection(openingHrsSpinner, customerInstance.getOpeningHours());

            Spinner buildingStructureSpinner = (Spinner) findViewById(R.id.details_building_structure);
            setSpinnerSelection(buildingStructureSpinner, customerInstance.getBuildingStructure());

            Spinner subcountySpinner2 = (Spinner) findViewById(R.id.details_subcounty);
            setSpinnerSelection(subcountySpinner2, customerInstance.getSubcounty().getName());
        }
    }

    private boolean saveCustomer() {
        boolean isSaved = false;
        try{
            if (customerInstance != null) {
                getCustomerFormValues();
                if (isNewCustomer()) {
                    customerDao.insert(customerInstance);
                } else {
                    customerDao.update(customerInstance);
                }
                isSaved = true;
            }
        }catch (Exception ex){

        }
        return isSaved;
    }

    private boolean isNewCustomer() {
        if (customerInstance.getId() == null || customerInstance.getId() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void setCustomerInstance(Long id) {
        try {
            if (id == null || id == 0) {
                customerInstance = new Customer(null);
            } else {
                customerInstance = customerDao.load(id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            customerInstance = new Customer(null);
        }
    }

    private void setSaveButtonText(Button saveCustomerBtn){
        if(isNewCustomer()){
            saveCustomerBtn.setText("Add New Customer");
        }else{
            saveCustomerBtn.setText("Update Customer");
        }
    }

    private void setSpinnerSelection(Spinner spinner, String item) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int position = adapter.getPosition(item);
        spinner.setSelection(position);
    }
}