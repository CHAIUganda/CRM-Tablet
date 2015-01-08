package org.chai.activities.tasks;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import org.chai.util.Utils;

import java.util.*;

/**
 * Created by victor on 12/22/14.
 */
public class TakeOrderFragment extends BaseContainerFragment {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;
    private OrderDao orderDao;
    private ProductDao productDao;
    private OrderDataDao orderDataDao;

    private TableLayout tableLayout;
    private List<Spinner> spinnerList;
    private List<EditText> quantityFields;

    private EditText dateEditTxt;
    private DatePickerDialog datePickerDialog;
    private String initialDate;
    private String initialMonth;
    private String initialYear;
    private List<Product> products;
    private Customer selectedCustomer;
    private boolean isUpdate = false;
    private Order orderInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.order_form,container,false);
        initialiseGreenDao();

        tableLayout = (TableLayout)view.findViewById(R.id.orders_table);
        spinnerList = new ArrayList<Spinner>();
        quantityFields = new ArrayList<EditText>();

        Spinner productsSpinner = (Spinner) view.findViewById(R.id.order_product);
        products = productDao.loadAll();
        productsSpinner.setAdapter(new ProductArrayAdapter(getActivity(),R.id.sales_product,products.toArray(new Product[products.size()])));

        spinnerList.add(productsSpinner);
        quantityFields.add((EditText)view. findViewById(R.id.order_quantity));

        List<Customer> customersList = customerDao.loadAll();
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.order_auto_complete_textview);
        ArrayAdapter<Customer> adapter = new ArrayAdapter<Customer>(getActivity(),android.R.layout.simple_dropdown_item_1line,customersList);
        textView.setAdapter(adapter);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Customer selected = (Customer)adapterView.getAdapter().getItem(position);
                selectedCustomer = selected;
            }
        });

        Button addButton = (Button)view.findViewById(R.id.order_add_more);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRowToTable(null, view);
            }
        });

        Button dateBtn = (Button)view.findViewById(R.id.order_delivery_date_btn);
        dateEditTxt = (EditText)view.findViewById(R.id.order_delivery_date);

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

        Button saveBtn = (Button)view.findViewById(R.id.order_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrder();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            Long orderId = bundle.getLong("orderId");
            bindOrderToUi(orderId, view);
        }
        return view;
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
            orderDao = daoSession.getOrderDao();
            productDao = daoSession.getProductDao();
            orderDataDao = daoSession.getOrderDataDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void bindOrderToUi(Long orderId, View view) {
        orderInstance = orderDao.loadDeep(orderId);
        if (orderInstance != null) {
            ((AutoCompleteTextView) view.findViewById(R.id.order_auto_complete_textview)).setText(orderInstance.getCustomer().getOutletName());
            selectedCustomer = orderInstance.getCustomer();
            dateEditTxt.setText(Utils.dateToString(orderInstance.getOrderDate()));
            bindOrderDataToUi(view, orderInstance);
        }

    }

    private void bindOrderDataToUi(View view, Order order) {
        List<OrderData> orderDatas = order.getOrderDatas();
        for (int i = 0; i < orderDatas.size(); ++i) {
            if (i == 0) {
                ((EditText) view.findViewById(R.id.order_quantity)).setText(orderDatas.get(i).getQuantity() + "");
                ((Spinner) view.findViewById(R.id.order_product)).setSelection(getProductPosition(orderDatas.get(i).getProduct()));
            } else {
                addRowToTable(orderDatas.get(i), view);
            }
        }
    }

    private void addRowToTable(OrderData orderData, View view) {
        TableRow tableRow = new TableRow(getActivity());
        tableRow.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        Spinner spinner  = new Spinner(getActivity());
        TableRow.LayoutParams spinnerParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        spinner.setLayoutParams(spinnerParams);
        spinner.setAdapter(new ProductArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,products.toArray(new Product[products.size()])));
        spinner.setBackgroundResource(R.drawable.btn_dropdown);

        tableRow.addView(spinner);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,0,10,0);
        EditText quantityView =(EditText)getActivity().getLayoutInflater().inflate(R.layout.edit_text_style,null);
        quantityView.setTextColor(Color.BLACK);
        quantityView.setLayoutParams(params);
        quantityView.setInputType(InputType.TYPE_CLASS_NUMBER);
        tableRow.addView(quantityView);
        if (orderData != null) {
            quantityView.setText(orderData.getQuantity() + "");
            spinner.setSelection(getProductPosition(orderData.getProduct()));
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
        if (!isUpdate) {
            orderInstance = new Order(null);
        }
        orderInstance.setClientRefId(UUID.randomUUID().toString());
        orderInstance.setCustomerId(selectedCustomer.getUuid());
        orderInstance.setCustomerRefId(selectedCustomer.getId());
        orderInstance.setDeliveryDate(Utils.stringToDate(((EditText) getActivity().findViewById(R.id.order_delivery_date)).getText().toString()));
        orderInstance.setOrderDate(new Date());
        if (isUpdate) {
            orderDao.update(orderInstance);
            submitOrderData(orderInstance.getId());
        } else {
            Long orderId = orderDao.insert(orderInstance);
            orderInstance.setId(orderId);
            submitOrderData(orderId);
        }
    }

    private void submitOrderData(Long orderId){
        for (int i = 0; i < spinnerList.size(); ++i) {
            OrderData orderData = instantiateOrder(i);
            orderData.setOrderId(orderId);
            orderData.setUuid(UUID.randomUUID().toString());
            orderData.setQuantity(Integer.parseInt(quantityFields.get(i).getText().toString()));
            Product product = (Product) spinnerList.get(i).getSelectedItem();
            orderData.setProductId(product.getUuid());
            orderData.setProductRefId(product.getId());
            if(orderData.getId()!=null){
                orderDataDao.update(orderData);
            }else{
                orderDataDao.insert(orderData);
            }
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

    private OrderData instantiateOrder(int index){
        List<OrderData> orderDatas = orderInstance.getOrderDatas();
        OrderData orderData;
        if(index < orderDatas.size()){
            orderData = orderDatas.get(index);
        }else{
            orderData = new OrderData(null);
        }
        return orderData;
    }

}