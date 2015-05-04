package org.chai.activities.tasks;

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

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.IViewManipulator;
import org.chai.adapter.FormListAdapter;
import org.chai.util.Utils;
import org.chai.util.customwidget.FormSearchTextField;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Zed on 5/2/2015.
 */
public class DiarrheaFormZincFragment extends Fragment implements IViewManipulator {
    AQuery aq;
    LinearLayout rowsContainer;
    View view;
    ImageView addButton;
    ArrayList<View> rows;
    int currentPosition = -1;

    boolean viewsHidden = false;

    String[] items = new String[]{
            "DT Zinc",
            "Zincocet",
            "Zinkid",
            "Zinc Sulphate",
            "Zincos"
    };

    DiarrheaFormActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (DiarrheaFormActivity)getActivity();

        if(view != null){
            ((ViewGroup)view.getParent()).removeView(view);
        }else{
            view = inflater.inflate(R.layout.diarrhea_form_zinc_fragment, container, false);

            aq = new AQuery(view);
            rowsContainer = (LinearLayout)view.findViewById(R.id.ln_rows_container);
            addButton = (ImageView)view.findViewById(R.id.btn_add_row);

            aq.id(R.id.btn_add_row).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addRow(rowsContainer, items, "Type or Select from list");
                }
            });

            aq.id(R.id.do_you_stock_zinc).itemSelected(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 2){
                        activity.pager.setCurrentItem(3);
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

        aq.id(R.id.btn_add_row).gone();
        aq.id(R.id.txt_prompt_title).gone();
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

        aq.id(R.id.btn_add_row).visible();
        aq.id(R.id.txt_prompt_title).visible();
        aq.id(R.id.txt_form_title).visible();
    }
}
