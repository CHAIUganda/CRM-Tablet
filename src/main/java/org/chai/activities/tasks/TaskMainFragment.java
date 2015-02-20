package org.chai.activities.tasks;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;

/**
 * Created by victor on 12/8/14.
 */
public class TaskMainFragment extends BaseContainerFragment {
    public final static String STATUS_NEW = "new", STATUS_COMPLETE = "complete",STATUS_CANCELLED = "cancelled";
    public final static String TASK_TYPE_ORDER = "Order";
   private FragmentTabHost tabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(),getChildFragmentManager(),R.layout.task_main_fragment);

        tabHost.addTab(tabHost.newTabSpec("Calender").setIndicator(getTabIndicator(tabHost.getContext(),"Calendar")),CalenderFragmentContainer.class,null);
        tabHost.addTab(tabHost.newTabSpec("location").setIndicator(getTabIndicator(tabHost.getContext(),"View by Location")),LocationFragmentContainer.class,null);
        tabHost.addTab(tabHost.newTabSpec("viewonmap").setIndicator(getTabIndicator(tabHost.getContext(),"View on Map")),TaskMapFragmentContainer.class,null);

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
