package org.chai.activities.tasks;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.BaseActivity;
import org.chai.activities.calls.HistoryActivity;
import org.chai.adapter.CustomerAutocompleteAdapter;
import org.chai.adapter.ProductArrayAdapter;
import org.chai.adapter.ProductGroupAdapter;
import org.chai.model.Customer;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.Order;
import org.chai.model.OrderDao;
import org.chai.model.OrderData;
import org.chai.model.OrderDataDao;
import org.chai.model.Product;
import org.chai.model.ProductDao;
import org.chai.model.ProductGroup;
import org.chai.model.ProductGroupDao;
import org.chai.model.User;
import org.chai.rest.RestClient;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.customwidget.CustomDatePicker;
import org.chai.util.migration.UpgradeOpenHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Zed on 4/23/2015.
 */
public class NewOrderActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;
    View view;
    LinearLayout rowContainer;
    EditText dateField;
    DateFormat dateFormat;
    ArrayList<View> rows;
    String dateString;
    long dateMillis;
    Date date;
    String orderId;
    Customer customer;
    Order order;

    private List<Customer> customers;
    private List<OrderData> orderDataItems;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;
    private OrderDao orderDao;
    private ProductGroupDao productGroupDao;
    private ProductDao productDao;
    private OrderDataDao orderDataDao;

    private boolean isFromHistory;

    private List<Product> allProducts;
    private List<ProductGroup> groups;

    private ArrayList<String> brands;
    private ArrayList<String> sizes;
    private ArrayList<String> formulations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_NEW_ORDER;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order_activity);

        aq = new AQuery(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rows = new ArrayList<>();
        orderDataItems = new ArrayList<>();

        rowContainer = (LinearLayout)findViewById(R.id.ln_orders_container);

        initialiseGreenDao();

        dateFormat = DateFormat.getDateInstance();
        Calendar c = new GregorianCalendar();

        allProducts = productDao.queryBuilder().orderAsc(ProductDao.Properties.Name).list();
        groups = productGroupDao.queryBuilder().orderAsc(ProductGroupDao.Properties.Name).list();

        ArrayList<ProductGroup> filteredGroups = new ArrayList<>();
        for(ProductGroup group: groups){
            if(group.getProducts().size() > 0){
                filteredGroups.add(group);
            }
        }

        groups = filteredGroups;

        ArrayList<Product> filtered = new ArrayList<>();
        for(Product p: allProducts){
            if(!p.getName().contains("Deleted Product")){
                filtered.add(p);
            }
        }

        allProducts = filtered;

        dateField = aq.id(R.id.due_date).getEditText();
        dateField.setHint(dateFormat.format(new Date(c.getTimeInMillis())));
        dateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showDatePicker();
                    return true;
                }
                return false;
            }
        });

        aq.id(R.id.btn_add_row).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow(new OrderData());
            }
        });

        orderId = getIntent().getStringExtra("order_id");
        isFromHistory = getIntent().getBooleanExtra("is_from_history", false);

        if(orderId != null){
            order = orderDao.load(orderId);
            if(order != null){
                populateFields();
            }
        }

        if(order == null){
            customers = customerDao.loadAll();

            AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.customer_id);
            CustomerAutocompleteAdapter adapter = new CustomerAutocompleteAdapter(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<Customer>(customers));
            textView.setAdapter(adapter);

            textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view1, int position, long l) {
                    Customer selected = (Customer)adapterView.getAdapter().getItem(position);
                    customer = selected;
                    if(customer != null){
                        try{
                            aq.id(R.id.txt_customer_location).text("District: " + customer.getSubcounty().getDistrict().getName() + " | " + "Subcounty: " + customer.getSubcounty().getName());
                        }catch (Exception ex){
                            aq.id(R.id.txt_customer_location).text("Failed to load District & Subcounty");
                        }
                    }
                }
            });
        }

        super.setUpDrawer(toolbar);

        setRequiredFields();
    }

    private void populateFields(){
        customer = order.getCustomer();
        date = order.getDeliveryDate();
        aq.id(R.id.customer_id).text(customer.getOutletName()).enabled(false);
        try{
            aq.id(R.id.txt_customer_location).text("District: " + customer.getSubcounty().getDistrict().getName() + " | " + "Subcounty: " + customer.getSubcounty().getName());
        }catch (Exception ex){
            aq.id(R.id.txt_customer_location).text("Failed to load District & Subcounty");
        }
        aq.id(R.id.due_date).text(dateFormat.format(date));

        for(OrderData data : order.getOrderDatas()){
            addRow(data);
        }
    }

    private void showDatePicker(){
        DialogFragment datepicker = new CustomDatePicker(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar c = new GregorianCalendar(year, month, day);
                dateString = c.get(Calendar.DAY_OF_MONTH) + "," + c.get(Calendar.MONTH) + "," + c.get(Calendar.YEAR);
                dateMillis = c.getTimeInMillis();
                date = new Date(dateMillis);

                aq.id(R.id.due_date).text(dateFormat.format(date));
            }
        };
        datepicker.show(getSupportFragmentManager(), "date");
    }

    private void addRow(final OrderData data){
        if(data.getUuid() == null){
            data.setDateCreated(new Date());
        }

        LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View row = inflator.inflate(R.layout.order_form_item_row, null);
        final Spinner spinner = (Spinner)row.findViewById(R.id.item);
        spinner.setAdapter(new ProductArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, allProducts.toArray(new Product[allProducts.size()])));

        AQuery a = new AQuery(row);

        final Spinner group = (Spinner)row.findViewById(R.id.group);
        final Spinner brand = (Spinner)row.findViewById(R.id.brand);
        final Spinner size = (Spinner)row.findViewById(R.id.size);
        final Spinner formulation = (Spinner)row.findViewById(R.id.formulation);

        group.setAdapter(new ProductGroupAdapter(this, android.R.layout.simple_spinner_dropdown_item, groups.toArray(new ProductGroup[groups.size()])));
        group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(group.getTag() != null || data.getProductId() == null){
                    ProductGroup g = ((ProductGroupAdapter) group.getAdapter()).getItem(position);
                    setBrands(brand, g);
                }
                group.setTag(1);
                ((TextView)group.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (brand.getTag() != null || data.getProductId() == null) {
                    ProductGroup g = (ProductGroup) group.getSelectedItem();
                    String selectedBrand = parent.getAdapter().getItem(position).toString();
                    setSizes(size, selectedBrand, g);
                }
                brand.setTag(1);
                ((TextView)brand.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (size.getTag() != null || data.getProductId() == null) {
                    ProductGroup g = (ProductGroup) group.getSelectedItem();
                    String selectedBrand = brand.getSelectedItem().toString();
                    String selectedSize = parent.getAdapter().getItem(position).toString();
                    setFormulations(formulation, selectedBrand, selectedSize, g);
                }
                size.setTag(1);
                ((TextView)size.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        formulation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (formulation.getTag() != null || data.getProductId() == null) {
                    formulations = new ArrayList<>();
                    ProductGroup g = (ProductGroup) group.getSelectedItem();
                    List<Product> products = g.getProducts();
                    String selectedBrand = "";
                    try {
                        selectedBrand = brand.getSelectedItem().toString();
                    } catch (Exception ex) {

                    }
                    String selectedSize = "";
                    try {
                        selectedSize = size.getSelectedItem().toString();
                    } catch (Exception ex) {

                    }
                    String selectedFormulation = "";
                    try {
                        selectedFormulation = parent.getAdapter().getItem(position).toString();
                    } catch (Exception ex) {

                    }
                    for (Product p : products) {
                        if (p.getFormulation() != null && p.getUnitOfMeasure() != null && p.getFormulation() != null) {
                            if (p.getName().equalsIgnoreCase(selectedBrand) && p.getUnitOfMeasure().equalsIgnoreCase(selectedSize) && selectedFormulation.equalsIgnoreCase(p.getFormulation())) {
                                int index = 0;
                                for (Product pp : allProducts) {
                                    if (pp.getUuid().equalsIgnoreCase(p.getUuid())) {
                                        index = allProducts.indexOf(p);
                                        break;
                                    }
                                }

                                spinner.setSelection(index);
                                break;
                            }
                        }
                    }
                }

                formulation.setTag(1);
                ((TextView)formulation.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(data.getUuid() != null){
            try{
                spinner.setSelection(((ProductArrayAdapter)spinner.getAdapter()).getPosition(data.getProduct()));
                a.id(R.id.drop_sample).checked(data.getDropSample());
                if(data.getQuantity() != 0){
                    a.id(R.id.quantity).text(Integer.toString(data.getQuantity()));
                }

                ProductGroup g = data.getProduct().getProductGroup();

                for(int i = 0; i < group.getAdapter().getCount(); i++){
                    String s = ((ProductGroup)group.getAdapter().getItem(i)).getName();
                    if(s.equalsIgnoreCase(data.getProduct().getProductGroup().getName())){
                        group.setSelection(i);
                        break;
                    }
                }

                setBrands(brand, g);
                setSizes(size, data.getProduct().getName(), g);
                setFormulations(formulation, data.getProduct().getName(), data.getProduct().getUnitOfMeasure(), g);

                for(int i = 0; i < brand.getAdapter().getCount(); i++){
                    String s = brand.getAdapter().getItem(i).toString();
                    if(s.equalsIgnoreCase(data.getProduct().getName())){
                        brand.setSelection(i);
                        break;
                    }
                }

                for(int i = 0; i < size.getAdapter().getCount(); i++){
                    String s = size.getAdapter().getItem(i).toString();
                    if(s.equalsIgnoreCase(data.getProduct().getUnitOfMeasure())){
                        size.setSelection(i);
                        break;
                    }
                }

                for(int i = 0; i < formulation.getAdapter().getCount(); i++){
                    String s = formulation.getAdapter().getItem(i).toString();
                    if(s.equalsIgnoreCase(data.getProduct().getFormulation())){
                        formulation.setSelection(i);
                        break;
                    }
                }
            }catch (Exception ex){
                Utils.log("Error populating order row");
            }
        }

        final ImageView remove = (ImageView)row.findViewById(R.id.btn_remove_row);
        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rows.remove(row);
                orderDataItems.remove(data);

                LinearLayout parent = (LinearLayout)v.getParent();
                LinearLayout root = (LinearLayout)parent.getParent();
                LinearLayout top = (LinearLayout)root.getParent();
                rowContainer.removeView(top);
            }
        });

        rowContainer.addView(row);
        rows.add(row);
        orderDataItems.add(data);
    }

    private void setRequiredFields(){
        view = getWindow().getDecorView().findViewById(android.R.id.content);
        List<View> required = Utils.getViewsByTag((ViewGroup)view, "required");
        for(View v : required){
            try{
                Utils.setRequired((TextView)v);
            }catch(Exception ex){
                Utils.log("Error setting view by tag -> " + ex.getMessage());
            }
        }
    }

    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
            orderDao = daoSession.getOrderDao();
            productGroupDao = daoSession.getProductGroupDao();
            productDao = daoSession.getProductDao();
            orderDataDao = daoSession.getOrderDataDao();
        } catch (Exception ex) {
            Log.d("Err", ex.getLocalizedMessage());
            Toast.makeText(this, "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if(!isFromHistory){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.save_form_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getItemId() == R.id.action_save){
            saveOrder();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveOrder(){
        if(customer == null){
            Toast.makeText(this, "Please select customer", Toast.LENGTH_LONG).show();
            return;
        }
        if(date == null){
            Toast.makeText(this, "Select the due date", Toast.LENGTH_LONG).show();
            return;
        }
        if(RestClient.getRole() == User.ROLE_DETAILER){
            Date d = Utils.addToDateOffset(new Date(), 7); //One week from now
            if(date.compareTo(d) < 0){
                Toast.makeText(this, "Due date cannot be in less than a week", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(date.compareTo(new Date()) < 0){
            Toast.makeText(this, "Due date cannot be in the past", Toast.LENGTH_LONG).show();
            return;
        }
        if(rows.size() == 0){
            Toast.makeText(this, "Add atleast one order", Toast.LENGTH_LONG).show();
            return;
        }

        //Set the data items
        Product p;
        OrderData d;

        for(View row : rows){
            AQuery a = new AQuery(row);
            p = allProducts.get(a.id(R.id.item).getSelectedItemPosition());
            String quantity = a.id(R.id.quantity).getText().toString();
            boolean sample = a.id(R.id.drop_sample).isChecked();
            if(quantity.isEmpty()){
                Toast.makeText(this, "Please select quantity for " + p.getName(), Toast.LENGTH_LONG).show();
                return;
            }

            d = orderDataItems.get(rows.indexOf(row));
            d.setIsDirty(true);
            d.setDropSample(sample);
            d.setProductId(p.getUuid());
            d.setProduct(p);
            d.setQuantity(Integer.parseInt(quantity));
        }

        if(order == null){
            order = new Order(UUID.randomUUID().toString());
            order.setIsDirty(true);
            order.setCustomerId(customer.getUuid());
            order.setCustomer(customer);
            order.setDeliveryDate(date);
            order.setOrderDate(new Date());
            orderDao.insert(order);
        }else{
            order.setIsDirty(true);
            order.setDeliveryDate(date);
            orderDao.update(order);
        }

        for(OrderData data : orderDataItems){
            data.setIsDirty(true);
            data.setLastUpdated(new Date());
            if(data.getUuid() == null){
                data.setUuid(UUID.randomUUID().toString());
                data.setOrderId(order.getUuid());
                data.setOrder(order);
                data.setDateCreated(new Date());
                orderDataDao.insert(data);
            }else{
                orderDataDao.update(data);
            }
        }

        Toast.makeText(this, "New order has been saved", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, HistoryActivity.class);
        i.putExtra("tab", RestClient.getRole().equalsIgnoreCase(User.ROLE_DETAILER) ? 2 : 2);
        startActivity(i);
    }

    private void setBrands(Spinner spinner, ProductGroup group){
        List<Product> products = group.getProducts();
        brands = new ArrayList<>();
        for (Product p : products) {
            if (!brands.contains(p.getName()) && !p.getName().contains("Deleted Product")) {
                brands.add(p.getName());
            }
        }
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, brands.toArray(new String[brands.size()])));
    }

    private void setSizes(Spinner spinner, String brand, ProductGroup group){
        List<Product> products = group.getProducts();
        sizes = new ArrayList<>();
        for(Product p: products){
            if(p.getUnitOfMeasure() != null){
                if(p.getName().equalsIgnoreCase(brand) && !sizes.contains(p.getUnitOfMeasure())){
                    sizes.add(p.getUnitOfMeasure());
                }
            }
        }
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sizes.toArray(new String[sizes.size()])));
    }

    private void setFormulations(Spinner spinner, String brand, String size, ProductGroup group){
        List<Product> products = group.getProducts();
        formulations = new ArrayList<>();
        for(Product p: products){
            if(p.getFormulation() != null && p.getUnitOfMeasure() != null){
                if(p.getName().equalsIgnoreCase(brand) && p.getUnitOfMeasure().equalsIgnoreCase(size) && !formulations.contains(p.getFormulation())){
                    formulations.add(p.getFormulation());
                }
            }
        }
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, formulations.toArray(new String[formulations.size()])));
    }
}
