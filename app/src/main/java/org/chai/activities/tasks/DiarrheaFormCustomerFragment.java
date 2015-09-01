package org.chai.activities.tasks;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.adapter.CustomerAutocompleteAdapter;
import org.chai.model.Customer;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zed on 5/2/2015.
 */
public class DiarrheaFormCustomerFragment extends Fragment {
    View view;
    AQuery aq;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;

    List<Customer> customers;
    Customer customer;
    String customerId;

    DiarrheaFormActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (DiarrheaFormActivity)getActivity();
        view = inflater.inflate(R.layout.diarrhea_form_customer_fragment, container, false);
        aq = new AQuery(view);
        setRequiredFields();
        initialiseGreenDao();

        customerId = getActivity().getIntent().getStringExtra("customer_id");
        if(customerId == null){
            customerId = activity.task.getCustomerId();
        }

        if(customerId != null){
            customer = customerDao.load(customerId);
            if(customer != null){
                aq.id(R.id.customer_id).enabled(false);
                aq.id(R.id.customer_id).text(customer.getOutletName()).enabled(false);
                try{
                    aq.id(R.id.txt_customer_location).text("District: " + customer.getSubcounty().getDistrict().getName() + " | " + "Subcounty: " + customer.getSubcounty().getName());
                }catch (Exception ex){
                    aq.id(R.id.txt_customer_location).text("Failed to load District & Subcounty");
                }
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
                        try{
                            aq.id(R.id.txt_customer_location).text("District: " + customer.getSubcounty().getDistrict().getName() + " | " + "Subcounty: " + customer.getSubcounty().getName());
                        }catch (Exception ex){
                            aq.id(R.id.txt_customer_location).text("Failed to load District & Subcounty");
                        }
                    }
                }
            });
        }

        if(activity.call.getUuid() != null){
            populateFields();
        }

        return view;
    }

    private void setLatLong(){
        MyApplication.registerEditTextForLocationUpdates(aq.id(R.id.gps).getEditText(), getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        setLatLong();
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

    private void populateFields(){
        int patients = activity.call.getDiarrheaPatientsInFacility();
        aq.id(R.id.diarrhea_patients).text(Integer.toString(patients));
    }

    public boolean saveFields(){
        if(customer == null){
            Toast.makeText(getActivity(), "Please select a customer", Toast.LENGTH_LONG).show();
            return false;
        }
        String patients = aq.id(R.id.diarrhea_patients).getText().toString();
        if(patients.isEmpty()){
            Toast.makeText(getActivity(), "Please enter number of diarrhea patients", Toast.LENGTH_LONG).show();
            return false;
        }

        activity.task.setCustomerId(customer.getUuid());
        activity.task.setCustomer(customer);
        activity.task.setDescription("Go check on " + customer.getOutletName());
        activity.call.setDiarrheaPatientsInFacility(Integer.parseInt(patients));
        activity.call.setLongitude(Utils.getLongFromLatLong(aq.id(R.id.gps).getText().toString()));
        activity.call.setLatitude(Utils.getLatFromLatLong(aq.id(R.id.gps).getText().toString()));

        return true;
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
}
