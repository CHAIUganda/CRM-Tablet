package org.chai.activities.org.chai.activities.forms;

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
public class MalariaFormFragment4 extends Fragment {
    View view;
    AQuery aq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.malaria_form_fragment_4, container, false);
        aq = new AQuery(view);
        return view;
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
        if(aq.id(R.id.start_recommending).isChecked()){
            recommendations += aq.id(R.id.start_recommending).getText().toString() + ", ";
        }
        if(aq.id(R.id.refer_patients).isChecked()){
            recommendations += aq.id(R.id.refer_patients).getText().toString() + ", ";
        }

        MalariaFormActivity ac = (MalariaFormActivity)getActivity();
        ac.call.setRecommendationNextStep(recommendations);
        ac.call.setPointOfsaleMaterial(materials);

        return true;
    }
}
