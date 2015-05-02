package org.chai.activities.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return view;
    }
}
