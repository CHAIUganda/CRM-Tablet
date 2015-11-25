package org.chai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import org.chai.model.Customer;
import org.chai.util.Utils;

import java.util.ArrayList;

/**
 * Created by victor on 28-Mar-15.
 */
public class CustomerAutocompleteAdapter extends ArrayAdapter<Customer> {
    private ArrayList<Customer> items;
    private ArrayList<Customer> itemsAll;
    private ArrayList<Customer> suggestions;
    private int viewResourceId;

    public CustomerAutocompleteAdapter(Context context,int viewResourceId, ArrayList<Customer> items){
        super(context,viewResourceId,items);
        this.items = items;
        this.itemsAll = (ArrayList<Customer>)items.clone();
        this.suggestions = new ArrayList<>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        Customer customer = items.get(position);
        if (customer != null) {
            TextView customerNameLabel = (TextView) v.findViewById(android.R.id.text1);
            customerNameLabel.setPadding(10,10,10,10);
            if (customerNameLabel != null) {
                customerNameLabel.setText(customer.getOutletName());
            }
        }
        return v;
    }
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Customer)(resultValue)).getOutletName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Customer customer : itemsAll) {
                    if(customer.getOutletName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Customer> filteredList = (ArrayList<Customer>) results.values;
            if(results != null && results.count > 0) {
                clear();
                try{
                    for (int i = 0; i < filteredList.size(); i++) {
                        add(filteredList.get(i));
                    }
                }catch (Exception ex){
                    Utils.log("Error in publishResults");
                }
                notifyDataSetChanged();
            }
        }
    };



}
