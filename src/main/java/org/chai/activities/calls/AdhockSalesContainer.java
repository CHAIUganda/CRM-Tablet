package org.chai.activities.calls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;

/**
 * Created by victor on 1/8/15.
 */
public class AdhockSalesContainer extends BaseContainerFragment {
    private boolean mIsViewInited;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.container_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mIsViewInited) {
            mIsViewInited = true;
            initView();
        }
    }

    private void initView() {
        replaceFragment(new AdhockSalesListFragment(), false);
    }

}
