package org.chai.activities.customer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;

import org.chai.R;

import java.util.ArrayList;

/**
 * Created by Zed on 4/29/2015.
 */
public class CustomerContactsFormFragment extends Fragment{
    AQuery aq;
    View view;
    LinearLayout rowContainer;
    ArrayList<View> rows;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_contacts_form_fragment, container, false);
        aq = new AQuery(view);
        rowContainer = (LinearLayout)view.findViewById(R.id.ln_contacts_container);

        aq.id(R.id.btn_add_row).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow();
            }
        });

        rows = new ArrayList<View>();

        return view;
    }

    private void addRow(){
        LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View row = inflator.inflate(R.layout.customer_contact_form_row, null);

        final ImageView remove = (ImageView)row.findViewById(R.id.btn_remove_row);
        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rows.remove(row);
                LinearLayout parent = (LinearLayout)v.getParent();
                LinearLayout root = (LinearLayout)parent.getParent();
                rowContainer.removeView(root);
            }
        });

        rowContainer.addView(row);
        rows.add(row);
    }

    public boolean saveFields(){
        return true;
    }
}
