package org.chai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.chai.model.Subcounty;
import org.chai.model.Village;

/**
 * Created by victor on 10/27/14.
 */
public class SubcountyArrayAdapter extends ArrayAdapter<Village> {

    private Context context;
    private Village[] villages;

    public SubcountyArrayAdapter(Context context, int textViewResourceId,Village[] subcounties) {
        super(context, textViewResourceId,subcounties);
        this.context = context;
        this.villages = subcounties;
    }

    public int getCount(){
        return villages.length;
    }

    public Village getItem(int position){
        return villages[position];
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(villages[position].getName());
        return label;
    }
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(villages[position].getName());

        return label;
    }

}
