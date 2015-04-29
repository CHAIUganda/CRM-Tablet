package org.chai.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by victor on 10/16/14.
 */
public class CustomerAdapter extends ArrayAdapter<Customer> implements Filterable {
    private CustomerFilter filter;
    private List<Customer> customers;

    public CustomerAdapter(Activity activity, List<Customer> items) {
        super(activity.getApplicationContext(), R.layout.customers_main_activity, items);
        this.customers = new ArrayList<Customer>();
        this.customers.addAll(items);
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

        Customer customer = getItem(position);
        CustomerContact customerCtct = null;
        if (customer.getCustomerContacts().size() > 0) {
            customerCtct = Utils.getKeyCustomerContact(customer.getCustomerContacts());
        }

        if (customerCtct != null) {
            viewHolder.telephone.setText(customerCtct.getContact());
        } else {
            viewHolder.telephone.setText("No Contact Available");
        }

        Utils.log("Setting outlet name -> " + customer.getOutletName());

        viewHolder.customerName.setText(customer.getOutletName());
        viewHolder.customerAddress.setText(Utils.truncateString(customer.getDescriptionOfOutletLocation(), 50));
        if(customer.getIsActive()!= null && !customer.getIsActive()){
            viewHolder.customerName.setTextColor(Color.parseColor("#C0C0C0"));
            viewHolder.customerAddress.setTextColor(Color.parseColor("#C0C0C0"));
            viewHolder.telephone.setTextColor(Color.parseColor("#C0C0C0"));
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new CustomerFilter();
        }
        return  filter;
    }

    private class CustomerFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = customers;
                results.count = customers.size();
            }else{
                List<Customer> cList = new ArrayList<Customer>();
                for(Customer c : customers){
                    if(c.getOutletName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))){
                        Utils.log("Adding " + c.getOutletName() + " -> " + constraint);
                        cList.add(c);
                    }
                }
                results.values = cList;
                results.count = cList.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Customer> filtered = (ArrayList<Customer>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0; i < filtered.size(); i++){
                add(filtered.get(i));
            }
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        TextView customerName;
        TextView customerAddress;
        TextView telephone;
    }
}
