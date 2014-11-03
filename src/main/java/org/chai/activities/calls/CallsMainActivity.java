package org.chai.activities.calls;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.chai.R;
import org.chai.activities.tasks.DetailersActivity;
import org.chai.adapter.DetailerCallAdapter;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.DetailerCall;
import org.chai.model.DetailerCallDao;

import java.util.ArrayList;
import java.util.List;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                DetailerCall itemAtPosition = (DetailerCall) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), DetailersActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("callId",itemAtPosition.getId());
                intent.putExtras(bundle);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "Item Selected" + itemAtPosition.getTask().getDescription(), Toast.LENGTH_LONG).show();
            }
        });
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


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.call_details_edit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.call_details_edit:
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}