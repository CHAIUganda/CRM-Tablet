package org.chai.activities.tasks;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.chai.R;

/**
 * Created by victor on 12/8/14.
 */
public class TaskLeftFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.task_left_fragment, container, false);
        return view;
    }
}
