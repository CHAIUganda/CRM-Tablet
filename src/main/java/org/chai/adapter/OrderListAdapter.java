package org.chai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.BaseEntity;
import org.chai.model.Order;
import org.chai.util.Utils;

import java.util.List;

/**
 * Created by victor on 1/6/15.
 */
public class OrderListAdapter extends BaseAdapter{
    private Context context;
    private List<Order> orders;
    private LayoutInflater layoutInflater;

    public OrderListAdapter(Context context,List<Order> orders){
        this.context = context;
        this.orders = orders;
    }
    public int getCount(){
        return orders.size();
    }

    public Order getItem(int position){
        return orders.get(position);
    }
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.call_list_row,null);
        }
        TextView orderCustomerName = (TextView)convertView.findViewById(R.id.call_task_description);
        TextView orderCustomerLocationTxtView = (TextView)convertView.findViewById(R.id.call_customername);
        TextView orderDateTxtView = (TextView)convertView.findViewById(R.id.call_customerlocation);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.callthumbnail);
        TextView txterror = (TextView) convertView.findViewById(R.id.bg_error);

        Order order = orders.get(position);
        if(order!=null){
            orderCustomerName.setText(order.getCustomer().getOutletName());
            orderCustomerLocationTxtView.setText(order.getCustomer().getDescriptionOfOutletLocation());
            orderDateTxtView.setText(Utils.dateToString(order.getOrderDate()));
            imageView.setImageResource(R.drawable.cart);
            if(order.getSyncronisationStatus()!= null && order.getSyncronisationStatus()==BaseEntity.SYNC_FAIL){
                txterror.setVisibility(View.VISIBLE);
                txterror.setError(order.getSyncronisationMessage());
            }
        }
        return convertView;
    }

}
