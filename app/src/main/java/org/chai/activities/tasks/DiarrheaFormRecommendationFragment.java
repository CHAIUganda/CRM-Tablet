package org.chai.activities.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.androidquery.AQuery;

import org.chai.R;

/**
 * Created by Zed on 5/2/2015.
 */
public class DiarrheaFormRecommendationFragment extends Fragment {
    AQuery aq;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        return view;
    }

    public boolean saveFields(){
        return true;
    }
}
