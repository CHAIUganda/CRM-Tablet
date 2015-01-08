package org.chai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.AdhockSale;
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
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.call_list_row,null);
        }
        TextView saleCustomerName = (TextView)convertView.findViewById(R.id.call_task_description);
        TextView saleCustomerLocationTxtView = (TextView)convertView.findViewById(R.id.call_customername);
        TextView saleDateTxtView = (TextView)convertView.findViewById(R.id.call_customerlocation);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.callthumbnail);

        AdhockSale adhockSale = sales.get(position);
        if(adhockSale!=null){
            saleCustomerName.setText(adhockSale.getCustomer().getOutletName());
            saleCustomerLocationTxtView.setText(adhockSale.getCustomer().getDescriptionOfOutletLocation());
            saleDateTxtView.setText(Utils.dateToString(adhockSale.getDateOfSale()));
            imageView.setImageResource(R.drawable.cart);
        }
        return convertView;
    }

}
