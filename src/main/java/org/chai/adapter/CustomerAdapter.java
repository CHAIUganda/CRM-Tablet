package org.chai.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.chai.R;
import org.chai.activities.customer.CustomerDetailsActivity;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by victor on 10/16/14.
 */
public class CustomerAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Customer> customers;
    private ArrayList<Customer> filterList;
    private Context context;

    public CustomerAdapter(Context context,Activity activity,List<Customer> customers){
        this.activity = activity;
        this.customers = customers;
        this.context = context;
        filterList = new ArrayList<Customer>();
        filterList.addAll(customers);
    }

    @Override
    public int getCount() {
        return customers.size();
    }

    @Override
    public Object getItem(int index) {
        return customers.get(index);
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
            convertView = inflater.inflate(R.layout.customer_list_row,null);
        }
        TextView customerName = (TextView)convertView.findViewById(R.id.customername);
        TextView customerAddress = (TextView)convertView.findViewById(R.id.customeraddress);
        TextView telephone = (TextView)convertView.findViewById(R.id.customertelephone);

        Customer customer = customers.get(position);
        CustomerContact customerCtct = null;
        if(customer.getContacts().size()>0){
            customerCtct = Utils.getKeyCustomerContact(customer.getContacts());
        }

        if(customerCtct!=null){
            telephone.setText(customerCtct.getContact());
        }else{
            telephone.setText("No Contact Available");
        }

        customerName.setText(customer.getOutletName());
        customerAddress.setText(Utils.truncateString(customer.getDescriptionOfOutletLocation(),50));

/*        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(activity.getApplicationContext(), CustomerDetailsActivity.class);
                    activity.startActivity(intent);
                }catch (Exception ex){
                    Toast.makeText(context, "error:" + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });*/
        return convertView;
    }

    public void filter(String term){
        term = term.toLowerCase(Locale.getDefault());
        customers.clear();
        if(term.length()== 0){
            customers.addAll(filterList);
        }else{
            for(Customer customer:filterList){
                if(customer.getOutletName().toLowerCase(Locale.getDefault()).contains(term)){
                    customers.add(customer);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateListView(List<Customer> customersList){
        customers = customersList;
        notifyDataSetChanged();
    }
}
