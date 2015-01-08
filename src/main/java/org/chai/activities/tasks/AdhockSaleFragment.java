package org.chai.activities.tasks;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.activities.HomeActivity;
import org.chai.adapter.ProductArrayAdapter;
import org.chai.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 1/7/15.
 */
public class AdhockSaleFragment extends BaseContainerFragment {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AdhockSaleDao saleDao;
    private SaleDataDao saleDataDao;
    private ProductDao productDao;
    private CustomerDao  customerDao;

    private TableLayout tableLayout;
    private List<Spinner> spinnerList;
    private List<EditText> quantityFields;
    private List<EditText> priceFields;
    private Sale saleInstance;
    private Customer salesCustomer;
    private List<Product> products;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.adhock_sale_form,container, false);
        try {
            initialiseGreenDao();
            tableLayout = (TableLayout)view.findViewById(R.id.sales_table);
            spinnerList = new ArrayList<Spinner>();
            quantityFields = new ArrayList<EditText>();
            priceFields = new ArrayList<EditText>();

            Spinner productSpinner = (Spinner) view.findViewById(R.id.sales_product);
            products = productDao.loadAll();
            productSpinner.setAdapter(new ProductArrayAdapter(getActivity(),R.id.sales_product,products.toArray(new Product[products.size()])));
            spinnerList.add(productSpinner);
            quantityFields.add((EditText)view.findViewById(R.id.sales_quantity));
            priceFields.add((EditText)view.findViewById(R.id.sales_price));

            List<Customer> customersList = customerDao.loadAll();
            AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.adhock_sale_customer);
            ArrayAdapter<Customer> adapter = new ArrayAdapter<Customer>(getActivity(),android.R.layout.simple_dropdown_item_1line,customersList);
            textView.setAdapter(adapter);

            textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Customer selected = (Customer)adapterView.getAdapter().getItem(position);
                    salesCustomer = selected;
                }
            });


            Button addButton = (Button)view.findViewById(R.id.sales_add_more);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addRowToTable(null, view);
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
            manageDoyouStockZincResponses(view);
//            bindSalesInfoToUI(view);

        } catch (Exception ex) {
            ex.printStackTrace();
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
            saleDao = daoSession.getAdhockSaleDao();
            saleDataDao = daoSession.getSaleDataDao();
            productDao = daoSession.getProductDao();
            customerDao = daoSession.getCustomerDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void addRowToTable(SaleData saleData, View view) {
        TableRow tableRow = new TableRow(getActivity());
        tableRow.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        Spinner spinner = new Spinner(getActivity());
        TableRow.LayoutParams spinnerParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        spinner.setLayoutParams(spinnerParams);
        spinner.setAdapter(new ProductArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,products.toArray(new Product[products.size()])));
        spinner.setBackgroundResource(R.drawable.btn_dropdown);

        tableRow.addView(spinner);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,0,10,0);
        EditText quantityView = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edit_text_style, null);
        quantityView.setTextColor(Color.BLACK);
        quantityView.setLayoutParams(params);
        quantityView.setInputType(InputType.TYPE_CLASS_NUMBER);
        tableRow.addView(quantityView);

        EditText priceView = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edit_text_style, null);
        priceView.setTextColor(Color.BLACK);
        priceView.setLayoutParams(params);
        priceView.setInputType(InputType.TYPE_CLASS_NUMBER);
        tableRow.addView(priceView);

        if (saleData != null) {
            quantityView.setText(saleData.getQuantity() + "");
            priceView.setText(saleData.getPrice() + "");
            spinner.setSelection(getProductPosition(saleData.getProduct()));
        }
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

        spinnerList.add(spinner);
        quantityFields.add(quantityView);
        priceFields.add(priceView);

        tableLayout.addView(tableRow, new TableLayout.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
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

    private void manageDoyouStockZincResponses(View view) {
        final Spinner spinner = (Spinner) view.findViewById(R.id.sales_do_you_stock_zinc);
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


    private int getProductPosition(Product product) {
        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).getId() == product.getId()) {
                return i;
            }
        }
        return 0;
    }

    private void submitSale(){
        AdhockSale sale = new AdhockSale(null);
        sale.setClientRefId(UUID.randomUUID().toString());
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
        sale.setCustomerId(salesCustomer.getUuid());
        sale.setCustomerRefId(salesCustomer.getId());

        Long saleId = saleDao.insert(sale);
        //add the different sales.
        submitSaleData(saleId);
    }

    private void submitSaleData(Long saleId) {
        for (int i = 0; i < spinnerList.size(); ++i) {
            SaleData saleData = new SaleData(null);
            saleData.setUuid(UUID.randomUUID().toString());
            saleData.setAdhockSaleId(saleId);
            saleData.setPrice(Integer.parseInt(priceFields.get(i).getText().toString()));
            saleData.setQuantity(Integer.parseInt(quantityFields.get(i).getText().toString()));
            Product product = (Product) spinnerList.get(i).getSelectedItem();
            saleData.setProductRefId(product.getId());
            saleData.setProductId(product.getUuid());
            saleDataDao.insert(saleData);
        }
    }

}
