package org.chai.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.model.Task;
import org.osmdroid.util.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by victor on 10/16/14.
 */
public class Utils {
    public static void log(String msg){
        Log.d("CHAI", msg);
    }

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
        byte[]  decrypted = Base64.decode(encryptedText.getBytes(), Base64.NO_WRAP);
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

    public static void showError(Activity activity,String title,String error) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(error)
                .setPositiveButton("ok", null)
                .show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null){
            return;
        }
        int maxHeight = 0;
        int h = 0;

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            h = listItem.getMeasuredHeight();
            totalHeight += h;
            if(maxHeight < h){
                maxHeight = h;
            }
        }

        totalHeight += (maxHeight * 2);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static ArrayList<View> getViewsByTag(ViewGroup root, String tag){
        ArrayList<View> views = new ArrayList<>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
    }

    public static double getLatFromLatLong(String latlong){
        try{
            String[] split = latlong.split(",");
            return Double.parseDouble(split[0]);
        }catch(Exception ex){
            Utils.log("Error parsing latlong");
        }

        return 0;
    }

    public static double getLongFromLatLong(String latlong){
        try{
            String[] split = latlong.split(",");
            return Double.parseDouble(split[1]);
        }catch(Exception ex){
            Utils.log("Error parsing latlong");
        }

        return 0;
    }

    public static boolean dbTableExists(String tableName, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if(cursor != null) {
            if(cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public static ArrayList<String> generatePriceList(int min, int max, int step, String prepend, String append){
        ArrayList<String> prices = new ArrayList<>();
        prices.add("");
        if(prepend != null){
            prices.add(prepend);
        }
        for(int i = min; i <= max; i += step){
            prices.add(Integer.toString(i));
        }
        if(append != null){
            prices.add(append);
        }
        return prices;
    }

    public static int parseICCMItemPrice(String price){
        int val = 0;
        int increament = 0;
        try{
            if(price.indexOf("<") != -1){ //Less than
                increament = -1;
            }
            if(price.indexOf(">") != -1){ //Greather than
                increament = 1;
            }
            price = price.replaceAll("[^\\d.]", "");
            val = Integer.parseInt(price);
        }catch(Exception ex){
        }
        val = val + increament;
        return val;
    }
}
