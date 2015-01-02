package org.chai.activities.tasks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.chai.R;
import org.chai.activities.HomeActivity;
import org.chai.activities.calls.CallsMainFragment;
import org.chai.adapter.DistrictArrayAdapter;
import org.chai.model.*;
import org.chai.util.GPSTracker;
import org.chai.util.Utils;

import java.util.*;

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
    private VillageDao villageDao;
    private TaskDao taskDao;
    private DetailerCallDao detailerCallDao;

    private TextView subcountyTxt;
    private DistrictArrayAdapter adapter;

    private DetailerCall detailerCall;
    private Task callDataTask;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailers_form);
        initialiseGreenDao();
        Bundle bundle = getIntent().getExtras();
        Long callId = bundle.getLong("callId");
        if (callId != 0 && callId != null) {
            //we are from call data listview
            detailerCall = detailerCallDao.loadDeep(callId);
            callDataTask = detailerCall.getTask();
        } else {
            //from tasklist view
            Log.i("callId=======================================================", callId + "");
            detailerCall = new DetailerCall(null);
            Long taskId = bundle.getLong("taskId");
            callDataTask = taskDao.loadDeep(taskId);
        }
        setDateWidget();
        setGpsWidget();
        List<Village> villageData = villageDao.loadAll();
        Village[] villages = villageData.toArray(new Village[villageData.size()]);
        subcountyTxt = (TextView) findViewById(R.id.detailer_subcounty);

        manageDoyouStockZincResponses();
        manageHowDidyouHearOtherOption();
        manageHaveYouHeardAboutDiarheaTreatment();

        Button saveDetailetCallBtn = (Button) findViewById(R.id.detailer_submit_btn);
        saveDetailetCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSaved = saveDetailerCall();
                if (isSaved) {
                    Toast.makeText(getApplicationContext(), "Detailer Information has been  successfully added!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), CallsMainFragment.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "A problem Occured while saving a new Detialer Information,please ensure that data is entered correctly", Toast.LENGTH_LONG).show();
                }
            }
        });
        bindDetailerCallToUi();
        Button takeOrder = (Button)findViewById(R.id.detailer_take_order);
        takeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TakeOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            villageDao = daoSession.getVillageDao();
            taskDao = daoSession.getTaskDao();
            detailerCallDao = daoSession.getDetailerCallDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setDateWidget() {
        Button dateBtn = (Button) findViewById(R.id.detailer_survey_date_btn);
        dateEditTxt = (EditText) findViewById(R.id.detailer_survey_date);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = null;
                String existingDate = (String) dateEditTxt.getText().toString();
                if (existingDate != null && !existingDate.equals("")) {
                    StringTokenizer stringTokenizer = new StringTokenizer(existingDate, "/");
                    initialMonth = stringTokenizer.nextToken();
                    initialDate = stringTokenizer.nextToken();
                    initialYear = stringTokenizer.nextToken();
                    if (datePickerDialog == null) {
                        datePickerDialog = new DatePickerDialog(view.getContext(), new PickDate(), Integer.parseInt(initialYear),
                                Integer.parseInt(initialMonth) - 1,
                                Integer.parseInt(initialDate));

                        datePickerDialog.updateDate(Integer.parseInt(initialYear),
                                Integer.parseInt(initialMonth) - 1,
                                Integer.parseInt(initialDate));
                    }
                } else {
                    calendar = Calendar.getInstance();
                    if (datePickerDialog == null) {
                        datePickerDialog = new DatePickerDialog(view.getContext(), new PickDate(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    }
                }
                datePickerDialog.show();

            }
        });
    }

    private void setGpsWidget() {
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

    private class PickDate implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            view.updateDate(year, monthOfYear, dayOfMonth);
            dateEditTxt.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
            datePickerDialog.hide();
        }
    }

    private void manageDoyouStockZincResponses() {
        final Spinner spinner = (Spinner) findViewById(R.id.detailer_do_you_stock_zinc);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout stockfieldsLayout = (LinearLayout) findViewById(R.id.detailer_zinc_stock_layout);
                LinearLayout ifnowhyLayout = (LinearLayout) findViewById(R.id.detailer_ifnowhy_layout);
                if ("No".equalsIgnoreCase(selected)) {
                    stockfieldsLayout.setVisibility(View.GONE);
                    ifnowhyLayout.setVisibility(View.VISIBLE);
                } else {
                    stockfieldsLayout.setVisibility(View.VISIBLE);
                    ifnowhyLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void  manageHaveYouHeardAboutDiarheaTreatment(){
        final Spinner spinner = (Spinner) findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout howdidYouHearAboutTreatment = (LinearLayout) findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors_layout);
                if ("No".equalsIgnoreCase(selected)) {
                    howdidYouHearAboutTreatment.setVisibility(View.GONE);
                } else {
                    howdidYouHearAboutTreatment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void manageHowDidyouHearOtherOption() {
        final Spinner spinner = (Spinner) findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout otherwaysinfoLayout = (LinearLayout) findViewById(R.id.detailer_howdidyouhearaboutzinc_other_layout);
                if ("Other".equalsIgnoreCase(selected)) {
                    otherwaysinfoLayout.setVisibility(View.VISIBLE);
                } else {
                    otherwaysinfoLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void bindUiToDetailerCall() {
        if (detailerCall == null) {
            detailerCall = new DetailerCall(null);
        }
        //
        detailerCall.setUuid(UUID.randomUUID().toString());
        detailerCall.setDateOfSurvey(new Date());
        detailerCall.setDiarrheaPatientsInFacility(Integer.parseInt(((EditText) findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility)).getText().toString()));
        detailerCall.setOtherWaysHowYouHeard(((EditText) findViewById(R.id.detailer_other_ways_youheard_about_zinc)).getText().toString());
        if (((Spinner) findViewById(R.id.detailer_do_you_stock_zinc)).getSelectedItem().toString().equals("Yes")) {
            detailerCall.setHowManyZincInStock(Integer.parseInt(((EditText) findViewById(R.id.detailers_howmany_in_stock_zinc)).getText().toString()));
            detailerCall.setHowmanyOrsInStock(Integer.parseInt(((EditText) findViewById(R.id.detailers_howmany_in_stock_ors)).getText().toString()));
            detailerCall.setZincBrandsold(((EditText) findViewById(R.id.detailers_brand_sold_zinc)).getText().toString());
            detailerCall.setOrsBrandSold(((EditText) findViewById(R.id.detailers_brand_sold_ors)).getText().toString());
        }

        detailerCall.setIfNoWhy(((EditText) findViewById(R.id.detailer_if_no_why)).getText().toString());
        detailerCall.setBuyingPrice(Double.parseDouble(((EditText) findViewById(R.id.detailer_whatpricedoyoubuy)).getText().toString()));
        detailerCall.setPointOfsaleMaterial(((Spinner) findViewById(R.id.detailer_point_of_sale)).getSelectedItem().toString());
        detailerCall.setRecommendationNextStep(((Spinner) findViewById(R.id.detailer_next_step_recommendation)).getSelectedItem().toString());

        detailerCall.setHeardAboutDiarrheaTreatmentInChildren(((Spinner) findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors)).getSelectedItem().toString());
        detailerCall.setHowDidYouHear(((Spinner) findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors)).getSelectedItem().toString());
        detailerCall.setWhatYouKnowAbtDiarrhea(((Spinner) findViewById(R.id.detailer_how_diarrhea_affects_community)).getSelectedItem().toString());
        detailerCall.setDiarrheaEffectsOnBody(((Spinner) findViewById(R.id.detailer_effect_diarrhea_has_on_the_body)).getSelectedItem().toString());
        detailerCall.setKnowledgeAbtOrsAndUsage(((Spinner) findViewById(R.id.detailer_how_ors_should_be_used)).getSelectedItem().toString());
        detailerCall.setWhyNotUseAntibiotics(((Spinner) findViewById(R.id.detailer_why_should_not_use_antibiotics)).getSelectedItem().toString());
        String stocksZinc = ((Spinner) findViewById(R.id.detailer_do_you_stock_zinc)).getSelectedItem().toString();
        detailerCall.setDoYouStockOrsZinc(stocksZinc.equalsIgnoreCase("Yes") ? true : false);
        detailerCall.setRecommendationLevel(((Spinner) findViewById(R.id.detailer_recommendation_level)).getSelectedItem().toString());
        detailerCall.setKnowledgeAbtZincAndUsage(((Spinner) findViewById(R.id.detailer_how_zinc_should_be_used)).getSelectedItem().toString());
        detailerCall.setLatitude(capturedLatitude);
        detailerCall.setLongitude(capturedLongitude);
    }

    private void bindDetailerCallToUi() {
        if (!isNewDetailerCall()) {
            Customer customer = detailerCall.getTask().getCustomer();
            ((EditText) findViewById(R.id.detailer_survey_date)).setText(Utils.dateToString(detailerCall.getDateOfSurvey()));
            ((TextView) findViewById(R.id.detailer_name)).setText(customer.getOutletName());
            ((TextView) findViewById(R.id.detailer_desc_location)).setText(customer.getDescriptionOfOutletLocation());
            ((TextView) findViewById(R.id.detailer_parish)).setText("");
            ((TextView) findViewById(R.id.detailer_village)).setText("");
            ((TextView) findViewById(R.id.detailer_subcounty)).setText(customer.getSubcounty().getName());
            ((TextView) findViewById(R.id.detailer_outlet_size)).setText(customer.getOutletSize());
            CustomerContact keyCustomerContact = Utils.getKeyCustomerContact(customer.getCustomerContacts());
            if(keyCustomerContact!= null){
                ((TextView) findViewById(R.id.detailer_key_retailer_name)).setText(keyCustomerContact.getFirstName());
                ((TextView) findViewById(R.id.detailer_key_retailer_contact)).setText(keyCustomerContact.getContact());
            }
            ((EditText) findViewById(R.id.detailers_gps_text)).setText(customer.getLatitude() + "," + customer.getLongitude());
            ((EditText) findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility)).setText(detailerCall.getDiarrheaPatientsInFacility() + "");
            ((EditText) findViewById(R.id.detailer_other_ways_youheard_about_zinc)).setText(detailerCall.getOtherWaysHowYouHeard());
            ((EditText) findViewById(R.id.detailers_howmany_in_stock_zinc)).setText(detailerCall.getHowManyZincInStock()+"");
            ((EditText) findViewById(R.id.detailers_howmany_in_stock_ors)).setText(detailerCall.getHowmanyOrsInStock()+"");
            ((EditText) findViewById(R.id.detailers_brand_sold_zinc)).setText(detailerCall.getZincBrandsold());
            ((EditText) findViewById(R.id.detailers_brand_sold_ors)).setText(detailerCall.getOrsBrandSold());
            ((EditText) findViewById(R.id.detailer_if_no_why)).setText(detailerCall.getIfNoWhy());
            ((EditText) findViewById(R.id.detailer_whatpricedoyoubuy)).setText(detailerCall.getBuyingPrice() + "");
            ((EditText)findViewById(R.id.detailers_gps_text)).setText(detailerCall.getLatitude() == null ? "0.0,0.0" : detailerCall.getLatitude() + ","+detailerCall.getLongitude());

            //spinners

            Spinner detailerHearAboutSpinner = (Spinner) findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors);
            Utils.setSpinnerSelection(detailerHearAboutSpinner, detailerCall.getHeardAboutDiarrheaTreatmentInChildren());

            Spinner howdidyouHearSpinner = (Spinner) findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors);
            Utils.setSpinnerSelection(howdidyouHearSpinner, detailerCall.getHowDidYouHear());

            Spinner effectsOfDiarrheaSpinner = (Spinner) findViewById(R.id.detailer_how_diarrhea_affects_community);
            Utils.setSpinnerSelection(effectsOfDiarrheaSpinner, detailerCall.getWhatYouKnowAbtDiarrhea());

            Spinner effectontheBodySpinner = (Spinner) findViewById(R.id.detailer_effect_diarrhea_has_on_the_body);
            Utils.setSpinnerSelection(effectontheBodySpinner, detailerCall.getDiarrheaEffectsOnBody());

            Spinner orsUsageSpinner = (Spinner) findViewById(R.id.detailer_how_ors_should_be_used);
            Utils.setSpinnerSelection(orsUsageSpinner, detailerCall.getKnowledgeAbtOrsAndUsage());

            Spinner dontUseAntiBioticsSpinner = (Spinner) findViewById(R.id.detailer_why_should_not_use_antibiotics);
            Utils.setSpinnerSelection(dontUseAntiBioticsSpinner, detailerCall.getWhyNotUseAntibiotics());

            Spinner doyouStockZincSpinner = (Spinner) findViewById(R.id.detailer_do_you_stock_zinc);
            Utils.setSpinnerSelection(doyouStockZincSpinner, detailerCall.getDoYouStockOrsZinc() + "");

            Spinner nextStepRecommendation = ((Spinner) findViewById(R.id.detailer_next_step_recommendation));
            Utils.setSpinnerSelection(nextStepRecommendation,detailerCall.getRecommendationNextStep());

            Spinner recommendationSpinner = (Spinner) findViewById(R.id.detailer_recommendation_level);
            Utils.setSpinnerSelection(recommendationSpinner, detailerCall.getRecommendationLevel());

            Spinner whatdoyouknowAboutZinc = (Spinner)findViewById(R.id.detailer_how_zinc_should_be_used);
            Utils.setSpinnerSelection(whatdoyouknowAboutZinc,detailerCall.getKnowledgeAbtZincAndUsage());

            Spinner pointOfSaleMaterial =  ((Spinner) findViewById(R.id.detailer_point_of_sale));
            Utils.setSpinnerSelection(pointOfSaleMaterial,detailerCall.getPointOfsaleMaterial());

        }else{
            Calendar calendar = Calendar.getInstance();
            ((EditText) findViewById(R.id.detailer_survey_date)).setText(calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.YEAR));
            Customer customer = callDataTask.getCustomer();
            if(customer!=null){
                ((TextView) findViewById(R.id.detailer_subcounty)).setText(customer.getSubcounty().getName());
                ((TextView) findViewById(R.id.detailer_outlet_size)).setText(customer.getOutletSize());
                ((TextView) findViewById(R.id.detailer_name)).setText(customer.getOutletName());
                ((TextView) findViewById(R.id.detailer_desc_location)).setText(customer.getDescriptionOfOutletLocation());
                ((TextView) findViewById(R.id.detailer_parish)).setText("");
                ((TextView) findViewById(R.id.detailer_village)).setText("");
                ((EditText) findViewById(R.id.detailers_gps_text)).setText(customer.getLatitude() + "," + customer.getLongitude());
                CustomerContact keyCustomerContact = Utils.getKeyCustomerContact(customer.getCustomerContacts());
                ((TextView) findViewById(R.id.detailer_key_retailer_name)).setText(keyCustomerContact!= null?keyCustomerContact.getFirstName():"");
                ((TextView) findViewById(R.id.detailer_key_retailer_contact)).setText(keyCustomerContact!= null?keyCustomerContact.getContact():"");
            }
        }
        setRequiredFields();
    }

    private boolean saveDetailerCall() {
        boolean isSaved = false;
        try {
            bindUiToDetailerCall();
            detailerCall.setTaskId(callDataTask.getId());
            if(isNewDetailerCall()){
                detailerCallDao.insert(detailerCall);
            }else{
                detailerCallDao.update(detailerCall);
            }
            callDataTask.setStatus(TasksMainActivity.STATUS_COMPLETE);
            taskDao.update(callDataTask);
            isSaved = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isSaved;
    }

    private boolean isNewDetailerCall() {
        if (detailerCall.getId() == null || detailerCall.getId() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_customer_form_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.customer_form_home:
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void setRequired(TextView textView){
      String required = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(textView.getText().toString());
        int start = builder.length();
        builder.append(required);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    private void setRequiredFields(){
        setRequired((TextView)findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility_view));
        setRequired((TextView)findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors_view));
        setRequired((TextView)findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors_view));
        setRequired((TextView)findViewById(R.id.detailer_how_diarrhea_affects_community_view));
        setRequired((TextView)findViewById(R.id.detailer_effect_diarrhea_has_on_the_body_view));
        setRequired((TextView)findViewById(R.id.detailer_how_ors_should_be_used_view));
        setRequired((TextView)findViewById(R.id.detailer_how_zinc_should_be_used_view));
        setRequired((TextView)findViewById(R.id.detailer_why_should_not_use_antibiotics_view));
    }

}