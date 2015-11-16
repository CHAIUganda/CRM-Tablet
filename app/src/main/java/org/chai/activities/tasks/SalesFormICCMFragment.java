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
import org.chai.util.ICCMPopupDialogFragment;
import org.chai.util.Utils;

/**
 * Created by Zed on 11/16/2015.
 */
public class SalesFormICCMFragment extends Fragment {
    AQuery aq;
    View view;

    SalesFormActivity parent;

    public boolean fieldsSaved = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = (SalesFormActivity)getActivity();
        view = inflater.inflate(R.layout.sales_form_iccm_fragment, container, false);
        aq = new AQuery(view);

        aq.id(R.id.spn_do_you_stock_ors).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    aq.id(R.id.ln_ors_price_container).visible();
                }else{
                    aq.id(R.id.ln_ors_price_container).gone();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aq.id(R.id.spn_do_you_stock_zinc).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    aq.id(R.id.ln_zinc_price_container).visible();
                }else{
                    aq.id(R.id.ln_zinc_price_container).gone();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aq.id(R.id.spn_do_you_stock_acts).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    aq.id(R.id.ln_acts_price_container).visible();
                }else{
                    aq.id(R.id.ln_acts_price_container).gone();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aq.id(R.id.spn_do_you_stock_amoxicillin).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    aq.id(R.id.ln_amoxicillin_price_container).visible();
                }else{
                    aq.id(R.id.ln_amoxicillin_price_container).gone();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public boolean saveFields() {
        String message = "";

        //ORS
        boolean stockOrs = (aq.id(R.id.spn_do_you_stock_ors).getSelectedItemPosition() == 1);
        String minOrsPrice = aq.id(R.id.txt_ors_lowest_price).getText().toString();
        if (minOrsPrice.length() == 0 && stockOrs) {
            Toast.makeText(getActivity(), "Please enter lowest price of ORS", Toast.LENGTH_LONG).show();
            return false;
        }
        if (stockOrs) {
            int minOrsPriceInt = -1;
            try {
                minOrsPriceInt = Integer.parseInt(minOrsPrice);
                if (minOrsPriceInt > 300) {
                    message += "Remind provider about the government approved Recommended Retail Price (300UGX per 1 sachet of ORS)\n\n";
                }
            } catch (NumberFormatException ex) {
                Utils.log("Cannot parse min ORS price");
                Toast.makeText(getActivity(), "Please enter a valid lowest price of ORS", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            message += "The MOH recommends ORS and zinc as the 1st line treatment for diarrhea - ALWAYS stock ORS!\n\n" +
                    "ORS replaces lost fluids and essential salts in a child with diarrhea - ORS saves the child's life!\n\n" +
                    "ORS should be given to a child until diarrhea stops - Prescribe 2 sachets per child!\n\n";
        }

        //Zinc
        boolean stockZinc = (aq.id(R.id.spn_do_you_stock_zinc).getSelectedItemPosition() == 1);
        String minZincPrice = aq.id(R.id.txt_zinc_lowest_price).getText().toString();
        if (minZincPrice.length() == 0 && stockZinc) {
            Toast.makeText(getActivity(), "Please enter lowest price of Zinc", Toast.LENGTH_LONG).show();
            return false;
        }
        if (stockZinc) {
            int minZincPriceInt = -1;
            try {
                minZincPriceInt = Integer.parseInt(minZincPrice);
                if (minZincPriceInt > 900) {
                    message += "Remind provider about the government approved Recommended Retail Price (900UGX per 10 tablets of zinc)\n\n";
                }
            } catch (NumberFormatException ex) {
                Utils.log("Cannot parse min Zinc price");
                Toast.makeText(getActivity(), "Please enter a valid lowest price of Zinc", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            message += "The MOH recommends that zinc always be given in COMBINATION with ORS as 1st line treatment for diarrhea - Always stock ORS and zinc!\n\n" +
                    "Zinc reduces the duration of diarrhea and future episodes of diarrhea - ORS saves the child's life and Zinc keeps the child healthy!\n\n" +
                    "Give zinc to child for at least 10 days (1 tablet per day) - Prescribe a COMPLETE course of 10 tablets!\n\n";
        }

        //ACT
        boolean stockACTs = (aq.id(R.id.spn_do_you_stock_acts).getSelectedItemPosition() == 1);
        String minACTPrice = aq.id(R.id.txt_act_lowest_price).getText().toString();
        if (minACTPrice.length() == 0 && stockACTs) {
            Toast.makeText(getActivity(), "Please enter lowest price of ACTs", Toast.LENGTH_LONG).show();
            return false;
        }
        if (stockZinc) {
            int minACTPriceInt = -1;
            try {
                minACTPriceInt = Integer.parseInt(minACTPrice);
                if (minACTPriceInt > 900) {
                    message += "Remind provider about the government approved Recommended Retail Price (XXX)\n\n";
                }
            } catch (NumberFormatException ex) {
                Utils.log("Cannot parse min ACT price");
                Toast.makeText(getActivity(), "Please enter a valid lowest price of ACTs", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            message += "The MOH recommends ACTs as the 1st line treatment for malaria - ACTs are the most effective antimalarial available!\n\n" +
                    "High-quality ACTs have a Green Leaf logo on the packaging - Always look for the Green Leaf!\n\n" +
                    "The MOH helps lower the cost of these Green Leaf ACTs - Sell ACTs at lower cost to make more affordable to patients!\n\n";
        }

        //AMOX
        boolean stockAmox = (aq.id(R.id.spn_do_you_stock_amoxicillin).getSelectedItemPosition() == 1);
        String minAmoxPrice = aq.id(R.id.txt_amoxicillin_lowest_price).getText().toString();
        if (minAmoxPrice.length() == 0 && stockAmox) {
            Toast.makeText(getActivity(), "Please enter lowest price of Amoxicillin", Toast.LENGTH_LONG).show();
            return false;
        }
        if (stockAmox) {
            int minAmoxPriceInt = -1;
            try {
                minAmoxPriceInt = Integer.parseInt(minAmoxPrice);
                if (minAmoxPriceInt > 900) {
                    message += "Remind provider about the government approved Recommended Retail Price (XXX)\n\n";
                }
            } catch (NumberFormatException ex) {
                Utils.log("Cannot parse min Amox price");
                Toast.makeText(getActivity(), "Please enter a valid lowest price of Amoxicillin", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            message += "The MOH now recommends Amoxicillin 250mg DT as the 1st line treatment for childhood pneumonia\n\n" +
                    "Proper diagnosis must be performed before prescribing - Remember!  Not all coughs need an antibiotic - Count breaths before treating!\n\n";
        }


        if (!fieldsSaved) {
            ICCMPopupDialogFragment.newInstance(parent, message, this).show(getActivity().getSupportFragmentManager(), "iccm_message");
        }

        return fieldsSaved;
    }
}
