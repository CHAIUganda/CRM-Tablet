package org.chai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.model.Task;
import org.chai.util.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by victor on 10/23/14.
 */
public class TaskListAdapter extends ArrayAdapter<Task> {
    private List<Task> taskList;

    public TaskListAdapter(Activity activity,List<Task> aTaskList){
        super(activity.getApplicationContext(), R.layout.task_calender_fragment, aTaskList);
        this.taskList = aTaskList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_list_item, parent, false);

            viewHolder = new ViewHolder();
            TextView taskListOutlet = (TextView) convertView.findViewById(R.id.task_list_outlet);
            TextView taskListAddress = (TextView) convertView.findViewById(R.id.task_list_outlet_contact);
            TextView taskListSubcounty = (TextView) convertView.findViewById(R.id.task_list_subcounty);
            TextView taskListLocationDesc = (TextView) convertView.findViewById(R.id.task_list_location_desc);

            viewHolder.subcounty = taskListSubcounty;
            viewHolder.outlet = taskListOutlet;
            viewHolder.contact = taskListAddress;
            viewHolder.locationDescription = taskListLocationDesc;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Task task = taskList.get(position);
        Customer customer = task.getCustomer();
        if(customer!=null){
            viewHolder.subcounty.setText(customer.getSubcounty().getName());
            viewHolder.outlet.setText(customer.getOutletName());
            viewHolder.locationDescription.setText(Utils.truncateString(customer.getDescriptionOfOutletLocation(),20));
        }
        if (customer!= null && customer.getCustomerContacts().size() > 0) {
            CustomerContact customerCtct = null;
            customerCtct = Utils.getKeyCustomerContact(customer.getCustomerContacts());
            if(customerCtct != null){
                viewHolder.contact.setText(customerCtct.getContact()!=null?customerCtct.getContact():"No Contact");
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView outlet;
        TextView contact;
        TextView subcounty;
        TextView locationDescription;
    }

    @Override
     public void notifyDataSetChanged() {
        Collections.sort(taskList, new Comparator<Task>() {
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
