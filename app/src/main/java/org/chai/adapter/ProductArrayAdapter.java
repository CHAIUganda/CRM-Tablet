package org.chai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.chai.model.Product;

/**
 * Created by victor on 12/26/14.
 */
public class ProductArrayAdapter extends ArrayAdapter<Product> {
    private Context context;
    private Product[] products;

    public ProductArrayAdapter(Context context, int textViewResourceId, Product[] products) {
        super(context, textViewResourceId,products);
        this.context = context;
        this.products = products;
    }

    public int getCount(){
        return products.length;
    }

    public Product getItem(int position){
        return products[position];
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.WHITE);
        label.setText(products[position].getName());
        label.setPadding(10, 10, 10, 10);
        return label;
    }
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.WHITE);
        label.setText(products[position].getName());
        label.setPadding(10, 10, 10, 10);

        return label;
    }
}
