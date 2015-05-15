package org.chai.activities.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.util.Utils;

/**
 * Created by Zed on 5/2/2015.
 */
public class DiarrheaFormRecommendationFragment extends Fragment {
    AQuery aq;
    View view;
    DiarrheaFormActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (DiarrheaFormActivity)getActivity();

        view = inflater.inflate(R.layout.diarrhea_form_recommendation_fragment, container, false);
        aq = new AQuery(view);

        aq.id(R.id.other).getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aq.id(R.id.ln_other_material_container).visible();
                }else{
                    aq.id(R.id.ln_other_material_container).gone();
                }
            }
        });

        aq.id(R.id.chk_other_recommendation).getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aq.id(R.id.ln_other_recommendation_container).visible();
                }else{
                    aq.id(R.id.ln_other_recommendation_container).gone();
                }
            }
        });

        if(activity.call.getUuid() != null){
            populateFields();
        }

        return view;
    }

    private void populateFields(){
        String recommendation = activity.call.getRecommendationLevel();
        if(recommendation != null){
            Spinner spinner = aq.id(R.id.spn_customer_recommendation).getSpinner();
            spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition(recommendation));
        }

        String materials = activity.call.getPointOfsaleMaterial();
        if(materials != null){
            aq.id(R.id.dangler).checked(materials.indexOf(aq.id(R.id.dangler).getText().toString()) != -1);
            aq.id(R.id.poster).checked(materials.indexOf(aq.id(R.id.poster).getText().toString()) != -1);
            aq.id(R.id.tent_card).checked(materials.indexOf(aq.id(R.id.tent_card).getText().toString()) != -1);
            aq.id(R.id.bunting).checked(materials.indexOf(aq.id(R.id.bunting).getText().toString()) != -1);
            aq.id(R.id.mrp_dangler).checked(materials.indexOf(aq.id(R.id.mrp_dangler).getText().toString()) != -1);
            aq.id(R.id.pen).checked(materials.indexOf(aq.id(R.id.pen).getText().toString()) != -1);

        }

        String recommendations = activity.call.getRecommendationNextStep();
        if(recommendations != null){
            aq.id(R.id.start_purchasing).checked(recommendations.indexOf(aq.id(R.id.start_purchasing).getText().toString()) != -1);
            aq.id(R.id.none).checked(recommendations.indexOf(aq.id(R.id.none).getText().toString()) != -1);
            aq.id(R.id.stock_ors).checked(recommendations.indexOf(aq.id(R.id.stock_ors).getText().toString()) != -1);
            aq.id(R.id.stock_zinc).checked(recommendations.indexOf(aq.id(R.id.stock_zinc).getText().toString()) != -1);
            aq.id(R.id.start_recommending).checked(recommendations.indexOf(aq.id(R.id.start_recommending).getText().toString()) != -1);
            aq.id(R.id.start_selling).checked(recommendations.indexOf(aq.id(R.id.start_selling).getText().toString()) != -1);
        }
    }

    public boolean saveFields(){
        String materials = "";
        String recommendations = "";
        String objecttion = aq.id(R.id.spn_customer_recommendation).getSelectedItem().toString();
        if(objecttion.isEmpty()){
            Toast.makeText(activity, "Please select customer's recommendation", Toast.LENGTH_LONG).show();
            return false;
        }

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

        activity.call.setRecommendationLevel(objecttion);
        activity.call.setRecommendationNextStep(recommendations);
        activity.call.setPointOfsaleMaterial(materials);
        return true;
    }
}
