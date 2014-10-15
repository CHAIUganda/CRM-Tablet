package org.chai.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.chai.R;
import org.chai.activities.calls.CallsMainActivity;
import org.chai.activities.customer.CustomersMainActivity;
import org.chai.activities.tasks.TasksMainActivity;

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
                Intent i = new Intent(getApplicationContext(), CallsMainActivity.class);
                startActivity(i);
            }
        });

        btnSyncronise.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
            }
        });
    }
}