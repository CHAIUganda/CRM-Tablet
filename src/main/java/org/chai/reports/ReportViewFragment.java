package org.chai.reports;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.SummaryReport;
import org.chai.model.SummaryReportDao;
import org.chai.util.customwidget.SummaryReportTable;

import java.util.List;

/**
 * Created by victor on 2/12/15.
 */
public class ReportViewFragment extends BaseContainerFragment{
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SummaryReportDao summaryReportDao;
    private TableLayout tableLayout;
    private List<SummaryReport> summaryReportList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.summary_report_layout,container,false);
        initialiseGreenDao();

        tableLayout = (TableLayout)view.findViewById(R.id.summary_report_table);
        summaryReportList = summaryReportDao.loadAll();
        if(!summaryReportList.isEmpty()){
            addReportsToTable(summaryReportList);
        }
        return view;
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            summaryReportDao = daoSession.getSummaryReportDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * @param summaryReportList
     */
    private void  addReportsToTable( List<SummaryReport> summaryReportList){
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        for(SummaryReport summaryReport:summaryReportList){
            tableLayout.addView(new SummaryReportTable(getActivity().getApplicationContext(),summaryReport.getItem().replace("_"," "),summaryReport.getWeek(),
                    summaryReport.getMonth(),summaryReport.getTeamAverageThisWeek(),summaryReport.getTeamAverageThisMonth()),params);
        }
    }

}
