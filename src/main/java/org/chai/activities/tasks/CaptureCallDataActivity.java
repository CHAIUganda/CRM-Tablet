package org.chai.activities.tasks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import org.chai.R;

import java.util.Calendar;
import java.util.StringTokenizer;

/**
 * Created by victor on 10/26/14.
 */
public class CaptureCallDataActivity extends Activity {
    static final int DATE_PICKER_ID = 1111;
    private EditText dateEditTxt;
    private DatePickerDialog datePickerDialog;
    private String initialDate;
    private String initialMonth;
    private String initialYear;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_calls_data);

        Button dateBtn = (Button)findViewById(R.id.capture_call_data_setdate);
        dateEditTxt = (EditText)findViewById(R.id.capture_call_data_date);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = null;
                String existingDate = (String)dateEditTxt.getText().toString();
                if(existingDate!=null && !existingDate.equals("")){
                    StringTokenizer stringTokenizer = new StringTokenizer(existingDate,"/");
                    initialMonth = stringTokenizer.nextToken();
                    initialDate = stringTokenizer.nextToken();
                    initialYear = stringTokenizer.nextToken();
                    if(datePickerDialog == null){
                        datePickerDialog = new DatePickerDialog(view.getContext(),new PickDate(),Integer.parseInt(initialYear),
                                Integer.parseInt(initialMonth)-1,
                                Integer.parseInt(initialDate));

                        datePickerDialog.updateDate(Integer.parseInt(initialYear),
                                Integer.parseInt(initialMonth)-1,
                                Integer.parseInt(initialDate));
                    }
                }else{
                    calendar = Calendar.getInstance();
                    if(datePickerDialog == null){
                        datePickerDialog = new DatePickerDialog(view.getContext(),new PickDate(),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                    }
                }
                datePickerDialog.show();

            }
        });
    }

    private class PickDate implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            view.updateDate(year, monthOfYear, dayOfMonth);
            dateEditTxt.setText(monthOfYear+"/"+dayOfMonth+"/"+year);
            datePickerDialog.hide();
        }
    }

}