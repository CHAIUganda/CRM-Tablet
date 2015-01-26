package org.chai.activities.tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.*;
import android.widget.*;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.WhereCondition;
import org.chai.R;
import org.chai.activities.HomeActivity;
import org.chai.adapter.DistrictArrayAdapter;
import org.chai.model.*;
import org.chai.util.GPSTracker;
import org.chai.util.Utils;

import java.util.*;

/**
 * Created by victor on 10/30/14.
 */
public class DetailersActivity extends Fragment {

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

    private DetailerCall detailerCallInstance;
    private Task callDataTask;
    private Button pointOfSalesOptionsButton;
    private CharSequence[] pointOfSalesOptions;
    private boolean[] selections;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.detailers_form,container, false);
        initialiseGreenDao();
        Bundle bundle = getArguments();
        String callId = bundle.getString("callId");
        if (callId != null) {
            //we are from call data listview
            detailerCallInstance = detailerCallDao.load(callId);
            callDataTask = detailerCallInstance.getTask();
        } else {
            //from tasklist view
            Log.i("callId=======================================================", callId + "");
            detailerCallInstance = new DetailerCall(null);
            String taskId = bundle.getString("taskId");
            callDataTask = taskDao.load(taskId);
            detailerCallInstance = getLastDetailerInfo(callDataTask.getCustomer());
        }
        setDateWidget(view);
        setGpsWidget(view);
        List<Village> villageData = villageDao.loadAll();
        Village[] villages = villageData.toArray(new Village[villageData.size()]);
        subcountyTxt = (TextView)view.findViewById(R.id.detailer_subcounty);

        manageDoyouStockZincResponses(view);
        manageHowDidyouHearOtherOption(view);
        manageHaveYouHeardAboutDiarheaTreatment(view);

        pointOfSalesOptionsButton = (Button)view.findViewById(R.id.detailer_point_of_sale);
        pointOfSalesOptions = getResources().getStringArray(R.array.point_of_sale_material);
        selections = new boolean[pointOfSalesOptions.length];

        pointOfSalesOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPointOfSaleDialog();
            }
        });



        Button saveDetailetCallBtn = (Button)view.findViewById(R.id.detailer_submit_btn);
        saveDetailetCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSaved = saveDetailerCall();
                if (isSaved) {
                    Toast.makeText(getActivity(), "Detailer Information has been  successfully added!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "A problem Occured while saving a new Detialer Information,please ensure that data is entered correctly", Toast.LENGTH_LONG).show();
                }
            }
        });
        bindDetailerCallToUi(view);
        managePointOfSaleOthers(view,false);
        return  view;
    }

    private DetailerCall getLastDetailerInfo(Customer customer) {
        try {
            Query query = detailerCallDao.queryBuilder().where(new WhereCondition.StringCondition(" T.'"+DetailerCallDao.Properties.
                    TaskId.columnName + "' IN " + "(SELECT " + TaskDao.Properties.Uuid.columnName + " FROM " + TaskDao.TABLENAME + " C WHERE C.'" + TaskDao.Properties.CustomerId.columnName + "' = '" + customer.getUuid()+"')")).build();
            List<DetailerCall> detailerCallList = query.list();
            if (!detailerCallList.isEmpty()) {
                return detailerCallList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        DetailerCall detailerCall = new DetailerCall(null);
        detailerCall.setIsNew(true);
        return detailerCall;
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            villageDao = daoSession.getVillageDao();
            taskDao = daoSession.getTaskDao();
            detailerCallDao = daoSession.getDetailerCallDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setDateWidget(View view) {
        Button dateBtn = (Button)view.findViewById(R.id.detailer_survey_date_btn);
        dateEditTxt = (EditText)view.findViewById(R.id.detailer_survey_date);
        dateEditTxt.setEnabled(false);

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
                                Integer.parseInt(initialMonth),
                                Integer.parseInt(initialDate));

                        datePickerDialog.updateDate(Integer.parseInt(initialYear),
                                Integer.parseInt(initialMonth),
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

    private void setGpsWidget(final View view1) {
                Button showGps = (Button)view1.findViewById(R.id.detailers_gps_btn);
                showGps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                gpsTracker = new GPSTracker(getActivity());
                                if (gpsTracker.canGetLocation()) {
                                        capturedLatitude = gpsTracker.getLatitude();
                                        capturedLongitude = gpsTracker.getLongitude();
                                        EditText detailsGps = (EditText)view1.findViewById(R.id.detailers_gps_text);
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
            dateEditTxt.setText(dayOfMonth+ "/"+(monthOfYear+1) + "/" + year);
            datePickerDialog.hide();
        }
    }

    private void manageDoyouStockZincResponses(View view) {
        final Spinner spinner = (Spinner)view.findViewById(R.id.detailer_do_you_stock_zinc);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout stockfieldsLayout = (LinearLayout)getActivity().findViewById(R.id.detailer_zinc_stock_layout);
                LinearLayout ifnowhyLayout = (LinearLayout)getActivity().findViewById(R.id.detailer_ifnowhy_layout);
                LinearLayout atWhatPriceDoYouBuyLayout = (LinearLayout)getActivity().findViewById(R.id.detailer_whatpricedoyoubuy_layout);
                if ("No".equalsIgnoreCase(selected)) {
                    stockfieldsLayout.setVisibility(View.GONE);
                    ifnowhyLayout.setVisibility(View.VISIBLE);
                    atWhatPriceDoYouBuyLayout.setVisibility(View.GONE);
                } else {
                    stockfieldsLayout.setVisibility(View.VISIBLE);
                    ifnowhyLayout.setVisibility(View.GONE);
                    atWhatPriceDoYouBuyLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void  manageHaveYouHeardAboutDiarheaTreatment(View view){
        final Spinner spinner = (Spinner)view.findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout howdidYouHearAboutTreatment = (LinearLayout)getActivity().findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors_layout);
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

    private void manageHowDidyouHearOtherOption(View view) {
        final Spinner spinner = (Spinner)view.findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = (String) spinner.getAdapter().getItem(position);
                LinearLayout otherwaysinfoLayout = (LinearLayout)getActivity().findViewById(R.id.detailer_howdidyouhearaboutzinc_other_layout);
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
        detailerCallInstance.setDateOfSurvey(new Date());
        detailerCallInstance.setDiarrheaPatientsInFacility(Integer.parseInt(((EditText)getActivity(). findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility)).getText().toString()));
        detailerCallInstance.setOtherWaysHowYouHeard(((EditText) getActivity().findViewById(R.id.detailer_other_ways_youheard_about_zinc)).getText().toString());
        if (((Spinner)getActivity(). findViewById(R.id.detailer_do_you_stock_zinc)).getSelectedItem().toString().equals("Yes")) {
            detailerCallInstance.setHowManyZincInStock(Integer.parseInt(((EditText)getActivity(). findViewById(R.id.detailers_howmany_in_stock_zinc)).getText().toString()));
            detailerCallInstance.setHowmanyOrsInStock(Integer.parseInt(((EditText)getActivity(). findViewById(R.id.detailers_howmany_in_stock_ors)).getText().toString()));
            detailerCallInstance.setZincBrandsold(((EditText)getActivity(). findViewById(R.id.detailers_brand_sold_zinc)).getText().toString());
            detailerCallInstance.setOrsBrandSold(((EditText)getActivity(). findViewById(R.id.detailers_brand_sold_ors)).getText().toString());
        }

        detailerCallInstance.setIfNoWhy(((EditText)getActivity(). findViewById(R.id.detailer_if_no_why)).getText().toString());
        detailerCallInstance.setBuyingPriceZinc(Double.parseDouble(((EditText) getActivity().findViewById(R.id.detailer_whatpricedoyoubuyzinc)).getText().toString()));
        detailerCallInstance.setBuyingPriceOrs(Double.parseDouble(((EditText) getActivity().findViewById(R.id.detailer_whatpricedoyoubuyors)).getText().toString()));
        detailerCallInstance.setPointOfsaleMaterial(((Button)getActivity(). findViewById(R.id.detailer_point_of_sale)).getText().toString()
                +","+((EditText)getActivity().findViewById(R.id.detailer_point_of_sale_others)).getText().toString());
        detailerCallInstance.setRecommendationNextStep(((Spinner)getActivity(). findViewById(R.id.detailer_next_step_recommendation)).getSelectedItem().toString());

        detailerCallInstance.setHeardAboutDiarrheaTreatmentInChildren(((Spinner)getActivity(). findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors)).getSelectedItem().toString());
        detailerCallInstance.setHowDidYouHear(((Spinner)getActivity(). findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors)).getSelectedItem().toString());
        detailerCallInstance.setWhatYouKnowAbtDiarrhea(((Spinner)getActivity(). findViewById(R.id.detailer_how_diarrhea_affects_community)).getSelectedItem().toString());
        detailerCallInstance.setDiarrheaEffectsOnBody(((Spinner) getActivity().findViewById(R.id.detailer_effect_diarrhea_has_on_the_body)).getSelectedItem().toString());
        detailerCallInstance.setKnowledgeAbtOrsAndUsage(((Spinner)getActivity(). findViewById(R.id.detailer_how_ors_should_be_used)).getSelectedItem().toString());
        detailerCallInstance.setWhyNotUseAntibiotics(((Spinner)getActivity(). findViewById(R.id.detailer_why_should_not_use_antibiotics)).getSelectedItem().toString());
        String stocksZinc = ((Spinner) getActivity().findViewById(R.id.detailer_do_you_stock_zinc)).getSelectedItem().toString();
        detailerCallInstance.setDoYouStockOrsZinc(stocksZinc.equalsIgnoreCase("Yes") ? true : false);
        detailerCallInstance.setRecommendationLevel(((Spinner)getActivity(). findViewById(R.id.detailer_recommendation_level)).getSelectedItem().toString());
        detailerCallInstance.setKnowledgeAbtZincAndUsage(((Spinner)getActivity(). findViewById(R.id.detailer_how_zinc_should_be_used)).getSelectedItem().toString());
        detailerCallInstance.setLatitude(capturedLatitude);
        detailerCallInstance.setLongitude(capturedLongitude);
    }

    private void bindDetailerCallToUi(View view) {
        if (!detailerCallInstance.getIsNew()) {
            Customer customer = detailerCallInstance.getTask().getCustomer();
            ((EditText)view. findViewById(R.id.detailer_survey_date)).setText(Utils.dateToString(detailerCallInstance.getDateOfSurvey()));
            ((TextView) view.findViewById(R.id.detailer_name)).setText(customer.getOutletName());
            ((TextView)view. findViewById(R.id.detailer_desc_location)).setText(customer.getDescriptionOfOutletLocation());
            ((TextView)view. findViewById(R.id.detailer_subcounty)).setText(customer.getSubcounty().getName());
            ((TextView)view. findViewById(R.id.detailer_outlet_size)).setText(customer.getOutletSize());
            ((TextView)view. findViewById(R.id.detailers_gps_text)).setText(detailerCallInstance.getLatitude()+","+ detailerCallInstance.getLongitude());
            CustomerContact keyCustomerContact = Utils.getKeyCustomerContact(customer.getCustomerContacts());
            if(keyCustomerContact!= null){
                ((TextView)view. findViewById(R.id.detailer_key_retailer_name)).setText(keyCustomerContact.getNames());
                ((TextView)view. findViewById(R.id.detailer_key_retailer_contact)).setText(keyCustomerContact.getContact());
            }
            ((EditText)view. findViewById(R.id.detailers_gps_text)).setText(customer.getLatitude() + "," + customer.getLongitude());
            ((EditText)view.findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility)).setText(detailerCallInstance.getDiarrheaPatientsInFacility() + "");
            ((EditText)view.findViewById(R.id.detailer_other_ways_youheard_about_zinc)).setText(detailerCallInstance.getOtherWaysHowYouHeard());
            ((EditText)view. findViewById(R.id.detailers_howmany_in_stock_zinc)).setText(detailerCallInstance.getHowManyZincInStock()+"");
            ((EditText)view.findViewById(R.id.detailers_howmany_in_stock_ors)).setText(detailerCallInstance.getHowmanyOrsInStock()+"");
            ((EditText)view. findViewById(R.id.detailers_brand_sold_zinc)).setText(detailerCallInstance.getZincBrandsold());
            ((EditText)view.findViewById(R.id.detailers_brand_sold_ors)).setText(detailerCallInstance.getOrsBrandSold());
            ((EditText)view. findViewById(R.id.detailer_if_no_why)).setText(detailerCallInstance.getIfNoWhy());
            ((EditText)view.findViewById(R.id.detailer_whatpricedoyoubuyzinc)).setText(detailerCallInstance.getBuyingPriceZinc() + "");
            ((EditText)view.findViewById(R.id.detailer_whatpricedoyoubuyors)).setText(detailerCallInstance.getBuyingPriceOrs() + "");
            ((EditText)view.findViewById(R.id.detailers_gps_text)).setText(detailerCallInstance.getLatitude() == null ? "0.0,0.0" : detailerCallInstance.getLatitude() + ","+ detailerCallInstance.getLongitude());

            //spinners

            Spinner detailerHearAboutSpinner = (Spinner)view. findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors);
            Utils.setSpinnerSelection(detailerHearAboutSpinner, detailerCallInstance.getHeardAboutDiarrheaTreatmentInChildren());

            Spinner howdidyouHearSpinner = (Spinner) view.findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors);
            Utils.setSpinnerSelection(howdidyouHearSpinner, detailerCallInstance.getHowDidYouHear());

            Spinner effectsOfDiarrheaSpinner = (Spinner)view. findViewById(R.id.detailer_how_diarrhea_affects_community);
            Utils.setSpinnerSelection(effectsOfDiarrheaSpinner, detailerCallInstance.getWhatYouKnowAbtDiarrhea());

            Spinner effectontheBodySpinner = (Spinner)view. findViewById(R.id.detailer_effect_diarrhea_has_on_the_body);
            Utils.setSpinnerSelection(effectontheBodySpinner, detailerCallInstance.getDiarrheaEffectsOnBody());

            Spinner orsUsageSpinner = (Spinner)view. findViewById(R.id.detailer_how_ors_should_be_used);
            Utils.setSpinnerSelection(orsUsageSpinner, detailerCallInstance.getKnowledgeAbtOrsAndUsage());

            Spinner dontUseAntiBioticsSpinner = (Spinner)view. findViewById(R.id.detailer_why_should_not_use_antibiotics);
            Utils.setSpinnerSelection(dontUseAntiBioticsSpinner, detailerCallInstance.getWhyNotUseAntibiotics());

            Spinner doyouStockZincSpinner = (Spinner)view. findViewById(R.id.detailer_do_you_stock_zinc);
            Boolean doYouStockOrsZinc = detailerCallInstance.getDoYouStockOrsZinc();
            Utils.setSpinnerSelection(doyouStockZincSpinner, doYouStockOrsZinc?"Yes":"");

            Spinner nextStepRecommendation = ((Spinner)view. findViewById(R.id.detailer_next_step_recommendation));
            Utils.setSpinnerSelection(nextStepRecommendation, detailerCallInstance.getRecommendationNextStep());

            Spinner recommendationSpinner = (Spinner)view. findViewById(R.id.detailer_recommendation_level);
            Utils.setSpinnerSelection(recommendationSpinner, detailerCallInstance.getRecommendationLevel());

            Spinner whatdoyouknowAboutZinc = (Spinner)view.findViewById(R.id.detailer_how_zinc_should_be_used);
            Utils.setSpinnerSelection(whatdoyouknowAboutZinc, detailerCallInstance.getKnowledgeAbtZincAndUsage());

            Button pointOfSaleMaterial =  ((Button)view. findViewById(R.id.detailer_point_of_sale));
            pointOfSaleMaterial.setText(detailerCallInstance.getPointOfsaleMaterial());

        }else{
            Calendar calendar = Calendar.getInstance();
            ((EditText)view. findViewById(R.id.detailer_survey_date)).setText(calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.YEAR));
            Customer customer = callDataTask.getCustomer();
            if(customer!=null){
                ((TextView) view.findViewById(R.id.detailer_subcounty)).setText(customer.getSubcounty().getName());
                ((TextView)view. findViewById(R.id.detailer_outlet_size)).setText(customer.getOutletSize());
                ((TextView)view. findViewById(R.id.detailer_name)).setText(customer.getOutletName());
                ((TextView)view. findViewById(R.id.detailer_desc_location)).setText(customer.getDescriptionOfOutletLocation());
                ((EditText)view. findViewById(R.id.detailers_gps_text)).setText(customer.getLatitude() + "," + customer.getLongitude());
                CustomerContact keyCustomerContact = Utils.getKeyCustomerContact(customer.getCustomerContacts());
                ((TextView)view. findViewById(R.id.detailer_key_retailer_name)).setText(keyCustomerContact!= null?keyCustomerContact.getNames():"");
                ((TextView)view.findViewById(R.id.detailer_key_retailer_contact)).setText(keyCustomerContact!= null?keyCustomerContact.getContact():"");
            }
        }
        setRequiredFields(view);
    }

    private boolean saveDetailerCall() {
        boolean isSaved = false;
        try {
            bindUiToDetailerCall();
            detailerCallInstance.setTaskId(callDataTask.getUuid());
            detailerCallInstance.setTask(callDataTask);
            if(detailerCallInstance.getIsNew()){
                detailerCallInstance.setUuid(UUID.randomUUID().toString());
                detailerCallInstance.setIsNew(false);
                detailerCallDao.insert(detailerCallInstance);
            }else{
                detailerCallDao.update(detailerCallInstance);
            }
            callDataTask.setStatus(TasksMainActivity.STATUS_COMPLETE);
            taskDao.update(callDataTask);
            isSaved = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isSaved;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.new_customer_form_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.customer_form_home:
                Intent i = new Intent(getActivity(), HomeActivity.class);
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

    private void setRequiredFields(View view){
        setRequired((TextView)view.findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility_view));
        setRequired((TextView)view.findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors_view));
        setRequired((TextView)view.findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors_view));
        setRequired((TextView)view.findViewById(R.id.detailer_how_diarrhea_affects_community_view));
        setRequired((TextView)view.findViewById(R.id.detailer_effect_diarrhea_has_on_the_body_view));
        setRequired((TextView)view.findViewById(R.id.detailer_how_ors_should_be_used_view));
        setRequired((TextView)view.findViewById(R.id.detailer_how_zinc_should_be_used_view));
        setRequired((TextView)view.findViewById(R.id.detailer_why_should_not_use_antibiotics_view));
    }


    protected void showPointOfSaleDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Point of Sale Material")
                .setMultiChoiceItems(pointOfSalesOptions,selections,new DialogSelectionClickHandler())
                .setPositiveButton("OK", new DialogButtonClickHandler());
        dialog.show();
    }


    public class DialogButtonClickHandler implements DialogInterface.OnClickListener
    {
        public void onClick( DialogInterface dialog, int clicked )
        {
            switch( clicked )
            {
                case DialogInterface.BUTTON_POSITIVE:
                    setSelectedOptions();
                    break;
            }
        }
    }

    public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener
    {
        public void onClick( DialogInterface dialog, int clicked, boolean selected )
        {
            Log.i( "ME================================", pointOfSalesOptions[clicked] + " selected: " + selected );
        }

    }

    private void setSelectedOptions() {
        pointOfSalesOptionsButton.setText("");
        for( int i = 0; i < pointOfSalesOptions.length; i++ ){
            Log.i( "ME", pointOfSalesOptions[ i ] + " selected: " + selections[i] );
            if (selections[i] && !pointOfSalesOptions[i].toString().equalsIgnoreCase("others")) {
                pointOfSalesOptionsButton.setText((pointOfSalesOptionsButton.getText().toString().equals("")?"":pointOfSalesOptionsButton.getText() + ",") + pointOfSalesOptions[i]);
            }else  if (selections[i] && pointOfSalesOptions[i].toString().equalsIgnoreCase("others")){
                managePointOfSaleOthers(getView(),true);
            }
        }
    }

    private void managePointOfSaleOthers(View view,boolean isShow) {
        LinearLayout pointOfSalesOthersLayout = (LinearLayout)view.findViewById(R.id.detailer_point_of_sale_others_layout);
        if (isShow) {
            pointOfSalesOthersLayout.setVisibility(View.VISIBLE);
        } else {
            pointOfSalesOthersLayout.setVisibility(View.GONE);
        }
    }

}