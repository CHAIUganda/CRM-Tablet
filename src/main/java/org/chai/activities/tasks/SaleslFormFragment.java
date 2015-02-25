package org.chai.activities.tasks;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.TableRow.LayoutParams;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.adapter.ProductArrayAdapter;
import org.chai.model.*;
import org.chai.util.CustomMultSelectDropDown;
import org.chai.util.GPSTracker;
import org.chai.util.Utils;
import org.chai.util.customwidget.GpsWidgetView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 10/26/14.
 */
public class SaleslFormFragment extends Fragment {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TaskDao taskDao;
    private SaleDao saleDao;
    private SaleDataDao saleDataDao;
    private StokeDataDao stokeDataDao;
    private ProductDao productDao;


    private TableLayout salesTableLayout;
    private TableLayout stockTableLayout;
    private List<Spinner> spinnerList;
    private List<EditText> quantityFields;
    private List<EditText> priceFields;
    private List<Spinner> stockSpinnerList;
    private List<EditText> stockQuantityFlds;
    private Task callDataTask;
    private Sale saleCallData;
    private Customer salesCustomer;
    private List<Product> products;
    private boolean isUpdate = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.sales_form,container, false);
        try {
            initialiseGreenDao();
            salesTableLayout = (TableLayout) view.findViewById(R.id.sales_table);
            stockTableLayout = (TableLayout) view.findViewById(R.id.sales_stock_table);
            spinnerList = new ArrayList<Spinner>();
            quantityFields = new ArrayList<EditText>();
            priceFields = new ArrayList<EditText>();
            stockSpinnerList = new ArrayList<Spinner>();
            stockQuantityFlds = new ArrayList<EditText>();

            Bundle bundle = getArguments();
            String callId = bundle.getString("callId");
            if (callId != null) {
                //from call list
                saleCallData = saleDao.load(callId);
                callDataTask = saleCallData.getTask();
                salesCustomer = callDataTask.getCustomer();
                isUpdate = true;
                if (saleCallData.getIsHistory() != null && saleCallData.getIsHistory()) {
                    setReadOnly(view);
                }
            } else {
                //from task list view
                String taskId = bundle.getString("taskId");
                callDataTask = taskDao.load(taskId);
                salesCustomer = callDataTask.getCustomer();
            }
            ((TextView) view.findViewById(R.id.sales_customer)).setText(salesCustomer.getOutletName());
            ((TextView)view.findViewById(R.id.sales_customer_location)).setText(salesCustomer.getDescriptionOfOutletLocation());

            Spinner productSpinner = (Spinner) view.findViewById(R.id.sales_product);
            Spinner stockProductSpinner =  (Spinner) view.findViewById(R.id.sales_stock_product);
            products = productDao.loadAll();

            ProductArrayAdapter adapter = new ProductArrayAdapter(getActivity(), R.id.sales_product, products.toArray(new Product[products.size()]));
            productSpinner.setAdapter(adapter);
            stockProductSpinner.setAdapter(adapter);

            spinnerList.add(productSpinner);
            quantityFields.add((EditText)view.findViewById(R.id.sales_quantity));
            priceFields.add((EditText)view.findViewById(R.id.sales_price));

            stockSpinnerList.add(stockProductSpinner);
            stockQuantityFlds.add((EditText)view.findViewById(R.id.sales_quantity));

            CustomMultSelectDropDown pointOfSaleMaterials = (CustomMultSelectDropDown)view.findViewById(R.id.sales_point_of_sale);
            pointOfSaleMaterials.setStringOptions(getResources().getStringArray(R.array.point_of_sale_material));


            CustomMultSelectDropDown recommendationNextStep = (CustomMultSelectDropDown)view.findViewById(R.id.sales_next_step_recommendation);
            recommendationNextStep.setStringOptions(getResources().getStringArray(R.array.recommendation_nextstep));

            Button addButton = (Button)view.findViewById(R.id.sales_add_more);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addRowToTable(null,null,null, view,false);
                }
            });

            Button addStockButton = (Button) view.findViewById(R.id.sales_stock_add_more);
            addStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addRowToTable(null,null,null, view,true);
                }
            });

            Button saveBtn = (Button)view.findViewById(R.id.sales_save_sale);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (allMandatoryFieldsFilled(view) && submitSale()) {
                        Toast.makeText(getActivity(), "Your Data has been successfully saved.", Toast.LENGTH_LONG).show();
                        ((BaseContainerFragment) getParentFragment()).popFragment();
                    } else {
                        Toast.makeText(getActivity(), "Unable to save data,Please ensure that all mandatory fields are entered", Toast.LENGTH_LONG).show();
                    }
                }
            });
            setRequiredFields(view);
            manageDoyouStockZincResponses(view);
            bindSalesInfoToUI(view);
            if(!isUpdate){
                prefillOrderData(view);
            }

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
            taskDao = daoSession.getTaskDao();
            saleDao = daoSession.getSaleDao();
            saleDataDao = daoSession.getSaleDataDao();
            productDao = daoSession.getProductDao();
            stokeDataDao = daoSession.getStokeDataDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void bindSalesInfoToUI(View view) {
        if (!isNewSalesCall()) {
            ((EditText) view.findViewById(R.id.sales_if_no_why)).setText(saleCallData.getIfNoWhy());
            ((GpsWidgetView) view.findViewById(R.id.sales_gps)).setLatLongText(saleCallData.getLatitude() + "," + saleCallData.getLongitude());
            ((TextView)view. findViewById(R.id.sales_district)).setText(callDataTask.getCustomer().getSubcounty().getDistrict().getName());
            ((TextView)view. findViewById(R.id.sales_subcounty)).setText(callDataTask.getCustomer().getSubcounty().getName());

            Spinner doyouStockZincSpinner = (Spinner) view.findViewById(R.id.sales_do_you_stock_zinc);
            Utils.setSpinnerSelection(doyouStockZincSpinner, (saleCallData.getDoYouStockOrsZinc() == true) ? "Yes" : "No");

            Spinner governmentApprovalSpinner = (Spinner) view.findViewById(R.id.sales_government_approval);
            Utils.setSpinnerSelection(governmentApprovalSpinner, saleCallData.getGovernmentApproval());

            Button pointOfSaleMaterial = (Button) view.findViewById(R.id.sales_point_of_sale);
            pointOfSaleMaterial.setText(saleCallData.getPointOfsaleMaterial());

            CustomMultSelectDropDown recommendationNextStep = (CustomMultSelectDropDown) view.findViewById(R.id.sales_next_step_recommendation);
            recommendationNextStep.setText(saleCallData.getRecommendationNextStep());

            bindSalesDataToUi(view);
            bindStokeDataToUi(view);
        }else{
            ((TextView)view. findViewById(R.id.sales_district)).setText(callDataTask.getCustomer().getSubcounty().getDistrict().getName());
            ((TextView)view. findViewById(R.id.sales_subcounty)).setText(callDataTask.getCustomer().getSubcounty().getName());
        }
    }

    private void bindSalesDataToUi(View view) {
        List<SaleData> salesDatas = saleCallData.getSalesDatas();
        for (int i = 0; i < salesDatas.size(); ++i) {
            if (i == 0) {
                ((EditText)view.findViewById(R.id.sales_quantity)).setText(salesDatas.get(i).getQuantity()+"");
                ((EditText)view.findViewById(R.id.sales_price)).setText(salesDatas.get(i).getPrice()+"");
                ((Spinner)view.findViewById(R.id.sales_product)).setSelection(getProductPosition(salesDatas.get(i).getProduct()));
            }else {
                addRowToTable(salesDatas.get(i),null,null, view,false);
            }
        }
    }

    private void bindStokeDataToUi(View view) {
        List<StokeData> stockDatas = saleCallData.getStockDatas();
        for (int i = 0; i < stockDatas.size(); ++i) {
            if (i == 0) {
                ((EditText)view.findViewById(R.id.sales_stock_quantity)).setText(stockDatas.get(i).getQuantity()+"");
                ((Spinner)view.findViewById(R.id.sales_stock_product)).setSelection(getProductPosition(stockDatas.get(i).getProduct()));
            }else {
                addRowToTable(null,stockDatas.get(i),null, view,true);
            }
        }
    }

    private void prefillOrderData(View view){
        List<TaskOrder> taskOrders = callDataTask.getLineItems();
        for (int i=0;i<taskOrders.size();++i){
            if (i == 0) {
                ((EditText)view.findViewById(R.id.sales_quantity)).setText(taskOrders.get(i).getQuantity()+"");
                ((Spinner)view.findViewById(R.id.sales_product)).setSelection(getProductPosition(taskOrders.get(i).getProduct()));
            }else {
                addRowToTable(null,null,taskOrders.get(i), view,false);
            }
        }
    }

    private void addRowToTable(SaleData saleData,StokeData stockData,TaskOrder taskOrder, View view,final boolean isStockTaking) {
        TableRow tableRow = new TableRow(getActivity());
        tableRow.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        final Spinner spinner = new Spinner(getActivity());
        LayoutParams spinnerParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        spinner.setLayoutParams(spinnerParams);
        spinner.setAdapter(new ProductArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,products.toArray(new Product[products.size()])));
        spinner.setBackgroundResource(R.drawable.btn_dropdown);

        tableRow.addView(spinner);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
        }else if(taskOrder != null){
            quantityView.setText(taskOrder.getQuantity());
            spinner.setSelection(getProductPosition(taskOrder.getProduct()));
        }
        Button deleteBtn = (Button) getActivity().getLayoutInflater().inflate(R.layout.delete_icon, null);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View row = (View) view.getParent();
                ViewGroup container = ((ViewGroup) row.getParent());
                container.removeView(row);
                if (isStockTaking) {
                    stockSpinnerList.remove(spinner);
                    stockQuantityFlds.remove(quantityView);
                } else {
                    spinnerList.remove(spinner);
                    quantityFields.remove(quantityView);
                    priceFields.remove(priceView);
                }
