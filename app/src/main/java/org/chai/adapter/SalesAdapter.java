package org.chai.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.Sale;

import java.util.List;

/**
 * Created by victor on 12/31/14.
 */
public class SalesAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private List<Sale> sales;

    public SalesAdapter(Context context,Activity activity,List<Sale> sales){
        this.context = context;
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
        if(inflater == null){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = inflater.inflate(R.layout.call_list_row,null);
        }
        TextView taskDescription = (TextView)convertView.findViewById(R.id.call_task_description);
        TextView customerNameTxtView = (TextView)convertView.findViewById(R.id.call_customername);
        TextView customerLocationTxtView = (TextView)convertView.findViewById(R.id.call_customerlocation);

        Sale sale = sales.get(position);
        if(sale!=null){
            try{
            taskDescription.setText(sale.getTask().getDescription());
            if(sale.getIsHistory()){
                taskDescription.setTextColor(Color.parseColor("#C0C0C0"));
                customerNameTxtView.setTextColor(Color.parseColor("#C0C0C0"));
                customerLocationTxtView.setTextColor(Color.parseColor("#C0C0C0"));
            }
                customerNameTxtView.setText(sale.getTask().getCustomer().getOutletName());
                customerLocationTxtView.setText(sale.getTask().getCustomer().getDescriptionOfOutletLocation());
            }catch (Exception ex){
                //
            }
        }
        return convertView;
    }

}
