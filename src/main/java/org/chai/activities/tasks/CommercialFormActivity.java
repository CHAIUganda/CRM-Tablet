package org.chai.activities.tasks;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.chai.R;
import android.widget.TableRow.LayoutParams;
import org.chai.activities.HomeActivity;
import org.chai.model.*;
import org.chai.rest.RestClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 10/26/14.
 */
public class CommercialFormActivity extends Activity {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TaskDao taskDao;
    private SaleDao saleDao;


    private TableLayout tableLayout;
    private List<Spinner> spinnerList;
    private List<EditText> quantityFields;
    private List<EditText> priceFields;
    private Task callDataTask;
    private Customer salesCustomer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_form);
        initialiseGreenDao();
        tableLayout = (TableLayout)findViewById(R.id.sales_table);
        spinnerList = new ArrayList<Spinner>();
        quantityFields = new ArrayList<EditText>();
        priceFields = new ArrayList<EditText>();
        Bundle bundle = getIntent().getExtras();
        Long taskId = bundle.getLong("taskId");
        callDataTask = taskDao.loadDeep(taskId);
        salesCustomer = callDataTask.getCustomer();
        ((TextView)findViewById(R.id.sales_customer)).setText(salesCustomer.getOutletName());
        ((TextView)findViewById(R.id.sales_customer_location)).setText(salesCustomer.getDescriptionOfOutletLocation());

        spinnerList.add((Spinner) findViewById(R.id.sales_product));
        quantityFields.add((EditText) findViewById(R.id.sales_quantity));
        priceFields.add((EditText) findViewById(R.id.sales_price));

        Button addButton = (Button)findViewById(R.id.sales_add_more);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRowToTable();
            }
        });

        Button takeOrder = (Button)findViewById(R.id.sales_take_order);
        takeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TakeOrderActivity.class);
                startActivity(intent);
               /* LayoutInflater layoutInflater = (LayoutInflater) CommercialFormActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View entryView = layoutInflater.inflate(R.layout.order_form, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(CommercialFormActivity.this);
                alert.setIcon(R.drawable.icon).setTitle("New Order").setView(entryView).setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int button) {
                       *//* CustomerContact customerContact = new CustomerContact(null);
                        customerContact.setUuid(UUID.randomUUID().toString());
                        customerContact.setContact(((EditText) entryView.findViewById(R.id.customer_contact_telephone)).getText().toString());
                        customerContact.setFirstName(((EditText) entryView.findViewById(R.id.customer_contact_firstname)).getText().toString());
                        customerContact.setSurname(((EditText) entryView.findViewById(R.id.customer_contact_surname)).getText().toString());
                        customerContact.setGender(((Spinner) entryView.findViewById(R.id.customer_contact_gender)).getSelectedItem().toString());
                        customerContact.setNetworkOrAssociation(Boolean.valueOf(((Spinner) entryView.findViewById(R.id.customer_contact_network)).getSelectedItem().toString()));
                        customerContact.setRole(((Spinner) entryView.findViewById(R.id.customer_contact_type)).getSelectedItem().toString());
                        customerContacts.add(customerContact);
                        //add to parent form
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.customer_contacts_layout);

                        TextView contactView = new TextView(CustomerForm.this);
                        contactView.setText(customerContact.getFirstName() + ":" + customerContact.getContact());
                        contactView.setTextSize(18);
                        contactView.setTextColor(Color.parseColor("#000000"));
                        linearLayout.addView(contactView);*//*

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int button) {

                    }
                });
                alert.show();*/
            }
        });

        Button saveBtn = (Button) findViewById(R.id.sales_save_sale);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitSale();
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });
        manageDoyouStockZincResponses();

    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            taskDao = daoSession.getTaskDao();
            saleDao = daoSession.getSaleDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
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
        EditText quantityView =(EditText)getLayoutInflater().inflate(R.layout.edit_text_style, null);
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
                removeRow();
            }
        });
        tableRow.addView(deleteBtn);

        tableLayout.addView(tableRow, new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void removeRow() {
        if (!spinnerList.isEmpty()) {
            spinnerList.remove(spinnerList.size() - 1);
        }
        if (!quantityFields.isEmpty()) {
            quantityFields.remove(quantityFields.size() - 1);
        }
        if (!priceFields.isEmpty()) {
            priceFields.remove(priceFields.size() - 1);
        }
    }

    private void submitSale() {
        for (int i = 0; i < spinnerList.size(); ++i) {
            Sale sale = new Sale(null);
            sale.setDateOfSale(new Date());
            sale.setQuantity(Integer.parseInt(quantityFields.get(i).getText().toString()));
            sale.setSalePrice(Integer.parseInt(priceFields.get(i).getText().toString()));
            sale.setOrderId(callDataTask.getId());
//            sale.setProductId();
//            saleDao.insert(sale);
        }
    }


    private void manageDoyouStockZincResponses() {
        final Spinner spinner = (Spinner) findViewById(R.id.sales_do_you_stock_zinc);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout stockfieldsLayout = (LinearLayout) findViewById(R.id.sales_zinc_stock_layout);
                LinearLayout ifnowhyLayout = (LinearLayout) findViewById(R.id.sales_ifnowhy_layout);
                if ("No".equalsIgnoreCase(selected)) {
                    stockfieldsLayout.setVisibility(View.GONE);
                    ifnowhyLayout.setVisibility(View.VISIBLE);
                } else {
                    stockfieldsLayout.setVisibility(View.VISIBLE);
                    ifnowhyLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}