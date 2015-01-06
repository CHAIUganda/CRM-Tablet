package org.chai.activities.calls;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;

/**
 * Created by victor on 1/6/15.
 */
public class HistoryMainFragment extends BaseContainerFragment {

    private FragmentTabHost tabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(),getChildFragmentManager(), R.layout.task_main_fragment);

        tabHost.addTab(tabHost.newTabSpec("calldata").setIndicator(getTabIndicator(tabHost.getContext(),"Call Data")),CallDataContainer.class,null);
        tabHost.addTab(tabHost.newTabSpec("orders").setIndicator(getTabIndicator(tabHost.getContext(),"Orders")),OrdersContainer.class,null);

        return tabHost;
    }
    private View getTabIndicator(Context context, String title) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }
}
