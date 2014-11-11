package org.chai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.chai.model.Parish;

/**
 * Created by victor on 11/10/14.
 */
public class ParishArrayAdapter extends ArrayAdapter<Parish> {

    private Context context;
    private Parish[] parishes;

    public ParishArrayAdapter(Context context, int textViewResourceId, Parish[] parishes) {
        super(context, textViewResourceId,parishes);
        this.context = context;
        this.parishes = parishes;
    }

    public int getCount(){
        return parishes.length;
    }

    public Parish getItem(int position){
        return parishes[position];
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.WHITE);
        label.setText(parishes[position].getName());
        return label;
    }
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.WHITE);
        label.setText(parishes[position].getName());

        return label;
    }

}
