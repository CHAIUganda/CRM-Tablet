package org.chai.activities.customer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.model.CustomerContact;
import org.chai.util.Utils;

import java.util.ArrayList;

/**
 * Created by Zed on 4/29/2015.
 */
public class CustomerContactsFormFragment extends Fragment{
    AQuery aq;
    View view;
    LinearLayout rowContainer;
    ArrayList<View> rows;
    AddNewCustomerActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (AddNewCustomerActivity)getActivity();

        view = inflater.inflate(R.layout.customer_contacts_form_fragment, container, false);
        aq = new AQuery(view);
        rowContainer = (LinearLayout)view.findViewById(R.id.ln_contacts_container);

        aq.id(R.id.btn_add_row).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow(new CustomerContact());
            }
        });

        rows = new ArrayList<>();

        return view;
    }

    @Override
    public void onPause() {
        Utils.log("onPause()");
        super.onPause();
        CustomerContact contact;
        try{
            for(View row : rows){
                AQuery a = new AQuery(row);
                String name = a.id(R.id.txt_customer_name).getText().toString();
                String phone = a.id(R.id.txt_customer_phone).getText().toString();
                String gender = a.id(R.id.contact_gender).getSelectedItem().toString();
                String role = a.id(R.id.contact_role).getSelectedItem().toString();
                contact = activity.contacts.get(rows.indexOf(row));
                contact.setContact(phone);
                contact.setNames(name);
                contact.setGender(gender);
                contact.setRole(role);
            }
        }catch(Exception ex){
            Utils.log("Error saving stock state -> " + ex.getMessage());
        }
    }

    @Override
    public void onResume() {
        Utils.log("onResume()");
        super.onResume();
        if(rows != null){
            for(View row: rows){
                ((ViewGroup)row.getParent()).removeView(row);
            }
        }

        ArrayList<CustomerContact> temp = new ArrayList<>();
        temp.addAll(activity.contacts);

        rows = new ArrayList<>();
        activity.contacts = new ArrayList<>();

        for(int i = 0; i < temp.size(); i++){
            addRow(temp.get(i));
        }
    }

    private void addRow(final CustomerContact contact){
        Utils.log("Adding contact -> " + contact.getNames());
        LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View row = inflator.inflate(R.layout.customer_contact_form_row, null);
        AQuery a = new AQuery(row);

        try{
            if(contact.getNames() != null){
                a.id(R.id.txt_customer_name).text(contact.getNames());
            }
            if(contact.getContact() != null){
                a.id(R.id.txt_customer_phone).text(contact.getContact());
            }
            if(contact.getGender() != null){
                Spinner gender = a.id(R.id.contact_gender).getSpinner();
                gender.setSelection(((ArrayAdapter<String>) gender.getAdapter()).getPosition(contact.getGender()));
            }
            if(contact.getRole() != null){
                Spinner role = a.id(R.id.contact_role).getSpinner();
                role.setSelection(((ArrayAdapter<String>) role.getAdapter()).getPosition(contact.getRole()));
            }
        }catch (Exception ex){
            Utils.log("Error adding contact row -> " + ex.getMessage());
        }

        rowContainer.addView(row);

        rows.add(row);
        activity.contacts.add(contact);

        final ImageView remove = (ImageView)row.findViewById(R.id.btn_remove_row);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rows.remove(row);
                activity.contacts.remove(contact);
                LinearLayout parent = (LinearLayout) v.getParent();
                LinearLayout root = (LinearLayout) parent.getParent();
                LinearLayout top = (LinearLayout) root.getParent();
                rowContainer.removeView(top);
            }
        });
    }

    public boolean saveFields(){
        if(rows.size() == 0){
            Toast.makeText(getActivity(), "Please enter atleast one contact person", Toast.LENGTH_LONG).show();
            return false;
        }

        AQuery a;
        CustomerContact contact;
        String name, phone, gender, role;
        int i = 1;
        for(View row: rows){
            a = new AQuery(row);

            name = a.id(R.id.txt_customer_name).getText().toString();
            phone = a.id(R.id.txt_customer_phone).getText().toString();
            int gPos = a.id(R.id.contact_gender).getSelectedItemPosition();
            int rPos = a.id(R.id.contact_role).getSelectedItemPosition();

            if(name.isEmpty()){
                Toast.makeText(getActivity(), "Enter contact name for contact on row " + i, Toast.LENGTH_LONG).show();
                return false;
            }
            if(phone.isEmpty()){
                Toast.makeText(getActivity(), "Enter phone number for contact on row " + i, Toast.LENGTH_LONG).show();
                return false;
            }
            if(gPos == 0){
                Toast.makeText(getActivity(), "Select gender for contact on row " + i, Toast.LENGTH_LONG).show();
                return false;
            }else{
                gender = a.id(R.id.contact_gender).getSelectedItem().toString();
            }
            if(rPos == 0){
                Toast.makeText(getActivity(), "Select role for contact on row " + i, Toast.LENGTH_LONG).show();
                return false;
            }else{
                role = a.id(R.id.contact_role).getSelectedItem().toString();
            }

            contact = activity.contacts.get(rows.indexOf(row));
            contact.setContact(phone);
            contact.setNames(name);
            contact.setGender(gender);
            contact.setRole(role);

            i++;
        }

        return true;
    }
}
