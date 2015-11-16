package org.chai.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.tasks.AdhockSalesFormActivity;
import org.chai.activities.tasks.AdhockSalesFormICCMFragment;

/**
 * Created by Zed on 11/16/2015.
 */
public class ICCMPopupDialogFragment extends DialogFragment {
    AQuery aq;
    static AdhockSalesFormActivity activity;
    static AdhockSalesFormICCMFragment fragment;

    public static ICCMPopupDialogFragment newInstance(AdhockSalesFormActivity a, String message, AdhockSalesFormICCMFragment f) {
        ICCMPopupDialogFragment frag = new ICCMPopupDialogFragment();
        Bundle b = new Bundle();
        b.putString("message", message);
        frag.setArguments(b);
        activity = a;
        fragment = f;


        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = getArguments().getString("message");
        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.iccm_popup_dialog, null);

        aq = new AQuery(v);
        aq.id(R.id.txt_message).text(message);

        builder.setView(v);

        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fragment.fieldsSaved = true; //This stops us from showing dialog again
                activity.saveForm(); //Re-save fields - will not show dialog again
            }
        });

        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }
}
