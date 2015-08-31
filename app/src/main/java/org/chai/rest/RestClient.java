package org.chai.rest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Created by victor on 11/3/14.
 */
public class RestClient {
    public static boolean PRODUCTION_SERVER = true;
    public static String userName;
    public static String password;
    private static String role;
    private static String PROD_REST_URL = "http://23.239.27.196:8080/web-crm/rest/";
    //public static String REST_URL = "http://192.168.1.107:8080/chai-crm/rest/";
    private static String TEST_REST_URL = "http://178.79.178.121:8080/test-web-crm/rest/";

    public static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(new MediaType("application", "json")));
        headers.set("device-imei", "Samsung Galaxy S3");
        headers.set("app-version-code",getVersionCode(MyApplication.getContext())+"");
        headers.set("app-version-name",getVersionName(MyApplication.getContext())+"");
        HttpAuthentication authHeader = new HttpBasicAuthentication(userName, password);
        headers.setAuthorization(authHeader);
        return headers;
    }

    public static String getRestUrl(){
        Utils.log("Getting rest URL -> Production " + PRODUCTION_SERVER);
        return (PRODUCTION_SERVER) ? PROD_REST_URL : TEST_REST_URL;
    }

    public static String getRole(){
        return role;
    }

    public static void setRole(String r){
        role = r;
    }

    public static  HttpEntity<?> getRequestEntity() {
        HttpEntity<?> requestEntity = new HttpEntity<Object>(getHeaders());
        return requestEntity;
    }

    public static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        converter.setObjectMapper(mapper);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "NA";
        }
    }
}
