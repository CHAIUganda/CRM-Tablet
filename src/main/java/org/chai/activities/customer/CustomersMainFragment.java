package org.chai.activities.customer;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.*;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import org.chai.R;
import org.chai.adapter.CustomerAdapter;
import org.chai.model.Customer;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by victor on 10/15/14.
 */
public class CustomersMainFragment extends ListFragment {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;

    private List<Customer> customerList = new ArrayList<Customer>();
    private CustomerAdapter customerAdapter;
    private String currentQuery = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialiseGreenDao();
        try {
            customerList.addAll(customerDao.loadAll());
            customerAdapter = new CustomerAdapter(getActivity(), customerList);
            setListAdapter(customerAdapter);
        } catch (Exception exception) {
            Toast.makeText(getActivity().getApplicationContext(), "error in CustomerList:" + exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_list_fragment, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Customer selectedCustomer = customerList.get(position);

        Intent intent = new Intent(getActivity().getApplicationContext(), CustomerDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id", selectedCustomer.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.customer_list_menu, menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(queryListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_customer:
                Intent intent = new Intent(getActivity(), CustomerForm.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            Toast.makeText(getActivity(), "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(TextUtils.isEmpty(newText)){
                currentQuery = null;
            }else{
                currentQuery = newText;
                customerAdapter.filter(currentQuery.toString().toLowerCase(Locale.getDefault()));
            }
            return false;
        }
    };

}