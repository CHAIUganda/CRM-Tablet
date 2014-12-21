package org.chai.util;


import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import org.chai.model.CustomerContact;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
        String encrypted = Base64.encodeToString(text.getBytes(),Base64.NO_WRAP);
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

    public static Date addToDate(Date currentDate,int toAdd){
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DAY_OF_YEAR, toAdd);
        return cal.getTime();
    }

}
