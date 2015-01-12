package org.chai.activities.tasks;

import android.content.Intent;
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
import org.chai.activities.HomeActivity;
import org.chai.adapter.ProductArrayAdapter;
import org.chai.model.*;
import org.chai.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 10/26/14.
 */
public class CommercialFormFragment extends Fragment {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TaskDao taskDao;
    private SaleDao saleDao;
    private SaleDataDao saleDataDao;
    private ProductDao productDao;


    private TableLayout tableLayout;
    private List<Spinner> spinnerList;
    private List<EditText> quantityFields;
    private List<EditText> priceFields;
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
            tableLayout = (TableLayout)view.findViewById(R.id.sales_table);
            spinnerList = new ArrayList<Spinner>();
            quantityFields = new ArrayList<EditText>();
            priceFields = new ArrayList<EditText>();
            Bundle bundle = getArguments();
            Long callId = bundle.getLong("callId");
            if (callId != 0 && callId != null) {
                //from call list
                saleCallData = saleDao.loadDeep(callId);
                callDataTask = saleCallData.getTask();
                salesCustomer = callDataTask.getCustomer();
                isUpdate = true;
            } else {
                //from task list view
                Long taskId = bundle.getLong("taskId");
                callDataTask = taskDao.loadDeep(taskId);
                salesCustomer = callDataTask.getCustomer();
            }
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
                    addRowToTable(null, view);
                }
            });

            Button saveBtn = (Button)view.findViewById(R.id.sales_save_sale);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (allMandatoryFieldsFilled(view) && submitSale()) {
                        Toast.makeText(getActivity(), "Your Data has been successfully saved.", Toast.LENGTH_LONG).show();
                        ((BaseContainerFragment)getParentFragment()).popFragment();
                    } else {
                        Toast.makeText(getActivity(), "Unable to save data,Please ensure that all mandatory fields are entered", Toast.LENGTH_LONG).show();
                    }
                }
            });
            setRequiredFields(view);
            manageDoyouStockZincResponses(view);
            bindSalesInfoToUI(view);

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
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void bindSalesInfoToUI(View view) {
        if (!isNewSalesCall()) {
            ((EditText) view.findViewById(R.id.sales_howmany_in_stock_ors)).setText(saleCallData.getHowmanyOrsInStock() + "");
            ((EditText) view.findViewById(R.id.sales_howmany_in_stock_zinc)).setText(saleCallData.getHowManyZincInStock() + "");
            ((EditText) view.findViewById(R.id.sales_if_no_why)).setText(saleCallData.getIfNoWhy());

            Spinner doyouStockZincSpinner = (Spinner) view.findViewById(R.id.sales_do_you_stock_zinc);
            Utils.setSpinnerSelection(doyouStockZincSpinner, (saleCallData.getDoYouStockOrsZinc() == true) ? "Yes" : "No");

            Spinner governmentApprovalSpinner = (Spinner) view.findViewById(R.id.sales_government_approval);
            Utils.setSpinnerSelection(governmentApprovalSpinner, saleCallData.getGovernmentApproval());

            Spinner pointOfSaleMaterial = (Spinner) view.findViewById(R.id.sales_point_of_sale);
            Utils.setSpinnerSelection(pointOfSaleMaterial, saleCallData.getPointOfsaleMaterial());

            Spinner recommendationNextStep = (Spinner) view.findViewById(R.id.sales_next_step_recommendation);
            Utils.setSpinnerSelection(recommendationNextStep, saleCallData.getRecommendationNextStep());

            Spinner recommendationNextLevel = (Spinner) view.findViewById(R.id.sales_recommendation_level);
            Utils.setSpinnerSelection(recommendationNextLevel, saleCallData.getRecommendationLevel());
            bindSalesDataToUi(view);
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
                addRowToTable(salesDatas.get(i), view);
            }
        }
    }

    private void addRowToTable(SaleData saleData, View view) {
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
        tableRow.addView(priceView);

        if (saleData != null) {
            quantityView.setText(saleData.getQuantity() + "");
            priceView.setText(saleData.getPrice() + "");
            spinner.setSelection(getProductPosition(saleData.getProduct()));
        }
        Button deleteBtn = (Button) getActivity().getLayoutInflater().inflate(R.layout.delete_icon, null);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View row = (View) view.getParent();
                ViewGroup container = ((ViewGroup)row.getParent());
                container.removeView(row);
               /* spinnerList.remove(spinner);
                quantityFields.remove(quantityView);
                priceFields.remove(priceView);*/
                removeRow();
                container.invalidate();
            }
        });
        tableRow.addView(deleteBtn);

        spinnerList.add(spinner);
        quantityFields.add(quantityView);
        priceFields.add(priceView);

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
            if (((Spinner) getActivity().findViewById(R.id.sales_do_you_stock_zinc)).getSelectedItem().toString().equals("Yes")) {
                saleCallData.setHowmanyOrsInStock(Integer.parseInt(((EditText) getActivity().findViewById(R.id.sales_howmany_in_stock_ors)).getText().toString()));
                saleCallData.setHowManyZincInStock(Integer.parseInt(((EditText) getActivity().findViewById(R.id.sales_howmany_in_stock_zinc)).getText().toString()));
            }
            saleCallData.setIfNoWhy(((EditText) getActivity().findViewById(R.id.sales_if_no_why)).getText().toString());
            saleCallData.setPointOfsaleMaterial(((Spinner) getActivity().findViewById(R.id.sales_point_of_sale)).getSelectedItem().toString());
            saleCallData.setRecommendationNextStep(((Spinner) getActivity().findViewById(R.id.sales_next_step_recommendation)).getSelectedItem().toString());
            saleCallData.setRecommendationLevel(((Spinner) getActivity().findViewById(R.id.sales_recommendation_level)).getSelectedItem().toString());
            saleCallData.setGovernmentApproval(((Spinner) getActivity().findViewById(R.id.sales_government_approval)).getSelectedItem().toString());
            saleCallData.setTaskId(callDataTask.getId());
            saleCallData.setOrderRefid(callDataTask.getId());
            saleCallData.setOrderId(callDataTask.getUuid());
            if(isUpdate){
                saleDao.update(saleCallData);
                submitSaleData(saleCallData.getId());
            }else {
                Long saleId = saleDao.insert(saleCallData);
                //add the different sales.
                submitSaleData(saleId);
                callDataTask.setStatus(TaskMainFragment.STATUS_COMPLETE);
                taskDao.update(callDataTask);
            }
            isSaved = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isSaved;
    }

    private void submitSaleData(Long saleId) {
        for (int i = 0; i < spinnerList.size(); ++i) {
            SaleData saleData = instantiateSaleData(i);
            saleData.setUuid(UUID.randomUUID().toString());
            saleData.setSaleId(saleId);
            saleData.setPrice(Integer.parseInt(priceFields.get(i).getText().toString()));
            saleData.setQuantity(Integer.parseInt(quantityFields.get(i).getText().toString()));
            Product product = (Product) spinnerList.get(i).getSelectedItem();
            saleData.setProductRefId(product.getId());
            saleData.setProductId(product.getUuid());

            if(saleData.getId()!=null){
                saleDataDao.update(saleData);
            }else{
                saleDataDao.insert(saleData);
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

    private boolean isNewSalesCall() {
        if (saleCallData == null || saleCallData.getId() == null) {
            return true;
        } else {
            return false;
        }
    }

    private int getProductPosition(Product product) {
        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).getId() == product.getId()) {
                return i;
            }
        }
        return 0;
    }

    private void setRequiredFields(View view) {
        Utils.setRequired((TextView) view.findViewById(R.id.sales_do_you_stock_zinc_view));
        Utils.setRequired((TextView) view.findViewById(R.id.sales_government_approval_lbl));
        Utils.setRequired((TextView) view.findViewById(R.id.sales_howmany_in_stock_ors_view));
        Utils.setRequired((TextView) view.findViewById(R.id.sales_howmany_in_stock_zinc_view));
    }

    private boolean allMandatoryFieldsFilled(View view) {
        if (((Spinner) getActivity().findViewById(R.id.sales_do_you_stock_zinc)).getSelectedItem().toString().equals("")) {
            if(((EditText)getActivity().findViewById(R.id.sales_howmany_in_stock_zinc)).getText().toString().equalsIgnoreCase("")){
                return false;
            }
            if(((EditText)getActivity().findViewById(R.id.sales_howmany_in_stock_ors)).getText().toString().equalsIgnoreCase("")){
                return false;
            }
        }
        return true;
    }

}