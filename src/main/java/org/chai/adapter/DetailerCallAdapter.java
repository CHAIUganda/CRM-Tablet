package org.chai.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.BaseEntity;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.model.DetailerCall;
import org.chai.util.ServerResponse;
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

    public DetailerCallAdapter(Activity activity,List<DetailerCall> detailerCalls){
        this.activity = activity;
        this.detailerCalls = detailerCalls;
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
        ViewHolder holder = null;
        if(inflater == null){
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = inflater.inflate(R.layout.call_list_row,null);
            holder = new ViewHolder();
            holder.taskDescription = (TextView)convertView.findViewById(R.id.call_task_description);
            holder.customerNameTxtView = (TextView)convertView.findViewById(R.id.call_customername);
            holder.customerLocationTxtView = (TextView)convertView.findViewById(R.id.call_customerlocation);
            holder.txterror = (ImageView) convertView.findViewById(R.id.bg_error);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final DetailerCall detailerCall = detailerCalls.get(position);
        if(detailerCall!=null){
            try{
                holder.taskDescription.setText(detailerCall.getTask().getDescription());
                if(detailerCall.getIsHistory()){
                    holder.taskDescription.setTextColor(Color.parseColor("#C0C0C0"));
                    holder.customerLocationTxtView.setTextColor(Color.parseColor("#C0C0C0"));
                    holder.customerNameTxtView.setTextColor(Color.parseColor("#C0C0C0"));
                }
                holder.customerNameTxtView.setText(detailerCall.getTask().getCustomer().getOutletName());
                holder.customerLocationTxtView.setText(detailerCall.getTask().getCustomer().getDescriptionOfOutletLocation());
                if(detailerCall.getTask().getSyncronisationStatus()!= null && detailerCall.getTask().getSyncronisationStatus()== BaseEntity.SYNC_FAIL){
                    holder.txterror.setVisibility(View.VISIBLE);
                }
                holder.txterror.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.displayPopupWindow(activity, view, ServerResponse.parseErrorMessage(detailerCall.getTask().getSyncronisationMessage()));
                    }
                });
            }catch (Exception ex){
                //
            }
        }
        return convertView;
    }

    static class ViewHolder{
        TextView taskDescription  ;
        TextView customerNameTxtView ;
        TextView customerLocationTxtView ;
        ImageView txterror;
    }
}
