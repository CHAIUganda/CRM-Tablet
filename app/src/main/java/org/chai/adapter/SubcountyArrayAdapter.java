package org.chai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.chai.model.Subcounty;

/**
 * Created by victor on 11/10/14.
 */
public class SubcountyArrayAdapter extends ArrayAdapter<Subcounty> {

    private Context context;
    private Subcounty[] subcounties;

    public SubcountyArrayAdapter(Context context, int textViewResourceId, Subcounty[] subcounties) {
        super(context, textViewResourceId, subcounties);
        this.context = context;
        this.subcounties = subcounties;
    }

    public int getCount(){
        return subcounties.length;
    }

    public Subcounty getItem(int position){
        return subcounties[position];
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.WHITE);
        label.setText(subcounties[position].getName());
        return label;
    }
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = new TextView(context);
        //label.setTextColor(Color.w);
        label.setText(subcounties[position].getName());
        label.setPadding(10, 10, 10, 10);

        return label;
    }

}
