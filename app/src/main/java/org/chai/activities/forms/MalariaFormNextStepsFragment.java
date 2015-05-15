package org.chai.activities.forms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.util.Utils;

/**
 * Created by Zed on 4/9/2015.
 */
public class MalariaFormNextStepsFragment extends Fragment {
    View view;
    AQuery aq;

    MalariaFormActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MalariaFormActivity)getActivity();
        view = inflater.inflate(R.layout.malaria_form_fragment_4, container, false);
        aq = new AQuery(view);
        if(activity.call != null){
            populateFields();
        }
        return view;
    }

    private void populateFields(){
        String materials = activity.call.getPointOfsaleMaterial();
        if(materials != null){
            aq.id(R.id.dangler).checked(materials.indexOf(aq.id(R.id.dangler).getText().toString()) != -1);
            aq.id(R.id.poster).checked(materials.indexOf(aq.id(R.id.poster).getText().toString()) != -1);
        }

        String recommendations = activity.call.getRecommendationNextStep();
        if(recommendations != null){
            aq.id(R.id.start_purchasing).checked(recommendations.indexOf(aq.id(R.id.start_purchasing).getText().toString()) != -1);
            aq.id(R.id.stock_green_leaf).checked(recommendations.indexOf(aq.id(R.id.stock_green_leaf).getText().toString()) != -1);
            aq.id(R.id.start_selling).checked(recommendations.indexOf(aq.id(R.id.start_selling).getText().toString()) != -1);
            aq.id(R.id.refer_patients).checked(recommendations.indexOf(aq.id(R.id.refer_patients).getText().toString()) != -1);
        }
    }

    public boolean saveFields(){
        String materials = "";
        String recommendations = "";

        if(aq.id(R.id.dangler).isChecked()){
            materials += aq.id(R.id.dangler).getText().toString() + ", ";
        }
        if(aq.id(R.id.poster).isChecked()) {
            materials += ", " + aq.id(R.id.poster).getText().toString() + ", ";
        }

        Utils.log("Materials -> " + materials);

        if(aq.id(R.id.start_purchasing).isChecked()){
            recommendations += aq.id(R.id.start_purchasing).getText().toString() + ", ";
        }
        if(aq.id(R.id.stock_green_leaf).isChecked()){
            recommendations += aq.id(R.id.stock_green_leaf).getText().toString() + ", ";
        }
        if(aq.id(R.id.start_selling).isChecked()){
            recommendations += aq.id(R.id.start_selling).getText().toString() + ", ";
        }
        if(aq.id(R.id.refer_patients).isChecked()){
            recommendations += aq.id(R.id.refer_patients).getText().toString() + ", ";
        }

        activity.call.setRecommendationNextStep(recommendations);
        activity.call.setPointOfsaleMaterial(materials);

        return true;
    }
}
