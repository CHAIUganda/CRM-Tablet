package org.chai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.model.Product;
import org.chai.util.Utils;

import java.util.List;

/**
 * Created by Zed on 8/20/2015.
 */
public class ProductListAdapter extends ArrayAdapter<Product> {
    private int lastPosition = -1;

    public ProductListAdapter(Activity activity, List<Product> items) {
        super(activity.getApplicationContext(), R.layout.product_list_row, items);
        for(Product p: items){
            Utils.log("Product -> " + p.getName() + " : " + p.getFormulation() + " : " + p.getGroupName());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Product p = getItem(position);

        if(row == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.product_list_row, parent, false);
        }

        AQuery aq = new AQuery(row);

        aq.id(R.id.txt_product_name).text(p.getName());
        aq.id(R.id.txt_product_group).text(p.getGroupName());

        Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        row.startAnimation(animation);
        lastPosition = position;

        return row;
    }
}
