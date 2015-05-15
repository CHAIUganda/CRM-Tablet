package org.chai.util.customwidget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.chai.model.User;
import org.chai.rest.RestClient;
import org.chai.util.Utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Zed on 5/1/2015.
 */
public class CustomDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        Date minDateDate = new Date(System.currentTimeMillis() - 1000);
        if(RestClient.getRole().equalsIgnoreCase(User.ROLE_DETAILER)){
            //a week from now
            minDateDate = Utils.addToDateOffset(new Date(), 7);
        }
        dialog.getDatePicker().setMinDate(minDateDate.getTime());
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
    }
}
