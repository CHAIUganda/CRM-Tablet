package org.chai.activities.org.chai.activities.forms;

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
 * Created by Zed on 4/9/2015.
 */
public class MalariaFormFragment1 extends Fragment {
    View view;
    AQuery aq;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;
    List<Customer> customers;
    Customer customer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.malaria_form_fragment_1, container, false);
        setRequiredFields();
        aq = new AQuery(view);
        initialiseGreenDao();

        customers = customerDao.loadAll();

        AutoCompleteTextView textView = (AutoCompleteTextView)view.findViewById(R.id.customer_id);
        CustomerAutocompleteAdapter adapter = new CustomerAutocompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, new ArrayList<Customer>(customers));
        textView.setAdapter(adapter);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view1, int position, long l) {
                Customer selected = (Customer)adapterView.getAdapter().getItem(position);
                customer = selected;
                if(customer != null){
                    aq.id(R.id.txt_customer_location).text("District: " + customer.getSubcounty().getDistrict().getName() + " | " + "Subcounty: " + customer.getSubcounty().getName());
                }
            }
        });

        return view;
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
}
