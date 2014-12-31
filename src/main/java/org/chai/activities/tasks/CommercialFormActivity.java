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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.chai.R;
import android.widget.TableRow.LayoutParams;
import org.chai.activities.HomeActivity;
import org.chai.adapter.ProductArrayAdapter;
import org.chai.model.*;
import org.chai.rest.RestClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 10/26/14.
 */
public class CommercialFormActivity extends Fragment {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TaskDao taskDao;
    private SaleDao saleDao;
    private ProductDao productDao;


    private TableLayout tableLayout;
    private List<Spinner> spinnerList;
    private List<EditText> quantityFields;
    private List<EditText> priceFields;
    private Task callDataTask;
    private Customer salesCustomer;
    private List<Product> products;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.sales_form,container, false);
        try {
            initialiseGreenDao();
            tableLayout = (TableLayout)view.findViewById(R.id.sales_table);
            spinnerList = new ArrayList<Spinner>();
            quantityFields = new ArrayList<EditText>();
            priceFields = new ArrayList<EditText>();
            Bundle bundle = getArguments();
            Long taskId = bundle.getLong("taskId");
            callDataTask = taskDao.loadDeep(taskId);
            salesCustomer = callDataTask.getCustomer();
            ((TextView) view.findViewById(R.id.sales_customer)).setText(salesCustomer.getOutletName());
            ((TextView)view.findViewById(R.id.sales_customer_location)).setText(salesCustomer.getDescriptionOfOutletLocation());

            Spinner productSpinner = (Spinner) view.findViewById(R.id.sales_product);
            products = productDao.loadAll();
            productSpinner.setAdapter(new ProductArrayAdapter(getActivity(),R.id.sales_product,products.toArray(new Product[products.size()])));
            spinnerList.add(productSpinner);
            quantityFields.add((EditText)view.findViewById(R.id.sales_quantity));
            priceFields.add((EditText)view.findViewById(R.id.sales_price));

            Button addButton = (Button)view.findViewById(R.id.sales_add_more);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addRowToTable();
                }
            });

            Button takeOrder = (Button)view.findViewById(R.id.sales_take_order);
            takeOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), TakeOrderActivity.class);
                    startActivity(intent);
                }
            });

            Button saveBtn = (Button)view.findViewById(R.id.sales_save_sale);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitSale();
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    startActivity(i);
                }
            });
            manageDoyouStockZincResponses();

        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Error in oncreate view:" + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        return view ;
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            taskDao = daoSession.getTaskDao();
            saleDao = daoSession.getSaleDao();
            productDao = daoSession.getProductDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void addRowToTable(){
        TableRow tableRow = new TableRow(getActivity());
        tableRow.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        Spinner spinner = new Spinner(getActivity());
        LayoutParams spinnerParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        spinner.setLayoutParams(spinnerParams);
        spinner.setAdapter(new ProductArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,products.toArray(new Product[products.size()])));
        spinner.setBackgroundResource(R.drawable.btn_dropdown);

        tableRow.addView(spinner);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        EditText quantityView = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edit_text_style, null);
        quantityView.setTextColor(Color.BLACK);
        quantityView.setLayoutParams(params);
        tableRow.addView(quantityView);


        EditText priceView = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edit_text_style, null);
        priceView.setTextColor(Color.BLACK);
        priceView.setLayoutParams(params);
        tableRow.addView(priceView);

        Button deleteBtn = new Button(getActivity());
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

    private void submitSale(){
        Sale sale = new Sale(null);
        sale.setUuid(UUID.randomUUID().toString());
        sale.setDateOfSale(new Date());
        String stocksZinc = ((Spinner) getActivity().findViewById(R.id.sales_do_you_stock_zinc)).getSelectedItem().toString();
        sale.setDoYouStockOrsZinc(stocksZinc.equalsIgnoreCase("Yes") ? true : false);
        sale.setHowmanyOrsInStock(Integer.parseInt(((EditText) getActivity().findViewById(R.id.sales_howmany_in_stock_ors)).getText().toString()));
        sale.setHowManyZincInStock(Integer.parseInt(((EditText) getActivity().findViewById(R.id.sales_howmany_in_stock_zinc)).getText().toString()));
        sale.setIfNoWhy(((EditText) getActivity().findViewById(R.id.sales_if_no_why)).getText().toString());
        sale.setPointOfsaleMaterial(((Spinner) getActivity().findViewById(R.id.sales_point_of_sale)).getSelectedItem().toString());
        sale.setRecommendationNextStep(((Spinner) getActivity().findViewById(R.id.sales_next_step_recommendation)).getSelectedItem().toString());
        sale.setRecommendationLevel(((Spinner) getActivity().findViewById(R.id.sales_recommendation_level)).getSelectedItem().toString());
        sale.setGovernmentApproval(((Spinner) getActivity().findViewById(R.id.sales_government_approval)).getSelectedItem().toString());
        sale.setOrderId(callDataTask.getId());
        Long saleId = saleDao.insert(sale);
        //add the different sales.
        submitSaleData(saleId);
    }

    private void submitSaleData(Long saleId) {
        for (int i = 0; i < spinnerList.size(); ++i) {
            SaleData saleData = new SaleData(null);
            saleData.setUuid(UUID.randomUUID().toString());
            saleData.setSaleId(saleId);
            saleData.setPrice(Integer.parseInt(priceFields.get(i).getText().toString()));
            saleData.setQuantity(Integer.parseInt(quantityFields.get(i).getText().toString()));
            saleData.setProductId( ((Product) spinnerList.get(i).getSelectedItem()).getId());
        }
    }


    private void manageDoyouStockZincResponses() {
        final Spinner spinner = (Spinner) getActivity().findViewById(R.id.sales_do_you_stock_zinc);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout stockfieldsLayout = (LinearLayout) getActivity().findViewById(R.id.sales_zinc_stock_layout);
                LinearLayout ifnowhyLayout = (LinearLayout) getActivity().findViewById(R.id.sales_ifnowhy_layout);
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