package org.chai.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.BaseEntity;
import org.chai.model.Sale;
import org.chai.util.ServerResponse;
import org.chai.util.Utils;

import java.util.List;

/**
 * Created by victor on 12/31/14.
 */
public class SalesAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Sale> sales;

    public SalesAdapter(Activity activity, List<Sale> sales) {
        this.sales = sales;
        this.activity = activity;
    }

    public int getCount(){
        return sales.size();
    }

    public Sale getItem(int position){
        return sales.get(position);
    }
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        final Sale sale = sales.get(position);
        if(sale!=null){
            try{
                holder.taskDescription.setText(sale.getTask().getDescription());
            if(sale.getIsHistory()){
                holder.taskDescription.setTextColor(Color.parseColor("#C0C0C0"));
                holder.customerNameTxtView.setTextColor(Color.parseColor("#C0C0C0"));
                holder.customerLocationTxtView.setTextColor(Color.parseColor("#C0C0C0"));
            }
                holder.customerNameTxtView.setText(sale.getTask().getCustomer().getOutletName());
                holder.customerLocationTxtView.setText(sale.getTask().getCustomer().getDescriptionOfOutletLocation());
                if(sale.getSyncronisationStatus()!= null && sale.getSyncronisationStatus()== BaseEntity.SYNC_FAIL){
                    holder.txterror.setVisibility(View.VISIBLE);
                }
                holder.txterror.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.displayPopupWindow(activity, view, ServerResponse.parseErrorMessage(sale.getSyncronisationMessage()));
                    }
                });
            }catch (Exception ex){
                //
            }
        }
        return convertView;
    }

    static class ViewHolder {
        TextView taskDescription;
        TextView customerNameTxtView;
        TextView customerLocationTxtView;
        ImageView txterror;
    }

}
