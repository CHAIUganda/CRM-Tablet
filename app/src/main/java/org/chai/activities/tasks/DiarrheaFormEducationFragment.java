package org.chai.activities.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.util.Utils;

import java.util.List;

/**
 * Created by Zed on 5/2/2015.
 */
public class DiarrheaFormEducationFragment extends Fragment {
    View view;
    AQuery aq;
    Spinner heardAboutTreatmentSpinner;
    DiarrheaFormActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (DiarrheaFormActivity)getActivity();
        view = inflater.inflate(R.layout.diarrhea_form_education_fragment, container, false);
        setRequiredFields();
        aq = new AQuery(view);
        heardAboutTreatmentSpinner = aq.id(R.id.heardofdiarrheatreament).getSpinner();
        heardAboutTreatmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 2){
                    aq.id(R.id.ln_how_did_you_hear).gone();
                    aq.id(R.id.ln_other_source_of_information).gone();
                }else{
                    aq.id(R.id.ln_how_did_you_hear).visible();
                    aq.id(R.id.ln_other_source_of_information).visible();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(activity.call.getUuid() != null){
            populateFields();
        }

        return view;
    }

    private void setRequiredFields(){
        List<View> required = Utils.getViewsByTag((ViewGroup) view, "required");
        for(View v : required){
            try{
                Utils.setRequired((TextView)v);
            }catch(Exception ex){
                Utils.log("Error setting view by tag -> " + ex.getMessage());
            }
        }
    }

    private void populateFields(){
        String headoftreatment = activity.call.getHeardAboutDiarrheaTreatmentInChildren();
        if(headoftreatment != null){
            Spinner s1 = aq.id(R.id.heardofdiarrheatreament).getSpinner();
            s1.setSelection(((ArrayAdapter<String>)s1.getAdapter()).getPosition(headoftreatment));
        }
        String how = activity.call.getHowDidYouHear();
        if(how != null){
            Spinner s2 = aq.id(R.id.how_did_you_hear).getSpinner();
            s2.setSelection(((ArrayAdapter<String>)s2.getAdapter()).getPosition(how));
        }
        String otherways = activity.call.getOtherWaysHowYouHeard();
        if(otherways != null){
            aq.id(R.id.other_source_of_information).text(otherways);
        }
        String diarrheaeffects = activity.call.getDiarrheaEffectsOnBody();
        if(diarrheaeffects != null){
            Spinner s3 = aq.id(R.id.diarrhea_effects_on_the_body).getSpinner();
            s3.setSelection(((ArrayAdapter<String>) s3.getAdapter()).getPosition(diarrheaeffects));
        }
        String whatyouknow = activity.call.getWhatYouKnowAbtDiarrhea();
        if(whatyouknow != null){
            Spinner s4 = aq.id(R.id.diarrhea_effects_on_the_community).getSpinner();
            s4.setSelection(((ArrayAdapter<String>)s4.getAdapter()).getPosition(whatyouknow));
        }
        String aboutors = activity.call.getKnowledgeAbtOrsAndUsage();
        if(aboutors != null){
            Spinner s5 = aq.id(R.id.ors_usage).getSpinner();
            s5.setSelection(((ArrayAdapter<String>)s5.getAdapter()).getPosition(aboutors));
        }
        String aboutzinc = activity.call.getKnowledgeAbtZincAndUsage();
        if(aboutzinc != null){
            Spinner s6 = aq.id(R.id.zinc_usage).getSpinner();
            s6.setSelection(((ArrayAdapter<String>)s6.getAdapter()).getPosition(aboutzinc));
        }
        String antibiotics = activity.call.getWhyNotUseAntibiotics();
        if(antibiotics != null){
            Spinner s7 = aq.id(R.id.why_should_you_not_use_antobiotics).getSpinner();
            s7.setSelection(((ArrayAdapter<String>)s7.getAdapter()).getPosition(antibiotics));
        }
    }

    public boolean saveFields(){
        Utils.log("Saving education fields");

        String heardOfTreatment = aq.id(R.id.heardofdiarrheatreament).getSelectedItem().toString();
        if(heardOfTreatment.isEmpty()){
            Toast.makeText(getActivity(), "Please answer question #2", Toast.LENGTH_LONG).show();
            return false;
        }
        String howDidYouHear = aq.id(R.id.how_did_you_hear).getSelectedItem().toString();
        String otherSources = aq.id(R.id.other_source_of_information).getText().toString();

        if(howDidYouHear.equalsIgnoreCase("Other") && otherSources.isEmpty()){
            Toast.makeText(getActivity(), "Please enter other sources of information", Toast.LENGTH_LONG).show();
            return false;
        }

        activity.call.setHeardAboutDiarrheaTreatmentInChildren(heardOfTreatment);
        activity.call.setHowDidYouHear(howDidYouHear);
        activity.call.setWhyNotUseAntibiotics(aq.id(R.id.why_should_you_not_use_antobiotics).getSelectedItem().toString());
        activity.call.setDiarrheaEffectsOnBody(aq.id(R.id.diarrhea_effects_on_the_body).getSelectedItem().toString());
        activity.call.setKnowledgeAbtOrsAndUsage(aq.id(R.id.ors_usage).getSelectedItem().toString());
        activity.call.setKnowledgeAbtZincAndUsage(aq.id(R.id.zinc_usage).getSelectedItem().toString());
        activity.call.setOtherWaysHowYouHeard(otherSources);
        activity.call.setWhatYouKnowAbtDiarrhea(aq.id(R.id.diarrhea_effects_on_the_community).getSelectedItem().toString());

        return true;
    }
}
