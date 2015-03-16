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
import android.text.Editable;
import android.text.TextWatcher;
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
import org.chai.util.customwidget.ContactWidgetView;
import org.chai.util.customwidget.GpsWidgetView;

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


    private Customer customerInstance;
//    private List<CustomerContact> customerContacts = new ArrayList<CustomerContact>();
    private List<ContactWidgetView> contactsToUpdate = new ArrayList<ContactWidgetView>();

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
            districtSpinner.setAdapter(new DistrictArrayAdapter(this, R.id.details_district, districtList.toArray(new District[districtList.size()])));

            districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String districtId = ((District) districtSpinner.getSelectedItem()).getUuid();
                    List<Subcounty> subcounties = subcountyDao.queryBuilder().where(SubcountyDao.Properties.DistrictId.eq(districtId)).list();
                    subcountySpinner.setAdapter(new SubcountyArrayAdapter(getApplicationContext(), R.id.details_subcounty, subcounties.toArray(new Subcounty[subcounties.size()])));
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
                    if (customerInstance.getUuid() == null && contactsToUpdate.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter atleast one contact!", Toast.LENGTH_LONG).show();
                    } else {
                        boolean isSaved = saveCustomer();
                        if (isSaved) {
                            Toast.makeText(getApplicationContext(), "Customer has been  successfully saved!", Toast.LENGTH_LONG).show();
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

        Button newContactBtn = (Button) findViewById(R.id.menu_add_new_customer_contact);
        newContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater) CustomerForm.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View entryView = layoutInflater.inflate(R.layout.add_contact_layout, null);
                final EditText contactNumber = ((EditText) entryView.findViewById(R.id.customer_contact_telephone));
                final AlertDialog.Builder alert = new AlertDialog.Builder(CustomerForm.this);
                alert.setIcon(R.drawable.icon).setTitle("New Contact").setView(entryView).setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int button) {

                        CustomerContact customerContact = new CustomerContact(null);
                        customerContact.setContact(((EditText) entryView.findViewById(R.id.customer_contact_telephone)).getText().toString());
                        customerContact.setNames(((EditText) entryView.findViewById(R.id.customer_contact_names)).getText().toString());
                        customerContact.setGender(((Spinner) entryView.findViewById(R.id.customer_contact_gender)).getSelectedItem().toString());
                        customerContact.setRole(((Spinner) entryView.findViewById(R.id.customer_contact_type)).getSelectedItem().toString());
//                        customerContacts.add(customerContact);
                        //add to parent form
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.customer_contacts_layout);
                        ContactWidgetView contactWidgetView = new ContactWidgetView(CustomerForm.this, customerContact);
                        contactsToUpdate.add(contactWidgetView);
                        linearLayout.addView(contactWidgetView);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int button) {

                    }
                });
                final AlertDialog dialog = alert.create();
                dialog.show();
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                contactMandatoryFieldsEntered(contactNumber,dialog);
            }
        });
        bindCustomerToUI();
        setMandatoryFields();
        manageLicenceVisible();
    }

    private void contactMandatoryFieldsEntered(EditText contactNumber, final AlertDialog dialog) {
        contactNumber.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {}
            @Override public void onTextChanged(CharSequence c, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // Will be called AFTER text has been changed.
                if (editable.toString().length() == 0){
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
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
                return true;
            case R.id.customer_logout:
                HomeActivity.logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void bindUIToCustomer() {
        try {
            customerInstance.setOutletName(((EditText) findViewById(R.id.detailsname)).getText().toString());
            customerInstance.setLengthOpen(((EditText) findViewById(R.id.details_date_outlet_opened)).getText().toString());
            customerInstance.setOutletType(((Spinner) findViewById(R.id.details_outlet_type)).getSelectedItem().toString());
            customerInstance.setOutletSize(((Spinner) findViewById(R.id.details_size)).getSelectedItem().toString());
            customerInstance.setSplit(((Spinner) findViewById(R.id.details_split)).getSelectedItem().toString());

            customerInstance.setMajoritySourceOfSupply(((EditText) findViewById(R.id.details_sources_of_supply)).getText().toString());
            customerInstance.setKeyWholeSalerName(((EditText) findViewById(R.id.details_key_wholesaler_name)).getText().toString());
            customerInstance.setKeyWholeSalerContact(((EditText) findViewById(R.id.details_key_wholesaler_contact)).getText().toString());

            String licenceVisible = ((Spinner) findViewById(R.id.details_licence_visible)).getSelectedItem().toString();
            customerInstance.setLicenceVisible(licenceVisible.equalsIgnoreCase("Yes") ? true : false);
            customerInstance.setTypeOfLicence(((Spinner) findViewById(R.id.details_licence_type)).getSelectedItem().toString().toLowerCase());

            customerInstance.setDescriptionOfOutletLocation(((EditText) findViewById(R.id.details_desc_location)).getText().toString());
            String emps = ((EditText) findViewById(R.id.details_number_of_employees)).getText().toString();
            customerInstance.setNumberOfEmployees(Integer.parseInt(emps.equals("") ? 0 + "" : emps));
            customerInstance.setNumberOfCustomersPerDay(Integer.parseInt(((EditText) findViewById(R.id.details_num_customers_per_day)).getText().toString()));
            customerInstance.setRestockFrequency(((Spinner) findViewById(R.id.details_restock_frequency)).getSelectedItem().toString().toLowerCase());
            if (!((GpsWidgetView) findViewById(R.id.customer_gps_view)).getLatLongText().equals("")) {
                String latLongText = ((GpsWidgetView) findViewById(R.id.customer_gps_view)).getLatLongText();
                customerInstance.setLongitude(Double.parseDouble(latLongText.split(",")[0]));
                customerInstance.setLatitude(Double.parseDouble(latLongText.split(",")[1]));
            }
            customerInstance.setSubcountyId(((Subcounty) subcountySpinner.getSelectedItem()).getUuid());
            customerInstance.setSubcountyUuid(((Subcounty) subcountySpinner.getSelectedItem()).getUuid());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void bindCustomerToUI() {
        if (!isNewCustomer()) {
            ((EditText) findViewById(R.id.detailsname)).setText(customerInstance.getOutletName() == null ? "" : customerInstance.getOutletName());
            ((EditText) findViewById(R.id.details_date_outlet_opened)).setText(customerInstance.getLengthOpen() == null ? "" : customerInstance.getLengthOpen());

            ((EditText) findViewById(R.id.details_sources_of_supply)).setText(customerInstance.getMajoritySourceOfSupply() == null ? "" : customerInstance.getMajoritySourceOfSupply());
            ((EditText) findViewById(R.id.details_key_wholesaler_name)).setText(customerInstance.getKeyWholeSalerName() == null ? "" : customerInstance.getKeyWholeSalerName());
            ((EditText) findViewById(R.id.details_key_wholesaler_contact)).setText(customerInstance.getKeyWholeSalerContact() == null ? "" : customerInstance.getKeyWholeSalerContact());


            ((EditText) findViewById(R.id.details_desc_location)).setText(customerInstance.getDescriptionOfOutletLocation() == null ? "" : customerInstance.getDescriptionOfOutletLocation());
            ((EditText) findViewById(R.id.details_number_of_employees)).setText(customerInstance.getNumberOfEmployees() == 0 ? "" : customerInstance.getNumberOfEmployees() + "");
            ((EditText) findViewById(R.id.details_num_customers_per_day)).setText(customerInstance.getNumberOfCustomersPerDay() == null ? "" : customerInstance.getNumberOfCustomersPerDay() + "");
            ((GpsWidgetView) findViewById(R.id.customer_gps_view)).setLatLongText(customerInstance.getLatitude() + "," + customerInstance.getLongitude());

            Spinner restockFreq = (Spinner) findViewById(R.id.details_restock_frequency);
            setSpinnerSelection(restockFreq, customerInstance.getRestockFrequency() == null ? "" : customerInstance.getRestockFrequency() + "");

            Spinner licenceVisibleSpinner = ((Spinner) findViewById(R.id.details_licence_visible));
            Boolean licenceVisible = customerInstance.getLicenceVisible();
            if (licenceVisible != null) {
                setSpinnerSelection(licenceVisibleSpinner, licenceVisible ? "Yes" : "No");
            }

            Spinner outletTypeSpinner = (Spinner) findViewById(R.id.details_outlet_type);
            setSpinnerSelection(outletTypeSpinner, customerInstance.getOutletType());

            Spinner outletSizeSpinner = (Spinner) findViewById(R.id.details_size);
            setSpinnerSelection(outletSizeSpinner, customerInstance.getOutletSize());

            Spinner splitSpinner = (Spinner) findViewById(R.id.details_split);
            setSpinnerSelection(splitSpinner, customerInstance.getSplit());

            addContactsToLayout(customerInstance.getCustomerContacts());


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
                    customerInstance.setIsActive(true);
                    customerDao.insert(customerInstance);
                } else {
                    customerDao.update(customerInstance);
                }
                saveCustomerContacts(customerInstance.getUuid());
                isSaved = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isSaved;
    }

    private boolean allMandatoryFieldsFilled() {
        EditText outletName = (EditText) findViewById(R.id.detailsname);
        EditText descLocation = (EditText) findViewById(R.id.details_desc_location);
        EditText diarreahPatients = (EditText) findViewById(R.id.details_num_customers_per_day);
        Spinner splitSpinner = ((Spinner) findViewById(R.id.details_split));
        Spinner outletType = ((Spinner) findViewById(R.id.details_outlet_type));
        Spinner outletSize = ((Spinner) findViewById(R.id.details_size));
        GpsWidgetView gpsWidgetView = ((GpsWidgetView) findViewById(R.id.customer_gps_view));
        if (!Utils.mandatoryFieldFilled(outletName)) {
            return false;
        } else if (!Utils.mandatorySpinnerFieldSelected(outletType)) {
            return false;
        } else if (!Utils.mandatorySpinnerFieldSelected(outletSize)) {
            return false;
        } else if (!Utils.mandatoryFieldFilled(descLocation)) {
            return false;
        } else if (!Utils.mandatoryFieldFilled(diarreahPatients)) {
            return false;
        } else if (!Utils.mandatorySpinnerFieldSelected(splitSpinner)) {
            return false;
        } else if (gpsWidgetView.getLatLongText().toString().equals("")) {
//            gpsWidgetView.setError("This field is mandatory");
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
        for (ContactWidgetView contactWidgetView : contactsToUpdate) {
            if (contactWidgetView.getCustomerContact().getUuid() == null) {
                CustomerContact customerContact = contactWidgetView.getCustomerContact();
                customerContact.setUuid(UUID.randomUUID().toString());
                customerContact.setCustomerId(customerId);
                customerContact.setCustomer(customerInstance);
                customerContactDao.insert(customerContact);
            } else {
                customerContactDao.update(contactWidgetView.getCustomerContact());
            }
        }
    }

    private void setMandatoryFields() {
        Utils.setRequired((TextView) findViewById(R.id.detailsnameview));
        Utils.setRequired((TextView) findViewById(R.id.details_outlet_type_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_size_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_subcounty_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_district_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_desc_location_lbl));
        Utils.setRequired((TextView) findViewById(R.id.details_num_customers_per_day_lbl));
        Utils.setRequired((TextView) findViewById(R.id.outlet_rural_village));
        Utils.setRequired((TextView) findViewById(R.id.customer_gps_lbl));
        Utils.setRequired((TextView) findViewById(R.id.customer_add_contact_lbl));
    }

    protected void manageLicenceVisible() {
        final Spinner spinner = (Spinner) findViewById(R.id.details_licence_visible);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout licenceTypeLayout = (LinearLayout) findViewById(R.id.type_of_licence_layout);
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

    private void addContactsToLayout(List<CustomerContact> customerContactList) {
        //add to parent form
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.customer_contacts_layout);
        for (CustomerContact customerContact : customerContactList) {
            ContactWidgetView contactWidgetView = new ContactWidgetView(CustomerForm.this, customerContact);
            linearLayout.addView(contactWidgetView);
            contactsToUpdate.add(contactWidgetView);
        }
    }

}