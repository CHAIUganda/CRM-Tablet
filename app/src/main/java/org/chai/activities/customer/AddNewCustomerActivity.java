package org.chai.activities.customer;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.BaseActivity;

/**
 * Created by Zed on 4/23/2015.
 */
public class AddNewCustomerActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_CUSTOMERS;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_customer_activity);

        aq = new AQuery(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        super.setUpDrawer(toolbar);
    }
}
