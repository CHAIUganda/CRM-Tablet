package org.chai.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import org.chai.R;

;

/**
 * Created by Zed on 1/23/2015.
 */
public class DrawerFragment extends Fragment {
    AQuery aq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer, container, false);
        aq = new AQuery(view);

        return view;
    }
}
