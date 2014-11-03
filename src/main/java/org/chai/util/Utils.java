package org.chai.util;


import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.chai.model.CustomerContact;

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
        for(int i=0;i<customerContacts.size();i++){
            if(customerContacts.get(i).getTypeOfContact().equalsIgnoreCase("key")){
                return customerContacts.get(i);
            }
        }
        return customerContacts.get(0);
    }

    public static void setSpinnerSelection(Spinner spinner, String item) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int position = adapter.getPosition(item);
        spinner.setSelection(position);
    }
}
