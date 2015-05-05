package org.chai.activities.org.chai.activities.forms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.IViewManipulator;
import org.chai.adapter.FormListAdapter;
import org.chai.util.Utils;
import org.chai.util.customwidget.FormSearchTextField;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Zed on 4/23/2015.
 */
public class MalariaFormFragment5 extends Fragment implements IViewManipulator{
    AQuery aq;
    LinearLayout rdtContainer;
    View view;
    ImageView addRdtButton;
    ArrayList<View> rows;
    int currentPosition = -1;

    boolean viewsHidden = false;

    String[] rdtItems = new String[]{
            "Parahit",
            "SD Bioline",
            "First Response Malaria",
            "PAN/Pf",
            "Clearview Malaria"
    };

    MalariaFormActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MalariaFormActivity)getActivity();
        if(view != null){
            ((ViewGroup)view.getParent()).removeView(view);
        }else{
            view = inflater.inflate(R.layout.malaria_form_fragment_5, container, false);

            aq = new AQuery(view);
            rdtContainer = (LinearLayout)view.findViewById(R.id.ln_rdts_container);
            addRdtButton = (ImageView)view.findViewById(R.id.btn_add_rdt_row);

            aq.id(R.id.btn_add_rdt_row).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addRow(rdtContainer, rdtItems, "Type or Select from list");
                }
            });

            aq.id(R.id.do_you_stock).itemSelected(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 2){
                        activity.pager.setCurrentItem(4);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            rows = new ArrayList<View>();
        }

        return view;
    }

    private void addRow(final LinearLayout container, String[] items, String hint){
        LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View row = inflator.inflate(R.layout.antimalarial_row, null);

        final ImageView remove = (ImageView)row.findViewById(R.id.btn_remove_row);
        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rows.remove(row);
                LinearLayout parent = (LinearLayout)v.getParent();
                LinearLayout root = (LinearLayout)parent.getParent();
                container.removeView(root);
            }
        });

        ListView list = (ListView)row.findViewById(R.id.lst_items);
        final FormListAdapter adapter = new FormListAdapter(getActivity(), R.layout.form_list_row, new ArrayList<>(Arrays.asList(items)));
        list.setAdapter(adapter);

        Utils.setListViewHeightBasedOnChildren(list);

        final FormSearchTextField text = (FormSearchTextField)row.findViewById(R.id.txt_antimalarial);
        text.setViewManipulator(this);
        text.setListView(list);
        text.setHint(hint);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!viewsHidden){
                    hideOtherRows(row);
                }
                return false;
            }
        });

        container.addView(row);
        rows.add(row);

        hideOtherRows(row);
    }

    private void hideOtherRows(View exclude){
        Utils.log("Hidding all rows");
        viewsHidden = true;

        for(View v: rows){
            if(!v.equals(exclude)){
                v.setVisibility(View.GONE);
            }
        }

        ListView list = (ListView)exclude.findViewById(R.id.lst_items);
        list.setVisibility(View.VISIBLE);
        ImageView btn = (ImageView)exclude.findViewById(R.id.btn_remove_row);
        btn.setVisibility(View.GONE);

        aq.id(R.id.btn_add_rdt_row).gone();
        aq.id(R.id.txt_rdts_title).gone();
        aq.id(R.id.txt_form_title).gone();
    }

    public void showAllViews(){
        viewsHidden = false;
        ListView list;
        for(View v: rows){
            v.setVisibility(View.VISIBLE);
            list = (ListView)v.findViewById(R.id.lst_items);
            list.setVisibility(View.GONE);

            ImageView btn = (ImageView)v.findViewById(R.id.btn_remove_row);
            btn.setVisibility(View.VISIBLE);
        }

        aq.id(R.id.btn_add_rdt_row).visible();
        aq.id(R.id.txt_rdts_title).visible();
        aq.id(R.id.txt_form_title).visible();
    }

    public boolean saveFields(){
        if(aq.id(R.id.do_you_stock).getSelectedItem().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please select wether customer stocks RDTs or not", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
