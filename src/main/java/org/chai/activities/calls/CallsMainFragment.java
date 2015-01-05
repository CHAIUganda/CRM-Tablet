package org.chai.activities.calls;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.activities.HomeActivity;
import org.chai.activities.tasks.CommercialFormFragment;
import org.chai.activities.tasks.DetailersActivity;
import org.chai.adapter.DetailerCallAdapter;
import org.chai.adapter.SalesAdapter;
import org.chai.model.*;

import java.util.ArrayList;
import java.util.List;

import android.view.ContextMenu.ContextMenuInfo;
import org.chai.rest.RestClient;

/**
 * Created by victor on 10/15/14.
 */
public class CallsMainFragment extends BaseContainerFragment {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private DetailerCallDao detailerCallDao;
    private SaleDao saleDao;

    private List<DetailerCall> detailerCalls = null;
    private List<Sale> sales = null;
    private ListView listView;
    private DetailerCallAdapter detailerCallAdapter;
    private SalesAdapter salesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.calls_main_activity,container,false);
        initialiseGreenDao();
        listView = (ListView)view.findViewById(R.id.callslistview);
        if(RestClient.role.equalsIgnoreCase(User.ROLE_SALES)){
            sales = new ArrayList<Sale>();
            sales.addAll(saleDao.loadAll());
            salesAdapter = new SalesAdapter(getActivity(),getActivity(),sales);
            listView.setAdapter(salesAdapter);
        }else {
            detailerCalls = new ArrayList<DetailerCall>();
            detailerCalls.addAll(detailerCallDao.loadAll());
            detailerCallAdapter = new DetailerCallAdapter(getActivity(),getActivity(),detailerCalls);
            listView.setAdapter(detailerCallAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                try{
                    if(RestClient.role.equalsIgnoreCase(User.ROLE_SALES)){
                        Sale sale = (Sale)adapterView.getItemAtPosition(position);
                        goToSalesForm(sale);
                    }else{
                        DetailerCall itemAtPosition = (DetailerCall) adapterView.getItemAtPosition(position);
                        goToDetailerForm(itemAtPosition);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    Toast.makeText(getActivity(), "Error Loading commercial form:" + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        registerForContextMenu(view.findViewById(R.id.callslistview));
        return view;
    }

    private void goToDetailerForm(DetailerCall itemAtPosition) {
        DetailersActivity detailersActivity = new DetailersActivity();
        Bundle bundle = new Bundle();
        bundle.putLong("callId", itemAtPosition.getId());
        detailersActivity.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction =  fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, detailersActivity);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void goToSalesForm(Sale sale){
        CommercialFormFragment commercialFormActivity = new CommercialFormFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("callId", sale.getId());
        commercialFormActivity.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction =  fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container,commercialFormActivity);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            detailerCallDao = daoSession.getDetailerCallDao();
            saleDao = daoSession.getSaleDao();
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.delete_context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.delete_menu_item:
                try{
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
                    int position = (int) info.id;
                    if(RestClient.role.equalsIgnoreCase(User.ROLE_SALES)){
                        saleDao.delete(sales.get(position));
                        sales.remove(position);
                        salesAdapter.notifyDataSetChanged();
                    }else{
                        detailerCalls.remove(position);
                        detailerCallDao.delete(detailerCalls.get(position));
                        detailerCallAdapter.notifyDataSetChanged();
                    }
                }catch (Exception ex){

                }
                return true;
            case R.id.call_details_edit:
                try{
                    AdapterView.AdapterContextMenuInfo info2 = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
                    int position2 = (int) info2.id;
                    DetailerCall detailerCall = detailerCalls.get(position2);
                    goToDetailerForm(detailerCall);
                }catch (Exception ex){

                }
                return true;
        }
        return super.onContextItemSelected(menuItem);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.new_customer_form_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.customer_form_home:
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}