package org.chai.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;

import org.chai.R;

/**
 * Created by Zed on 11/16/2015.
 */
public class AdhockICCMPopupDialogFragment extends DialogFragment {
    AQuery aq;

    public static AdhockICCMPopupDialogFragment newInstance(String message) {
        AdhockICCMPopupDialogFragment frag = new AdhockICCMPopupDialogFragment();
        Bundle b = new Bundle();
        b.putString("message", message);
        frag.setArguments(b);

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

        builder.setPositiveButton("DONE", null);

        return builder.create();
    }
}
