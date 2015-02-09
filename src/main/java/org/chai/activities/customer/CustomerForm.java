package org.chai.activities.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.chai.R;
import org.chai.activities.HomeActivity;
import org.chai.adapter.DistrictArrayAdapter;
import org.chai.adapter.ParishArrayAdapter;
import org.chai.adapter.SubcountyArrayAdapter;
import org.chai.model.*;
import org.chai.util.GPSTracker;
import org.chai.util.Utils;

import java.util.*;

/**
 * Created by victor on 10/29/14.
 */
public class CustomerForm extends Activity {

    private Spinner subcountySpinner;
    private Spinner districtSpinner;
    private DistrictArrayAdapter adapter;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;
    private SubcountyDao subcountyDao;
    private DistrictDao districtDao;
    private VillageDao villageDao;
    private CustomerContactDao customerContactDao;

    private GPSTracker gpsTracker;
    private double capturedLatitude;
    private double capturedLongitude;

    private Customer customerInstance;
    private List<CustomerContact> customerContacts = new ArrayList<CustomerContact>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_form);
        initialiseGreenDao();
        Bundle bundle = getIntent().getExtras();
        String customerId = bundle.getString("id");
        setCustomerInstance(customerId);
        try {
            List<Subcounty> subcountiesList = subcountyDao.loadAll();
            List<District> districtList = districtDao.loadAll();

            subcountySpinner = (Spinner) findViewById(R.id.details_subcounty);
            districtSpinner = (Spinner) findViewById(R.id.details_district);
            districtSpinner.setAdapter(new DistrictArrayAdapter(this,R.id.details_district,districtList.toArray(new District[districtList.size()])));

            districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String districtId = ((District) districtSpinner.getSelectedItem()).getUuid();
                    List<Subcounty> subcounties = subcountyDao.queryBuilder().where(SubcountyDao.Properties.DistrictId.eq(districtId)).list();
                    subcountySpinner.setAdapter(new SubcountyArrayAdapter(getApplicationContext(),R.id.details_subcounty,subcounties.toArray(new Subcounty[subcounties.size()])));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            subcountySpinner.setAdapter(new SubcountyArrayAdapter(this, R.id.details_subcounty, subcountiesList.toArray(new Subcounty[subcountiesList.size()])));

            Button saveCustomerBtn = (Button) findViewById(R.id.menu_add_new_customer);
            setSaveButtonText(saveCustomerBtn);
            saveCustomerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(customerInstance.getUuid()==null && customerContacts.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter atleast one contact!", Toast.LENGTH_LONG).show();
                    }else{
                        boolean isSaved = saveCustomer();
                        if (isSaved) {
                            Toast.makeText(getApplicationContext(), "New Customer has been  successfully added!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "A problem Occured while saving a new Customer,please ensure that data is entered correctly", Toast.LENGTH_LONG).show();
                        }
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

        Button newContactBtn = (Button) findViewById(R.id.menu_add_new_customer_contact);
        newContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater) CustomerForm.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View entryView = layoutInflater.inflate(R.layout.add_contact_layout, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(CustomerForm.this);
                alert.setIcon(R.drawable.icon).setTitle("New Contact").setView(entryView).setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int button) {
                        CustomerContact customerContact = new CustomerContact(null);
                        customerContact.setUuid(UUID.randomUUID().toString());
                        customerContact.setContact(((EditText) entryView.findViewById(R.id.customer_contact_telephone)).getText().toString());
                        customerContact.setNames(((EditText) entryView.findViewById(R.id.customer_contact_names)).getText().toString());
                        customerContact.setGender(((Spinner) entryView.findViewById(R.id.customer_contact_gender)).getSelectedItem().toString());
                        customerContact.setRole(((Spinner) entryView.findViewById(R.id.customer_contact_type)).getSelectedItem().toString());
                        customerContacts.add(customerContact);
                        //add to parent form
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.customer_contacts_layout);

                        TextView contactView = new TextView(CustomerForm.this);
                        contactView.setText(customerContact.getNames() + ":" + customerContact.getContact());
                        contactView.setTextSize(18);
                        contactView.setTextColor(Color.parseColor("#000000"));
                        linearLayout.addView(contactView);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int button) {

                    }
                });
                alert.show();
            }
        });
        bindCustomerToUI();
        setMandatoryFields();
        manageLicenceVisible();
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            subcountyDao = daoSession.getSubcountyDao();
            districtDao = daoSession.getDistrictDao();
            customerDao = daoSession.getCustomerDao();
            customerContactDao = daoSession.getCustomerContactDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
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
            case R.id.customer_form_home:
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void bindUIToCustomer() {
        try {
            customerInstance.setOutletName(((EditText) findViewById(R.id.detailsname)).getText().toString());
            customerInstance.setDateOutletOpened(Utils.stringToDate(((EditText) findViewById(R.id.details_date_outlet_opened)).getText().toString()));
            customerInstance.setOutletType(((Spinner) findViewById(R.id.details_outlet_type)).getSelectedItem().toString());
            customerInstance.setOutletSize(((Spinner) findViewById(R.id.details_size)).getSelectedItem().toString());
            customerInstance.setSplit(((Spinner) findViewById(R.id.details_split)).getSelectedItem().toString());

            customerInstance.setMajoritySourceOfSupply(((EditText) findViewById(R.id.details_sources_of_supply)).getText().toString());
            customerInstance.setKeyWholeSalerName(((EditText) findViewById(R.id.details_key_wholesaler_name)).getText().toString());
            customerInstance.setKeyWholeSalerContact(((EditText) findViewById(R.id.details_key_wholesaler_contact)).getText().toString());

            String licenceVisible = ((Spinner)findViewById(R.id.details_licence_visible)).getSelectedItem().toString();
            customerInstance.setLicenceVisible(licenceVisible.equalsIgnoreCase("Yes") ? true : false);
            customerInstance.setTypeOfLicence(((Spinner) findViewById(R.id.details_licence_type)).getSelectedItem().toString().toLowerCase());

            customerInstance.setDescriptionOfOutletLocation(((EditText) findViewById(R.id.details_desc_location)).getText().toString());
            customerInstance.setNumberOfEmployees(Integer.parseInt(((EditText) findViewById(R.id.details_number_of_employees)).getText().toString()));
            customerInstance.setNumberOfCustomersPerDay(Integer.parseInt(((EditText) findViewById(R.id.details_num_customers_per_day)).getText().toString()));
            customerInstance.setRestockFrequency(((Spinner) findViewById(R.id.details_restock_frequency)).getSelectedItem().toString().toLowerCase());
            customerInstance.setLongitude(capturedLongitude);
            customerInstance.setLatitude(capturedLatitude);
            customerInstance.setSubcountyId(((Subcounty) subcountySpinner.getSelectedItem()).getUuid());
            customerInstance.setSubcountyUuid(((Subcounty) subcountySpinner.getSelectedItem()).getUuid());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void bindCustomerToUI() {
        if (!isNewCustomer()) {
            ((EditText) findViewById(R.id.detailsname)).setText(customerInstance.getOutletName() == null ? "" : customerInstance.getOutletName());
            ((EditText) findViewById(R.id.details_date_outlet_opened)).setText(customerInstance.getDateOutletOpened() == null ? "" : Utils.dateToString(customerInstance.getDateOutletOpened()));

            ((EditText) findViewById(R.id.details_sources_of_supply)).setText(customerInstance.getMajoritySourceOfSupply() == null ? "" : customerInstance.getMajoritySourceOfSupply());
            ((EditText) findViewById(R.id.details_key_wholesaler_name)).setText(customerInstance.getKeyWholeSalerName() == null ? "" : customerInstance.getKeyWholeSalerName());
            ((EditText) findViewById(R.id.details_key_wholesaler_contact)).setText(customerInstance.getKeyWholeSalerContact() == null ? "" : customerInstance.getKeyWholeSalerContact());


            ((EditText) findViewById(R.id.details_desc_location)).setText(customerInstance.getDescriptionOfOutletLocation() == null ? "" : customerInstance.getDescriptionOfOutletLocation());
            ((EditText) findViewById(R.id.details_number_of_employees)).setText(customerInstance.getNumberOfEmployees() == null ? "" : customerInstance.getNumberOfEmployees() + "");
            ((EditText) findViewById(R.id.details_num_customers_per_day)).setText(customerInstance.getNumberOfCustomersPerDay() == null ? "" : customerInstance.getNumberOfCustomersPerDay() + "");
            ((EditText) findViewById(R.id.details_gps)).setText(customerInstance.getLatitude()+","+customerInstance.getLongitude());

            Spinner restockFreq = (Spinner)findViewById(R.id.details_restock_frequency);
            setSpinnerSelection(restockFreq,customerInstance.getRestockFrequency() == null ? "" : customerInstance.getRestockFrequency() + "");

            Spinner licenceVisibleSpinner = ((Spinner) findViewById(R.id.details_licence_visible));
            Boolean licenceVisible = customerInstance.getLicenceVisible();
            if(licenceVisible!=null){
                setSpinnerSelection(licenceVisibleSpinner, licenceVisible ? "Yes" : "No");
            }

            Spinner outletTypeSpinner = (Spinner) findViewById(R.id.details_outlet_type);
            setSpinnerSelection(outletTypeSpinner, customerInstance.getOutletType());

            Spinner outletSizeSpinner = (Spinner) findViewById(R.id.details_size);
            setSpinnerSelection(outletSizeSpinner, customerInstance.getOutletSize());

            Spinner splitSpinner = (Spinner) findViewById(R.id.details_split);
            setSpinnerSelection(splitSpinner, customerInstance.getSplit());

        }
    }

    private boolean saveCustomer() {
        boolean isSaved = false;
        try {
            if (customerInstance != null && allMandatoryFieldsFilled()) {
                bindUIToCustomer();
                customerInstance.setIsDirty(true);
                if (isNewCustomer()) {
                    customerInstance.setUuid(UUID.randomUUID().toString());
                    Long customerId = customerDao.insert(customerInstance);
                    saveCustomerContacts(customerInstance.getUuid());
                } else {
                    customerDao.update(customerInstance);
                }
                isSaved = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isSaved;
    }

    private boolean allMandatoryFieldsFilled() {
        if (((EditText) findViewById(R.id.detailsname)).getText().toString().equals("")) {
            return false;
        } else if (((Spinner) findViewById(R.id.details_outlet_type)).getSelectedItem().toString().equals("")) {
            return false;
        } else if (((Spinner) findViewById(R.id.details_size)).getSelectedItem().toString().equals("")) {
            return false;
        } else if (((EditText) findViewById(R.id.details_desc_location)).getText().toString().equals("")) {
            return false;
        } else if (((EditText) findViewById(R.id.details_date_outlet_opened)).getText().toString().equals("")) {
            return false;
        } else if (((EditText) findViewById(R.id.details_number_of_employees)).getText().toString().equals("")) {
            return false;
        } else if (((EditText) findViewById(R.id.details_num_customers_per_day)).getText().toString().equals("")) {
            return false;
        } else if (((EditText) findViewById(R.id.details_sources_of_supply)).getText().toString().equals("")) {
            return false;
        } else if (((Spinner) findViewById(R.id.details_restock_frequency)).getSelectedItem().toString().equals("")) {
            return false;
        }else if (((Spinner) findViewById(R.id.details_split)).getSelectedItem().toString().equals("")) {
            return false;
        }
        return true;
    }

    private boolean isNewCustomer() {
        if (customerInstance.getUuid() == null) {
            return true;
        } else {
            return false;
        }
    }

    private void setCustomerInstance(String id) {
        try {
            if (id == null) {
                customerInstance = new Customer(null);
            } else {
                customerInstance = customerDao.load(id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setSaveButtonText(Button saveCustomerBtn) {
        if (isNewCustomer()) {
            saveCustomerBtn.setText("Add New Customer");
        } else {
            saveCustomerBtn.setText("Update Customer");
        }
    }

    private void setSpinnerSelection(Spinner spinner, String item) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int position = adapter.getPosition(item);
        spinner.setSelection(position);
    }

    private void saveCustomerContacts(String customerId) {
        for (CustomerContact customerContact : customerContacts) {
            customerContact.setCustomerId(customerId);
            customerContactDao.insert(customerContact);
        }
    }

    private void setMandatoryFields() {
        Utils.setRequired((TextView) findViewById(R.id.detailsnameview));
        Utils.setRequired((TextView) findViewById(R.id.details_outlet_type_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_size_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_subcounty_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_district_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_desc_location_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_date_outlet_opened_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_number_of_employees_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_num_customers_per_day_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_sources_of_supply_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_restock_frequency_lbl));
        Utils.setRequired((TextView) findViewById(R.id.outlet_rural_village));
    }

    protected void manageLicenceVisible() {
        final Spinner spinner = (Spinner)findViewById(R.id.details_licence_visible);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout licenceTypeLayout = (LinearLayout)findViewById(R.id.type_of_licence_layout);
                if ("No".equalsIgnoreCase(selected)) {
                    licenceTypeLayout.setVisibility(View.GONE);
                } else {
                    licenceTypeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}