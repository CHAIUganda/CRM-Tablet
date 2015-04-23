package org.chai.reports;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.BaseActivity;

/**
 * Created by Zed on 4/23/2015.
 */
public class ReportsActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_REPORT;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports_activity);

        aq = new AQuery(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        super.setUpDrawer(toolbar);
    }
}
