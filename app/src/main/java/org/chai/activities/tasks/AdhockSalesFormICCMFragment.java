package org.chai.activities.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.util.AdhockICCMPopupDialogFragment;
import org.chai.util.Utils;

/**
 * Created by Zed on 11/16/2015.
 */
public class AdhockSalesFormICCMFragment extends Fragment {
    AQuery aq;
    View view;

    AdhockSalesFormActivity parent;

    boolean stockOrs, stockZinc, stockACTs, stockAmox;

    String minOrsPrice = "";
    String minZincPrice = "";
    String minACTPrice = "";
    String minAmoxPrice = "";

    int minOrsPriceVal = -1;
    int minZincPriceVal = -1;
    int minACTPriceVal = -1;
    int minAmoxPriceVal = -1;

    String message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = (AdhockSalesFormActivity)getActivity();
        view = inflater.inflate(R.layout.sales_form_iccm_fragment, container, false);
        aq = new AQuery(view);

        aq.id(R.id.spn_do_you_stock_ors).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 2){
                    aq.id(R.id.ln_ors_price_container).gone();
                    message = "The MOH recommends ORS and zinc as the 1st line treatment for diarrhea - ALWAYS stock ORS!\n\n" +
                            "ORS replaces lost fluids and essential salts in a child with diarrhea - ORS saves the child's life!\n\n" +
                            "ORS should be given to a child until diarrhea stops - Prescribe 2 sachets per child!";

                    AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                }else{
                    aq.id(R.id.ln_ors_price_container).visible();
                }
                if(position != 0){
                    aq.id(R.id.ln_zinc_container).visible();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aq.id(R.id.spn_do_you_stock_zinc).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 2){
                    aq.id(R.id.ln_zinc_price_container).gone();
                    message = "The MOH recommends that zinc always be given in COMBINATION with ORS as 1st line treatment for diarrhea - Always stock ORS and zinc!\n\n" +
                            "Zinc reduces the duration of diarrhea and future episodes of diarrhea - ORS saves the child's life and Zinc keeps the child healthy!\n\n" +
                            "Give zinc to child for at least 10 days (1 tablet per day) - Prescribe a COMPLETE course of 10 tablets!";;

                    AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                }else{
                    aq.id(R.id.ln_zinc_price_container).visible();
                }
                if(position != 0){
                    aq.id(R.id.ln_act_container).visible();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aq.id(R.id.spn_do_you_stock_acts).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 2){
                    aq.id(R.id.ln_acts_price_container).gone();
                    message = "The MOH recommends ACTs as the 1st line treatment for malaria - ACTs are the most effective antimalarial available!\n\n" +
                            "High-quality ACTs have a Green Leaf logo on the packaging - Always look for the Green Leaf!\n\n" +
                            "The MOH helps lower the cost of these Green Leaf ACTs - Sell ACTs at lower cost to make more affordable to patients!";

                    AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                }else{
                    aq.id(R.id.ln_acts_price_container).visible();
                }
                if(position != 0){
                    aq.id(R.id.ln_amox_container).visible();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aq.id(R.id.spn_do_you_stock_amoxicillin).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 2){
                    aq.id(R.id.ln_amoxicillin_price_container).gone();
                    message = "The MOH now recommends Amoxicillin 250mg DT as the 1st line treatment for childhood pneumonia\n\n" +
                            "Proper diagnosis must be performed before prescribing - Remember!  Not all coughs need an antibiotic - Count breaths before treating!";

                    AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                }else{
                    aq.id(R.id.ln_amoxicillin_price_container).visible();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aq.id(R.id.txt_ors_lowest_price).getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    stockOrs = (aq.id(R.id.spn_do_you_stock_ors).getSelectedItemPosition() == 1);
                    minOrsPrice = aq.id(R.id.txt_ors_lowest_price).getText().toString();
                    if (minOrsPrice.length() == 0 && stockOrs) {
                        Toast.makeText(getActivity(), "Please enter lowest price of ORS", Toast.LENGTH_LONG).show();
                    }
                    if (stockOrs) {
                        try {
                            minOrsPriceVal = Integer.parseInt(minOrsPrice);
                            Utils.log("Lowest ORS price -> " + minOrsPriceVal);
                            if (minOrsPriceVal > 300) {
                                message = "Remind provider about the government approved Recommended Retail Price (300UGX per 1 sachet of ORS)";
                                AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                            }
                        } catch (NumberFormatException ex) {
                            Utils.log("Cannot parse min ORS price");
                            Toast.makeText(getActivity(), "Please enter a valid lowest price of ORS", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        aq.id(R.id.txt_zinc_lowest_price).getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    stockZinc = (aq.id(R.id.spn_do_you_stock_zinc).getSelectedItemPosition() == 1);
                    minZincPrice = aq.id(R.id.txt_zinc_lowest_price).getText().toString();
                    if(minZincPrice.length() == 0 && stockZinc){
                        Toast.makeText(getActivity(), "Please enter lowest price of Zinc", Toast.LENGTH_LONG).show();
                    }
                    if(stockZinc){
                        try{
                            minZincPriceVal = Integer.parseInt(minZincPrice);
                            if(minZincPriceVal > 900){
                                message = "Remind provider about the government approved Recommended Retail Price (900UGX per 10 tablets of zinc)";
                                AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                            }
                        }catch (NumberFormatException ex){
                            Utils.log("Cannot parse min Zinc price");
                            Toast.makeText(getActivity(), "Please enter a valid lowest price of Zinc", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        aq.id(R.id.txt_act_lowest_price).getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    stockACTs = (aq.id(R.id.spn_do_you_stock_acts).getSelectedItemPosition() == 1);
                    minACTPrice = aq.id(R.id.txt_act_lowest_price).getText().toString();
                    if(minACTPrice.length() == 0 && stockACTs){
                        Toast.makeText(getActivity(), "Please enter lowest price of ACTs", Toast.LENGTH_LONG).show();
                    }
                    if(stockZinc){
                        try{
                            minACTPriceVal = Integer.parseInt(minACTPrice);
                            if(minACTPriceVal > 900){
                                message = "Remind provider about the government approved Recommended Retail Price (XXX)";
                                AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                            }
                        }catch (NumberFormatException ex){
                            Utils.log("Cannot parse min ACT price");
                            Toast.makeText(getActivity(), "Please enter a valid lowest price of ACTs", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        aq.id(R.id.txt_act_lowest_price).getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    stockAmox = (aq.id(R.id.spn_do_you_stock_amoxicillin).getSelectedItemPosition() == 1);
                    minAmoxPrice = aq.id(R.id.txt_amoxicillin_lowest_price).getText().toString();
                    if(minAmoxPrice.length() == 0 && stockAmox){
                        Toast.makeText(getActivity(), "Please enter lowest price of Amoxicillin", Toast.LENGTH_LONG).show();
                    }
                    if(stockAmox){
                        try{
                            minAmoxPriceVal = Integer.parseInt(minAmoxPrice);
                            if(minAmoxPriceVal > 900){
                                message = "Remind provider about the government approved Recommended Retail Price (XXX)";
                                AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                            }
                        }catch (NumberFormatException ex){
                            Utils.log("Cannot parse min Amox price");
                            Toast.makeText(getActivity(), "Please enter a valid lowest price of Amoxicillin", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        return view;
    }

    public boolean saveFields(){
        return true;
    }
}
