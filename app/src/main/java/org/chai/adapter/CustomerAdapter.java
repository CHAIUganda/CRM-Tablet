package org.chai.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.androidquery.AQuery;

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
        View row = convertView;
        Customer customer = getItem(position);

        if(row == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.customer_list_row, parent, false);
        }

        AQuery aq = new AQuery(row);

        CustomerContact contact = null;
        if (customer.getCustomerContacts().size() > 0) {
            contact = Utils.getKeyCustomerContact(customer.getCustomerContacts());
        }

        if (contact != null) {
            aq.id(R.id.customertelephone).text(contact.getContact());
        } else {
            aq.id(R.id.customertelephone).text("No Contact Available");
        }

        aq.id(R.id.customername).text(customer.getOutletName());
        aq.id(R.id.customeraddress).text(Utils.truncateString(customer.getDescriptionOfOutletLocation(), 50));
        if(customer.getIsActive()!= null && !customer.getIsActive()){
            int inactiveColor = Color.parseColor("#C0C0C0");
            aq.id(R.id.customername).textColor(inactiveColor);
            aq.id(R.id.customeraddress).textColor(inactiveColor);
            aq.id(R.id.customertelephone).textColor(inactiveColor);
            aq.id(R.id.thumbnail).image(R.drawable.user_inactive);
        }else{
            aq.id(R.id.customername).textColor(R.color.list_main_text_color);
            aq.id(R.id.customeraddress).textColor(R.color.list_main_text_color);
            aq.id(R.id.customertelephone).textColor(R.color.list_subtext_color);
            aq.id(R.id.thumbnail).image(R.drawable.user_inactive);
            aq.id(R.id.thumbnail).image(R.drawable.user_active);
        }

        return row;
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
}
