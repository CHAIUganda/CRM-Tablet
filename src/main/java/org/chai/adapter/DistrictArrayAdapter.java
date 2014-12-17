package org.chai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.chai.model.District;

/**
 * Created by victor on 10/27/14.
 */
public class DistrictArrayAdapter extends ArrayAdapter<District> {

    private Context context;
    private District[] districts;

    public DistrictArrayAdapter(Context context, int textViewResourceId, District[] districts1) {
        super(context, textViewResourceId,districts1);
        this.context = context;
        this.districts = districts1;
    }

    public int getCount(){
        return districts.length;
    }

    public District getItem(int position){
        return districts[position];
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(districts[position].getName());
        return label;
    }
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(districts[position].getName());

        return label;
    }

}
