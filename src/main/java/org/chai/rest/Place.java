package org.chai.rest;

import android.os.AsyncTask;
import android.util.Log;
import org.chai.model.Region;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

/**
 * Created by victor on 11/3/14.
 */
public class Place {
    public static String REST_URL ="http://23.239.27.196:8080/web-crm/rest/";

    public Place(){}

    public Region[] downloadRegions(String path){
        Log.i("Rest Url====================================================:",REST_URL+path);
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(new MediaType("application","json")));
            headers.set("device-id", UUID.randomUUID().toString());
            HttpAuthentication authHeader = new HttpBasicAuthentication("root", "pass");
            headers.setAuthorization(authHeader);

            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Region[]> responseEntity = restTemplate.exchange(REST_URL+path,HttpMethod.GET,requestEntity,Region[].class);
            Region[] regions = responseEntity.getBody();
            return regions;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}
