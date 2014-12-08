package org.chai.activities;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.chai.R;
import org.chai.activities.customer.CustomersMainFragment;

import android.app.ActionBar.Tab;
import org.chai.sync.CHAISynchroniser;

/**
 * Created by victor on 10/15/14.
 */
public class HomeActivity extends Activity{

    private String[] tabHeaders = {"Customers", "Tasks", "History"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        Tab tab = actionBar.newTab()
             .setText(tabHeaders[0])
                .setTabListener(new HomeTabListener<CustomersMainFragment>(this,tabHeaders[0],CustomersMainFragment.class));

        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(tabHeaders[1])
                .setTabListener(new HomeTabListener<CustomersMainFragment>(this,tabHeaders[1],CustomersMainFragment.class));

        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(tabHeaders[2])
                .setTabListener(new HomeTabListener<CustomersMainFragment>(this,tabHeaders[2],CustomersMainFragment.class));

        actionBar.addTab(tab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.home_sync:
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }
}