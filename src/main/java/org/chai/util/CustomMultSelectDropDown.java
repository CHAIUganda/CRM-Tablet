package org.chai.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.chai.R;

/**
 * Created by victor on 1/28/15.
 */
public class CustomMultSelectDropDown extends Button implements View.OnClickListener {

    protected CharSequence[] stringOptions;
    protected boolean[] booleanSelections;
    private Context context;

    public CustomMultSelectDropDown(Context context) {
        super(context);
        init();
        this.context = context;
    }

    public CustomMultSelectDropDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        this.context = context;
    }

    private void init() {
        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        showPointOfSaleDialog();
    }

    protected void showPointOfSaleDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Choose from Options")
                .setMultiChoiceItems(stringOptions, booleanSelections, new DialogSelectionClickHandler())
                .setPositiveButton("OK", new DialogButtonClickHandler());
        dialog.show();
    }


    public class DialogButtonClickHandler implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int clicked) {
            switch (clicked) {
                case DialogInterface.BUTTON_POSITIVE:
                    setSelectedOptions();
                    break;
            }
        }
    }

    public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener {
        public void onClick(DialogInterface dialog, int clicked, boolean selected) {
            Log.i("ME================================", stringOptions[clicked] + " selected: " + selected);
        }

    }

    protected void setSelectedOptions() {
        this.setText("");
        for (int i = 0; i < stringOptions.length; i++) {
            Log.i("ME", stringOptions[i] + " selected: " + booleanSelections[i]);
            if (booleanSelections[i] && !stringOptions[i].toString().equalsIgnoreCase("others")) {
                this.setText((this.getText().toString().equals("") ? "" : this.getText() + ",") + stringOptions[i]);
            } else if (booleanSelections[i] && stringOptions[i].toString().equalsIgnoreCase("others")) {

            }
        }
    }

    public void setStringOptions(CharSequence[] stringOptions) {
        this.stringOptions = stringOptions;
        booleanSelections = new boolean[stringOptions.length];
    }


}
