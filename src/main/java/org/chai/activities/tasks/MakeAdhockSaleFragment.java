package org.chai.activities.tasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import org.chai.util.CustomMultSelectDropDown;
import org.chai.util.GPSTracker;
import org.chai.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 1/7/15.
 */
public class MakeAdhockSaleFragment extends BaseContainerFragment {
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
    private AdhockSale saleInstance;
    private Customer salesCustomer;
    private List<Product> products;
    private boolean isUpdate = false;

    private GPSTracker gpsTracker;
    private double capturedLatitude;
    private double capturedLongitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.adhock_sale_form,container, false);
        try {
            initialiseGreenDao();
            tableLayout = (TableLayout)view.findViewById(R.id.adhock_sale_table);
            spinnerList = new ArrayList<Spinner>();
            quantityFields = new ArrayList<EditText>();
            priceFields = new ArrayList<EditText>();

            Spinner productSpinner = (Spinner) view.findViewById(R.id.adhock_sale_product);
            products = productDao.loadAll();
            productSpinner.setAdapter(new ProductArrayAdapter(getActivity(),R.id.adhock_sale_product,products.toArray(new Product[products.size()])));
            spinnerList.add(productSpinner);
            quantityFields.add((EditText)view.findViewById(R.id.adhock_sale_quantity));
            priceFields.add((EditText)view.findViewById(R.id.adhock_sale_price));

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

            CustomMultSelectDropDown pointOfSaleMaterials = (CustomMultSelectDropDown)view.findViewById(R.id.adhock_sale_point_of_sale);
            pointOfSaleMaterials.setStringOptions(getResources().getStringArray(R.array.point_of_sale_material));

            CustomMultSelectDropDown recommendationNextStep = (CustomMultSelectDropDown)view.findViewById(R.id.adhock_sale_next_step_recommendation);
            recommendationNextStep.setStringOptions(getResources().getStringArray(R.array.recommendation_nextstep));

            Button addButton = (Button)view.findViewById(R.id.adhock_sale_add_more);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addRowToTable(null, view);
                }
            });

            Button saveBtn = (Button)view.findViewById(R.id.adhock_sale_save_sale);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        submitSale();
                        Intent i = new Intent(getActivity(), HomeActivity.class);
                        startActivity(i);
                    }catch (Exception ex){
                        Toast.makeText(getActivity(),"A problem Occured while saving a new Sale,please ensure that data is entered correctly",Toast.LENGTH_LONG).show();
                    }
                }
            });
            manageDoyouStockZincResponses(view);
            setRequiredFields(view);
            setGpsWidget(view);

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getActivity(), "Error in oncreate view:" + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String saleId = bundle.getString("saleId");
            bindSalesInfoToUI(saleId, view);
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

    private void bindSalesInfoToUI(String saleId,View view) {
        saleInstance = saleDao.load(saleId);
        if (saleInstance!=null) {
            ((AutoCompleteTextView) view.findViewById(R.id.adhock_sale_customer)).setText(saleInstance.getCustomer().getOutletName());
            salesCustomer = saleInstance.getCustomer();
            ((EditText) view.findViewById(R.id.adhock_sale_howmany_in_stock_ors)).setText(saleInstance.getHowManyOrsInStock() + "");
            ((EditText) view.findViewById(R.id.adhock_sale_howmany_in_stock_zinc)).setText(saleInstance.getHowManyZincInStock() + "");
            ((EditText) view.findViewById(R.id.adhock_sale_if_no_why)).setText(saleInstance.getIfNoWhy());

            Spinner doyouStockZincSpinner = (Spinner) view.findViewById(R.id.adhock_sale_do_you_stock_zinc);
            Utils.setSpinnerSelection(doyouStockZincSpinner, (saleInstance.getDoYouStockOrsZinc() == true) ? "Yes" : "No");

            Spinner governmentApprovalSpinner = (Spinner) view.findViewById(R.id.adhock_sale_government_approval);
            Utils.setSpinnerSelection(governmentApprovalSpinner, saleInstance.getGovernmentApproval());

            Button pointOfSaleMaterial = (Button) view.findViewById(R.id.adhock_sale_point_of_sale);
           pointOfSaleMaterial.setText(saleInstance.getPointOfsaleMaterial());


            CustomMultSelectDropDown recommendationNextStep = (CustomMultSelectDropDown) view.findViewById(R.id.adhock_sale_next_step_recommendation);
            recommendationNextStep.setText(saleInstance.getRecommendationNextStep());

            bindSalesDataToUi(view);
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
                addRowToTable(salesDatas.get(i), view);
            }
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
        Button deleteBtn = (Button) getActivity().getLayoutInflater().inflate(R.layout.delete_icon, null);
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
        final Spinner spinner = (Spinner) view.findViewById(R.id.adhock_sale_do_you_stock_zinc);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout stockfieldsLayout = (LinearLayout) getActivity().findViewById(R.id.adhock_sale_zinc_stock_layout);
                LinearLayout ifnowhyLayout = (LinearLayout) getActivity().findViewById(R.id.adhock_sale_ifnowhy_layout);
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
            if (products.get(i).getUuid() == product.getUuid()) {
                return i;
            }
        }
        return 0;
    }

    private void submitSale(){
        if(!isUpdate){
         saleInstance = new AdhockSale(UUID.randomUUID().toString());
        }
        saleInstance.setDateOfSale(new Date());
        String stocksZinc = ((Spinner) getActivity().findViewById(R.id.adhock_sale_do_you_stock_zinc)).getSelectedItem().toString();
        saleInstance.setDoYouStockOrsZinc(stocksZinc.equalsIgnoreCase("Yes") ? true : false);
        if (stocksZinc.equals("Yes")) {
            saleInstance.setHowManyOrsInStock(Integer.parseInt(((EditText) getActivity().findViewById(R.id.adhock_sale_howmany_in_stock_ors)).getText().toString()));
            saleInstance.setHowManyZincInStock(Integer.parseInt(((EditText) getActivity().findViewById(R.id.adhock_sale_howmany_in_stock_zinc)).getText().toString()));
        }else{
            saleInstance.setIfNoWhy(((EditText) getActivity().findViewById(R.id.adhock_sale_if_no_why)).getText().toString());
        }
        saleInstance.setPointOfsaleMaterial(((Button) getActivity().findViewById(R.id.adhock_sale_point_of_sale)).getText().toString()
                +","+((EditText)getActivity().findViewById(R.id.adhoc_point_of_sale_others)).getText().toString());
        saleInstance.setRecommendationNextStep(((CustomMultSelectDropDown) getActivity().findViewById(R.id.adhock_sale_next_step_recommendation)).getText().toString());
        saleInstance.setGovernmentApproval(((Spinner) getActivity().findViewById(R.id.adhock_sale_government_approval)).getSelectedItem().toString());
        saleInstance.setCustomerId(salesCustomer.getUuid());
        saleInstance.setLatitude(capturedLatitude);
        saleInstance.setLongitude(capturedLongitude);

        if(isUpdate){
            saleDao.update(saleInstance);
            submitSaleData(saleInstance.getUuid());
        }else{
            Long saleId = saleDao.insert(saleInstance);
            //add the different sales.
            submitSaleData(saleInstance.getUuid());
        }
    }

    private void submitSaleData(String saleId) {
        for (int i = 0; i < spinnerList.size(); ++i) {
            try {
                SaleData saleData = instantiateSaleData(i);
                saleData.setUuid(UUID.randomUUID().toString());
                saleData.setAdhockSaleId(saleId);
                saleData.setPrice(Integer.parseInt(priceFields.get(i).getText().toString()));
                saleData.setQuantity(Integer.parseInt(quantityFields.get(i).getText().toString()));
                Product product = (Product) spinnerList.get(i).getSelectedItem();
                saleData.setProductId(product.getUuid());
                if(saleData.getUuid()!=null){
                    saleDataDao.update(saleData);
                }else{
                    saleDataDao.insert(saleData);
                }
            }catch (Exception ex){
                //ignore
            }
        }
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
        Utils.setRequired((TextView) view.findViewById(R.id.adhock_sale_howmany_in_stock_zinc_view));
        Utils.setRequired((TextView) view.findViewById(R.id.adhock_sale_howmany_in_stock_ors_view));
        Utils.setRequired((TextView) view.findViewById(R.id.adhock_sale_customer_lbl));
    }

    private void setGpsWidget(final View view1) {
        Button showGps = (Button)view1.findViewById(R.id.adhok_sales_capture_gps);
        showGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpsTracker = new GPSTracker(getActivity());
                if (gpsTracker.canGetLocation()) {
                    capturedLatitude = gpsTracker.getLatitude();
                    capturedLongitude = gpsTracker.getLongitude();
                    EditText detailsGps = (EditText)view1.findViewById(R.id.adhoc_sales_gps);
                    detailsGps.setText(capturedLatitude + "," + capturedLongitude);
                } else {
                    gpsTracker.showSettingsAlert();
                }
            }
        });
    }



}
