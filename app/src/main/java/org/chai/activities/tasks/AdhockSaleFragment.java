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
import android.widget.TableRow.LayoutParams;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.activities.HomeActivity;
import org.chai.adapter.ProductArrayAdapter;
import org.chai.model.*;
import org.chai.util.CustomMultSelectDropDown;
import org.chai.util.Utils;
import org.chai.util.customwidget.GpsWidgetView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 1/7/15.
 */
/*
TO DO: this class needs to be refactored with sales to
inherit from a super class since they share attrs
 */
public class AdhockSaleFragment extends BaseContainerFragment {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AdhockSaleDao adhockSaleDao;
    private SaleDataDao saleDataDao;
    private StokeDataDao stokeDataDao;
    private ProductDao productDao;
    private CustomerDao  customerDao;

    private TableLayout salesTableLayout;
    private TableLayout stockTableLayout;
    private List<Spinner> spinnerList;
    private List<EditText> quantityFields;
    private List<EditText> priceFields;
    private List<Spinner> stockSpinnerList;
    private List<EditText> stockQuantityFlds;
    private AdhockSale saleInstance;
    private Customer salesCustomer;
    private List<Product> products;
    private boolean isUpdate = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.adhock_sale_form,container, false);
        try {
            initialiseGreenDao();
            salesTableLayout = (TableLayout)view.findViewById(R.id.adhock_sale_table);
            stockTableLayout = (TableLayout) view.findViewById(R.id.adhoc_sales_stock_table);
            spinnerList = new ArrayList<Spinner>();
            quantityFields = new ArrayList<EditText>();
            priceFields = new ArrayList<EditText>();
            stockSpinnerList = new ArrayList<Spinner>();
            stockQuantityFlds = new ArrayList<EditText>();

            Spinner productSpinner = (Spinner) view.findViewById(R.id.adhock_sale_product);
            Spinner stockProductSpinner =  (Spinner) view.findViewById(R.id.adhoc_sales_stock_product);
            products = productDao.loadAll();


            ProductArrayAdapter adapter1 = new ProductArrayAdapter(getActivity(), R.id.adhock_sale_product, products.toArray(new Product[products.size()]));
            productSpinner.setAdapter(adapter1);
            stockProductSpinner.setAdapter(adapter1);

            spinnerList.add(productSpinner);
            quantityFields.add((EditText)view.findViewById(R.id.adhock_sale_quantity));
            priceFields.add((EditText)view.findViewById(R.id.adhock_sale_price));

            stockSpinnerList.add(stockProductSpinner);
            stockQuantityFlds.add((EditText)view.findViewById(R.id.adhoc_sales_stock_quantity));


            List<Customer> customersList = customerDao.loadAll();
            AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.adhock_sale_customer);
            ArrayAdapter<Customer> adapter = new ArrayAdapter<Customer>(getActivity(),android.R.layout.simple_dropdown_item_1line,customersList);
            textView.setAdapter(adapter);

            textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                             @Override
                             public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                 Customer selected = (Customer)adapterView.getAdapter().getItem(position);
                                 salesCustomer = selected;
                                 try{
                                     if(salesCustomer!=null){
                                         ((TextView)getActivity(). findViewById(R.id.adhoc_sales_district)).setText(salesCustomer.getSubcounty().getDistrict().getName());
                                         ((TextView)getActivity(). findViewById(R.id.adhoc_sales_subcounty)).setText(salesCustomer.getSubcounty().getName());
                                     }
                                 }catch (Exception ex){
                                     //ignore
                                 }
                             }
                         });

            CustomMultSelectDropDown pointOfSaleMaterials = (CustomMultSelectDropDown)view.findViewById(R.id.adhock_sale_point_of_sale);
            pointOfSaleMaterials.setStringOptions(getResources().getStringArray(R.array.point_of_sale_material));

            CustomMultSelectDropDown recommendationNextStep = (CustomMultSelectDropDown)view.findViewById(R.id.adhock_sale_next_step_recommendation);
            recommendationNextStep.setStringOptions(getResources().getStringArray(R.array.recommendation_nextstep));

            Button addButton = (Button)view.findViewById(R.id.adhock_sale_add_more);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addRowToTable(null, null, view, false);
                }
            });

            Button addStockButton = (Button) view.findViewById(R.id.adhoc_sales_stock_add_more);
            addStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addRowToTable(null,null, view,true);
                }
            });

            Button saveBtn = (Button)view.findViewById(R.id.adhock_sale_save_sale);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        if(!allMandatoryFieldsFilled()){
                            Toast.makeText(getActivity(),"Please ensure that data is entered correctly",Toast.LENGTH_LONG).show();
                        }else{
                            submitSale();
                            resetFragment(R.id.frame_container, new AdhockSaleFragment());
                            Intent i = new Intent(getActivity(), HomeActivity.class);
                            startActivity(i);
                        }
                    }catch (Exception ex){
                        Toast.makeText(getActivity(),"A problem Occured while saving a new Sale,please ensure that data is entered correctly",Toast.LENGTH_LONG).show();
                    }
                }
            });
            manageDoyouStockZincResponses(view);
            setRequiredFields(view);

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getActivity(), "Error in oncreate view:" + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String saleId = bundle.getString("saleId");
            saleInstance = adhockSaleDao.load(saleId);
            bindSalesInfoToUI(saleInstance, view);
            if (saleInstance.getIsHistory() != null && saleInstance.getIsHistory()) {
                setReadOnly(view);
            }
        }
        return view ;
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            adhockSaleDao = daoSession.getAdhockSaleDao();
            saleDataDao = daoSession.getSaleDataDao();
            productDao = daoSession.getProductDao();
            customerDao = daoSession.getCustomerDao();
            stokeDataDao = daoSession.getStokeDataDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void bindSalesInfoToUI(AdhockSale adhockSale, View view) {
        if (adhockSale != null) {
            ((AutoCompleteTextView) view.findViewById(R.id.adhock_sale_customer)).setText(saleInstance.getCustomer().getOutletName());
            salesCustomer = saleInstance.getCustomer();
            ((EditText) view.findViewById(R.id.adhock_sale_if_no_why)).setText(saleInstance.getIfNoWhy());

            ((GpsWidgetView)view.findViewById(R.id.adhoc_sales_gps)).setLatLongText(saleInstance.getLatitude()+","+saleInstance.getLongitude());

            Spinner doyouStockZincSpinner = (Spinner) view.findViewById(R.id.adhock_sale_do_you_stock_zinc);
            Utils.setSpinnerSelection(doyouStockZincSpinner, (saleInstance.getDoYouStockOrsZinc() == true) ? "Yes" : "No");

            Spinner governmentApprovalSpinner = (Spinner) view.findViewById(R.id.adhock_sale_government_approval);
            Utils.setSpinnerSelection(governmentApprovalSpinner, saleInstance.getGovernmentApproval());

            Button pointOfSaleMaterial = (Button) view.findViewById(R.id.adhock_sale_point_of_sale);
           pointOfSaleMaterial.setText(saleInstance.getPointOfsaleMaterial());


            CustomMultSelectDropDown recommendationNextStep = (CustomMultSelectDropDown) view.findViewById(R.id.adhock_sale_next_step_recommendation);
            recommendationNextStep.setText(saleInstance.getRecommendationNextStep());

            bindSalesDataToUi(view);
            bindStokeDataToUi(view);
        }
    }

    private void bindSalesDataToUi(View view) {
        List<SaleData> salesDatas = saleInstance.getAdhockSalesDatas();
        for (int i = 0; i < salesDatas.size(); ++i) {
            if (i == 0) {
                ((EditText)view.findViewById(R.id.adhock_sale_quantity)).setText(salesDatas.get(i).getQuantity()+"");
                ((EditText)view.findViewById(R.id.adhock_sale_price)).setText(salesDatas.get(i).getPrice()+"");
                ((Spinner)view.findViewById(R.id.adhock_sale_product)).setSelection(getProductPosition(salesDatas.get(i).getProduct()));
            }else {
                addRowToTable(salesDatas.get(i),null, view,false);
            }
        }
    }

    private void bindStokeDataToUi(View view) {
        List<StokeData> stockDatas = saleInstance.getAdhockStockDatas();
        for (int i = 0; i < stockDatas.size(); ++i) {
            if (i == 0) {
                ((EditText)view.findViewById(R.id.adhoc_sales_stock_quantity)).setText(stockDatas.get(i).getQuantity()+"");
                ((Spinner)view.findViewById(R.id.adhoc_sales_stock_product)).setSelection(getProductPosition(stockDatas.get(i).getProduct()));
            }else {
                addRowToTable(null,stockDatas.get(i), view,true);
            }
        }
    }


    private void addRowToTable(SaleData saleData,StokeData stockData, View view,final boolean isStockTaking) {
        TableRow tableRow = new TableRow(getActivity());
        tableRow.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        final Spinner spinner = new Spinner(getActivity());
        TableRow.LayoutParams spinnerParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        spinner.setLayoutParams(spinnerParams);
        spinner.setAdapter(new ProductArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,products.toArray(new Product[products.size()])));
        spinner.setBackgroundResource(R.drawable.btn_dropdown);

        tableRow.addView(spinner);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,0,10,0);
        final EditText quantityView = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edit_text_style, null);
        quantityView.setTextColor(Color.BLACK);
        quantityView.setLayoutParams(params);
        quantityView.setInputType(InputType.TYPE_CLASS_NUMBER);
        tableRow.addView(quantityView);

        final EditText priceView = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edit_text_style, null);
        priceView.setTextColor(Color.BLACK);
        priceView.setLayoutParams(params);
        priceView.setInputType(InputType.TYPE_CLASS_NUMBER);

        if (saleData != null) {
            quantityView.setText(saleData.getQuantity() + "");
            priceView.setText(saleData.getPrice() + "");
            spinner.setSelection(getProductPosition(saleData.getProduct()));
        }else if(stockData!=null){
            quantityView.setText(stockData.getQuantity() + "");
            spinner.setSelection(getProductPosition(stockData.getProduct()));
        }
        Button deleteBtn = (Button) getActivity().getLayoutInflater().inflate(R.layout.delete_icon, null);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View row = (View) view.getParent();
                ViewGroup container = ((ViewGroup)row.getParent());
                container.removeView(row);
                if (isStockTaking) {
                    stockSpinnerList.remove(spinner);
                    stockQuantityFlds.remove(quantityView);
                } else {
                    spinnerList.remove(spinner);
                    quantityFields.remove(quantityView);
                    priceFields.remove(priceView);
                }
                container.invalidate();
            }
        });
        if (isStockTaking) {
            stockSpinnerList.add(spinner);
            stockQuantityFlds.add(quantityView);
            tableRow.addView(deleteBtn);

            stockTableLayout.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        } else {
            tableRow.addView(priceView);
            spinnerList.add(spinner);
            quantityFields.add(quantityView);
            priceFields.add(priceView);
            tableRow.addView(deleteBtn);

            salesTableLayout.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        } }

    private void manageDoyouStockZincResponses(View view) {
        final Spinner spinner = (Spinner) view.findViewById(R.id.adhock_sale_do_you_stock_zinc);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout ifnowhyLayout = (LinearLayout) getActivity().findViewById(R.id.adhock_sale_ifnowhy_layout);
                if ("No".equalsIgnoreCase(selected)) {
                    ifnowhyLayout.setVisibility(View.VISIBLE);
                } else {
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
                      if (products.get(i).getUuid() == product.getUuid()) {
                          return i;
                      }
                  }
                  return 0;
              }

    private void submitSale(){
        if(!isUpdate){
         saleInstance = new AdhockSale(UUID.randomUUID().toString());
            saleInstance.setIsHistory(false);
        }
        saleInstance.setDateOfSale(new Date());
        String stocksZinc = ((Spinner) getActivity().findViewById(R.id.adhock_sale_do_you_stock_zinc)).getSelectedItem().toString();
        saleInstance.setDoYouStockOrsZinc(stocksZinc.equalsIgnoreCase("Yes") ? true : false);
        if (stocksZinc.equals("No")) {
            saleInstance.setIfNoWhy(((EditText) getActivity().findViewById(R.id.adhock_sale_if_no_why)).getText().toString());
        }
        saleInstance.setPointOfsaleMaterial(((Button) getActivity().findViewById(R.id.adhock_sale_point_of_sale)).getText().toString());
        saleInstance.setRecommendationNextStep(((CustomMultSelectDropDown) getActivity().findViewById(R.id.adhock_sale_next_step_recommendation)).getText().toString());
        saleInstance.setGovernmentApproval(((Spinner) getActivity().findViewById(R.id.adhock_sale_government_approval)).getSelectedItem().toString());
        saleInstance.setCustomerId(salesCustomer.getUuid());

        if (!((GpsWidgetView) getActivity().findViewById(R.id.adhoc_sales_gps)).getLatLongText().equals("")) {
            String latLongText = ((GpsWidgetView) getActivity().findViewById(R.id.adhoc_sales_gps)).getLatLongText();

            saleInstance.setLatitude(Double.parseDouble(latLongText.split(",")[0]));
            saleInstance.setLongitude(Double.parseDouble(latLongText.split(",")[1]));
        }

        if(isUpdate){
            adhockSaleDao.update(saleInstance);
            submitSaleData(saleInstance.getUuid());
            submitStockData(saleInstance);
        }else{
            Long saleId = adhockSaleDao.insert(saleInstance);
            //add the different sales.
            submitSaleData(saleInstance.getUuid());
            submitStockData(saleInstance);
        }
    }

    private void submitSaleData(String saleId) {
        for (int i = 0; i < spinnerList.size(); ++i) {
            try {
                SaleData saleData = instantiateSaleData(i);
                saleData.setAdhockSaleId(saleId);
                saleData.setPrice(Integer.parseInt(priceFields.get(i).getText().toString()));
                saleData.setQuantity(Integer.parseInt(quantityFields.get(i).getText().toString()));
                Product product = (Product) spinnerList.get(i).getSelectedItem();
                saleData.setProductId(product.getUuid());
                if(saleData.getUuid()!=null){
                    saleDataDao.update(saleData);
                }else{
                    saleData.setUuid(UUID.randomUUID().toString());
                    saleDataDao.insert(saleData);
                }
            }catch (Exception ex){
                //ignore
            }
        }
    }

    private void submitStockData(AdhockSale saleCallData) {
        for (int i = 0; i < stockSpinnerList.size(); ++i) {
            try{
                StokeData stokeData = instantiateStockData(i);
                stokeData.setSaleId(stokeData.getUuid());
                stokeData.setAdhockSale(saleCallData);
                stokeData.setQuantity(Integer.parseInt(stockQuantityFlds.get(i).getText().toString()));
                Product product = (Product) stockSpinnerList.get(i).getSelectedItem();
                stokeData.setProductId(product.getUuid());

                if(stokeData.getUuid()!=null){
                    stokeDataDao.update(stokeData);
                }else{
                    stokeData.setUuid(UUID.randomUUID().toString());
                    stokeDataDao.insert(stokeData);
                }
            }catch (Exception ex){
                //ignore
            }
        }
    }

    private StokeData instantiateStockData(int index){
        List<StokeData> stockDatas = saleInstance.getAdhockStockDatas();
        StokeData stokeData;
        if(index < stockDatas.size()){
            stokeData = stockDatas.get(index);
        }else{
            stokeData = new StokeData(null);
        }
        return stokeData;
    }
    private SaleData instantiateSaleData(int index){
        List<SaleData> saleDatas = saleInstance.getAdhockSalesDatas();
        SaleData saleData;
        if(index < saleDatas.size()){
            saleData = saleDatas.get(index);
        }else{
            saleData = new SaleData(null);
        }
        return saleData;
    }

    private void setRequiredFields(View view) {
        Utils.setRequired((TextView) view.findViewById(R.id.adhock_sale_do_you_stock_zinc_view));
        Utils.setRequired((TextView) view.findViewById(R.id.adhock_sale_government_approval_lbl));
        Utils.setRequired((TextView) view.findViewById(R.id.adhock_sale_customer_lbl));
        Utils.setRequired((TextView) view.findViewById(R.id.adhoc_gps_lbl));
    }

    private boolean allMandatoryFieldsFilled() {
        Spinner doYouStockZinc = ((Spinner) getActivity().findViewById(R.id.adhock_sale_do_you_stock_zinc));
        Spinner governmentApproval = ((Spinner) getActivity().findViewById(R.id.adhock_sale_government_approval));
        if (!Utils.mandatorySpinnerFieldSelected(doYouStockZinc)) {
            return false;
        }else if (((GpsWidgetView)getActivity().findViewById(R.id.adhoc_sales_gps)).getLatLongText().toString().equals("")) {
            return false;
        }else if (!Utils.mandatorySpinnerFieldSelected(governmentApproval)) {
            return false;
        }
        return true;
    }

    private void setReadOnly(View view) {
        view.findViewById(R.id.adhock_sale_customer).setEnabled(false);
        view.findViewById(R.id.adhock_sale_if_no_why).setEnabled(false);
        view.findViewById(R.id.adhoc_sales_gps).setEnabled(false);
        view.findViewById(R.id.adhock_sale_do_you_stock_zinc).setEnabled(false);
        view.findViewById(R.id.adhock_sale_government_approval).setEnabled(false);
        view.findViewById(R.id.adhock_sale_point_of_sale).setEnabled(false);
        view.findViewById(R.id.adhock_sale_next_step_recommendation).setEnabled(false);
        view.findViewById(R.id.adhock_sale_table).setEnabled(false);
        view.findViewById(R.id.adhoc_sales_stock_table).setEnabled(false);
        view.findViewById(R.id.adhock_sale_save_sale).setEnabled(false);

    }


}
