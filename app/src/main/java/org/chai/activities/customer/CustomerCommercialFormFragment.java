package org.chai.activities.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.model.Customer;
import org.chai.util.Utils;

import java.util.List;

/**
 * Created by Zed on 4/29/2015.
 */
public class CustomerCommercialFormFragment extends Fragment {
    AQuery aq;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_commercial_form_fragment, container, false);
        aq = new AQuery(view);
        setRequiredFields();

        AddNewCustomerActivity ac = (AddNewCustomerActivity)getActivity();
        if(ac.customer.getUuid() != null){
            populateFields(ac.customer);
        }

        return view;
    }

    private void populateFields(Customer c){
        aq.id(R.id.months_open).text(c.getLengthOpen());
        aq.id(R.id.number_of_employees).text(Integer.toString(c.getNumberOfEmployees()));
        aq.id(R.id.diarrhea_patients).text(Integer.toString(c.getNumberOfCustomersPerDay()));
        aq.id(R.id.main_supplier).text(c.getMajoritySourceOfSupply());
        aq.id(R.id.key_wholesaler).text(c.getKeyWholeSalerName());
        aq.id(R.id.key_wholesaler_contact).text(c.getKeyWholeSalerContact());

        Spinner freq = aq.id(R.id.stock_frequency).getSpinner();
        freq.setSelection(((ArrayAdapter<String>)freq.getAdapter()).getPosition(c.getRestockFrequency()));
    }

    public boolean saveFields(){
        String monthsOpen = aq.id(R.id.months_open).getText().toString().trim();
        String emps = aq.id(R.id.number_of_employees).getText().toString();
        int numberOfEmployees = (emps.isEmpty()) ? 0 : Integer.parseInt(emps);
        String patients = aq.id(R.id.diarrhea_patients).getText().toString();
        int numberOfPatients = (patients.isEmpty()) ? 0 : Integer.parseInt(patients);
        String supplier = aq.id(R.id.main_supplier).getText().toString().trim();
        String wholesaler = aq.id(R.id.key_wholesaler).getText().toString();
        String wholesalerContact = aq.id(R.id.key_wholesaler_contact).getText().toString();
        String stockPerWeek = aq.id(R.id.stock_frequency).getSelectedItem().toString();

        if(patients.isEmpty()){
            Toast.makeText(getActivity(), "Enter the number of diarrhea patients below 5 years", Toast.LENGTH_LONG).show();
            return false;
        }

        if(supplier.isEmpty()){
            Toast.makeText(getActivity(), "Enter the name of the main supplier", Toast.LENGTH_LONG).show();
            return false;
        }

        if(wholesaler.isEmpty()){
            Toast.makeText(getActivity(), "Enter the name of the wholesaler", Toast.LENGTH_LONG).show();
            return false;
        }

        AddNewCustomerActivity a = (AddNewCustomerActivity)getActivity();
        a.customer.setLengthOpen(monthsOpen);
        a.customer.setNumberOfEmployees(numberOfEmployees);
        a.customer.setNumberOfCustomersPerDay(numberOfPatients);
        a.customer.setKeyWholeSalerName(wholesaler);
        a.customer.setKeyWholeSalerContact(wholesalerContact);
        a.customer.setRestockFrequency(stockPerWeek);
        a.customer.setMajoritySourceOfSupply(supplier);

        return true;
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
}
