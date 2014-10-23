package org.chai.activities.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.chai.R;

/**
 * Created by victor on 10/17/14.
 */
public class CustomerDetailsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_details_activity);
    }


    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.customer_details_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.customer_details_edit:
                Intent intent = new Intent(getApplicationContext(),CustomerEditActivity.class);
                startActivity(intent);
            case R.id.customer_details_inactive:
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}