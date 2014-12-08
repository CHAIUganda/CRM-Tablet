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
import org.chai.adapter.ParishArrayAdapter;
import org.chai.adapter.SubcountyArrayAdapter;
import org.chai.adapter.VillageArrayAdapter;
import org.chai.model.*;
import org.chai.util.GPSTracker;
import org.chai.util.Utils;

import java.util.*;

/**
 * Created by victor on 10/29/14.
 */
public class CustomerForm extends Activity {

    private Spinner subcountySpinner;
    private Spinner parishSpinner;
    private Spinner villageSpinner;
    private VillageArrayAdapter adapter;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;
    private SubcountyDao subcountyDao;
    private ParishDao parishDao;
    private VillageDao villageDao;
    private CustomerContactDao customerContactDao;

    private GPSTracker gpsTracker;
    private double capturedLatitude;
    private double capturedLongitude;

    private Customer customerInstance;
    private List<CustomerContact> customerContacts = new ArrayList<CustomerContact>();
    private DatePickerDialog datePickerDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_form);
        initialiseGreenDao();
        Bundle bundle = getIntent().getExtras();
        Long customerId = bundle.getLong("id");
        setCustomerInstance(customerId);
        try {
            List<Subcounty> subcountiesList = subcountyDao.loadAll();
            List<Parish> parishes = parishDao.loadAll();
            List<Village> villageData = villageDao.loadAll();

            subcountySpinner = (Spinner) findViewById(R.id.details_subcounty);
            parishSpinner = (Spinner) findViewById(R.id.details_parish);
            villageSpinner = (Spinner) findViewById(R.id.details_village);

            subcountySpinner.setAdapter(new SubcountyArrayAdapter(this, R.id.details_subcounty, subcountiesList.toArray(new Subcounty[subcountiesList.size()])));

            adapter = new VillageArrayAdapter(this, android.R.layout.simple_spinner_item, villageData.toArray(new Village[villageData.size()]));
            villageSpinner.setAdapter(adapter);

            subcountySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    Long subcountyId = ((Subcounty) subcountySpinner.getSelectedItem()).getId();
                    List<Parish> parishList = parishDao.queryBuilder().where(ParishDao.Properties.SubCountyId.eq(subcountyId)).list();
                    parishSpinner.setAdapter(new ParishArrayAdapter(getApplicationContext(), R.id.details_parish, parishList.toArray(new Parish[parishList.size()])));

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            parishSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Long parishId = ((Parish) parishSpinner.getSelectedItem()).getId();
                    List<Village> villageList = villageDao.queryBuilder().where(VillageDao.Properties.ParishId.eq(parishId)).list();
                    villageSpinner.setAdapter(new VillageArrayAdapter(getApplicationContext(), R.id.details_village, villageList.toArray(new Village[villageList.size()])));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            Button saveCustomerBtn = (Button) findViewById(R.id.menu_add_new_customer);
            setSaveButtonText(saveCustomerBtn);
            saveCustomerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isSaved = saveCustomer();
                    if (isSaved) {
                        Toast.makeText(getApplicationContext(), "New Customer has been  successfully added!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), CustomersMainFragment.class);
                        startActivity(intent);
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
                        customerContact.setTitle(((EditText) entryView.findViewById(R.id.customer_contact_telephone)).getText().toString());
                        customerContact.setFirstName(((EditText) entryView.findViewById(R.id.customer_contact_firstname)).getText().toString());
                        customerContact.setSurname(((EditText) entryView.findViewById(R.id.customer_contact_surname)).getText().toString());
                        customerContact.setGender(((Spinner) entryView.findViewById(R.id.customer_contact_gender)).getSelectedItem().toString());
                        customerContact.setNetworkOrAssociation(Boolean.valueOf(((Spinner) entryView.findViewById(R.id.customer_contact_network)).getSelectedItem().toString()));
                        customerContact.setRole(((Spinner) entryView.findViewById(R.id.customer_contact_type)).getSelectedItem().toString());
                        customerContacts.add(customerContact);
                        //add to parent form
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.customer_contacts_layout);

                        TextView contactView = new TextView(CustomerForm.this);
                        contactView.setText(customerContact.getFirstName() + ":" + customerContact.getTitle());
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
//        setDateWidget();
        bindCustomerToUI();
        setMandatoryFields();
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
            subcountyDao = daoSession.getSubcountyDao();
            parishDao = daoSession.getParishDao();
            customerContactDao = daoSession.getCustomerContactDao();
            villageDao = daoSession.getVillageDao();
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
            customerInstance.setUuid(UUID.randomUUID().toString());
            customerInstance.setOutletName(((EditText) findViewById(R.id.detailsname)).getText().toString());
            customerInstance.setDateOutletOpened(Utils.stringToDate(((EditText) findViewById(R.id.details_date_outlet_opened)).getText().toString()));
            customerInstance.setOutletType(((Spinner) findViewById(R.id.details_outlet_type)).getSelectedItem().toString());
            customerInstance.setOutletSize(((Spinner) findViewById(R.id.details_size)).getSelectedItem().toString());
            customerInstance.setSplit(((Spinner) findViewById(R.id.details_split)).getSelectedItem().toString());
            customerInstance.setOpeningHours(((Spinner) findViewById(R.id.details_opening_hrs)).getSelectedItem().toString());

            customerInstance.setMajoritySourceOfSupply(((EditText) findViewById(R.id.details_sources_of_supply)).getText().toString());
            customerInstance.setKeyWholeSalerName(((EditText) findViewById(R.id.details_key_wholesaler_name)).getText().toString());
            customerInstance.setKeyWholeSalerContact(((EditText) findViewById(R.id.details_key_wholesaler_contact)).getText().toString());

            customerInstance.setBuildingStructure(((Spinner) findViewById(R.id.details_building_structure)).getSelectedItem().toString());

            customerInstance.setDescriptionOfOutletLocation(((EditText) findViewById(R.id.details_desc_location)).getText().toString());
            customerInstance.setNumberOfEmployees(Integer.parseInt(((EditText) findViewById(R.id.details_number_of_employees)).getText().toString()));
            customerInstance.setHasSisterBranch(Boolean.valueOf(((Spinner) findViewById(R.id.details_has_sister_branches)).getSelectedItem().toString().toLowerCase()));
            customerInstance.setNumberOfCustomersPerDay(Integer.parseInt(((EditText) findViewById(R.id.details_num_customers_per_day)).getText().toString()));
            customerInstance.setNumberOfProducts(((EditText) findViewById(R.id.details_num_products)).getText().toString());
            customerInstance.setRestockFrequency(Integer.parseInt(((EditText) findViewById(R.id.details_restock_frequency)).getText().toString()));
            customerInstance.setTurnOver(((EditText) findViewById(R.id.details_turn_over)).getText().toString());
            customerInstance.setLongitude(capturedLongitude);
            customerInstance.setLatitude(capturedLatitude);
            customerInstance.setVillageId(((Village) villageSpinner.getSelectedItem()).getId());
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
            ((EditText) findViewById(R.id.details_num_products)).setText(customerInstance.getNumberOfProducts() == null ? "" : customerInstance.getNumberOfProducts() + "");
            ((EditText) findViewById(R.id.details_restock_frequency)).setText(customerInstance.getRestockFrequency() == null ? "" : customerInstance.getRestockFrequency() + "");

            ((EditText) findViewById(R.id.details_turn_over)).setText(customerInstance.getTurnOver() == null ? "" : customerInstance.getTurnOver() + "");

            Spinner hasSisterBranches = ((Spinner) findViewById(R.id.details_has_sister_branches));
            setSpinnerSelection(hasSisterBranches, customerInstance.getHasSisterBranch() == null ? "no" : "yes");

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

            villageSpinner = (Spinner) findViewById(R.id.details_village);
            VillageArrayAdapter adapter = (VillageArrayAdapter) villageSpinner.getAdapter();
            int position = adapter.getPosition(customerInstance.getVillage());
            villageSpinner.setSelection(position);
        }
    }

    private boolean saveCustomer() {
        boolean isSaved = false;
        try {
            if (customerInstance != null && allMandatoryFieldsFilled()) {
                bindUIToCustomer();
                customerInstance.setIsDirty(true);
                if (isNewCustomer()) {
                    Long customerId = customerDao.insert(customerInstance);
                    saveCustomerContacts(customerId);
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
        } else if (((Spinner) findViewById(R.id.details_opening_hrs)).getSelectedItem().toString().equals("")) {
            return false;
        } else if (((EditText) findViewById(R.id.details_date_outlet_opened)).getText().toString().equals("")) {
            return false;
        } else if (((EditText) findViewById(R.id.details_number_of_employees)).getText().toString().equals("")) {
            return false;
        } else if (((EditText) findViewById(R.id.details_turn_over)).getText().toString().equals("")) {
            return false;
        }  else if (((Spinner) findViewById(R.id.details_has_sister_branches)).getSelectedItem().toString().equals("")) {
            return false;
        } else if (((EditText) findViewById(R.id.details_num_customers_per_day)).getText().toString().equals("")) {
            return false;
        } else if (((EditText) findViewById(R.id.details_sources_of_supply)).getText().toString().equals("")) {
            return false;
        } else if (((EditText) findViewById(R.id.details_restock_frequency)).getText().toString().equals("")) {
            return false;
        } else if (((EditText) findViewById(R.id.details_num_products)).getText().toString().equals("")) {
            return false;
        } else if (((Spinner) findViewById(R.id.details_building_structure)).getSelectedItem().toString().equals("")) {
            return false;
        }
        return true;
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

    private void saveCustomerContacts(Long customerId) {
        for (CustomerContact customerContact : customerContacts) {
            customerContact.setCustomerId(customerId);
            customerContactDao.insert(customerContact);
        }
    }

    private void setDateWidget() {
       /* Button dateBtn = (Button) findViewById(R.id.details_date_outlet_opened_btn);
        final EditText dateEditTxt = (EditText) findViewById(R.id.details_date_outlet_opened);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = null;
                String existingDate = (String) dateEditTxt.getText().toString();
                if (existingDate != null && !existingDate.equals("")) {
                    String initialDate;
                    String initialMonth;
                    String initialYear;
                    StringTokenizer stringTokenizer = new StringTokenizer(existingDate, "/");
                    initialMonth = stringTokenizer.nextToken();
                    initialDate = stringTokenizer.nextToken();
                    initialYear = stringTokenizer.nextToken();
                    if (datePickerDialog == null) {
                        datePickerDialog = new DatePickerDialog(view.getContext(), new PickDate(), Integer.parseInt(initialYear),
                                Integer.parseInt(initialMonth) - 1,
                                Integer.parseInt(initialDate));

                        datePickerDialog.updateDate(Integer.parseInt(initialYear),
                                Integer.parseInt(initialMonth) - 1,
                                Integer.parseInt(initialDate));
                    }
                } else {
                    calendar = Calendar.getInstance();
                    if (datePickerDialog == null) {
                        datePickerDialog = new DatePickerDialog(view.getContext(), new PickDate(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    }
                }
                datePickerDialog.show();

            }
        });*/
    }

    private class PickDate implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            view.updateDate(year, monthOfYear, dayOfMonth);
            ((EditText) findViewById(R.id.details_date_outlet_opened)).setText(monthOfYear + "/" + dayOfMonth + "/" + year);
            datePickerDialog.hide();
        }
    }

    private void setMandatoryFields() {
        Utils.setRequired((TextView) findViewById(R.id.detailsnameview));
        Utils.setRequired((TextView) findViewById(R.id.details_outlet_type_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_size_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_subcounty_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_parish_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_village_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_desc_location_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_opening_hrs_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_date_outlet_opened_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_number_of_employees_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_turn_over_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_has_sister_branches_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_num_customers_per_day_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_sources_of_supply_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_restock_frequency_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_num_products_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_building_structure_lbl));
    }

}