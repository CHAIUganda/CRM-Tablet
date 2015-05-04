package org.chai.activities.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.chai.R;
import org.chai.activities.HomeActivity;
import org.chai.adapter.CustomerAutocompleteAdapter;
import org.chai.model.Customer;
import org.chai.model.DetailerCall;
import org.chai.model.Task;
import org.chai.util.CustomMultSelectDropDown;
import org.chai.util.Utils;
import org.chai.util.customwidget.GpsWidgetView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 1/28/15.
 */
public class AdhockDetailerFrgment extends BaseDetailerFragment {

    private Customer customer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.adhoc_detailer_form, container, false);
        initialiseGreenDao();

        initDetailerInstance();
        List<Customer> customersList = customerDao.loadAll();
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.adhock_detailer_customer);
        CustomerAutocompleteAdapter adapter = new CustomerAutocompleteAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line,new ArrayList<Customer>(customersList));
        textView.setAdapter(adapter);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view1, int position, long l) {
                Customer selected = (Customer) adapterView.getAdapter().getItem(position);
                customer = selected;
                if(customer!=null){
                    ((TextView)view. findViewById(R.id.adhoc_detailer_district)).setText(customer.getSubcounty().getDistrict().getName());
                    ((TextView)view. findViewById(R.id.adhoc_detailer_subcounty)).setText(customer.getSubcounty().getName());
                    detailerCallInstance = getLastDetailerInfo(customer);
                    if (detailerCallInstance == null) {
                        initDetailerInstance();
                    }
                    detailerCallInstance.setIsNew(true);
                    detailerCallInstance.setIsHistory(false);
                    bindDetailerCallToUi(view);
                }
            }
        });

        manageDoyouStockZincResponses(view);
        manageDoyouStockOrsResponses(view);
        manageHowDidyouHearOtherOption(view);
        manageHaveYouHeardAboutDiarheaTreatment(view);
        managePointOfSaleWidget(view);
        CustomMultSelectDropDown recommendationNextStep = (CustomMultSelectDropDown)view.findViewById(R.id.detailer_next_step_recommendation);
        recommendationNextStep.setStringOptions(getResources().getStringArray(R.array.recommendation_nextstep));

        Button saveDetailetCallBtn = (Button) view.findViewById(R.id.adhoc_detailer_submit_btn);
        saveDetailetCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if(!allMandatoryFieldsFilled(view)){
                    Utils.showError(getActivity(),"Error:","Please fill in all the mandaory fields");
                }else if(saveForm()){
                    Utils.showError(getActivity(),"Infor","Detailer Information has been  successfully added!");
                    //resetFragment(R.id.frame_container, new AdhockDetailerFrgment());
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    startActivity(i);
                } else {
                    Utils.showError(getActivity(),"Error:","A problem Occured while saving a new Detialer Information,please ensure that data is entered correctly");
                }
            }
        });
        managePointOfSaleOthers(view, false);
        setRequiredFields(view);
        addZincStockToTable(view,detailerCallInstance);
        addOrsStockToTable(view, detailerCallInstance);

        return view;
    }

    @Override
    protected DetailerCall initDetailerInstance() {
        detailerCallInstance = new DetailerCall(null);
        detailerCallInstance.setIsHistory(false);
        detailerCallInstance.setIsNew(true);
        return detailerCallInstance;
    }

    @Override
    protected boolean saveForm() {
        try{
            bindUiToDetailerCall();
            Task task = new Task(UUID.randomUUID().toString());
            task.setStatus(TaskMainFragment.STATUS_COMPLETE);
            task.setCustomer(customer);
            task.setDescription("Go check on "+customer.getOutletName());
            task.setType("detailer");
            task.setIsAdhock(true);

            taskDao.insert(task);
            detailerCallInstance.setTask(task);
            detailerCallInstance.setTaskId(task.getUuid());

            detailerCallInstance.setUuid(UUID.randomUUID().toString());
            detailerCallInstance.setIsNew(false);
            detailerCallDao.insert(detailerCallInstance);
            submitDetailerStock(detailerCallInstance);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected void bindUiToDetailerCall() {
        detailerCallInstance.setDateOfSurvey(new Date());
        detailerCallInstance.setDiarrheaPatientsInFacility(Integer.parseInt(((EditText) getActivity().findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility)).getText().toString()));
        detailerCallInstance.setOtherWaysHowYouHeard(((EditText) getActivity().findViewById(R.id.detailer_other_ways_youheard_about_zinc)).getText().toString());

        if (((Spinner)getActivity(). findViewById(R.id.detailer_do_you_stock_zinc)).getSelectedItem().toString().equals("Yes")) {
           /* detailerCallInstance.setHowManyZincInStock(Integer.parseInt(((EditText)getActivity(). findViewById(R.id.detailers_howmany_in_stock_zinc)).getText().toString()));
            detailerCallInstance.setZincBrandsold(((EditText)getActivity(). findViewById(R.id.detailers_brand_sold_zinc)).getText().toString());
            detailerCallInstance.setBuyingPriceZinc(Double.parseDouble(((EditText) getActivity().findViewById(R.id.detailer_whatpricedoyoubuyzinc)).getText().toString()));
            detailerCallInstance.setZincSellingPrice(Double.parseDouble(((EditText) getActivity().findViewById(R.id.detailer_sellingPrice_zinc)).getText().toString()));*/
        }


        if (((Spinner)getActivity(). findViewById(R.id.detailer_do_you_stock_ors)).getSelectedItem().toString().equals("Yes")) {
           /* detailerCallInstance.setHowmanyOrsInStock(Integer.parseInt(((EditText) getActivity().findViewById(R.id.detailers_howmany_in_stock_ors)).getText().toString()));
            detailerCallInstance.setOrsBrandSold(((EditText) getActivity().findViewById(R.id.detailers_brand_sold_ors)).getText().toString());
            detailerCallInstance.setBuyingPriceOrs(Double.parseDouble(((EditText) getActivity().findViewById(R.id.detailer_whatpricedoyoubuyors)).getText().toString()));
            detailerCallInstance.setOrsSellingPrice(Double.parseDouble(((EditText) getActivity().findViewById(R.id.detailer_sellingPrice_ors)).getText().toString()));*/
        }

        detailerCallInstance.setIfNoZincWhy(((EditText) getActivity().findViewById(R.id.detailer_if_no_zinc_why)).getText().toString());
        detailerCallInstance.setIfNoOrsWhy(((EditText) getActivity().findViewById(R.id.detailer_if_no_ors_why)).getText().toString());

        detailerCallInstance.setPointOfsaleMaterial(((Button) getActivity().findViewById(R.id.detailer_point_of_sale)).getText().toString()
                + "," + ((EditText) getActivity().findViewById(R.id.detailer_point_of_sale_others)).getText().toString());
        detailerCallInstance.setRecommendationNextStep(((Button) getActivity().findViewById(R.id.detailer_next_step_recommendation)).getText().toString());

        detailerCallInstance.setHeardAboutDiarrheaTreatmentInChildren(((Spinner) getActivity().findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors)).getSelectedItem().toString());
        detailerCallInstance.setHowDidYouHear(((Spinner) getActivity().findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors)).getSelectedItem().toString());
        detailerCallInstance.setWhatYouKnowAbtDiarrhea(((Spinner) getActivity().findViewById(R.id.detailer_how_diarrhea_affects_community)).getSelectedItem().toString());
        detailerCallInstance.setDiarrheaEffectsOnBody(((Spinner) getActivity().findViewById(R.id.detailer_effect_diarrhea_has_on_the_body)).getSelectedItem().toString());
        detailerCallInstance.setKnowledgeAbtOrsAndUsage(((Spinner) getActivity().findViewById(R.id.detailer_how_ors_should_be_used)).getSelectedItem().toString());
        detailerCallInstance.setWhyNotUseAntibiotics(((Spinner) getActivity().findViewById(R.id.detailer_why_should_not_use_antibiotics)).getSelectedItem().toString());
        detailerCallInstance.setRecommendationLevel(((Spinner) getActivity().findViewById(R.id.detailer_recommendation_level)).getSelectedItem().toString());

        String stocksZinc = ((Spinner) getActivity().findViewById(R.id.detailer_do_you_stock_zinc)).getSelectedItem().toString();
        detailerCallInstance.setDoYouStockZinc(stocksZinc.equalsIgnoreCase("Yes") ? true : false);
        String stocksOrs = ((Spinner) getActivity().findViewById(R.id.detailer_do_you_stock_ors)).getSelectedItem().toString();
        detailerCallInstance.setDoYouStockOrs(stocksOrs.equalsIgnoreCase("Yes") ? true : false);

        detailerCallInstance.setKnowledgeAbtZincAndUsage(((Spinner) getActivity().findViewById(R.id.detailer_how_zinc_should_be_used)).getSelectedItem().toString());
        if(!((GpsWidgetView)getActivity().findViewById(R.id.detailers_gps_view)).getLatLongText().toString().equals("")){
            String latLongText = ((GpsWidgetView) getActivity().findViewById(R.id.detailers_gps_view)).getLatLongText();
            detailerCallInstance.setLatitude(Double.parseDouble(latLongText.split(",")[0]));
            detailerCallInstance.setLongitude(Double.parseDouble(latLongText.split(",")[1]));
        }
        detailerCallInstance.setObjections(((EditText) getActivity().findViewById(R.id.detailer_customer_objections)).getText().toString());
    }

    private void bindDetailerCallToUi(View view) {
        if (detailerCallInstance.getUuid() != null) {
            //since this is adhoc we assume its new
            detailerCallInstance.setUuid(UUID.randomUUID().toString());
            ((TextView)view. findViewById(R.id.adhoc_detailer_district)).setText(customer.getSubcounty().getDistrict().getName());
            ((TextView)view. findViewById(R.id.adhoc_detailer_subcounty)).setText(customer.getSubcounty().getName());
            ((GpsWidgetView)view. findViewById(R.id.detailers_gps_view)).setLatLongText(detailerCallInstance.getLatitude()+","
                    + detailerCallInstance.getLongitude());
            ((GpsWidgetView)view. findViewById(R.id.detailers_gps_view)).setLatLongText(customer.getLatitude() + "," + customer.getLongitude());
            ((EditText)view.findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility)).setText(detailerCallInstance.getDiarrheaPatientsInFacility() + "");
            ((EditText)view.findViewById(R.id.detailer_other_ways_youheard_about_zinc)).setText(detailerCallInstance.getOtherWaysHowYouHeard());
            ((EditText)view. findViewById(R.id.detailer_if_no_zinc_why)).setText(detailerCallInstance.getIfNoZincWhy());
            ((EditText)view. findViewById(R.id.detailer_if_no_ors_why)).setText(detailerCallInstance.getIfNoOrsWhy());
            ((GpsWidgetView)view.findViewById(R.id.detailers_gps_view)).setLatLongText(
                    detailerCallInstance.getLatitude() == null ? "0.0,0.0" : detailerCallInstance.getLatitude() + ","+ detailerCallInstance.getLongitude());

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
            Utils.setSpinnerSelection(doyouStockOrsSpinner, doYouStockOrs ? "Yes" : "No");

            CustomMultSelectDropDown recommendationNextStep = (CustomMultSelectDropDown) view.findViewById(R.id.detailer_next_step_recommendation);
            recommendationNextStep.setText(detailerCallInstance.getRecommendationNextStep());

            Spinner whatdoyouknowAboutZinc = (Spinner)view.findViewById(R.id.detailer_how_zinc_should_be_used);
            Utils.setSpinnerSelection(whatdoyouknowAboutZinc, detailerCallInstance.getKnowledgeAbtZincAndUsage());

            Button pointOfSaleMaterial =  ((Button)view. findViewById(R.id.detailer_point_of_sale));
            pointOfSaleMaterial.setText(detailerCallInstance.getPointOfsaleMaterial());

            Spinner recomendationLevel = (Spinner)view.findViewById(R.id.detailer_recommendation_level);
            Utils.setSpinnerSelection(recomendationLevel, detailerCallInstance.getRecommendationLevel());

        }
    }
}