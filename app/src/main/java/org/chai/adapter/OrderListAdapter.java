package org.chai.adapter;

import android.app.Activity;
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
import org.chai.util.ServerResponse;
import org.chai.util.Utils;

import java.util.List;

/**
 * Created by victor on 1/6/15.
 */
public class OrderListAdapter extends BaseAdapter{
    private Activity activity;
    private List<Order> orders;
    private LayoutInflater layoutInflater;

    public OrderListAdapter(Activity activity,List<Order> orders){
        this.activity = activity;
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
        OrderHolder orderHolder = null;
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.call_list_row,null);
            orderHolder = new OrderHolder();
            orderHolder.orderCustomerName = (TextView) convertView.findViewById(R.id.call_task_description);
            orderHolder.orderCustomerLocationTxtView = (TextView) convertView.findViewById(R.id.call_customername);
            orderHolder.orderDateTxtView = (TextView) convertView.findViewById(R.id.call_customerlocation);
            orderHolder.imageView = (ImageView) convertView.findViewById(R.id.callthumbnail);
            orderHolder.txterror = (ImageView) convertView.findViewById(R.id.bg_error);

            convertView.setTag(orderHolder);

        }else{
            orderHolder = (OrderHolder) convertView.getTag();
        }
        final Order order = orders.get(position);
        if(order!=null){
            orderHolder.orderCustomerName.setText(order.getCustomer().getOutletName());
            orderHolder.orderCustomerLocationTxtView.setText(order.getCustomer().getDescriptionOfOutletLocation());
            orderHolder.orderDateTxtView.setText(Utils.dateToString(order.getOrderDate()));
            orderHolder.imageView.setImageResource(R.drawable.cart);
            if(order.getSyncronisationStatus()!= null && order.getSyncronisationStatus()==BaseEntity.SYNC_FAIL){
                orderHolder.txterror.setVisibility(View.VISIBLE);
            }
            orderHolder.txterror.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(order.getSyncronisationStatus()!= null && order.getSyncronisationStatus()==BaseEntity.SYNC_FAIL){
                        Utils.displayPopupWindow(activity, view, ServerResponse.parseErrorMessage(order.getSyncronisationMessage()));
                    }
                }
            });
        }
        return convertView;
    }

    static class OrderHolder {
        TextView orderCustomerName;
        TextView orderCustomerLocationTxtView;
        TextView orderDateTxtView;
        ImageView imageView;
        ImageView txterror;
    }

}
