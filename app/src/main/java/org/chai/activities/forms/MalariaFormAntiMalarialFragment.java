package org.chai.activities.forms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.IViewManipulator;
import org.chai.adapter.FormListAdapter;
import org.chai.model.DetailerStock;
import org.chai.util.Utils;
import org.chai.util.customwidget.FormSearchTextField;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Zed on 4/9/2015.
 */
public class MalariaFormAntiMalarialFragment extends Fragment implements IViewManipulator {
    public static final String STOCK_TYPE = "antimalarial";
    AQuery aq;
    LinearLayout antimalarialContainer;
    View view;
    ImageView addAntimalarialButton;
    ArrayList<View> rows;

    public boolean viewsHidden = false;

    String[] items = new String[]{
            "Artemether lumefantrine, such as Lonart, Artefan, Lumartem, Coartem, Lumaren",
            "Artesunate amodiaquine, such as DUAC, Coarsucam, Winthrop",
            "Other artemisinin combination therapies, such as Arco",
            "Artemether monotherapies, such as Larither, Artemether Rtitas, Artemedine, Artenam, Romether",
            "Artesunate monotherapies, such as Plasmotrim, Artesun, G-Sunate",
            "Chloroquine, such as Sugaquin, Mediquine, Kam quin, Bioquin, Renequin, Maxaquin, Oroquin",
            "SP, such as Fansidar, Malaren, Kamsidar, Agosidar, Neosidar",
            "Quinine, such as Quinas, Quine, Quinfer",
            "Amodiaquine, such as Amobin",
            "Mefloquine, such as Mephaquin, Meflotas",
            "Syrups or suspensions, such as Quinine-K, Quinimix, Requin, Ago-quinine, Co-malartem suspension, Ago-CQ",
            "Injectables, such as Rogoquin, Artemether, Quinax, Larither, Kwinil"
    };

    MalariaFormActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MalariaFormActivity)getActivity();

        view = inflater.inflate(R.layout.malaria_form_fragment_3, container, false);

        aq = new AQuery(view);
        antimalarialContainer = (LinearLayout)view.findViewById(R.id.ln_antimalarials_container);
        addAntimalarialButton = (ImageView)view.findViewById(R.id.btn_add_antimalarial_row);

        aq.id(R.id.btn_add_antimalarial_row).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow(new DetailerStock(), true);
            }
        });

        aq.id(R.id.do_you_stock).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    clearStocks();
                    activity.pager.setCurrentItem(3);
                }
                if (position == 1 && rows.size() == 0) {
                    addRow(new DetailerStock(), true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(activity.call.getUuid() != null){
            populateFields();
        }

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && viewsHidden){
                    showAllViews();
                    return true;
                }
                return false;
            }
        });
        return view;
    }



    private void clearStocks(){
        for(View row : rows){
            ((ViewGroup)row.getParent()).removeView(row);
            activity.antimalarials = new ArrayList<DetailerStock>();
            rows = new ArrayList<View>();
        }
    }

    @Override
    public void onResume() {
        Utils.log("onResume()");
        super.onResume();

        if(rows != null){
            for(View row: rows){
                ((ViewGroup)row.getParent()).removeView(row);
            }
        }

        ArrayList<DetailerStock> temp = new ArrayList<DetailerStock>();
        temp.addAll(activity.antimalarials);

        rows = new ArrayList<View>();
        activity.antimalarials = new ArrayList<DetailerStock>();

        for(int i = 0; i < temp.size(); i++){
            addRow(temp.get(i), false);
        }
    }

    @Override
    public void onPause() {
        Utils.log("onPause()");
        super.onPause();
        DetailerStock stock;
        try{
            for(View row : rows){
                AQuery a = new AQuery(row);
                String brand = a.id(R.id.txt_antimalarial).getText().toString();
                String level = a.id(R.id.txt_stock_level).getText().toString();
                String buying = a.id(R.id.txt_buying_price).getText().toString();
                String selling = a.id(R.id.txt_selling_price).getText().toString();
                String pack = a.id(R.id.spn_pack_size).getSelectedItem().toString();

                stock = activity.antimalarials.get(rows.indexOf(row));
                stock.setCategory(STOCK_TYPE);
                if(!brand.isEmpty()){
                    stock.setBrand(brand);
                }
                if(!level.isEmpty()){
                    stock.setStockLevel(Double.parseDouble(level));
                }
                if(!buying.isEmpty()){
                    stock.setBuyingPrice(Double.parseDouble(buying));
                }
                if(!selling.isEmpty()){
                    stock.setSellingPrice(Double.parseDouble(selling));
                }
                stock.setPackSize(pack);
            }
        }catch(Exception ex){
            Utils.log("Error saving stock state -> " + ex.getMessage());
        }
    }

    private void addRow(final DetailerStock stock, boolean hideOthers){
        Utils.log("Adding row -> " + stock.getBrand() + " : " + hideOthers);
        LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View row = inflator.inflate(R.layout.antimalarial_row, null);
        AQuery a = new AQuery(row);
        final ImageView remove = (ImageView)row.findViewById(R.id.btn_remove_row);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rows.remove(row);
                activity.antimalarials.remove(stock);
                LinearLayout parent = (LinearLayout) v.getParent();
                LinearLayout root = (LinearLayout) parent.getParent();
                antimalarialContainer.removeView(root);
            }
        });

        ListView list = (ListView)row.findViewById(R.id.lst_items);
        final FormListAdapter adapter = new FormListAdapter(getActivity(), R.layout.form_list_row, new ArrayList<>(Arrays.asList(items)));
        list.setAdapter(adapter);

        Utils.setListViewHeightBasedOnChildren(list);

        final FormSearchTextField text = (FormSearchTextField)row.findViewById(R.id.txt_antimalarial);
        text.setViewManipulator(this);
        text.setListView(list);
        text.setHint("Type or select brand");

        Spinner packsizeSpinner = a.id(R.id.spn_pack_size).getSpinner();

        try{
            if(stock.getBrand() != null){
                text.setText(stock.getBrand());
            }
            if(stock.getStockLevel() != 0){
                a.id(R.id.txt_stock_level).text(Double.toString(stock.getStockLevel()));
            }
            if(stock.getBuyingPrice() != null){
                a.id(R.id.txt_buying_price).text(Double.toString(stock.getBuyingPrice()));
            }
            if(stock.getSellingPrice() != null){
                a.id(R.id.txt_selling_price).text(Double.toString(stock.getSellingPrice()));
            }
            if(stock.getPackSize() != null){
                packsizeSpinner.setSelection(((ArrayAdapter<String>)packsizeSpinner.getAdapter()).getPosition(stock.getPackSize()));
            }
        }catch (Exception ex){
            Utils.log("Error populating stock items -> " + ex.getMessage());
        }

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
                if (!viewsHidden) {
                    hideOtherRows(row);
                }
                return false;
            }
        });

        antimalarialContainer.addView(row);
        rows.add(row);
        activity.antimalarials.add(stock);

        if(hideOthers){
            hideOtherRows(row);
        }
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

        aq.id(R.id.btn_add_antimalarial_row).gone();
        aq.id(R.id.txt_antimalarial_title).gone();
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

        aq.id(R.id.btn_add_antimalarial_row).visible();
        aq.id(R.id.txt_antimalarial_title).visible();
        aq.id(R.id.txt_form_title).visible();
    }

    private void populateFields(){
        try{
            boolean doyoustock = activity.call.getDoYouStockAntimalarials();
            if(doyoustock){
                aq.id(R.id.do_you_stock).setSelection(1);
            }else{
                aq.id(R.id.do_you_stock).setSelection(2);
            }
        }catch (Exception ex){

        }
    }

    public boolean saveFields(){
        if(aq.id(R.id.do_you_stock).getSelectedItem().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please select wether customer stocks Antimalarials or not", Toast.LENGTH_LONG).show();
            return false;
        }

        activity.call.setDoYouStockAntimalarials(aq.id(R.id.do_you_stock).getSelectedItemPosition() == 1);

        int i = 1;
        for(View row: rows){
            AQuery a = new AQuery(row);
            String brand = a.id(R.id.txt_antimalarial).getText().toString();
            if(brand.isEmpty()){
                Toast.makeText(getActivity(), "Select Antimalarial brand on row " + i, Toast.LENGTH_LONG).show();
                return false;
            }

            DetailerStock stock = activity.antimalarials.get(rows.indexOf(row));
            stock.setBrand(brand);
            stock.setCategory(STOCK_TYPE);
            stock.setPackSize(a.id(R.id.spn_pack_size).getSelectedItem().toString());
            try{
                stock.setBuyingPrice(Double.parseDouble(a.id(R.id.txt_buying_price).getText().toString()));
                stock.setSellingPrice(Double.parseDouble(a.id(R.id.txt_selling_price).getText().toString()));
                stock.setStockLevel(Double.parseDouble(a.id(R.id.txt_stock_level).getText().toString()));
            }catch(Exception ex){
                Toast.makeText(getActivity(), "Please enter Prices and Quantities for Antimalarial Brand on row " + i, Toast.LENGTH_LONG).show();
                return false;
            }

            i++;
        }
        return true;
    }
}
