package org.chai.activities.tasks;

import android.content.Context;
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
import org.chai.adapter.CustomerAutocompleteAdapter;
import org.chai.adapter.ProductArrayAdapter;
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

    private List<Product> products;
    private List<Customer> customers;
    private List<OrderData> orderDataItems;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;
    private OrderDao orderDao;
    private ProductDao productDao;
    private OrderDataDao orderDataDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_NEW_ORDER;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order_activity);

        aq = new AQuery(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rows = new ArrayList<View>();
        orderDataItems = new ArrayList<OrderData>();

        rowContainer = (LinearLayout)findViewById(R.id.ln_orders_container);

        initialiseGreenDao();

        dateFormat = DateFormat.getDateInstance();
        Calendar c = new GregorianCalendar();

        products = productDao.loadAll();
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
                    aq.id(R.id.txt_customer_location).text("District: " + customer.getSubcounty().getDistrict().getName() + " | " + "Subcounty: " + customer.getSubcounty().getName());
                }
            }
        });

        dateField = aq.id(R.id.due_date).getEditText();
        dateField.setHint(dateFormat.format(new Date(c.getTimeInMillis())));
        dateField.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    showDatePicker();
                    return true;
                }
                return false;
            }
        });

        aq.id(R.id.btn_add_row).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow(null);
            }
        });

        orderId = "5452a886-5968-487e-b202-7b0eb6a6e789";//getIntent().getStringExtra("id");
        if(orderId != null){
            order = orderDao.load(orderId);
            if(order != null){
                populateFields();
            }
        }

        super.setUpDrawer(toolbar);

        setRequiredFields();
    }

    private void populateFields(){
        customer = order.getCustomer();
        date = order.getDeliveryDate();
        aq.id(R.id.customer_id).text(customer.getOutletName());
        aq.id(R.id.txt_customer_location).text("District: " + customer.getSubcounty().getDistrict().getName() + " | " + "Subcounty: " + customer.getSubcounty().getName());
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

    private void addRow(OrderData data){
        if(data == null){
            data = new OrderData();
            data.setDateCreated(new Date());
        }
        LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View row = inflator.inflate(R.layout.order_form_item_row, null);
        Spinner spinner = (Spinner)row.findViewById(R.id.item);
        spinner.setAdapter(new ProductArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, products.toArray(new Product[products.size()])));

        AQuery a = new AQuery(row);
        spinner.setSelection(((ProductArrayAdapter)spinner.getAdapter()).getPosition(data.getProduct()));
        if(data.getQuantity() != 0){
            a.id(R.id.quantity).text(Integer.toString(data.getQuantity()));
        }
        a.id(R.id.drop_sample).checked(data.getDropSample());

        final ImageView remove = (ImageView)row.findViewById(R.id.btn_remove_row);
        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rows.remove(row);
                orderDataItems.remove(rows.indexOf(row));

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
            productDao = daoSession.getProductDao();
            orderDataDao = daoSession.getOrderDataDao();
        } catch (Exception ex) {
            Log.d("Err", ex.getLocalizedMessage());
            Toast.makeText(this, "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_form_menu, menu);
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
        if(rows.size() == 0){
            Toast.makeText(this, "Add atleast one order", Toast.LENGTH_LONG).show();
            return;
        }

        //Set the data items
        Product p;
        OrderData d;

        for(View row : rows){
            AQuery a = new AQuery(row);
            p = products.get(a.id(R.id.item).getSelectedItemPosition());
            String quantity = a.id(R.id.quantity).getText().toString();
            boolean sample = a.id(R.id.drop_sample).isChecked();
            Utils.log(p.getName() + " : " + quantity + " -> " + sample);
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
            Utils.log("Inserting order");
            order = new Order(UUID.randomUUID().toString());
            order.setIsDirty(true);
            order.setCustomerId(customer.getUuid());
            order.setCustomer(customer);
            order.setDeliveryDate(date);
            order.setOrderDate(new Date());
            orderDao.insert(order);
        }else{
            Utils.log("Updating order");
            order.setIsDirty(true);
            order.setCustomerId(customer.getUuid());
            order.setCustomer(customer);
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
                Utils.log("Updating order data");
            }
        }

        Toast.makeText(this, "New order has been saved", Toast.LENGTH_LONG).show();
        finish();
    }
}
