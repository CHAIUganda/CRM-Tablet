package org.chai.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.chai.R;
import org.chai.activities.calls.CallsMainActivity;
import org.chai.activities.customer.CustomersMainActivity;
import org.chai.activities.tasks.TasksMainActivity;
import org.chai.sync.CHAISynchroniser;

/**
 * Created by victor on 10/15/14.
 */
public class HomeActivity extends Activity {

    private ProgressDialog progressDialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        Button btnTasks = (Button) findViewById(R.id.btn_tasks);
        Button btnCustomers = (Button)findViewById(R.id.btn_customers);
        Button btnCallsData = (Button)findViewById(R.id.btn_calls);
        Button btnSyncronise = (Button)findViewById(R.id.btn_sync);
        progressDialog = new ProgressDialog(this);

        btnTasks.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TasksMainActivity.class);
                startActivity(i);
            }
        });

        btnCustomers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CustomersMainActivity.class);
                startActivity(i);
            }
        });

        btnCallsData.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CallsMainActivity.class);
                startActivity(i);
            }
        });

        btnSyncronise.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Syncronising with Server:) ");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setIndeterminate(false);
                progressDialog.setMax(100);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CHAISynchroniser chaiSynchroniser = new CHAISynchroniser(HomeActivity.this,progressDialog);
                        chaiSynchroniser.startSyncronisationProcess();
                        progressDialog.dismiss();
                    }
                }).start();
            }
        });

    }

    public void open(View view){
        progressDialog.setMessage("Downloading Music :) ");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }
}