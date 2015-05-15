package org.chai.activities.forms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.util.Utils;

import java.util.List;

/**
 * Created by Zed on 4/9/2015.
 */
public class MalariaFormEducationFragment extends Fragment {
    View view;
    AQuery aq;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.malaria_form_fragment_2, container, false);
        setRequiredFields();
        aq = new AQuery(view);
        manageToogleFields();
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

    private void manageToogleFields(){
        aq.id(R.id.doyouknow_moh_guidelines).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    aq.id(R.id.ln_moh_guidelines_probe).visible();
                }else{
                    aq.id(R.id.ln_moh_guidelines_probe).gone();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aq.id(R.id.know_about_green_leaf).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    aq.id(R.id.ln_green_leaf_container).visible();
                }else{
                    aq.id(R.id.ln_green_leaf_container).gone();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aq.id(R.id.do_you_prescribe_without_greenleaf).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    aq.id(R.id.ln_prescribe_antimalarials_container).gone();
                }else{
                    aq.id(R.id.ln_prescribe_antimalarials_container).visible();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aq.id(R.id.do_you_know_server_malaria).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    aq.id(R.id.ln_server_malaria_container).visible();
                }else{
                    aq.id(R.id.ln_server_malaria_container).gone();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean saveFields(){
        String howdoyouuspect = aq.id(R.id.how_do_you_suspect).getSelectedItem().toString();
        if(howdoyouuspect.isEmpty()){
            Toast.makeText(getActivity(), "Select how the customer suspects malaria", Toast.LENGTH_LONG).show();
            return false;
        }
        String doyoumohguidelines = aq.id(R.id.doyouknow_moh_guidelines).getSelectedItem().toString();
        if(doyoumohguidelines.isEmpty()){
            Toast.makeText(getActivity(), "Select customer's knownledge on MOH Guidelines", Toast.LENGTH_LONG).show();
            return false;
        }
        String greenleaf = aq.id(R.id.know_about_green_leaf).getSelectedItem().toString();
        if(greenleaf.isEmpty()){
            Toast.makeText(getActivity(), "Select wether customer knows about antimalarials with a Green Leaf", Toast.LENGTH_LONG).show();
            return false;
        }
        String greenleafprescription = aq.id(R.id.do_you_prescribe_without_greenleaf).getSelectedItem().toString();
        if(greenleafprescription.isEmpty()){
            Toast.makeText(getActivity(), "Please answer question #6", Toast.LENGTH_LONG).show();
            return false;
        }

        MalariaFormActivity ac = (MalariaFormActivity)getActivity();
        ac.call.setHowYouSuspectMalaria(howdoyouuspect);
        ac.call.setDoYouKnowMOHGuidelines(doyoumohguidelines);
        ac.call.setMohGuidelines(aq.id(R.id.spn_moh_guidelines).getSelectedItem().toString());
        ac.call.setKnowAboutGreenLeafAntimalarials(greenleaf);
        ac.call.setWhatGreenLeafRepresents(aq.id(R.id.green_leaf_representation).getSelectedItem().toString());
        ac.call.setDoYouPrescribeWithoutGreenLeaf(aq.id(R.id.do_you_prescribe_without_greenleaf).getSelectedItem().toString());
        ac.call.setWhyPrescribeWithoutGreenLeaf(aq.id(R.id.why_prescribe_antimalarials_without_green_leaf).getSelectedItem().toString());
        ac.call.setKnowWhatSevereMalariaIs(aq.id(R.id.do_you_know_server_malaria).getSelectedItem().toString());
        ac.call.setSignsOfSevereMalaria(aq.id(R.id.signs_of_severe_malaria).getSelectedItem().toString());
        ac.call.setHowToManagePatientsWithSevereMalaria(aq.id(R.id.how_to_manage_malaria_patients).getSelectedItem().toString());

        return true;
    }
}
