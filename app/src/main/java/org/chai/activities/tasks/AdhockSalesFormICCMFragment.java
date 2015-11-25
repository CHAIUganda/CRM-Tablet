package org.chai.activities.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

    boolean stockOrs, stockZinc, stockACTs, stockAmox, stockRDT;

    String minOrsPrice = "";
    String minZincPrice = "";
    String minACTPrice = "";
    String minAmoxPrice = "";
    String minRDTPrice = "";

    int minOrsPriceVal = -1;
    int minZincPriceVal = -1;
    int minACTPriceVal = -1;

    boolean canFireSpinnerEvent = false;

    String message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = (AdhockSalesFormActivity)getActivity();
        view = inflater.inflate(R.layout.sales_form_iccm_fragment, container, false);
        aq = new AQuery(view);

        aq.id(R.id.spn_do_you_stock_ors).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!canFireSpinnerEvent){
                    return;
                }
                if(position == 2){
                    aq.id(R.id.ln_zinc_container).visible();

                    message = "The MOH recommends ORS and zinc as the 1st line treatment for diarrhea - ALWAYS stock ORS!\n\n" +
                            "ORS replaces lost fluids and essential salts in a child with diarrhea - ORS saves the child's life!\n\n" +
                            "ORS should be given to a child until diarrhea stops - Prescribe 2 sachets per child!";

                    AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                }
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
                if(!canFireSpinnerEvent){
                    return;
                }
                if (position == 2) {
                    aq.id(R.id.ln_act_container).visible();
                    message = "The MOH recommends that zinc always be given in COMBINATION with ORS as 1st line treatment for diarrhea - Always stock ORS and zinc!\n\n" +
                            "Zinc reduces the duration of diarrhea and future episodes of diarrhea - ORS saves the child's life and Zinc keeps the child healthy!\n\n" +
                            "Give zinc to child for at least 10 days (1 tablet per day) - Prescribe a COMPLETE course of 10 tablets!";

                    AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                }
                if (position == 1) {
                    aq.id(R.id.ln_zinc_price_container).visible();
                } else {
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
                if(!canFireSpinnerEvent){
                    return;
                }
                if (position == 2) {
                    aq.id(R.id.ln_rdt_container).visible();

                    message = "The MOH recommends ACTs as the 1st line treatment for malaria - ACTs are the most effective antimalarial available!\n\n" +
                            "High-quality ACTs have a Green Leaf logo on the packaging - Always look for the Green Leaf!\n\n" +
                            "The MOH helps lower the cost of these Green Leaf ACTs - Sell ACTs at lower cost to make more affordable to patients!";

                    AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                }
                if (position == 1) {
                    aq.id(R.id.ln_acts_price_container).visible();
                } else {
                    aq.id(R.id.ln_acts_price_container).gone();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aq.id(R.id.spn_do_you_stock_rdt).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!canFireSpinnerEvent) {
                    return;
                }
                if (position == 2) {
                    aq.id(R.id.ln_amox_container).visible();

                    message = "An mRDT is a simple to use test that can detect malaria in human blood and give results in 15 minutes.\n\n" +
                            "The MOH recommends that every fever case be tested with an mRDT - Always test and treat!\n\n" +
                            "Only treat a patient with an ACT if a positive diagnosis is confirmed through testing";

                    AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                }
                if (position == 1) {
                    aq.id(R.id.ln_rdt_price_container).visible();
                } else {
                    aq.id(R.id.ln_rdt_price_container).gone();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aq.id(R.id.spn_do_you_stock_amoxicillin).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!canFireSpinnerEvent) {
                    return;
                }
                if (position == 2) {
                    message = "The MOH now recommends Amoxicillin 250mg DT as the 1st line treatment for childhood pneumonia\n\n" +
                            "Proper diagnosis must be performed before prescribing - Remember!  Not all coughs need an antibiotic - Count breaths before treating!";

                    AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                }
                if (position == 1) {
                    aq.id(R.id.ln_amoxicillin_price_container).visible();
                } else {
                    aq.id(R.id.ln_amoxicillin_price_container).gone();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner lowestOrsPrice = aq.id(R.id.spn_ors_lowest_price).getSpinner();
        lowestOrsPrice.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Utils.generatePriceList(200, 1500, 100, null, "> 1500")));

        lowestOrsPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!canFireSpinnerEvent){
                    return;
                }
                stockOrs = (aq.id(R.id.spn_do_you_stock_ors).getSelectedItemPosition() == 1);
                minOrsPrice = lowestOrsPrice.getAdapter().getItem(position).toString();
                if (minOrsPrice.length() == 0) {
                    return;
                }
                if (stockOrs) {
                    try {
                        minOrsPriceVal = Utils.parseICCMItemPrice(minOrsPrice);
                        if (minOrsPriceVal > 300) {
                            message = "Remind provider about the government approved Recommended Retail Price (300UGX per 1 sachet of ORS)";
                            AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                        }
                    } catch (NumberFormatException ex) {
                        Utils.log("Cannot parse min ORS price");
                        Toast.makeText(getActivity(), "Please enter a valid lowest price of ORS", Toast.LENGTH_LONG).show();
                    }
                }
                aq.id(R.id.ln_zinc_container).visible();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner lowestZincPrice = aq.id(R.id.spn_zinc_lowest_price).getSpinner();
        lowestZincPrice.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Utils.generatePriceList(50, 300, 10, null, "> 300")));

        lowestZincPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!canFireSpinnerEvent){
                    return;
                }
                stockZinc = (aq.id(R.id.spn_do_you_stock_zinc).getSelectedItemPosition() == 1);
                minZincPrice = lowestZincPrice.getAdapter().getItem(position).toString();
                if (minZincPrice.length() == 0) {
                    return;
                }
                if (stockZinc) {
                    try {
                        minZincPriceVal = Utils.parseICCMItemPrice(minZincPrice);
                        if (minZincPriceVal > 90) {
                            message = "Remind provider about the government approved Recommended Retail Price (90UGX per 1 tablet of zinc)\n";
                            AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                        }
                    } catch (NumberFormatException ex) {
                        Utils.log("Cannot parse min Zinc price");
                        Toast.makeText(getActivity(), "Please enter a valid lowest price of Zinc", Toast.LENGTH_LONG).show();
                    }
                }
                aq.id(R.id.ln_act_container).visible();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner lowestACTPrice = aq.id(R.id.spn_act_lowest_price).getSpinner();
        lowestACTPrice.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Utils.generatePriceList(200, 600, 10, "< 200", "> 600")));

        lowestACTPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!canFireSpinnerEvent){
                    return;
                }
                stockACTs = (aq.id(R.id.spn_do_you_stock_acts).getSelectedItemPosition() == 1);
                minACTPrice = lowestACTPrice.getAdapter().getItem(position).toString();
                if (minACTPrice.length() == 0) {
                    return;
                }
                if (stockZinc) {
                    try {
                        minACTPriceVal = Utils.parseICCMItemPrice(minACTPrice);
                        if (minACTPriceVal > 900) {
                            message = "Remind provider about the government approved Recommended Retail Price (XXX)";
                            AdhockICCMPopupDialogFragment.newInstance(message).show(getActivity().getSupportFragmentManager(), "iccm_message");
                        }
                    } catch (NumberFormatException ex) {
                        Utils.log("Cannot parse min ACT price");
                        Toast.makeText(getActivity(), "Please enter a valid lowest price of ACTs", Toast.LENGTH_LONG).show();
                    }
                }
                aq.id(R.id.ln_rdt_container).visible();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner lowestRDTPrice = aq.id(R.id.spn_rdt_lowest_price).getSpinner();
        lowestRDTPrice.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Utils.generatePriceList(2000, 5000, 100, "< 2000", "> 5000")));

        lowestRDTPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!canFireSpinnerEvent) {
                    return;
                }
                stockRDT = (aq.id(R.id.spn_do_you_stock_rdt).getSelectedItemPosition() == 1);
                minRDTPrice = lowestRDTPrice.getAdapter().getItem(position).toString();
                aq.id(R.id.ln_amox_container).visible();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner lowestAmoxPrice = aq.id(R.id.spn_amox_lowest_price).getSpinner();
        lowestAmoxPrice.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Utils.generatePriceList(200, 600, 10, "< 200", "> 600")));

        lowestAmoxPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!canFireSpinnerEvent) {
                    return;
                }
                stockAmox = (aq.id(R.id.spn_do_you_stock_amoxicillin).getSelectedItemPosition() == 1);
                minAmoxPrice = lowestAmoxPrice.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(parent.sale.getUuid() != null){
            stockOrs = parent.sale.getStocksORS();
            stockZinc = parent.sale.getStocksZinc();
            stockACTs = parent.sale.getStocksACTs();
            stockRDT = parent.sale.getStocksRDT();
            stockAmox = parent.sale.getStocksAmox();

            minOrsPrice = parent.sale.getMinORSPrice();
            minZincPrice = parent.sale.getMinZincPrice();
            minACTPrice = parent.sale.getMinACTPrice();
            minRDTPrice = parent.sale.getMinRDTPrice();
            minAmoxPrice = parent.sale.getMinAmoxPrice();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        canFireSpinnerEvent = false;
        populateFields();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                canFireSpinnerEvent = true;
            }
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        assignVariables();
    }

    private void populateFields(){
        int priceIndex = 0;
        if(parent.sale.getStocksORS() != null){
            aq.id(R.id.ln_ors_container).visible();
            aq.id(R.id.spn_do_you_stock_ors).setSelection(parent.sale.getStocksORS() ? 1 : 2);
            if(parent.sale.getStocksORS()){
                aq.id(R.id.ln_ors_price_container).visible();
                if(!TextUtils.isEmpty(parent.sale.getMinORSPrice())){
                    priceIndex = ((ArrayAdapter)aq.id(R.id.spn_ors_lowest_price).getSpinner().getAdapter()).getPosition(parent.sale.getMinORSPrice());
                    aq.id(R.id.spn_ors_lowest_price).setSelection(priceIndex);
                }
            }
        }

        if(parent.sale.getStocksZinc() != null){
            aq.id(R.id.ln_zinc_container).visible();
            aq.id(R.id.spn_do_you_stock_zinc).setSelection(parent.sale.getStocksZinc() ? 1 : 2);
            if(parent.sale.getStocksAmox()){
                aq.id(R.id.ln_zinc_price_container).visible();
                if(!TextUtils.isEmpty(parent.sale.getMinZincPrice())){
                    priceIndex = ((ArrayAdapter)aq.id(R.id.spn_zinc_lowest_price).getSpinner().getAdapter()).getPosition(parent.sale.getMinZincPrice());
                    aq.id(R.id.spn_zinc_lowest_price).setSelection(priceIndex);
                }
            }
        }

        if(parent.sale.getStocksACTs() != null){
            aq.id(R.id.ln_act_container).visible();
            aq.id(R.id.spn_do_you_stock_acts).setSelection(parent.sale.getStocksACTs() ? 1 : 2);
            if(parent.sale.getStocksACTs()){
                aq.id(R.id.ln_acts_price_container).visible();
                if(!TextUtils.isEmpty(parent.sale.getMinACTPrice())){
                    priceIndex = ((ArrayAdapter)aq.id(R.id.spn_act_lowest_price).getSpinner().getAdapter()).getPosition(parent.sale.getMinACTPrice());
                    aq.id(R.id.spn_act_lowest_price).setSelection(priceIndex);
                }
            }

        }

        if(parent.sale.getStocksRDT() != null){
            aq.id(R.id.ln_rdt_container).visible();
            aq.id(R.id.spn_do_you_stock_rdt).setSelection(parent.sale.getStocksRDT() ? 1 : 2);
            if(parent.sale.getStocksRDT()){
                aq.id(R.id.ln_rdt_price_container).visible();
                if(!TextUtils.isEmpty(parent.sale.getMinRDTPrice())){
                    priceIndex = ((ArrayAdapter)aq.id(R.id.spn_rdt_lowest_price).getSpinner().getAdapter()).getPosition(parent.sale.getMinRDTPrice());
                    aq.id(R.id.spn_rdt_lowest_price).setSelection(priceIndex);
                }
            }
        }

        if(parent.sale.getStocksAmox() != null){
            aq.id(R.id.ln_amox_container).visible();
            aq.id(R.id.spn_do_you_stock_amoxicillin).setSelection(parent.sale.getStocksAmox() ? 1 : 2);
            if(parent.sale.getStocksAmox()){
                aq.id(R.id.ln_amoxicillin_price_container).visible();
                if(!TextUtils.isEmpty(parent.sale.getMinAmoxPrice())){
                    priceIndex = ((ArrayAdapter)aq.id(R.id.spn_amox_lowest_price).getSpinner().getAdapter()).getPosition(parent.sale.getMinAmoxPrice());
                    aq.id(R.id.spn_amox_lowest_price).setSelection(priceIndex);
                }
            }
        }
    }

    public boolean saveFields(){
        if(aq.id(R.id.spn_do_you_stock_ors).getSelectedItemPosition() == 0){
            Toast.makeText(getActivity(), "Please select wether customer stocks ORS", Toast.LENGTH_LONG).show();
            return false;
        }else{
            stockOrs = aq.id(R.id.spn_do_you_stock_ors).getSelectedItemPosition() == 1;
        }
        if(aq.id(R.id.spn_do_you_stock_zinc).getSelectedItemPosition() == 0){
            Toast.makeText(getActivity(), "Please select wether customer stocks Zinc", Toast.LENGTH_LONG).show();
            return false;
        }else{
            stockZinc = aq.id(R.id.spn_do_you_stock_zinc).getSelectedItemPosition() == 1;
        }
        if(aq.id(R.id.spn_do_you_stock_acts).getSelectedItemPosition() == 0){
            Toast.makeText(getActivity(), "Please select wether customer stocks ACTs", Toast.LENGTH_LONG).show();
            return false;
        }else{
            stockACTs = aq.id(R.id.spn_do_you_stock_acts).getSelectedItemPosition() == 1;
        }
        if(aq.id(R.id.spn_do_you_stock_rdt).getSelectedItemPosition() == 0){
            Toast.makeText(getActivity(), "Please select wether customer stocks RDTs", Toast.LENGTH_LONG).show();
            return false;
        }else{
            stockRDT = aq.id(R.id.spn_do_you_stock_rdt).getSelectedItemPosition() == 1;
        }

        if(aq.id(R.id.spn_do_you_stock_amoxicillin).getSelectedItemPosition() == 0){
            Toast.makeText(getActivity(), "Please select wether customer stocks Amoxicillin", Toast.LENGTH_LONG).show();
            return false;
        }else{
            stockAmox = aq.id(R.id.spn_do_you_stock_amoxicillin).getSelectedItemPosition() == 1;
        }

        assignVariables();

        return true;
    }

    private void assignVariables(){
        parent.sale.setStocksORS(stockOrs);
        parent.sale.setStocksZinc(stockZinc);
        parent.sale.setStocksACTs(stockACTs);
        parent.sale.setStocksRDT(stockRDT);
        parent.sale.setStocksAmox(stockAmox);

        parent.sale.setMinORSPrice(minOrsPrice);
        parent.sale.setMinZincPrice(minZincPrice);
        parent.sale.setMinACTPrice(minACTPrice);
        parent.sale.setMinRDTPrice(minRDTPrice);
        parent.sale.setMinAmoxPrice(minAmoxPrice);
    }
}
