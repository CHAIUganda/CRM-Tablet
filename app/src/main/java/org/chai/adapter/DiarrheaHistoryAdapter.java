package org.chai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.model.DetailerCall;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

/**
 * Created by Zed on 5/12/2015.
 */
public class DiarrheaHistoryAdapter extends ArrayAdapter<DetailerCall> {
    private int lastPosition = -1;

    public DiarrheaHistoryAdapter(Context context, int resource, List<DetailerCall> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null){
            LayoutInflater inflator = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.history_item_row, null);
        }

        DetailerCall m = getItem(position);
        Customer c = m.getTask().getCustomer();

        CustomerContact contact = c.getCustomerContacts().get(0);

        AQuery aq = new AQuery(row);
        aq.id(R.id.txt_customer_name).text(c.getOutletName());
        aq.id(R.id.txt_time).text(new PrettyTime().format(m.getTask().getCompletionDate()));
        aq.id(R.id.txt_customer_contact).text(contact.getContact() + " - " + c.getSubcounty().getName() + " | " + c.getSubcounty().getDistrict().getName());

        Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        row.startAnimation(animation);
        lastPosition = position;

        return row;
    }
}
