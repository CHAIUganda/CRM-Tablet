package org.chai.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.chai.R;

/**
 * Created by Zed on 4/10/2015.
 */
public class BlankActivity extends BaseActivity{
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = getIntent().getIntExtra("position", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_activity);

        String title = getIntent().getStringExtra("title");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);

        super.setUpDrawer(toolbar);
    }
}
