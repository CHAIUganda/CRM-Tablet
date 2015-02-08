package org.chai.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.chai.R;

/**
 * Created by victor on 1/28/15.
 */
public class CustomMultSelectDropDown extends Button implements View.OnClickListener {

    protected CharSequence[] stringOptions;
    protected boolean[] booleanSelections;
    private Context context;
    private String otherText = "";

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
            if (stringOptions[clicked].toString().equals("Other")) {
                //show other dialog with a textfield
                Log.i("other:","showing others dialog");
                showOtherTextFieldDialog();
            }
        }

    }

    protected void setSelectedOptions() {
        this.setText("");
        for (int i = 0; i < stringOptions.length; i++) {
            Log.i("ME", stringOptions[i] + " selected: " + booleanSelections[i]);
            if (booleanSelections[i] && !stringOptions[i].toString().equalsIgnoreCase("Other")) {
                this.setText((this.getText().toString().equals("") ? "" : this.getText() + ",") + stringOptions[i]);
            } else if (booleanSelections[i] && stringOptions[i].toString().equalsIgnoreCase("Other")) {
                this.setText((this.getText().toString().equals("") ? otherText : this.getText() + ",") + otherText);
            }
        }
    }

    public void setStringOptions(CharSequence[] stringOptions) {
        this.stringOptions = stringOptions;
        booleanSelections = new boolean[stringOptions.length];
    }

    private void showOtherTextFieldDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final EditText quantityView = (EditText) inflater.inflate(R.layout.edit_text_style, null);
        quantityView.setTextColor(Color.BLACK);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Please Specify")
                .setView(quantityView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int button) {
                        otherText = quantityView.getText().toString();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int button) {

            }
        });
        dialog.show();
    }

}
