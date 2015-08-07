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
import org.chai.model.Task;
import org.chai.util.CustomerSegmentComparator;

import java.util.Collections;
import java.util.List;

/**
 * Created by victor on 10/23/14.
 */
public class TaskListAdapter extends ArrayAdapter<Task> {
    private List<Task> tasks;
    private int lastPosition = -1;
    Context cxt;

    public TaskListAdapter(Context activity, List<Task> aTaskList){
        super(activity.getApplicationContext(), R.layout.task_calender_fragment, aTaskList);
        this.tasks = aTaskList;
        cxt = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null){
            LayoutInflater inflator = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.history_item_row, null);
        }

        Task t = getItem(position);

        Customer c = t.getCustomer();
        CustomerContact contact = c.getCustomerContacts().get(0);

        AQuery aq = new AQuery(row);

        try{
            int segment = cxt.getResources().getIdentifier("segment_" + c.getSegment().toLowerCase(), "drawable", cxt.getPackageName());
            aq.id(R.id.img_segment).image(segment);
        }catch (Exception ex){
            aq.id(R.id.img_segment).gone();
        }

        aq.id(R.id.txt_customer_name).text(c.getOutletName());
        aq.id(R.id.txt_time).text(c.getDescriptionOfOutletLocation());
        aq.id(R.id.txt_customer_contact).text(contact.getContact());


        Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        row.startAnimation(animation);
        lastPosition = position;

        return row;
    }

    @Override
     public void notifyDataSetChanged() {
        Collections.sort(tasks, new CustomerSegmentComparator());
        super.notifyDataSetChanged();
    }
}
