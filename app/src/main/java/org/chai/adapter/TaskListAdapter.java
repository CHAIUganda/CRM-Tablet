package org.chai.adapter;

import android.app.Activity;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by victor on 10/23/14.
 */
public class TaskListAdapter extends ArrayAdapter<Task> {
    private List<Task> tasks;
    private int lastPosition = -1;

    public TaskListAdapter(Activity activity,List<Task> aTaskList){
        super(activity.getApplicationContext(), R.layout.task_calender_fragment, aTaskList);
        this.tasks = aTaskList;
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
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                Customer customer1 = task1.getCustomer();
                Customer customer2 = task2.getCustomer();
                if (customer1 == null) {
                    return -1;
                }
                if (customer2 == null) {
                    return 1;
                }
                if (customer1.equals(customer2)) {
                    return 0;
                }

                int ret = customer1.getOutletName().compareToIgnoreCase(customer2.getOutletName());
                if(ret!=0){
                    return ret;
                }else{
                    return -1;
                }
            }
        });
        super.notifyDataSetChanged();
    }
}
