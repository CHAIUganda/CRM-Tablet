package org.chai.activities.customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.chai.R;
import org.chai.adapter.CustomerAdapter;
import org.chai.model.*;
import org.chai.rest.RestClient;

import java.util.*;

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
            registerForContextMenu(getListView());
        } catch (Exception exception) {
            Toast.makeText(getActivity().getApplicationContext(), "error in CustomerList:" + exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
        customerAdapter = new CustomerAdapter(getActivity(), customerList);
        setListAdapter(customerAdapter);
        customerAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_list_fragment, container, false);
        Button newCustomerBtn = (Button)view.findViewById(R.id.btn_add_new_customer);
        newCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomerForm.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Customer selectedCustomer = customerList.get(position);

        Intent intent = new Intent(getActivity().getApplicationContext(), CustomerDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", selectedCustomer.getUuid());
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.inactivate_context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.customer_mark_inactive:
                try {
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
                    int position = (int) info.id;
                    askBeforeInactivate(position).show();
                } catch (Exception ex) {

                }
                return true;
        }
        return super.onContextItemSelected(menuItem);
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

    private AlertDialog askBeforeInactivate(final int position) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Inactivate")
                .setMessage("Are you sure you want to mark this customer as inactive?")
                .setIcon(R.drawable.delete_icon)

                .setPositiveButton("Inactivate", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Customer customer = customerList.get(position);
                        customer.setIsDirty(true);
                        customer.setIsActive(false);
                        customerDao.update(customer);
                        customerList.remove(position);
                        customerAdapter.notifyDataSetChanged();
                        getListView().invalidateViews();
                        onResume();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return dialog;

    }

    @Override
     public void onResume() {
        super.onResume();
        loadDataFromDb();
        Log.d("Customer Main Fragment", "List Frag Resumed");

    }

}