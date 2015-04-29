package org.chai.activities.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.chai.R;

/**
 * Created by Zed on 4/29/2015.
 */
public class CustomerCommercialFormFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customer_commercial_form_fragment, container, false);
    }
}
