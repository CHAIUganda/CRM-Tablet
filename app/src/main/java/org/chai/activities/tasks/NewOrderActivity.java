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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.BaseActivity;
import org.chai.adapter.ProductArrayAdapter;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.OrderDao;
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
    private List<Product> products;

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
        rowContainer = (LinearLayout)findViewById(R.id.ln_orders_container);

        initialiseGreenDao();
        products = productDao.loadAll();
        Utils.log("Loaded products -> " + products.size());

        dateFormat = DateFormat.getDateInstance();
        Calendar c = new GregorianCalendar();

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
                addRow();
            }
        });

        super.setUpDrawer(toolbar);
    }

    private void showDatePicker(){
        DialogFragment datepicker = new CustomDatePicker(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar c = new GregorianCalendar(year, month, day);
                dateString = c.get(Calendar.DAY_OF_MONTH) + "," + c.get(Calendar.MONTH) + "," + c.get(Calendar.YEAR);
                dateMillis = c.getTimeInMillis();
                Date date = new Date(dateMillis);

                aq.id(R.id.due_date).text(dateFormat.format(date));
            }
        };
        datepicker.show(getSupportFragmentManager(), "date");
    }

    private void addRow(){
        LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View row = inflator.inflate(R.layout.order_form_item_row, null);
        Spinner spinner = (Spinner)row.findViewById(R.id.item);
        spinner.setAdapter(new ProductArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, products.toArray(new Product[products.size()])));

        final ImageView remove = (ImageView)row.findViewById(R.id.btn_remove_row);
        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rows.remove(row);
                LinearLayout parent = (LinearLayout)v.getParent();
                LinearLayout root = (LinearLayout)parent.getParent();
                LinearLayout top = (LinearLayout)root.getParent();
                rowContainer.removeView(top);
            }
        });

        rowContainer.addView(row);
        rows.add(row);
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
        String customeruuid = aq.id(R.id.customer_id).getText().toString();
        if(customeruuid.isEmpty()){
            Toast.makeText(this, "Please select customer", Toast.LENGTH_LONG).show();
            return;
        }
        if(dateString == null || dateString.isEmpty()){
            Toast.makeText(this, "Select the due date", Toast.LENGTH_LONG).show();
            return;
        }
        if(rows.size() == 0){
            Toast.makeText(this, "Add atleast one order", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "New order has been saved", Toast.LENGTH_LONG).show();
        finish();
    }
}
