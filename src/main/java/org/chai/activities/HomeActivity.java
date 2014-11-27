package org.chai.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.chai.R;
import org.chai.activities.calls.CallsMainActivity;
import org.chai.activities.customer.CustomersMainActivity;
import org.chai.activities.tasks.TasksMainActivity;
import org.chai.sync.CHAISynchroniser;

/**
 * Created by victor on 10/15/14.
 */
public class HomeActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        Button btnTasks = (Button) findViewById(R.id.btn_tasks);
        Button btnCustomers = (Button)findViewById(R.id.btn_customers);
        Button btnCallsData = (Button)findViewById(R.id.btn_calls);
        Button btnSyncronise = (Button)findViewById(R.id.btn_sync);

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
                try{
                    Intent i = new Intent(getApplicationContext(), CallsMainActivity.class);
                    startActivity(i);
                }catch (Exception ex){
                    Toast.makeText(HomeActivity.this.getApplicationContext(),
                            "Unable to View Call Data please ensure you have synchronised",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSyncronise.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog  = new ProgressDialog(HomeActivity.this);
                progressDialog.setMessage("Syncronising with Server...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setIndeterminate(false);
                progressDialog.setMax(100);
                progressDialog.setCanceledOnTouchOutside(false);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }
}