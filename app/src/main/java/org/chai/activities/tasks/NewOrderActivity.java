package org.chai.activities.tasks;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.BaseActivity;

/**
 * Created by Zed on 4/23/2015.
 */
public class NewOrderActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_NEW_ORDER;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order_activity);

        aq = new AQuery(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        super.setUpDrawer(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_form_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getItemId() == R.id.action_save){
            Toast.makeText(this, "Order has been saved", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
