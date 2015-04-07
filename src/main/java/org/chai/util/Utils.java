package org.chai.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import org.chai.R;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.model.Task;
import org.osmdroid.util.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by victor on 10/16/14.
 */
public class Utils {
    public static String truncateString(String text,int size){
        try{
            if(text.length()<size){
                return text;
            }else{
                return text.substring(0,size);
            }
        }catch (Exception ex){

        }
        return "";
    }

    public static CustomerContact getKeyCustomerContact(List<CustomerContact> customerContacts){
        if(customerContacts.size()>0){
            return customerContacts.get(0);
        }else{
            return null;
        }
    }

    public static void setSpinnerSelection(Spinner spinner, String item) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int position = adapter.getPosition(item);
        spinner.setSelection(position);
    }

    public static Date stringToDate(String dateString){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try{
            return simpleDateFormat.parse(dateString);
        }catch (Exception ex){
        }
        return new Date();
    }

    public static String dateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try{
            String datetime = simpleDateFormat.format(date);
            return datetime;
        }catch (Exception ex){
        }
        return "";
    }
    public static void setRequired(TextView textView){
        String required = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(textView.getText().toString());
        int start = builder.length();
        builder.append(required);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    public static String encrypeString(String text){
        String encrypted = Base64.encodeToString(text.getBytes(), Base64.NO_WRAP);
        return  encrypted;
    }

    public static String decryptString(String encryptedText){
        byte[]  decrypted = Base64.decode(encryptedText.getBytes(),Base64.NO_WRAP);
        return new String(decrypted);
    }

    public static int getItemPosition(String item,String []array){
        for(int i=0;i<array.length;++i){
            if(item.equalsIgnoreCase(array[i])){
                return i;
            }
        }
        return 0;
    }

    public static Date addToDateOffset(Date currentDate, int toAdd){
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(currentDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, toAdd);
        return cal.getTime();
    }
    public static Date addToDateMax(Date currentDate, int toAdd){
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(currentDate);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.add(Calendar.DAY_OF_YEAR, toAdd);
        return cal.getTime();
    }


    public static List<Task> orderAndFilterUsingRealDistanceTo(GeoPoint referencePoint, List<Task> taskList, int radiusInKm) {

        List<Task> filteredTasks = new ArrayList<Task>();
        for (Task task : taskList) {
            try {
                Customer customer = task.getCustomer();
                double pointLatitude = customer.getLatitude() == null ? 0.0 : customer.getLatitude();
                double pointLongitude = customer.getLongitude() == null ? 0.0 : customer.getLongitude();
                Log.i("Task:", pointLatitude + "," + pointLongitude);
                double distanceInKm = getHeversineDistance(referencePoint, new GeoPoint(pointLatitude, pointLongitude));
                Log.i("============================distance in Km:", distanceInKm + "");
                if (distanceInKm <= radiusInKm) {
                    filteredTasks.add(task);
                }
            } catch (Exception ex) {
                //ignore
            }
        }
        return filteredTasks;
    }

    private static double getHeversineDistance(GeoPoint p1,GeoPoint p2){
        final double DEG_RAD = 0.01745329251994;
        final double R_EARTH = 6367.45;
        double haversine, distance;
        double dLat, dLon;
        dLat = (p2.getLatitude() - p1.getLatitude()) * DEG_RAD;
        dLon = (p2.getLongitude() - p1.getLongitude()) * DEG_RAD;

        haversine = Math.sin(dLat * 0.5) * Math.sin(dLat * 0.5) +
                Math.sin(dLon * 0.5) * Math.sin(dLon * 0.5) *
                        Math.cos(p1.getLatitude() * DEG_RAD) *
                        Math.cos(p2.getLatitude() * DEG_RAD);

        distance = Math.asin(Math.sqrt(haversine)) * R_EARTH * 2.0;
        return distance;
    }

    public static Date flattenDate(Date date){
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static void fixUpDatePickerCalendarView(Date date,DatePickerDialog datePicker) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final CalendarView calendarView = datePicker.getDatePicker().getCalendarView();
            if (calendarView != null) {
                Calendar calender = GregorianCalendar.getInstance();
                calender.setTime(date);
                calender.add(Calendar.MONTH, 24);
                calendarView.setDate(calender.getTimeInMillis(), false, true);
                calender.add(Calendar.MONTH, -24);
                calendarView.setDate(calender.getTimeInMillis(), false, true);
            }
        }
    }

    public static void setMinimumDateInDatePicker(Date date,DatePickerDialog datePickerDialog){
        Date currentDate = Utils.flattenDate(date);
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTime());
        fixUpDatePickerCalendarView(currentDate,datePickerDialog);
    }

    public static boolean mandatoryFieldFilled(EditText editText){
        if (editText.getText().toString().equals("")) {
            editText.setError("This Field is mandatory!");
            return false;
        }else{
            return true;
        }
    }

    public static boolean mandatorySpinnerFieldSelected(Spinner spinner){
        if (spinner.getSelectedItem().toString().equals("")) {
            ((TextView)spinner.getChildAt(0)).setError("This Field is mandatory!");
            return false;
        }else{
            return true;
        }
    }

    public static void showError(Activity activity,String title,String error) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(error)
                .setPositiveButton("ok", null)
                .show();
    }

    public static void displayPopupWindow(Activity activity,View anchorView,String message) {
        PopupWindow popup = new PopupWindow(activity);
        View layout = activity.getLayoutInflater().inflate(R.layout.popup, null);
        popup.setContentView(layout);
        TextView popupText = (TextView)layout.findViewById(R.id.popupTxt);
        popupText.setText(message);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(anchorView);
    }


}
