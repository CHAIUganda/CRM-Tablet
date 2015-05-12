package org.chai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.model.MalariaDetail;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

/**
 * Created by Zed on 5/12/2015.
 */
public class MalariaHistoryAdapter extends ArrayAdapter<MalariaDetail> {
    public MalariaHistoryAdapter(Context context, int resource, List<MalariaDetail> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null){
            LayoutInflater inflator = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.history_item_row, null);
        }

        MalariaDetail m = getItem(position);
        Customer c = m.getTask().getCustomer();
        CustomerContact contact = c.getCustomerContacts().get(0);

        AQuery aq = new AQuery(row);
        aq.id(R.id.txt_customer_name).text(c.getOutletName());
        aq.id(R.id.txt_time).text(new PrettyTime().format(m.getTask().getCompletionDate()));
        aq.id(R.id.txt_customer_contact).text(contact.getContact() + " - " + c.getSubcounty().getName() + " | " + c.getSubcounty().getDistrict().getName());
        return row;
    }
}
