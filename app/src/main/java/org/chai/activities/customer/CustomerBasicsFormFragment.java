package org.chai.activities.customer;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.adapter.DistrictArrayAdapter;
import org.chai.adapter.SubcountyArrayAdapter;
import org.chai.model.Customer;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.District;
import org.chai.model.DistrictDao;
import org.chai.model.Subcounty;
import org.chai.model.SubcountyDao;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.List;

/**
 * Created by Zed on 4/29/2015.
 */
public class CustomerBasicsFormFragment extends Fragment {
    Spinner subcountySpinner;
    Spinner districtSpinner;
    View view;
    AQuery aq;

    private SubcountyDao subcountyDao;
    private DistrictDao districtDao;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_basics_form_fragment, container, false);

        aq = new AQuery(view);

        initialiseGreenDao();

        List<Subcounty> subcountiesList = subcountyDao.loadAll();
        List<District> districtList = districtDao.loadAll();

        subcountySpinner = aq.id(R.id.subcounty).getSpinner();
        districtSpinner = aq.id(R.id.district).getSpinner();
        districtSpinner.setAdapter(new DistrictArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, districtList.toArray(new District[districtList.size()])));

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String districtId = ((District) districtSpinner.getSelectedItem()).getUuid();
                List<Subcounty> subcounties = subcountyDao.queryBuilder().where(SubcountyDao.Properties.DistrictId.eq(districtId)).list();
                subcountySpinner.setAdapter(new SubcountyArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, subcounties.toArray(new Subcounty[subcounties.size()])));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subcountySpinner.setAdapter(new SubcountyArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, subcountiesList.toArray(new Subcounty[subcountiesList.size()])));

        /*Button saveCustomerBtn = (Button) findViewById(R.id.menu_add_new_customer);
        setSaveButtonText(saveCustomerBtn);
        saveCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customerInstance.getUuid() == null && contactsToUpdate.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "Please enter atleast one contact!", Toast.LENGTH_LONG).show();
                    Utils.showError(CustomerForm.this, "Error:", "Please enter atleast one contact!");
                } else {
                    boolean isSaved = saveCustomer();
                    if (isSaved) {
                        Toast.makeText(getApplicationContext(), "Customer has been  successfully saved!", Toast.LENGTH_LONG).show();
//                            Utils.showError(CustomerForm.this,"Info:", "Customer has been  successfully saved!");
                        finish();
                    } else {
//                            Toast.makeText(getApplicationContext(), "A problem Occured while saving a new Customer,please ensure that data is entered correctly", Toast.LENGTH_LONG).show();
                        Utils.showError(CustomerForm.this,"Error:", "A problem Occured while saving a new Customer,please ensure that data is entered correctly");
                    }
                }
            }
        });*/

        setRequiredFields();

        return view;
    }

    private void setRequiredFields(){
        List<View> required = Utils.getViewsByTag((ViewGroup)view, "required");
        for(View v : required){
            try{
                Utils.setRequired((TextView)v);
            }catch(Exception ex){
                Utils.log("Error setting view by tag -> " + ex.getMessage());
            }
        }
    }

    public boolean saveFields(){
        String name = aq.id(R.id.outlet_name).getText().toString().trim();
        String type = aq.id (R.id.outlettype).getSelectedItem().toString();
        String size = aq.id(R.id.outletsize).getSelectedItem().toString();
        String licenceVisible = aq.id(R.id.licencevisible).getSelectedItem().toString();
        String ruralOrUrban = aq.id(R.id.ruralorurban).getSelectedItem().toString();
        String licenceType = aq.id(R.id.licencetype).getSelectedItem().toString();
        Subcounty subcounty =  ((SubcountyArrayAdapter)subcountySpinner.getAdapter()).getItem(subcountySpinner.getSelectedItemPosition());
        String subcountyUuid = subcounty.getUuid();
        String tradingCenter = aq.id(R.id.trading_center).getText().toString().trim();
        String directions = aq.id(R.id.directions).getText().toString().trim();
        String gps = aq.id(R.id.location_gps).getText().toString().trim();

        Utils.log("Saving fields -> " + name);

        if(name.isEmpty()){
            Toast.makeText(getActivity(), "Enter the outlet name", Toast.LENGTH_LONG).show();
            return false;
        }

        if(type.isEmpty()){
            Toast.makeText(getActivity(), "Select the outley type", Toast.LENGTH_LONG).show();
            return false;
        }

        if(size.isEmpty()){
            Toast.makeText(getActivity(), "Select the outlet size", Toast.LENGTH_LONG).show();
            return false;
        }

        if(ruralOrUrban.isEmpty()){
            Toast.makeText(getActivity(), "Select rural/urban field for the outlet", Toast.LENGTH_LONG).show();
            return false;
        }

        if(subcountyUuid.isEmpty()){
            Toast.makeText(getActivity(), "Select the subcounty", Toast.LENGTH_LONG).show();
            return false;
        }

        AddNewCustomerActivity a = (AddNewCustomerActivity)getActivity();
        if(a.customer == null){
            a.customer = new Customer();
        }

        a.customer.setOutletName(name);
        a.customer.setLicenceVisible(licenceVisible.equalsIgnoreCase("yes"));
        a.customer.setOutletSize(size);
        a.customer.setOutletType(type);
        a.customer.setSplit(ruralOrUrban);
        a.customer.setSubcountyUuid(subcountyUuid);
        a.customer.setSubcountyId(subcountyUuid);
        a.customer.setSubcounty(subcounty);
        a.customer.setTypeOfLicence(licenceType);
        a.customer.setTradingCenter(tradingCenter);
        a.customer.setDescriptionOfOutletLocation(directions);
        a.customer.setLatitude(32.390303); //to fix
        a.customer.setLongitude(1.2829093); //to fix

        return true;
    }

    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            subcountyDao = daoSession.getSubcountyDao();
            districtDao = daoSession.getDistrictDao();
        } catch (Exception ex) {
            Utils.log("Error initializing green DAO");
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
