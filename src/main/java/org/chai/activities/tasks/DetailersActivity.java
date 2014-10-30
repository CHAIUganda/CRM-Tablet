package org.chai.activities.tasks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.chai.R;
import org.chai.adapter.SubcountyArrayAdapter;
import org.chai.model.*;
import org.chai.util.GPSTracker;

import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by victor on 10/30/14.
 */
public class DetailersActivity extends Activity {

    static final int DATE_PICKER_ID = 1111;
    private EditText dateEditTxt;
    private DatePickerDialog datePickerDialog;
    private String initialDate;
    private String initialMonth;
    private String initialYear;

    private GPSTracker gpsTracker;
    private double capturedLatitude;
    private double capturedLongitude;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SubcountyDao subcountyDao;
    private TaskDao taskDao;

    private Spinner subcountySpinner;
    private SubcountyArrayAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailers_form);
        initialiseGreenDao();
        Bundle bundle = getIntent().getExtras();
        Long taskId = bundle.getLong("id");
        setDateWidget();
        setGpsWidget();
        List<Subcounty> subcountyData = subcountyDao.loadAll();
        Subcounty[] subcounties = subcountyData.toArray(new Subcounty[subcountyData.size()]);
        subcountySpinner = (Spinner) findViewById(R.id.detailer_subcounty);

        adapter = new SubcountyArrayAdapter(this, android.R.layout.simple_spinner_item, subcounties);
        subcountySpinner.setAdapter(adapter);
        manageDoyouStockZincResponses();
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            subcountyDao = daoSession.getSubcountyDao();
            taskDao = daoSession.getTaskDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setDateWidget(){
        Button dateBtn = (Button)findViewById(R.id.detailer_survey_date_btn);
        dateEditTxt = (EditText)findViewById(R.id.detailer_survey_date);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = null;
                String existingDate = (String)dateEditTxt.getText().toString();
                if(existingDate!=null && !existingDate.equals("")){
                    StringTokenizer stringTokenizer = new StringTokenizer(existingDate,"/");
                    initialMonth = stringTokenizer.nextToken();
                    initialDate = stringTokenizer.nextToken();
                    initialYear = stringTokenizer.nextToken();
                    if(datePickerDialog == null){
                        datePickerDialog = new DatePickerDialog(view.getContext(),new PickDate(),Integer.parseInt(initialYear),
                                Integer.parseInt(initialMonth)-1,
                                Integer.parseInt(initialDate));

                        datePickerDialog.updateDate(Integer.parseInt(initialYear),
                                Integer.parseInt(initialMonth)-1,
                                Integer.parseInt(initialDate));
                    }
                }else{
                    calendar = Calendar.getInstance();
                    if(datePickerDialog == null){
                        datePickerDialog = new DatePickerDialog(view.getContext(),new PickDate(),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                    }
                }
                datePickerDialog.show();

            }
        });
    }

    private class PickDate implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            view.updateDate(year, monthOfYear, dayOfMonth);
            dateEditTxt.setText(monthOfYear+"/"+dayOfMonth+"/"+year);
            datePickerDialog.hide();
        }
    }

    private void setGpsWidget(){
        Button showGps = (Button) findViewById(R.id.detailers_gps_btn);
        showGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpsTracker = new GPSTracker(DetailersActivity.this);
                if (gpsTracker.canGetLocation()) {
                    capturedLatitude = gpsTracker.getLatitude();
                    capturedLongitude = gpsTracker.getLongitude();
                    EditText detailsGps = (EditText) findViewById(R.id.detailers_gps_text);
                    detailsGps.setText(capturedLatitude + "," + capturedLongitude);
                } else {
                    gpsTracker.showSettingsAlert();
                }
            }
        });
    }

    private void manageDoyouStockZincResponses(){
        final Spinner spinner = (Spinner)findViewById(R.id.detailer_do_you_stock_zinc);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String)spinner.getAdapter().getItem(position);
                Toast.makeText(DetailersActivity.this, "selected" + selected, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}