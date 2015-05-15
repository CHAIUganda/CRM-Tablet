package org.chai.activities.forms;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.Globals;
import org.chai.R;
import org.chai.adapter.CustomerAutocompleteAdapter;
import org.chai.model.Customer;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.util.GPSTracker;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zed on 4/9/2015.
 */
public class MalariaFormCustomerFragment extends Fragment {
    View view;
    AQuery aq;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;
    List<Customer> customers;
    Customer customer;
    String customerId;

    GPSTracker tracker;

    MalariaFormActivity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = (MalariaFormActivity)getActivity();

        view = inflater.inflate(R.layout.malaria_form_fragment_1, container, false);
        setRequiredFields();
        aq = new AQuery(view);
        initialiseGreenDao();

        aq.id(R.id.heard_about_green_leaf).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    aq.id(R.id.ln_heard_about_container).visible();
                }else{
                    aq.id(R.id.ln_heard_about_container).gone();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setLatLong();

        customerId = getActivity().getIntent().getStringExtra("customer_id");
        if(customerId == null){
            customerId = parent.task.getCustomerId();
        }

        if(customerId != null){
            customer = customerDao.load(customerId);
            if(customer != null){
                aq.id(R.id.customer_id).enabled(false);
                aq.id(R.id.customer_id).text(customer.getOutletName());
                aq.id(R.id.txt_customer_location).text("District: " + customer.getSubcounty().getDistrict().getName() + " | " + "Subcounty: " + customer.getSubcounty().getName());
            }
        }

        if(customer == null) {
            customers = customerDao.loadAll();

            AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.customer_id);
            CustomerAutocompleteAdapter adapter = new CustomerAutocompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, new ArrayList<Customer>(customers));
            textView.setAdapter(adapter);

            textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view1, int position, long l) {
                    Customer selected = (Customer) adapterView.getAdapter().getItem(position);
                    customer = selected;
                    if (customer != null) {
                        aq.id(R.id.txt_customer_location).text("District: " + customer.getSubcounty().getDistrict().getName() + " | " + "Subcounty: " + customer.getSubcounty().getName());
                    }
                }
            });
        }

        if(parent.call != null){
            populateFields();
        }

        return view;
    }

    private void setLatLong(){
        tracker = Globals.getInstance().getGpsTracker();
        aq.id(R.id.gps).text(tracker.getLatitude() + "," + tracker.getLongitude());
    }

    private void setRequiredFields(){
        List<View> required = Utils.getViewsByTag((ViewGroup) view, "required");
        for(View v : required){
            try{
                Utils.setRequired((TextView)v);
            }catch(Exception ex){
                Utils.log("Error setting view by tag -> " + ex.getMessage());
            }
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
            Log.d("Err", ex.getLocalizedMessage());
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void populateFields(){
        aq.id(R.id.patients_per_week).text(Integer.toString(parent.call.getMalariaPatientsInFacility()));
        aq.id(R.id.children).text(Integer.toString(parent.call.getNumberOfChildren()));
        String doyouprescribe = parent.call.getDoYouPrescribeTreatment();
        if(doyouprescribe != null){
            Spinner s1 = aq.id(R.id.do_you_prescribe_treatment).getSpinner();
            s1.setSelection(((ArrayAdapter<String>)s1.getAdapter()).getPosition(doyouprescribe));
        }
        String heard = parent.call.getHeardAboutGreenLeaf();
        if(heard != null){
            Spinner s2 = aq.id(R.id.heard_about_green_leaf).getSpinner();
            s2.setSelection(((ArrayAdapter<String>)s2.getAdapter()).getPosition(heard));
        }
        String sources = parent.call.getHowDidYouHear();
        if(sources != null){
            aq.id(R.id.radio).checked(sources.indexOf(aq.id(R.id.radio).getText().toString()) != -1);
            aq.id(R.id.tv).checked(sources.indexOf(aq.id(R.id.tv).getText().toString()) != -1);
            aq.id(R.id.billboards).checked(sources.indexOf(aq.id(R.id.billboards).getText().toString()) != -1);
            aq.id(R.id.newspaper).checked(sources.indexOf(aq.id(R.id.newspaper).getText().toString()) != -1);
            aq.id(R.id.religious).checked(sources.indexOf(aq.id(R.id.religious).getText().toString()) != -1);
            aq.id(R.id.friend).checked(sources.indexOf(aq.id(R.id.friend).getText().toString()) != -1);
        }
    }

    public boolean saveFields(){
        if(customer == null){
            Toast.makeText(getActivity(), "Please select a customer", Toast.LENGTH_LONG).show();
            return false;
        }
        String malariaPatients = aq.id(R.id.patients_per_week).getText().toString();
        String children = aq.id(R.id.children).getText().toString();
        if(malariaPatients.isEmpty()){
            Toast.makeText(getActivity(), "Please enter number of Malaria patients per week", Toast.LENGTH_LONG).show();
            return false;
        }
        if(children.isEmpty()){
            Toast.makeText(getActivity(), "Please enter how many of the patients are children", Toast.LENGTH_LONG).show();
            return false;
        }
        String gps = aq.id(R.id.gps).getText().toString();

        MalariaFormActivity ac = (MalariaFormActivity)getActivity();
        ac.task.setCustomerId(customer.getUuid());
        ac.task.setCustomer(customer);
        ac.task.setDescription("Detailing " + customer.getOutletName() + " - Malaria");

        ac.call.setLatitude(Utils.getLatFromLatLong(gps));
        ac.call.setLongitude(Utils.getLongFromLatLong(gps));
        ac.call.setMalariaPatientsInFacility(Integer.parseInt(malariaPatients));
        ac.call.setNumberOfChildren(Integer.parseInt(children));
        ac.call.setDoYouPrescribeTreatment(aq.id(R.id.do_you_prescribe_treatment).getSelectedItem().toString());
        ac.call.setHeardAboutGreenLeaf(aq.id(R.id.heard_about_green_leaf).getSelectedItem().toString());

        String sourceofInfo = "";

        if(aq.id(R.id.radio).isChecked()){
            sourceofInfo += aq.id(R.id.radio).getText().toString() + ", ";
        }
        if(aq.id(R.id.tv).isChecked()){
            sourceofInfo += aq.id(R.id.tv).getText().toString() + ", ";
        }
        if(aq.id(R.id.billboards).isChecked()){
            sourceofInfo += aq.id(R.id.billboards).getText().toString() + ", ";
        }
        if(aq.id(R.id.newspaper).isChecked()){
            sourceofInfo += aq.id(R.id.newspaper).getText().toString() + ", ";
        }
        if(aq.id(R.id.religious).isChecked()){
            sourceofInfo += aq.id(R.id.religious).getText().toString() + ", ";
        }
        if(aq.id(R.id.friend).isChecked()){
            sourceofInfo += aq.id(R.id.friend).getText().toString() + ", ";
        }

        ac.call.setHowDidYouHear(sourceofInfo);

        return true;
    }
}
