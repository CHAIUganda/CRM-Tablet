package org.chai.activities.customer;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.chai.R;
import org.chai.activities.HomeActivity;
import org.chai.adapter.CustomerAdapter;
import org.chai.model.Customer;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.rest.RestClient;
import org.chai.util.SampleData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by victor on 10/15/14.
 */
public class CustomersMainActivity extends Activity {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;
    private SampleData sampleData;


    private List<Customer> customerList = new ArrayList<Customer>();
    private ListView listView;
    private CustomerAdapter customerAdapter;
    private EditText searchText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customers_main_activity);
        initialiseGreenDao();
        try {
            customerList.addAll(customerDao.loadAll());
            listView = (ListView) findViewById(R.id.customerlist);

            customerAdapter = new CustomerAdapter(this.getApplicationContext(), this, customerList);
            listView.setAdapter(customerAdapter);

            searchText = (EditText) findViewById(R.id.customersearch);
            searchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String term = searchText.getText().toString().toLowerCase(Locale.getDefault());
                    customerAdapter.filter(term);

                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), CustomerDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("id", ((Customer) adapterView.getItemAtPosition(position)).getId());
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });
        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), "error in CustomerList:" + exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.customer_list_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_new_customer:
                Intent intent = new Intent(getApplicationContext(), CustomerForm.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.menu_nearby_customer:
                return true;
            case R.id.customer_list_home:
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        customerList.clear();
        customerList.addAll(customerDao.loadAll());
        customerAdapter.notifyDataSetChanged();
    }
}