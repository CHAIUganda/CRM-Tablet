package org.chai.activities.calls;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.chai.R;
import org.chai.activities.HomeActivity;
import org.chai.activities.customer.CustomerForm;
import org.chai.activities.tasks.DetailersActivity;
import org.chai.adapter.DetailerCallAdapter;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.DetailerCall;
import org.chai.model.DetailerCallDao;

import java.util.ArrayList;
import java.util.List;

import android.view.ContextMenu.ContextMenuInfo;

/**
 * Created by victor on 10/15/14.
 */
public class CallsMainActivity extends Activity {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private DetailerCallDao detailerCallDao;

    private List<DetailerCall> detailerCalls = new ArrayList<DetailerCall>();
    private ListView listView;
    private DetailerCallAdapter detailerCallAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calls_main_activity);
        initialiseGreenDao();
        detailerCalls.addAll(detailerCallDao.loadAll());
        listView = (ListView)findViewById(R.id.callslistview);

        detailerCallAdapter = new DetailerCallAdapter(this.getApplicationContext(),this,detailerCalls);
        listView.setAdapter(detailerCallAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                try{
                    DetailerCall itemAtPosition = (DetailerCall) adapterView.getItemAtPosition(position);
                    goToDetailerForm(itemAtPosition);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        registerForContextMenu(findViewById(R.id.callslistview));
    }

    private void goToDetailerForm(DetailerCall itemAtPosition) {
        Intent intent = new Intent(getApplicationContext(), DetailersActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("callId", itemAtPosition.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            detailerCallDao = daoSession.getDetailerCallDao();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.delete_menu_item:
                try{
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
                    int position = (int) info.id;
                    detailerCalls.remove(position);
                    detailerCallDao.delete(detailerCalls.get(position));
                    detailerCallAdapter.notifyDataSetChanged();
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
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_customer_form_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.customer_form_home:
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }


    @Override
    public void  onRestart(){
        super.onRestart();
        if(daoSession!= null){
            daoSession.clear();
        }
    }
}