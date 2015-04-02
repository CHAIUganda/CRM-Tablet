package org.chai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.AdhockSale;
import org.chai.model.BaseEntity;
import org.chai.util.Utils;

import java.util.List;

/**
 * Created by victor on 1/8/15.
 */
public class AdhockSalesAdapter extends BaseAdapter {
    private Context context;
    private List<AdhockSale> sales;
    private LayoutInflater layoutInflater;

    public AdhockSalesAdapter(Context context,List<AdhockSale> adhockSales){
        this.context = context;
        this.sales = adhockSales;
    }
    public int getCount(){
        return sales.size();
    }

    public AdhockSale getItem(int position){
        return sales.get(position);
    }
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.call_list_row,null);
            holder = new ViewHolder();
            holder.saleCustomerName = (TextView) convertView.findViewById(R.id.call_task_description);
            holder.saleCustomerLocationTxtView = (TextView) convertView.findViewById(R.id.call_customername);
            holder.saleDateTxtView = (TextView) convertView.findViewById(R.id.call_customerlocation);
            holder.imageView = (ImageView) convertView.findViewById(R.id.callthumbnail);
            holder.txterror = (TextView) convertView.findViewById(R.id.bg_error);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AdhockSale adhockSale = sales.get(position);
        if(adhockSale!=null){
            try {
                if(adhockSale.getIsHistory()){
                    holder.saleCustomerName.setTextColor(Color.parseColor("#C0C0C0"));
                    holder.saleCustomerLocationTxtView.setTextColor(Color.parseColor("#C0C0C0"));
                    holder.saleDateTxtView.setTextColor(Color.parseColor("#C0C0C0"));
                }
                holder.saleCustomerName.setText(adhockSale.getCustomer().getOutletName());
                holder.saleCustomerLocationTxtView.setText(adhockSale.getCustomer().getDescriptionOfOutletLocation());
                holder.saleDateTxtView.setText(Utils.dateToString(adhockSale.getDateOfSale()));
                holder.imageView.setImageResource(R.drawable.cart);
                if(adhockSale.getSyncronisationStatus()!= null && adhockSale.getSyncronisationStatus()== BaseEntity.SYNC_FAIL){
                    holder.txterror.setVisibility(View.VISIBLE);
                    holder.txterror.setError(adhockSale.getSyncronisationMessage());
                }
            }catch (Exception ex){
                //
            }
        }
        return convertView;
    }

    static class ViewHolder {
        TextView saleCustomerName;
        TextView saleCustomerLocationTxtView;
        TextView saleDateTxtView;
        ImageView imageView;
        TextView txterror;
    }

}
