package org.chai.activities.tasks;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.chai.R;
import android.widget.TableRow.LayoutParams;

/**
 * Created by victor on 10/26/14.
 */
public class CommercialFormActivity extends Activity {
    static final int DATE_PICKER_ID = 1111;
    private EditText dateEditTxt;
    private DatePickerDialog datePickerDialog;
    private String initialDate;
    private String initialMonth;
    private String initialYear;
    private TableLayout tableLayout;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_form);
        tableLayout = (TableLayout)findViewById(R.id.sales_table);
        Button addButton = (Button)findViewById(R.id.sales_add_more);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRowToTable();
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

    private void addRowToTable(){
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        Spinner spinner  = new Spinner(this);
        LayoutParams spinnerParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        spinner.setLayoutParams(spinnerParams);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[] { "Zinc", "ORS"});
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setBackgroundResource(R.drawable.btn_dropdown);

        tableRow.addView(spinner);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        EditText quantityView =(EditText)getLayoutInflater().inflate(R.layout.edit_text_style,null);
        quantityView.setTextColor(Color.BLACK);
        quantityView.setLayoutParams(params);
        tableRow.addView(quantityView);


        EditText priceView = (EditText)getLayoutInflater().inflate(R.layout.edit_text_style,null);
        priceView.setTextColor(Color.BLACK);
        priceView.setLayoutParams(params);
        tableRow.addView(priceView);

        Button deleteBtn = new Button(this);
        deleteBtn.setBackgroundColor(Color.parseColor("#428bca"));
        deleteBtn.setText("Remove");
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View row = (View) view.getParent();
                ViewGroup container = ((ViewGroup)row.getParent());
                container.removeView(row);
                container.invalidate();
            }
        });
        tableRow.addView(deleteBtn);

        tableLayout.addView(tableRow, new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }



}