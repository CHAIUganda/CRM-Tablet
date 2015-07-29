package org.chai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.model.Order;
import org.chai.util.Utils;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Zed on 5/12/2015.
 */
public class OrderHistoryAdapter extends ArrayAdapter<Order> {
    private int lastPosition = -1;
    private ArrayList<Order> originalItems;
    private ArrayList<Order> filteredItems;
    private HistoryFilter historyFilter;

    public OrderHistoryAdapter(Context context, int resource, List<Order> items) {
        super(context, resource, items);

        originalItems = new ArrayList<>();
        originalItems.addAll(items);

        filteredItems = new ArrayList<>();
        filteredItems.addAll(items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null){
            LayoutInflater inflator = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.history_item_row, null);
        }

        Order m = getItem(position);
        Customer c = m.getCustomer();

        AQuery aq = new AQuery(row);
        aq.id(R.id.txt_customer_name).text(c.getOutletName());
        Date d = m.getLastUpdated();
        if(d == null){
            d = m.getOrderDate();
        }
        if(d != null){
            aq.id(R.id.txt_time).text(new PrettyTime().format(d));
        }
        try{
            CustomerContact contact = c.getCustomerContacts().get(0);
            aq.id(R.id.txt_customer_contact).text(contact.getContact() + " - " + c.getSubcounty().getName() + " | " + c.getSubcounty().getDistrict().getName());
        }catch (Exception ex){
            Utils.log("Error loading history contact");
        }

        /*if(m.getIsDirty()){
            aq.id(R.id.txt_customer_name).textColor(Color.parseColor("#242527"));
            aq.id(R.id.txt_time).textColor(Color.parseColor("#55595d"));
            aq.id(R.id.txt_customer_contact).textColor(Color.parseColor("#55595d"));
        }else{
            int inactiveColor = Color.parseColor("#C0C0C0");
            aq.id(R.id.txt_customer_name).textColor(inactiveColor);
            aq.id(R.id.txt_time).textColor(inactiveColor);
            aq.id(R.id.txt_customer_contact).textColor(inactiveColor);
        }*/

        aq.id(R.id.img_segment).gone();

        Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        row.startAnimation(animation);
        lastPosition = position;

        return row;
    }

    @Override
    public Filter getFilter() {
        if(historyFilter == null){
            historyFilter = new HistoryFilter();
        }

        return historyFilter;
    }

    private class HistoryFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = originalItems;
                results.count = originalItems.size();
            }else{
                List<Order> nFilteredList = new ArrayList<>();
                for(Order m : originalItems){
                    Customer c = m.getCustomer();
                    if(c != null){
                        if(c.getOutletName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))){
                            nFilteredList.add(m);
                        }else if(c.getSubcounty() != null){
                            if(c.getSubcounty().getName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))){
                                nFilteredList.add(m);
                            }else if(c.getSubcounty().getDistrict() != null){
                                if(c.getSubcounty().getDistrict().getName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
                                    nFilteredList.add(m);
                                }
                            }
                        }
                    }
                }

                results.values = nFilteredList;
                results.count = nFilteredList.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Order> filtered = (List<Order>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0; i < filtered.size(); i++){
                add(filtered.get(i));
            }
            notifyDataSetChanged();
        }
    }
}
