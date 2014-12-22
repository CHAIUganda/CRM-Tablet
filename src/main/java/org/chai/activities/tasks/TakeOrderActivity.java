package org.chai.activities.tasks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.chai.R;
import org.chai.model.*;

import java.util.*;

/**
 * Created by victor on 12/22/14.
 */
public class TakeOrderActivity extends Activity {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;
    private OrderDao orderDao;

    private TableLayout tableLayout;
    private List<Spinner> spinnerList;
    private List<EditText> quantityFields;

    private EditText dateEditTxt;
    private DatePickerDialog datePickerDialog;
    private String initialDate;
    private String initialMonth;
    private String initialYear;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_form);
        initialiseGreenDao();

        tableLayout = (TableLayout)findViewById(R.id.orders_table);
        spinnerList = new ArrayList<Spinner>();
        quantityFields = new ArrayList<EditText>();

        spinnerList.add((Spinner) findViewById(R.id.order_product));
        quantityFields.add((EditText) findViewById(R.id.order_quantity));

        List<Customer> customersList = customerDao.loadAll();
        String[] customers = getCustomerName(customersList);
        AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.sales_auto_complete_textview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,customers);
        textView.setAdapter(adapter);

        Button addButton = (Button)findViewById(R.id.order_add_more);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRowToTable();
            }
        });

        Button dateBtn = (Button)findViewById(R.id.order_delivery_date_btn);
        dateEditTxt = (EditText)findViewById(R.id.order_delivery_date);

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

        Button saveBtn = (Button)findViewById(R.id.order_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrder();
            }
        });

    }

    private String[] getCustomerName(List<Customer> customers) {
        String[] customerArray = new String[customers.size()];
        for(int i=0;i<customers.size();++i){
            customerArray[i] = customers.get(i).getOutletName();
        }
        return customerArray;
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
            orderDao = daoSession.getOrderDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void addRowToTable(){
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        Spinner spinner  = new Spinner(this);
        TableRow.LayoutParams spinnerParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        spinner.setLayoutParams(spinnerParams);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[] { "Zinc", "ORS"});
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setBackgroundResource(R.drawable.btn_dropdown);

        tableRow.addView(spinner);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        EditText quantityView =(EditText)getLayoutInflater().inflate(R.layout.edit_text_style,null);
        quantityView.setTextColor(Color.BLACK);
        quantityView.setLayoutParams(params);
        tableRow.addView(quantityView);

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
                removeRow();
            }
        });
        tableRow.addView(deleteBtn);

        tableLayout.addView(tableRow, new TableLayout.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
    }

    private void removeRow() {
        if (!spinnerList.isEmpty()) {
            spinnerList.remove(spinnerList.size() - 1);
        }
        if (!quantityFields.isEmpty()) {
            quantityFields.remove(quantityFields.size() - 1);
        }
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

    private void  submitOrder(){
        for (int i = 0; i < spinnerList.size(); ++i) {
            Order order = new Order(null);
            order.setQuantity(Double.parseDouble(quantityFields.get(i).getText().toString()));
            order.setCustomerId(4L);
            order.setProductId(2L);
            order.setDeliveryDate(new Date());
            order.setOrderDate(new Date());
//            orderDao.insert(order)
        }
    }



}