package org.chai.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.chai.R;

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
            }
        });

        btnCustomers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            }
        });

        btnCallsData.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
            }
        });

        btnSyncronise.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
            }
        });
    }
}