package org.chai.activities.forms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
    MalariaFormActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MalariaFormActivity)getActivity();
        view = inflater.inflate(R.layout.malaria_form_fragment_2, container, false);
        setRequiredFields();
        aq = new AQuery(view);
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
        String suspect = activity.call.getHowYouSuspectMalaria();
        if(suspect != null){
            Spinner s1 = aq.id(R.id.how_do_you_suspect).getSpinner();
            s1.setSelection(((ArrayAdapter<String>)s1.getAdapter()).getPosition(suspect));
        }
        String probe = activity.call.getMohGuidelines();
        if(probe != null){
            Spinner s3 = aq.id(R.id.spn_moh_guidelines).visible().getSpinner();
            s3.setSelection(((ArrayAdapter<String>)s3.getAdapter()).getPosition(probe));
        }
        String greenleafprobe = activity.call.getWhatGreenLeafRepresents();
        if(greenleafprobe != null){
            Spinner s6 = aq.id(R.id.green_leaf_representation).visible().getSpinner();
            s6.setSelection(((ArrayAdapter<String>)s6.getAdapter()).getPosition(greenleafprobe));
        }
        String prescribe = activity.call.getWhyPrescribeWithoutGreenLeaf();
        if(prescribe != null){
            Spinner s5 = aq.id(R.id.why_prescribe_antimalarials_without_green_leaf).visible().getSpinner();
            s5.setSelection(((ArrayAdapter<String>)s5.getAdapter()).getPosition(prescribe));
        }
        String signs = activity.call.getSignsOfSevereMalaria();
        if(signs != null){
            Spinner s6 = aq.id(R.id.signs_of_severe_malaria).visible().getSpinner();
            s6.setSelection(((ArrayAdapter<String>)s6.getAdapter()).getPosition(signs));
        }
        String manage = activity.call.getHowToManagePatientsWithSevereMalaria();
        if(manage != null){
            Spinner s7 = aq.id(R.id.how_to_manage_malaria_patients).visible().getSpinner();
            s7.setSelection(((ArrayAdapter<String>)s7.getAdapter()).getPosition(manage));
        }
    }

    public boolean saveFields(){
        String howdoyouuspect = aq.id(R.id.how_do_you_suspect).getSelectedItem().toString();
        if(howdoyouuspect.isEmpty()){
            Toast.makeText(getActivity(), "Select how the customer suspects malaria", Toast.LENGTH_LONG).show();
            return false;
        }
        String greenleafprescription = aq.id(R.id.why_prescribe_antimalarials_without_green_leaf).getSelectedItem().toString();
        if(greenleafprescription.isEmpty()){
            Toast.makeText(getActivity(), "Please answer question #11", Toast.LENGTH_LONG).show();
            return false;
        }

        MalariaFormActivity ac = (MalariaFormActivity)getActivity();
        ac.call.setHowYouSuspectMalaria(howdoyouuspect);
        //ac.call.setDoYouKnowMOHGuidelines(doyoumohguidelines);
        ac.call.setMohGuidelines(aq.id(R.id.spn_moh_guidelines).getSelectedItem().toString());
        //ac.call.setKnowAboutGreenLeafAntimalarials(greenleaf);
        ac.call.setWhatGreenLeafRepresents(aq.id(R.id.green_leaf_representation).getSelectedItem().toString());
        //ac.call.setDoYouPrescribeWithoutGreenLeaf(aq.id(R.id.do_you_prescribe_without_greenleaf).getSelectedItem().toString());
        ac.call.setWhyPrescribeWithoutGreenLeaf(aq.id(R.id.why_prescribe_antimalarials_without_green_leaf).getSelectedItem().toString());
        //ac.call.setKnowWhatSevereMalariaIs(aq.id(R.id.do_you_know_server_malaria).getSelectedItem().toString());
        ac.call.setSignsOfSevereMalaria(aq.id(R.id.signs_of_severe_malaria).getSelectedItem().toString());
        ac.call.setHowToManagePatientsWithSevereMalaria(aq.id(R.id.how_to_manage_malaria_patients).getSelectedItem().toString());

        return true;
    }
}
