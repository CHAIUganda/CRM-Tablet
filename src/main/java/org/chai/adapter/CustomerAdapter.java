package org.chai.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.chai.R;
import org.chai.activities.customer.CustomerDetailsActivity;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by victor on 10/16/14.
 */
public class CustomerAdapter extends ArrayAdapter<Customer> {

    private List<Customer> customers;
    private ArrayList<Customer> filterList;

    public CustomerAdapter(Activity activity, List<Customer> customers) {
        super(activity.getApplicationContext(), R.layout.customers_main_activity, customers);
        this.customers = customers;
        filterList = new ArrayList<Customer>();
        filterList.addAll(customers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.customer_list_row, parent, false);

            viewHolder = new ViewHolder();
            TextView customerNameView = (TextView) convertView.findViewById(R.id.customername);
            TextView customerAddressView = (TextView) convertView.findViewById(R.id.customeraddress);
            TextView customerViewTelephone = (TextView) convertView.findViewById(R.id.customertelephone);

            viewHolder.customerName = customerNameView;
            viewHolder.customerAddress = customerAddressView;
            viewHolder.telephone = customerViewTelephone;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Customer customer = customers.get(position);
        CustomerContact customerCtct = null;
        if (customer.getCustomerContacts().size() > 0) {
            customerCtct = Utils.getKeyCustomerContact(customer.getCustomerContacts());
        }

        if (customerCtct != null) {
            viewHolder.telephone.setText(customerCtct.getContact());
        } else {
            viewHolder.telephone.setText("No Contact Available");
        }

        viewHolder.customerName.setText(customer.getOutletName());
        viewHolder.customerAddress.setText(Utils.truncateString(customer.getDescriptionOfOutletLocation(), 50));

        return convertView;
    }

    public void filter(String term){
        term = term.toLowerCase(Locale.getDefault());
        customers.clear();
        if(term.length()== 0){
            customers.addAll(filterList);
        }else{
            for(Customer customer:filterList){
                if(customer.getOutletName().toLowerCase(Locale.getDefault()).contains(term)){
                    customers.add(customer);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView customerName;
        TextView customerAddress;
        TextView telephone;
    }
}
