package org.chai.activities.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.BaseActivity;

/**
 * Created by Zed on 4/23/2015.
 */
public class CustomersActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_CUSTOMERS;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.customers_activity);

        aq = new AQuery(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aq.id(R.id.btn_add_new_customer).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomersActivity.this, AddNewCustomerActivity.class);
                startActivity(i);
            }
        });

        super.setUpDrawer(toolbar);
    }
}
