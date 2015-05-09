package org.chai.activities.org.chai.activities.forms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.androidquery.AQuery;

import org.chai.R;

/**
 * Created by Zed on 4/9/2015.
 */
public class FormListPopupDialogFragment extends DialogFragment {
    AQuery aq;
    ProgressDialog progressDialog;
    static EditText textBox;

    public static FormListPopupDialogFragment getInstance(Context cxt, EditText t){
        FormListPopupDialogFragment frag = new FormListPopupDialogFragment();
        textBox = t;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        WindowManager manager = (WindowManager)getActivity().getSystemService(Activity.WINDOW_SERVICE);
        int width, height;
        WindowManager.LayoutParams params;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            width = manager.getDefaultDisplay().getWidth();
            height = manager.getDefaultDisplay().getHeight();
        } else {
            Point point = new Point();
            manager.getDefaultDisplay().getSize(point);
            width = point.x;
            height = point.y;
        }

        params = new WindowManager.LayoutParams();
        params.width = width;
        params.height = height;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.form_list_popup_dialog, null);
        aq = new AQuery(v);
        builder.setView(v);

        Dialog dialog = builder.create();
        dialog.getWindow().setAttributes(params);

        return dialog;
    }
}
