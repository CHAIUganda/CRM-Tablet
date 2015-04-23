package org.chai.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidquery.AQuery;

import org.chai.Globals;
import org.chai.R;
import org.chai.activities.customer.CustomersActivity;
import org.chai.activities.org.chai.activities.forms.MalariaFormActivity;
import org.chai.model.User;
import org.chai.rest.RestClient;
import org.chai.util.AccountManager;

/**
 * Created by Zed on 2/4/2015.
 */
public class BaseActivity extends ActionBarActivity{
    AQuery aquery;

    boolean skipLogin = false;

    public int SCREEN_TASKS = 0;
    public int SCREEN_CUSTOMERS = 1;
    public int SCREEN_HISTORY = 2;
    public int SCREEN_NEW_ORDER = 3;
    public int SCREEN_DETAILING = 4;
    public int SCREEN_REPORT = 5;

    public int CURRENT_SCREEN = 0;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    String[] drawerItems = new String[]{
            "Tasks",
            "Customers",
            "History",
            "New Order",
            "Unscheduled Detailing",
            "My Report"
    };
    int[] iconsNormal = new int[]{
            R.drawable.ic_drawer_tasks,
            R.drawable.ic_drawer_customers,
            R.drawable.ic_drawer_history,
            R.drawable.ic_drawer_order,
            R.drawable.ic_drawer_detailing,
            R.drawable.ic_drawer_reports
    };
    int[] iconsSelected = new int[]{
            R.drawable.ic_drawer_tasks_active,
            R.drawable.ic_drawer_customers_active,
            R.drawable.ic_drawer_history_active,
            R.drawable.ic_drawer_order_active,
            R.drawable.ic_drawer_detailing_active,
            R.drawable.ic_drawer_reports_active
    };
    int[] screens = new int[]{
            SCREEN_TASKS,
            SCREEN_CUSTOMERS,
            SCREEN_HISTORY,
            SCREEN_NEW_ORDER,
            SCREEN_DETAILING,
            SCREEN_REPORT
    };

    ListView drawerMenuListView;

    String username;

    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        aquery = new AQuery(this);

        //we initialise gps tracker her to start computing to get accuracy quickly
        Globals.getInstance().initGpsTracker(this);

        if(!skipLogin){
            AccountManager.offlineLogin(this, true);

            if(RestClient.role.equalsIgnoreCase(User.ROLE_SALES)){
                drawerItems[4] = "Unscheduled Sale";
            }
        }
    }

    public void setUpDrawer(Toolbar toolbar) {
        username = AccountManager.getUsername(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        drawerMenuListView = (ListView) findViewById(R.id.lst_drawer);
        drawerMenuListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, drawerItems) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = convertView;
                boolean active = (CURRENT_SCREEN == screens[position]);
                int[] icons = iconsNormal;
                if (active) {
                    icons = iconsSelected;
                }

                if (row == null) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(R.layout.drawer_item, null);
                }

                AQuery aq = new AQuery(row);
                if (active) {
                    aq.backgroundColor(Color.parseColor("#bacce1"));
                } else {
                    aq.background(android.R.color.transparent);
                }

                aq.id(R.id.img_icon).image(icons[position]);
                aq.id(R.id.txt_title).text(drawerItems[position]);

                if (active) {
                    aq.id(R.id.txt_title).textColor(R.color.primary);
                } else {
                    aq.id(R.id.txt_title).textColor(R.color.app_text_color);
                }

                return row;
            }
        });

        drawerMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class target = null;
                switch (position) {
                    case 0:
                        target = HomeActivity.class;
                        break;
                    case 1:
                        target = CustomersActivity.class;
                        break;
                    case 2:
                        target = BlankActivity.class;
                        break;
                    case 3:
                        target = BlankActivity.class;
                        break;
                    case 4:
                        target = MalariaFormActivity.class;
                        break;
                    case 5:
                        target = BlankActivity.class;
                        break;
                }
                if (target != null) {
                    Intent i = new Intent(BaseActivity.this, target);
                    i.putExtra("title", drawerItems[position]);
                    i.putExtra("username", username);
                    i.putExtra("position", position);
                    startActivity(i);
                }
                drawerLayout.closeDrawers();
            }
        });

        aquery.id(R.id.txt_logout).clicked(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AccountManager.logout(BaseActivity.this);
            }
        });

        aquery.id(R.id.txt_email).text(username);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.START|Gravity.LEFT)){
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}