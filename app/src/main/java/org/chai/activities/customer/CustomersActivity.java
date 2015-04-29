package org.chai.activities.customer;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.BaseActivity;
import org.chai.adapter.CustomerAdapter;
import org.chai.model.Customer;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Zed on 4/23/2015.
 */
public class CustomersActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;

    private List<Customer> customerList = new ArrayList<Customer>();
    private CustomerAdapter adapter;
    private String currentQuery = null;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_CUSTOMERS;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.customers_activity);

        aq = new AQuery(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.lst_customers);

        aq.id(R.id.btn_add_new_customer).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomersActivity.this, AddNewCustomerActivity.class);
                startActivity(i);
            }
        });

        super.setUpDrawer(toolbar);

        initialiseGreenDao();

        loadDataFromDb();
    }

    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadDataFromDb() {
        daoSession.clear();
        customerList.clear();
        customerList.addAll(customerDao.loadAll());
        Collections.sort(customerList, new Comparator<Customer>() {
            @Override
            public int compare(Customer customer1, Customer customer2) {
                return customer1.getOutletName().compareToIgnoreCase(customer2.getOutletName());
            }
        });
        adapter = new CustomerAdapter(this, customerList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                menu.findItem(R.id.action_search).collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Utils.log("Query changed: " + newText);
                filterList(newText);
                return false;
            }

        });
        return true;
    }

    private void filterList(String filter){
        if(adapter == null){
            return;
        }
        adapter.getFilter().filter(filter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
