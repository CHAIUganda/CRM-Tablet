package org.chai.util;


import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.chai.model.CustomerContact;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by victor on 10/16/14.
 */
public class Utils {
    public static String truncateString(String text,int size){
        if(text.length()<size){
            return text;
        }else{
            return text.substring(0,size);
        }
    }

    public static CustomerContact getKeyCustomerContact(List<CustomerContact> customerContacts){
        try{
            for(int i=0;i<customerContacts.size();i++){
                if(customerContacts.get(i).getRole().equalsIgnoreCase("key")){
                    return customerContacts.get(i);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static void setSpinnerSelection(Spinner spinner, String item) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int position = adapter.getPosition(item);
        spinner.setSelection(position);
    }

    public static Date stringToDate(String dateString){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            return simpleDateFormat.parse(dateString);
        }catch (Exception ex){
        }
        return new Date();
    }
}
