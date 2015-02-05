package org.chai.activities.tasks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.*;
import android.widget.*;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.WhereCondition;
import org.chai.R;
import org.chai.activities.HomeActivity;
import org.chai.model.*;
import org.chai.util.CustomMultSelectDropDown;
import org.chai.util.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 10/30/14.
 */
public class DetailersActivity extends BaseDetailerFragment {

    private Task callDataTask;
    private Button pointOfSalesOptionsButton;
    private CharSequence[] pointOfSalesOptions;
    private boolean[] selections;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.detailers_form,container, false);
        initialiseGreenDao();
        Bundle bundle = getArguments();
        String callId = bundle.getString("callId");
        if (callId != null) {
            //we are from call data listview
            detailerCallInstance = detailerCallDao.load(callId);
            callDataTask = detailerCallInstance.getTask();
            if (detailerCallInstance.getIsHistory() != null && detailerCallInstance.getIsHistory()) {
                setReadOnly(view);
            }
        } else {
            //from tasklist view
            detailerCallInstance = new DetailerCall(null);
            String taskId = bundle.getString("taskId");
            callDataTask = taskDao.load(taskId);
            detailerCallInstance = getLastDetailerInfo(callDataTask.getCustomer());
            detailerCallInstance.setIsNew(true);
            detailerCallInstance.setIsHistory(false);
        }
        setDateWidget(view);
        setGpsWidget(view);
        List<Village> villageData = villageDao.loadAll();
        Village[] villages = villageData.toArray(new Village[villageData.size()]);
        subcountyTxt = (TextView)view.findViewById(R.id.detailer_subcounty);

        manageDoyouStockZincResponses(view);
        manageDoyouStockOrsResponses(view);
        manageHowDidyouHearOtherOption(view);
        manageHaveYouHeardAboutDiarheaTreatment(view);
        managePointOfSaleWidget(view);
        CustomMultSelectDropDown recommendationNextStep = (CustomMultSelectDropDown)view.findViewById(R.id.detailer_next_step_recommendation);
        recommendationNextStep.setStringOptions(getResources().getStringArray(R.array.recommendation_nextstep));

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
            public void onClick(View view1) {
                if(!allMandatoryFieldsFilled(view)){
                    Toast.makeText(getActivity(), "Please fill in all the mandaory fields", Toast.LENGTH_LONG).show();
                }else if(saveForm()){
                    Toast.makeText(getActivity(), "Detailer Information has been  successfully added!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "A problem Occured while saving a new Detialer Information,please ensure that data is entered correctly", Toast.LENGTH_LONG).show();
                }
            }
        });
        bindDetailerCallToUi(view);
        managePointOfSaleOthers(view, false);
        setRequiredFields(view);
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
        return detailerCall;
    }

    @Override
    protected boolean saveForm() {   boolean isSaved = false;
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
            callDataTask.setStatus(TaskMainFragment.STATUS_COMPLETE);
            taskDao.update(callDataTask);
            isSaved = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isSaved;
    }

    @Override
    protected void bindUiToDetailerCall() {
        detailerCallInstance.setDateOfSurvey(new Date());
        detailerCallInstance.setDiarrheaPatientsInFacility(Integer.parseInt(((EditText)getActivity(). findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility)).getText().toString()));
        detailerCallInstance.setOtherWaysHowYouHeard(((EditText) getActivity().findViewById(R.id.detailer_other_ways_youheard_about_zinc)).getText().toString());
        if (((Spinner)getActivity(). findViewById(R.id.detailer_do_you_stock_zinc)).getSelectedItem().toString().equals("Yes")) {
            detailerCallInstance.setHowManyZincInStock(Integer.parseInt(((EditText)getActivity(). findViewById(R.id.detailers_howmany_in_stock_zinc)).getText().toString()));
            detailerCallInstance.setZincBrandsold(((EditText)getActivity(). findViewById(R.id.detailers_brand_sold_zinc)).getText().toString());
            detailerCallInstance.setBuyingPriceZinc(Double.parseDouble(((EditText) getActivity().findViewById(R.id.detailer_whatpricedoyoubuyzinc)).getText().toString()));
            detailerCallInstance.setZincSellingPrice(Double.parseDouble(((EditText) getActivity().findViewById(R.id.detailer_sellingPrice_zinc)).getText().toString()));
        }


        if (((Spinner)getActivity(). findViewById(R.id.detailer_do_you_stock_ors)).getSelectedItem().toString().equals("Yes")) {
            detailerCallInstance.setHowmanyOrsInStock(Integer.parseInt(((EditText)getActivity(). findViewById(R.id.detailers_howmany_in_stock_ors)).getText().toString()));
            detailerCallInstance.setOrsBrandSold(((EditText)getActivity(). findViewById(R.id.detailers_brand_sold_ors)).getText().toString());
            detailerCallInstance.setBuyingPriceOrs(Double.parseDouble(((EditText) getActivity().findViewById(R.id.detailer_whatpricedoyoubuyors)).getText().toString()));
            detailerCallInstance.setOrsSellingPrice(Double.parseDouble(((EditText) getActivity().findViewById(R.id.detailer_sellingPrice_ors)).getText().toString()));
        }


        detailerCallInstance.setIfNoZincWhy(((EditText) getActivity().findViewById(R.id.detailer_if_no_zinc_why)).getText().toString());
        detailerCallInstance.setIfNoOrsWhy(((EditText) getActivity().findViewById(R.id.detailer_if_no_ors_why)).getText().toString());

        detailerCallInstance.setPointOfsaleMaterial(((Button)getActivity(). findViewById(R.id.detailer_point_of_sale)).getText().toString()
                +","+((EditText)getActivity().findViewById(R.id.detailer_point_of_sale_others)).getText().toString());
        detailerCallInstance.setRecommendationNextStep(((CustomMultSelectDropDown) getActivity().findViewById(R.id.detailer_next_step_recommendation)).getText().toString());

        detailerCallInstance.setHeardAboutDiarrheaTreatmentInChildren(((Spinner)getActivity(). findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors)).getSelectedItem().toString());
        detailerCallInstance.setHowDidYouHear(((Spinner)getActivity(). findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors)).getSelectedItem().toString());
        detailerCallInstance.setWhatYouKnowAbtDiarrhea(((Spinner)getActivity(). findViewById(R.id.detailer_how_diarrhea_affects_community)).getSelectedItem().toString());
        detailerCallInstance.setDiarrheaEffectsOnBody(((Spinner) getActivity().findViewById(R.id.detailer_effect_diarrhea_has_on_the_body)).getSelectedItem().toString());
        detailerCallInstance.setKnowledgeAbtOrsAndUsage(((Spinner)getActivity(). findViewById(R.id.detailer_how_ors_should_be_used)).getSelectedItem().toString());
        detailerCallInstance.setWhyNotUseAntibiotics(((Spinner)getActivity(). findViewById(R.id.detailer_why_should_not_use_antibiotics)).getSelectedItem().toString());

        String stocksZinc = ((Spinner) getActivity().findViewById(R.id.detailer_do_you_stock_zinc)).getSelectedItem().toString();
        detailerCallInstance.setDoYouStockZinc(stocksZinc.equalsIgnoreCase("Yes") ? true : false);
        String stocksOrs = ((Spinner) getActivity().findViewById(R.id.detailer_do_you_stock_ors)).getSelectedItem().toString();
        detailerCallInstance.setDoYouStockOrs(stocksOrs.equalsIgnoreCase("Yes") ? true : false);

        detailerCallInstance.setKnowledgeAbtZincAndUsage(((Spinner)getActivity(). findViewById(R.id.detailer_how_zinc_should_be_used)).getSelectedItem().toString());
        detailerCallInstance.setLatitude(capturedLatitude);
        detailerCallInstance.setLongitude(capturedLongitude);
    }

    @Override
    protected void initDetailerInstance() {

    }

    private void bindDetailerCallToUi(View view) {
        if (detailerCallInstance.getUuid() != null) {
            Customer customer = detailerCallInstance.getTask().getCustomer();
            ((EditText)view. findViewById(R.id.detailer_survey_date)).setText(Utils.dateToString(detailerCallInstance.getDateOfSurvey()));
            ((TextView) view.findViewById(R.id.detailer_name)).setText(customer.getOutletName());
            ((TextView)view. findViewById(R.id.detailer_desc_location)).setText(customer.getDescriptionOfOutletLocation());
            ((TextView)view. findViewById(R.id.detailer_district)).setText(customer.getSubcounty().getDistrict().getName());
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
            ((EditText)view. findViewById(R.id.detailer_if_no_zinc_why)).setText(detailerCallInstance.getIfNoZincWhy());
            ((EditText)view. findViewById(R.id.detailer_if_no_ors_why)).setText(detailerCallInstance.getIfNoOrsWhy());
            ((EditText)view.findViewById(R.id.detailer_whatpricedoyoubuyzinc)).setText(detailerCallInstance.getBuyingPriceZinc() + "");
            ((EditText)view.findViewById(R.id.detailer_whatpricedoyoubuyors)).setText(detailerCallInstance.getBuyingPriceOrs() + "");

            ((EditText)view.findViewById(R.id.detailer_sellingPrice_zinc)).setText(detailerCallInstance.getZincSellingPrice() + "");
            ((EditText)view.findViewById(R.id.detailer_sellingPrice_ors)).setText(detailerCallInstance.getOrsSellingPrice() + "");
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
            Boolean doYouStockOrsZinc = detailerCallInstance.getDoYouStockZinc();
            Utils.setSpinnerSelection(doyouStockZincSpinner, doYouStockOrsZinc?"Yes":"No");

            Spinner doyouStockOrsSpinner = (Spinner)view. findViewById(R.id.detailer_do_you_stock_ors);
            Boolean doYouStockOrs = detailerCallInstance.getDoYouStockOrs();
            Utils.setSpinnerSelection(doyouStockOrsSpinner, doYouStockOrs?"Yes":"No");

            CustomMultSelectDropDown recommendationNextStep = (CustomMultSelectDropDown) view.findViewById(R.id.detailer_next_step_recommendation);
            recommendationNextStep.setText(detailerCallInstance.getRecommendationNextStep());

            Spinner whatdoyouknowAboutZinc = (Spinner)view.findViewById(R.id.detailer_how_zinc_should_be_used);
            Utils.setSpinnerSelection(whatdoyouknowAboutZinc, detailerCallInstance.getKnowledgeAbtZincAndUsage());

            Button pointOfSaleMaterial =  ((Button)view. findViewById(R.id.detailer_point_of_sale));
            pointOfSaleMaterial.setText(detailerCallInstance.getPointOfsaleMaterial());

        }else{
            Calendar calendar = Calendar.getInstance();
            ((EditText)view. findViewById(R.id.detailer_survey_date)).setText(calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.YEAR));
            Customer customer = callDataTask.getCustomer();
            if(customer!=null){
                ((TextView)view. findViewById(R.id.detailer_district)).setText(customer.getSubcounty().getDistrict().getName());
                ((TextView) view.findViewById(R.id.detailer_subcounty)).setText(customer.getSubcounty().getName());
                ((TextView)view. findViewById(R.id.detailer_outlet_size)).setText(customer.getOutletSize());
                ((TextView)view. findViewById(R.id.detailer_name)).setText(customer.getOutletName());
                ((TextView)view. findViewById(R.id.detailer_desc_location)).setText(customer.getDescriptionOfOutletLocation());
                ((EditText)view. findViewById(R.id.detailers_gps_text)).setText(customer.getLatitude() + "," + customer.getLongitude());
                CustomerContact keyCustomerContact = Utils.getKeyCustomerContact(customer.getCustomerContacts());
                ((TextView)view. findViewById(R.id.detailer_key_retailer_name)).setText(keyCustomerContact!= null?keyCustomerContact.getNames():"");
                ((TextView)view.findViewById(R.id.detailer_key_retailer_contact)).setText(keyCustomerContact!= null?keyCustomerContact.getContact():"");
                ((EditText)view.findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility)).setText(customer.getNumberOfCustomersPerDay() + "");
            }
        }
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

    private void setReadOnly(View view) {
        view.findViewById(R.id.detailer_survey_date).setEnabled(false);
        view.findViewById(R.id.detailer_name).setEnabled(false);
        view.findViewById(R.id.detailer_desc_location).setEnabled(false);
        view.findViewById(R.id.detailer_subcounty).setEnabled(false);
        view.findViewById(R.id.detailer_outlet_size).setEnabled(false);
        view.findViewById(R.id.detailers_gps_text).setEnabled(false);
        view.findViewById(R.id.detailer_key_retailer_name).setEnabled(false);
        view.findViewById(R.id.detailer_key_retailer_contact).setEnabled(false);
        view.findViewById(R.id.detailers_gps_text).setEnabled(false);
        view.findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility).setEnabled(false);
        view.findViewById(R.id.detailer_other_ways_youheard_about_zinc).setEnabled(false);
        view.findViewById(R.id.detailers_howmany_in_stock_zinc).setEnabled(false);
        view.findViewById(R.id.detailers_howmany_in_stock_ors).setEnabled(false);
        view.findViewById(R.id.detailers_brand_sold_zinc).setEnabled(false);
        view.findViewById(R.id.detailers_brand_sold_ors).setEnabled(false);
        view.findViewById(R.id.detailer_if_no_zinc_why).setEnabled(false);
        view.findViewById(R.id.detailer_if_no_ors_why).setEnabled(false);
        view.findViewById(R.id.detailer_whatpricedoyoubuyzinc).setEnabled(false);
        view.findViewById(R.id.detailer_whatpricedoyoubuyors).setEnabled(false);
        view.findViewById(R.id.detailers_gps_text).setEnabled(false);

        //spinners
        view.findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors).setEnabled(false);
        view.findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors).setEnabled(false);
        view.findViewById(R.id.detailer_how_diarrhea_affects_community).setEnabled(false);
        view.findViewById(R.id.detailer_effect_diarrhea_has_on_the_body).setEnabled(false);
        view.findViewById(R.id.detailer_how_ors_should_be_used).setEnabled(false);
        view.findViewById(R.id.detailer_why_should_not_use_antibiotics).setEnabled(false);
        view.findViewById(R.id.detailer_do_you_stock_zinc).setEnabled(false);
        view.findViewById(R.id.detailer_next_step_recommendation).setEnabled(false);
        view.findViewById(R.id.detailer_how_zinc_should_be_used).setEnabled(false);
        view.findViewById(R.id.detailer_point_of_sale).setEnabled(false);
        view.findViewById(R.id.detailer_submit_btn).setEnabled(false);
    }

}