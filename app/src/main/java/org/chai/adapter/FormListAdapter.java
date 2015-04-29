package org.chai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.androidquery.AQuery;

import org.chai.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Zed on 4/9/2015.
 */
public class FormListAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> items;
    ItemFilter itemFilter;

    public FormListAdapter(Context context, int resource, ArrayList<String> i) {
        super(context, resource, i);
        items = new ArrayList<String>();
        items.addAll(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.form_list_row, null);
        }

        AQuery aq = new AQuery(row);
        aq.id(R.id.txt_title).text(getItem(position));

        return  row;
    }

    @Override
    public Filter getFilter() {
        if(itemFilter == null){
            itemFilter = new ItemFilter();
        }
        return  itemFilter;
    }

    private class ItemFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = items;
                results.count = items.size();
            }else{
                List<String> nStringList = new ArrayList<String>();
                for(String s : items){
                    if(s.toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))){
                        nStringList.add(s);
                    }
                }
                results.values = nStringList;
                results.count = nStringList.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<String> filtered = (ArrayList<String>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0; i < filtered.size(); i++){
                add(filtered.get(i));
            }
            notifyDataSetChanged();
        }
    }
}
