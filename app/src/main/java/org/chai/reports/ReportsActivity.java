package org.chai.reports;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.BaseActivity;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.SummaryReport;
import org.chai.model.SummaryReportDao;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.List;

/**
 * Created by Zed on 4/23/2015.
 */
public class ReportsActivity extends BaseActivity {
    Toolbar toolbar;
    AQuery aq;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SummaryReportDao summaryReportDao;
    private TableLayout tableLayout;
    private List<SummaryReport> summaryReportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CURRENT_SCREEN = SCREEN_REPORT;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports_activity);

        aq = new AQuery(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialiseGreenDao();

        super.setUpDrawer(toolbar);

        tableLayout = (TableLayout)findViewById(R.id.summary_report_table);
        summaryReportList = summaryReportDao.loadAll();

        LayoutInflater inflator = this.getLayoutInflater();
        AQuery a;
        for(SummaryReport r: summaryReportList){
            View v = inflator.inflate(R.layout.report_table_row, null);
            a = new AQuery(v);
            a.id(R.id.txt_label).text(r.getItem());
            a.id(R.id.txt_week).text(r.getWeek());
            a.id(R.id.txt_month).text(r.getMonth());

            tableLayout.addView(v);
        }
    }

    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            summaryReportDao = daoSession.getSummaryReportDao();
        } catch (Exception ex) {
            Utils.log("Error initializing databse");
            Toast.makeText(this, "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
