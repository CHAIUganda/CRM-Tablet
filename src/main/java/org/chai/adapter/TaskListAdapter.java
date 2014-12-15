package org.chai.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.model.Task;
import org.chai.util.Utils;

import java.util.HashMap;
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
            TextView taskListtype = (TextView) convertView.findViewById(R.id.task_list_type);
            TextView taskListOutlet = (TextView) convertView.findViewById(R.id.task_list_outlet);
            TextView taskListAddress = (TextView) convertView.findViewById(R.id.task_list_address);

            viewHolder.type = taskListtype;
            viewHolder.outlet = taskListOutlet;
            viewHolder.address = taskListAddress;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Task task = taskList.get(position);
        Customer customer = task.getCustomer();

        viewHolder.type.setText(task.getType());
        viewHolder.outlet.setText(","+customer.getOutletName());
        viewHolder.address.setText(","+Utils.truncateString(customer.getDescriptionOfOutletLocation(), 50));

        return convertView;
    }

    static class ViewHolder {
        TextView type;
        TextView outlet;
        TextView address;
    }


}
