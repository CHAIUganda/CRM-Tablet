package org.chai.activities.tasks;

import android.app.AlertDialog;
 import android.app.DatePickerDialog;
 import android.content.DialogInterface;
 import android.database.sqlite.SQLiteDatabase;
 import android.graphics.Color;
 import android.text.Spannable;
 import android.text.SpannableStringBuilder;
 import android.text.style.ForegroundColorSpan;
 import android.util.Log;
 import android.view.View;
 import android.widget.*;
 import org.chai.R;
 import org.chai.activities.BaseContainerFragment;
 import org.chai.adapter.DistrictArrayAdapter;
 import org.chai.model.*;
 import org.chai.util.GPSTracker;
 import org.chai.util.StockTableRow;
 import org.chai.util.Utils;
import org.chai.util.customwidget.GpsWidgetView;

import java.util.*;

/**
  * Created by victor on 1/28/15.
  */
 public abstract class BaseDetailerFragment extends BaseContainerFragment {

     protected EditText dateEditTxt;
     protected DatePickerDialog datePickerDialog;
     protected String initialDate;
     protected String initialMonth;
     protected String initialYear;

     protected GPSTracker gpsTracker;

     protected SQLiteDatabase db;
     protected DaoMaster daoMaster;
     protected DaoSession daoSession;
     protected VillageDao villageDao;
     protected TaskDao taskDao;
     protected DetailerCallDao detailerCallDao;
     protected CustomerDao customerDao;
     protected DetailerStockDao detailerStockDao;

     protected TextView subcountyTxt;
     protected DistrictArrayAdapter adapter;

     protected DetailerCall detailerCallInstance;
     protected Task callDataTask;
     protected Button pointOfSalesOptionsButton;
     protected CharSequence[] pointOfSalesOptions;
     protected boolean[] selections;
     protected List<StockTableRow> zincStocks = new ArrayList<StockTableRow>();
     protected List<StockTableRow> orsStocks = new ArrayList<StockTableRow>();
    protected List<DetailerStock> detailerStocksList = new ArrayList<DetailerStock>();

     protected abstract boolean saveForm();

     protected abstract void bindUiToDetailerCall();

     protected abstract void initDetailerInstance();


     protected void initialiseGreenDao() {
         try {
             DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "chai-crm-db", null);
             db = helper.getWritableDatabase();
             daoMaster = new DaoMaster(db);
             daoSession = daoMaster.newSession();
             villageDao = daoSession.getVillageDao();
             taskDao = daoSession.getTaskDao();
             detailerCallDao = daoSession.getDetailerCallDao();
             customerDao = daoSession.getCustomerDao();
             detailerStockDao = daoSession.getDetailerStockDao();
         } catch (Exception ex) {
             Log.d("Error=====================================", ex.getLocalizedMessage());
             Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
         }
     }

     protected void loadCustomerInfo(Customer customer, View view) {
         ((TextView) view.findViewById(R.id.detailer_name)).setText(customer.getOutletName());
         ((TextView) view.findViewById(R.id.detailer_desc_location)).setText(customer.getDescriptionOfOutletLocation());
         ((TextView) view.findViewById(R.id.detailer_subcounty)).setText(customer.getSubcounty().getName());
         ((TextView) view.findViewById(R.id.detailer_outlet_size)).setText(customer.getOutletSize());
         ((GpsWidgetView) view.findViewById(R.id.detailers_gps_view)).setLatLongText(detailerCallInstance.getLatitude() + "," + detailerCallInstance.getLongitude());
         CustomerContact keyCustomerContact = Utils.getKeyCustomerContact(customer.getCustomerContacts());
         if (keyCustomerContact != null) {
             ((TextView) view.findViewById(R.id.detailer_key_retailer_name)).setText(keyCustomerContact.getNames());
             ((TextView) view.findViewById(R.id.detailer_key_retailer_contact)).setText(keyCustomerContact.getContact());
         }
     }

     protected void setDateWidget(View view) {
         Button dateBtn = (Button) view.findViewById(R.id.detailer_survey_date_btn);
         dateEditTxt = (EditText) view.findViewById(R.id.detailer_survey_date);
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
                 Utils.setMinimumDateInDatePicker(new Date(), datePickerDialog);
                 datePickerDialog.show();

             }
         });
     }

     protected class PickDate implements DatePickerDialog.OnDateSetListener {
         @Override
         public void onDateSet(DatePicker view, int year, int monthOfYear,
                               int dayOfMonth) {
             view.updateDate(year, monthOfYear, dayOfMonth);
             dateEditTxt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
             datePickerDialog.hide();
         }
     }

     protected void manageDoyouStockZincResponses(View view) {
         final Spinner spinner = (Spinner) view.findViewById(R.id.detailer_do_you_stock_zinc);
         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                 String selected = (String) spinner.getAdapter().getItem(position);
                 LinearLayout stocTable = (LinearLayout) getActivity().findViewById(R.id.detailer_zinc_stock_table2);
                 LinearLayout ifnowhyLayout = (LinearLayout) getActivity().findViewById(R.id.detailer_if_zincnowhy_layout);
                 if ("No".equalsIgnoreCase(selected)) {
                     stocTable.setVisibility(View.GONE);
                     ifnowhyLayout.setVisibility(View.VISIBLE);
                 } else {
                     stocTable.setVisibility(View.VISIBLE);
                     ifnowhyLayout.setVisibility(View.GONE);
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         });
     }

     protected void manageDoyouStockOrsResponses(View view) {
         final Spinner spinner = (Spinner) view.findViewById(R.id.detailer_do_you_stock_ors);
         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                 String selected = (String) spinner.getAdapter().getItem(position);
                 LinearLayout stockTable = (LinearLayout) getActivity().findViewById(R.id.detailer_ors_stock_table2);
                 LinearLayout ifnowhyLayout = (LinearLayout) getActivity().findViewById(R.id.detailer_if_orsnowhy_layout);
                 if ("No".equalsIgnoreCase(selected)) {
                     stockTable.setVisibility(View.GONE);
                     ifnowhyLayout.setVisibility(View.VISIBLE);
                 } else {
                     stockTable.setVisibility(View.VISIBLE);
                     ifnowhyLayout.setVisibility(View.GONE);
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         });
     }

     protected void manageHaveYouHeardAboutDiarheaTreatment(View view) {
         final Spinner spinner = (Spinner) view.findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors);
         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                 String selected = (String) spinner.getAdapter().getItem(position);
                 LinearLayout howdidYouHearAboutTreatment = (LinearLayout) getActivity().findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors_layout);
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

     protected void manageHowDidyouHearOtherOption(View view) {
         final Spinner spinner = (Spinner) view.findViewById(R.id.detailer_how_did_you_hearabout_zinc_ors);
         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                 String selected = (String) spinner.getAdapter().getItem(position);
                 LinearLayout otherwaysinfoLayout = (LinearLayout) getActivity().findViewById(R.id.detailer_howdidyouhearaboutzinc_other_layout);
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

     protected void setRequiredFields(View view) {
         setRequired((TextView) view.findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility_view));
         setRequired((TextView) view.findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors_view));
         setRequired((TextView) view.findViewById(R.id.detailer_how_ors_should_be_used_view));
         setRequired((TextView) view.findViewById(R.id.detailer_how_zinc_should_be_used_view));
         setRequired((TextView) view.findViewById(R.id.detailer_why_should_not_use_antibiotics_view));
         setRequired((TextView) view.findViewById(R.id.detailer_do_you_stock_zinc_view));
         setRequired((TextView) view.findViewById(R.id.detailer_point_of_sale_view));
         setRequired((TextView) view.findViewById(R.id.detailer_recommendation_next_step_view));
     }

     private void setRequired(TextView textView) {
         String required = "*";
         SpannableStringBuilder builder = new SpannableStringBuilder();
         builder.append(textView.getText().toString());
         int start = builder.length();
         builder.append(required);
         int end = builder.length();
         builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
         textView.setText(builder);
     }

     protected void managePointOfSaleWidget(View view) {
         pointOfSalesOptionsButton = (Button) view.findViewById(R.id.detailer_point_of_sale);
         pointOfSalesOptions = getResources().getStringArray(R.array.point_of_sale_material);
         selections = new boolean[pointOfSalesOptions.length];

         pointOfSalesOptionsButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 showPointOfSaleDialog();
             }
         });
     }


     protected void showPointOfSaleDialog() {
         AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
         dialog.setTitle("Point of Sale Material")
                 .setMultiChoiceItems(pointOfSalesOptions, selections, new DialogSelectionClickHandler())
                 .setPositiveButton("OK", new DialogButtonClickHandler());
         dialog.show();
     }

     public class DialogButtonClickHandler implements DialogInterface.OnClickListener {
         public void onClick(DialogInterface dialog, int clicked) {
             switch (clicked) {
                 case DialogInterface.BUTTON_POSITIVE:
                     setSelectedOptions();
                     break;
             }
         }
     }

     public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener {
         public void onClick(DialogInterface dialog, int clicked, boolean selected) {
             Log.i("ME================================", pointOfSalesOptions[clicked] + " selected: " + selected);
         }

     }

     protected void setSelectedOptions() {
         pointOfSalesOptionsButton.setText("");
         for (int i = 0; i < pointOfSalesOptions.length; i++) {
             Log.i("ME", pointOfSalesOptions[i] + " selected: " + selections[i]);
             if (selections[i] && !pointOfSalesOptions[i].toString().equalsIgnoreCase("others")) {
                 pointOfSalesOptionsButton.setText((pointOfSalesOptionsButton.getText().toString().equals("") ? "" : pointOfSalesOptionsButton.getText() + ",") + pointOfSalesOptions[i]);
             } else if (selections[i] && pointOfSalesOptions[i].toString().equalsIgnoreCase("others")) {
                 managePointOfSaleOthers(getView(), true);
             }
         }
     }


     protected void managePointOfSaleOthers(View view, boolean isShow) {
         LinearLayout pointOfSalesOthersLayout = (LinearLayout) view.findViewById(R.id.detailer_point_of_sale_others_layout);
         if (isShow) {
             pointOfSalesOthersLayout.setVisibility(View.VISIBLE);
         } else {
             pointOfSalesOthersLayout.setVisibility(View.GONE);
         }
     }

     protected boolean allMandatoryFieldsFilled(View view) {
         if (((EditText) view.findViewById(R.id.detailer_how_many_diarrhea_patients_in_facility)).getText().toString().equals("")) {
             return false;
         } else if (((Spinner) view.findViewById(R.id.detailer_hearabout_treatment_with_zinc_ors)).getSelectedItem().toString().equals("")) {
             return false;
         } else if (((Spinner) view.findViewById(R.id.detailer_effect_diarrhea_has_on_the_body)).getSelectedItem().toString().equals("")) {
             return false;
         } else if (((Spinner) view.findViewById(R.id.detailer_how_ors_should_be_used)).getSelectedItem().toString().equals("")) {
             return false;
         } else if (((Spinner) view.findViewById(R.id.detailer_how_zinc_should_be_used)).getSelectedItem().toString().equals("")) {
             return false;
         } else if (((Spinner) view.findViewById(R.id.detailer_why_should_not_use_antibiotics)).getSelectedItem().toString().equals("")) {
             return false;
         } else if (((Spinner) view.findViewById(R.id.detailer_do_you_stock_zinc)).getSelectedItem().toString().equals("")) {
             return false;
         } else if (((Button) view.findViewById(R.id.detailer_next_step_recommendation)).getText().toString().equals("")) {
             return false;
         }
         return true;
     }


     protected void getZincStockFromUI() {
         detailerCallInstance.getDetailerStocks().clear();
         for (StockTableRow stockTableRow : zincStocks) {
             addStockToDetailer(detailerCallInstance, stockTableRow.getBrandName(), Double.parseDouble(stockTableRow.getStockLevel()),
                     Double.parseDouble(stockTableRow.getBuyingPrice()),
                     Double.parseDouble(stockTableRow.getSellingPrice()), "zinc");
         }

     }


     protected void getOrsStockFromUI() {
         for (StockTableRow stockTableRow : orsStocks) {
             addStockToDetailer(detailerCallInstance, stockTableRow.getBrandName(), Double.parseDouble(stockTableRow.getStockLevel()),
                     Double.parseDouble(stockTableRow.getBuyingPrice()),
                     Double.parseDouble(stockTableRow.getSellingPrice()), "ors");
         }
     }

     private void addStockToDetailer(DetailerCall detailerCallInstance,
                                     String brand, double stockLevel, double buyingPrice, double sellingPrice, String category) {
         DetailerStock detailerStock = new DetailerStock(null);
         detailerStock.setBrand(brand);
         detailerStock.setCategory(category);
         detailerStock.setStockLevel(stockLevel);
         detailerStock.setBuyingPrice(buyingPrice);
         detailerStock.setSellingPrice(sellingPrice);
         detailerStock.setDetailerCall(detailerCallInstance);
         detailerStock.setDetailerId(detailerCallInstance.getUuid());
         detailerStocksList.add(detailerStock);
     }

     protected void addZincStockToTable(View view,DetailerCall detailerCall) {
         List<DetailerStock> detailerStocks = new ArrayList<DetailerStock>();
         TableLayout tableLayout = (TableLayout) view.findViewById(R.id.detailer_zinc_stock_table2);
         if(detailerCall.getUuid() != null){
              detailerStocks = detailerStockDao.queryBuilder().where(DetailerStockDao.Properties.Category.eq("zinc"),
                     DetailerStockDao.Properties.DetailerId.eq(detailerCallInstance.getUuid())).list();
         }
         TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
         if (detailerStocks.isEmpty()) {
             StockTableRow dtzinc = new StockTableRow(view.getContext(), "DT Zinc", "", "", "");
             tableLayout.addView(dtzinc, params);
             zincStocks.add(dtzinc);

             StockTableRow zincocet = new StockTableRow(view.getContext(), "Zincocet", "", "", "");
             tableLayout.addView(zincocet, params);
             zincStocks.add(zincocet);

             StockTableRow zinkid = new StockTableRow(view.getContext(), "Zinkid", "", "", "");
             tableLayout.addView(zinkid, params);
             zincStocks.add(zinkid);

             StockTableRow sulphate = new StockTableRow(view.getContext(), "Zinc Sulphate", "", "", "");
             tableLayout.addView(sulphate, params);
             zincStocks.add(sulphate);

             StockTableRow zincos = new StockTableRow(view.getContext(), "Zincos", "", "", "");
             tableLayout.addView(zincos, params);
             zincStocks.add(zincos);
         } else {
             for (DetailerStock stock : detailerStocks) {
                 StockTableRow dtzinc = new StockTableRow(view.getContext(), stock.getBrand(), stock.getStockLevel() + "", stock.getBuyingPrice() + "", stock.getSellingPrice() + "");
                 tableLayout.addView(dtzinc, params);
                 zincStocks.add(dtzinc);
             }
         }
     }

     protected void addOrsStockToTable(View view,DetailerCall detailerCall) {
         List<DetailerStock> detailerStocks = new ArrayList<DetailerStock>();
         if(detailerCall.getUuid() != null){
             detailerStocks = detailerStockDao.queryBuilder().where(DetailerStockDao.Properties.Category.eq("ors"),
                     DetailerStockDao.Properties.DetailerId.eq(detailerCallInstance.getUuid())).list();
         }
         TableLayout tableLayout = (TableLayout) view.findViewById(R.id.detailer_ors_stock_table2);
         TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
         if (detailerStocks.isEmpty()) {
             StockTableRow oralyte = new StockTableRow(view.getContext(), "Oralyte", "", "", "");
             tableLayout.addView(oralyte, params);
             orsStocks.add(oralyte);

             StockTableRow restors = new StockTableRow(view.getContext(), "Restors", "", "", "");
             tableLayout.addView(restors, params);
             orsStocks.add(restors);

             StockTableRow revive = new StockTableRow(view.getContext(), "Revive", "", "", "");
             tableLayout.addView(revive, params);
             orsStocks.add(revive);
         } else {
             for (DetailerStock stock : detailerStocks) {
                 StockTableRow dtzinc = new StockTableRow(view.getContext(), stock.getBrand(), stock.getStockLevel() + "", stock.getBuyingPrice() + "", stock.getSellingPrice() + "");
                 tableLayout.addView(dtzinc, params);
                 orsStocks.add(dtzinc);
             }
         }
     }

    protected void submitDetailerStock(DetailerCall detailerCall) {
        if (((Spinner)getActivity(). findViewById(R.id.detailer_do_you_stock_zinc)).getSelectedItem().toString().equals("Yes")) {
            getZincStockFromUI();
        }

        if (((Spinner)getActivity(). findViewById(R.id.detailer_do_you_stock_ors)).getSelectedItem().toString().equals("Yes")) {
            getOrsStockFromUI();
        }
        if(!detailerStocksList.isEmpty()){
            //delete before inserting,temp fix for one to one in green dao
            detailerStockDao.queryBuilder().where(DetailerStockDao.Properties.DetailerId.eq(detailerCallInstance.getUuid())).buildDelete().executeDeleteWithoutDetachingEntities();
        }
        for (DetailerStock stock : detailerStocksList) {
            stock.setUuid(UUID.randomUUID().toString());
            detailerStockDao.insert(stock);
        }
    }


}
