package org.chai.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.model.DetailerCall;
import org.chai.util.Utils;

import java.util.List;

/**
 * Created by victor on 10/31/14.
 */
public class DetailerCallAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DetailerCall> detailerCalls;
    private Context context;

    public DetailerCallAdapter(Context context,Activity activity,List<DetailerCall> detailerCalls){
        this.activity = activity;
        this.detailerCalls = detailerCalls;
        this.context = context;
    }

    @Override
    public int getCount() {
        return detailerCalls.size();
    }

    @Override
    public Object getItem(int index) {
        return detailerCalls.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null){
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = inflater.inflate(R.layout.call_list_row,null);
        }
        TextView taskDescription = (TextView)convertView.findViewById(R.id.call_task_description);
        TextView customerNameTxtView = (TextView)convertView.findViewById(R.id.call_customername);
        TextView customerLocationTxtView = (TextView)convertView.findViewById(R.id.call_customerlocation);

        DetailerCall detailerCall = detailerCalls.get(position);
        if(detailerCall!=null){
            taskDescription.setText(detailerCall.getTask().getDescription());
            customerNameTxtView.setText(detailerCall.getTask().getCustomer().getOutletName());
            customerLocationTxtView.setText(detailerCall.getTask().getCustomer().getDescriptionOfOutletLocation());
        }
        return convertView;
    }
}
