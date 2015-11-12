package org.chai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.chai.model.ProductGroup;

/**
 * Created by Zed on 11/12/2015.
 */
public class ProductGroupAdapter extends ArrayAdapter<ProductGroup> {
    private Context context;
    private ProductGroup[] groups;

    public ProductGroupAdapter(Context context, int textViewResourceId, ProductGroup[] groups) {
        super(context, textViewResourceId, groups);
        this.context = context;
        this.groups = groups;
    }

    public int getCount() {
        return groups.length;
    }

    public ProductGroup getItem(int position) {
        return groups[position];
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.WHITE);
        label.setText(groups[position].getName());
        label.setPadding(10, 10, 10, 10);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.WHITE);
        label.setText(groups[position].getName());
        label.setPadding(10, 10, 10, 10);

        return label;
    }
}
