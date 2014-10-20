package org.chai.util;


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
}
