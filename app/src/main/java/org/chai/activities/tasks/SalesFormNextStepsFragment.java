package org.chai.activities.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.util.Utils;

/**
 * Created by Zed on 5/3/2015.
 */
public class SalesFormNextStepsFragment extends Fragment {
    AQuery aq;
    View view;
    SalesFormActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sales_form_next_steps_fragment, container, false);
        aq = new AQuery(view);

        return view;
    }

    public boolean saveFields(){
        String materials = "";
        String recommendations = "";

        if(aq.id(R.id.dangler).isChecked()){
            materials += aq.id(R.id.dangler).getText().toString() + ", ";
        }
        if(aq.id(R.id.tent_card).isChecked()){
            materials += aq.id(R.id.tent_card).getText().toString() + ", ";
        }
        if(aq.id(R.id.poster).isChecked()){
            materials += ", " + aq.id(R.id.poster).getText().toString() + ", ";
        }
        if(aq.id(R.id.bunting).isChecked()){
            materials += ", " + aq.id(R.id.bunting).getText().toString() + ", ";
        }
        if(aq.id(R.id.mrp_dangler).isChecked()){
            materials += ", " + aq.id(R.id.mrp_dangler).getText().toString() + ", ";
        }
        if(aq.id(R.id.pen).isChecked()){
            materials += ", " + aq.id(R.id.pen).getText().toString() + ", ";
        }
        if(!aq.id(R.id.txt_other_material).getText().toString().isEmpty()){
            materials += ", " + aq.id(R.id.txt_other_material).getText().toString();
        }

        Utils.log("Materials -> " + materials);

        if(aq.id(R.id.none).isChecked()){
            recommendations += aq.id(R.id.none).getText().toString() + ", ";
        }
        if(aq.id(R.id.stock_ors).isChecked()){
            recommendations += aq.id(R.id.stock_ors).getText().toString() + ", ";
        }
        if(aq.id(R.id.stock_zinc).isChecked()){
            recommendations += aq.id(R.id.stock_zinc).getText().toString() + ", ";
        }
        if(aq.id(R.id.start_recommending).isChecked()){
            recommendations += aq.id(R.id.start_recommending).getText().toString() + ", ";
        }
        if(aq.id(R.id.start_selling).isChecked()){
            recommendations += aq.id(R.id.start_selling).getText().toString() + ", ";
        }
        if(aq.id(R.id.start_purchasing).isChecked()){
            recommendations += aq.id(R.id.start_purchasing).getText().toString() + ", ";
        }
        if(!aq.id(R.id.txt_other_recommendation).getText().toString().isEmpty()){
            recommendations += aq.id(R.id.txt_other_recommendation).getText().toString();
        }

        activity = (SalesFormActivity)getActivity();
        activity.sale.setGovernmentApproval(aq.id(R.id.spn_described_government_approved_mrp).getSelectedItem().toString());
        activity.sale.setRecommendationNextStep(recommendations);
        activity.sale.setPointOfsaleMaterial(materials);

        return true;
    }
}
