package org.chai.activities.customer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import org.chai.R;
import org.chai.adapter.CustomerAdapter;
import org.chai.model.Customer;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;

import java.util.ArrayList;
import java.util.List;

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

}