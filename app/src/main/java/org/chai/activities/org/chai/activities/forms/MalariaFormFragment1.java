package org.chai.activities.org.chai.activities.forms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.chai.R;
import org.chai.util.Utils;

import java.util.List;

/**
 * Created by Zed on 4/9/2015.
 */
public class MalariaFormFragment1 extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.malaria_form_fragment_1, container, false);
        setRequiredFields();
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
}
