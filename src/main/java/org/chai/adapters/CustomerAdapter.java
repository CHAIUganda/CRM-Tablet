package org.chai.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.util.Utils;

import java.util.List;

/**
 * Created by victor on 10/16/14.
 */
public class CustomerAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Customer> customers;

    public CustomerAdapter(Activity activity,List<Customer> customers){
        this.activity = activity;
        this.customers = customers;
    }

    @Override
    public int getCount() {
        return customers.size();
    }

    @Override
    public Object getItem(int index) {
        return customers.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null){
           inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = inflater.inflate(R.layout.customer_list_row,null);
        }
        TextView customerName = (TextView)convertView.findViewById(R.id.customername);
        TextView customerAddress = (TextView)convertView.findViewById(R.id.customeraddress);
        TextView telephone = (TextView)convertView.findViewById(R.id.customertelephone);

        Customer customer = customers.get(position);
        CustomerContact customerContact = null;
        if(customer.getContacts().size()>0){
            customerContact = getKeyCustomerContact(customer.getContacts());
        }

        if(customerContact!=null){
            telephone.setText(customerContact.getContact());
        }else{
            telephone.setText("No Contact Available");
        }

        customerName.setText(customer.getOutletName());
        customerAddress.setText(Utils.truncateString(customer.getDescriptionOfOutletLocation(),20));
        return convertView;
    }

    private CustomerContact getKeyCustomerContact(List<CustomerContact> customerContacts){
        for(int i=0;i<customerContacts.size();i++){
            if(customerContacts.get(i).getTypeOfContact().equalsIgnoreCase("key")){
                return customerContacts.get(i);
            }
        }
        return customerContacts.get(0);
    }
}