//                removeRow();
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
        }
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

    private boolean submitSale(){
        boolean isSaved = false;
        try {
            if (!isUpdate) {
                saleCallData = new Sale(null);
            }
            saleCallData.setUuid(UUID.randomUUID().toString());
            saleCallData.setDateOfSale(new Date());
            String stocksZinc = ((Spinner) getActivity().findViewById(R.id.sales_do_you_stock_zinc)).getSelectedItem().toString();
            saleCallData.setDoYouStockOrsZinc(stocksZinc.equalsIgnoreCase("Yes") ? true : false);
            saleCallData.setIfNoWhy(((EditText) getActivity().findViewById(R.id.sales_if_no_why)).getText().toString()
                    +","+((EditText)getActivity().findViewById(R.id.sales_if_no_why)).getText().toString());
            saleCallData.setPointOfsaleMaterial(((Button) getActivity().findViewById(R.id.sales_point_of_sale)).getText().toString());
            saleCallData.setRecommendationNextStep(((Button) getActivity().findViewById(R.id.sales_next_step_recommendation)).getText().toString());
            saleCallData.setGovernmentApproval(((Spinner) getActivity().findViewById(R.id.sales_government_approval)).getSelectedItem().toString());
            saleCallData.setTaskId(callDataTask.getUuid());
            saleCallData.setOrderId(callDataTask.getUuid());
            saleCallData.setOrderId(callDataTask.getUuid());
            if (!((GpsWidgetView) getActivity().findViewById(R.id.sales_gps)).getLatLongText().equals("")) {
                saleCallData.setLatitude(((GpsWidgetView) getActivity().findViewById(R.id.sales_gps)).getMlocation().getLatitude());
                saleCallData.setLongitude(((GpsWidgetView) getActivity().findViewById(R.id.sales_gps)).getMlocation().getLongitude());
            }
            if(isUpdate){
                saleDao.update(saleCallData);
                submitSaleData(saleCallData);
                submitStockData(saleCallData);
            }else {
                Long saleId = saleDao.insert(saleCallData);
                //add the different sales.
                submitSaleData(saleCallData);
                submitStockData(saleCallData);
                callDataTask.setStatus(TaskMainFragment.STATUS_COMPLETE);
                taskDao.update(callDataTask);
            }
            isSaved = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isSaved;
    }

    private void submitStockData(Sale saleCallData) {
        for (int i = 0; i < stockSpinnerList.size(); ++i) {
            try{
                StokeData stokeData = instantiateStockData(i);
                stokeData.setSaleId(stokeData.getUuid());
                stokeData.setSale(saleCallData);
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

    private void submitSaleData(Sale sale) {
        for (int i = 0; i < spinnerList.size(); ++i) {
            try{
                SaleData saleData = instantiateSaleData(i);
                saleData.setSaleId(sale.getUuid());
                saleData.setSale(sale);
                saleData.setPrice(Integer.parseInt(priceFields.get(i).getText().toString()));
                saleData.setQuantity(new Double(quantityFields.get(i).getText().toString()).intValue());
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

    private SaleData instantiateSaleData(int index){
        List<SaleData> saleDatas = saleCallData.getSalesDatas();
        SaleData saleData;
        if(index < saleDatas.size()){
            saleData = saleDatas.get(index);
        }else{
            saleData = new SaleData(null);
        }
        return saleData;
    }

    private StokeData instantiateStockData(int index){
        List<StokeData> stockDatas = saleCallData.getStockDatas();
        StokeData stokeData;
        if(index < stockDatas.size()){
            stokeData = stockDatas.get(index);
        }else{
            stokeData = new StokeData(null);
        }
        return stokeData;
    }


    private void manageDoyouStockZincResponses(View view) {
        final Spinner spinner = (Spinner) view.findViewById(R.id.sales_do_you_stock_zinc);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout ifnowhyLayout = (LinearLayout) getActivity().findViewById(R.id.sales_ifnowhy_layout);
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

    private boolean isNewSalesCall() {
        if (saleCallData == null || saleCallData.getUuid() == null) {
            return true;
        } else {
            return false;
        }
    }

    private int getProductPosition(Product product) {
        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).getUuid() == product.getUuid()) {
                return i;
            }
        }
        return 0;
    }

    private void setRequiredFields(View view) {
        Utils.setRequired((TextView) view.findViewById(R.id.sales_do_you_stock_zinc_view));
        Utils.setRequired((TextView) view.findViewById(R.id.sales_government_approval_lbl));
    }

    private boolean allMandatoryFieldsFilled(View view) {
        if (((Spinner) getActivity().findViewById(R.id.sales_do_you_stock_zinc)).getSelectedItem().toString().equals("")) {
           return false;
        }
        return true;
    }



    private void setReadOnly(View view) {
        view.findViewById(R.id.sales_gps).setEnabled(false);
        view.findViewById(R.id.sales_do_you_stock_zinc).setEnabled(false);
        view.findViewById(R.id.sales_if_no_why).setEnabled(false);
        view.findViewById(R.id.sales_stock_product).setEnabled(false);
        view.findViewById(R.id.sales_stock_quantity).setEnabled(false);
        view.findViewById(R.id.sales_stock_add_more).setEnabled(false);
        view.findViewById(R.id.sales_product).setEnabled(false);
        view.findViewById(R.id.sales_quantity).setEnabled(false);
        view.findViewById(R.id.sales_price).setEnabled(false);
        view.findViewById(R.id.sales_add_more).setEnabled(false);
        view.findViewById(R.id.sales_government_approval).setEnabled(false);
        view.findViewById(R.id.sales_point_of_sale).setEnabled(false);
        view.findViewById(R.id.sales_next_step_recommendation).setEnabled(false);
        view.findViewById(R.id.sales_save_sale).setEnabled(false);
    }

}