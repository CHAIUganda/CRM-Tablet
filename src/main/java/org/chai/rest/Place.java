package org.chai.rest;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import org.chai.model.*;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

/**
 * Created by victor on 11/3/14.
 */
public class Place extends RestClient {

    public Place(){}

    public Region[] downloadRegions(){
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Region[]> responseEntity = restTemplate.exchange(REST_URL+"place/regions",HttpMethod.GET,getRequestEntity(),Region[].class);
            Region[] regions = responseEntity.getBody();
            Log.i("REST CLIENT:","found "+regions.length+" Regions");
            return regions;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public District[] downloadDistricts(){
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<District[]> responseEntity = restTemplate.exchange(REST_URL+"place/districts",HttpMethod.GET,getRequestEntity(),District[].class);
            District[] districts = responseEntity.getBody();
            Log.i("REST CLIENT:","found "+districts.length+" Districts");
            return districts;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  null;
    }

    public Subcounty[] downloadSubcounties(){
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Subcounty[]> responseEntity = restTemplate.exchange(REST_URL+"place/subCounties",HttpMethod.GET,getRequestEntity(),Subcounty[].class);
            Subcounty[] subcounties = responseEntity.getBody();
            Log.i("REST CLIENT:","found "+subcounties.length+" Subcounties");
            return subcounties;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  null;
    }

    public void uploadSubcounties(Subcounty[] subcounties){
        //
    }

    public Parish[] downloadParishes(){
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Parish[]> responseEntity = restTemplate.exchange(REST_URL+"place/parishes",HttpMethod.GET,getRequestEntity(),Parish[].class);
            Parish[] parishs = responseEntity.getBody();
            Log.i("REST CLIENT:","found "+parishs.length+" Parishes");
            return parishs;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  null;
    }

    public void uploadParishes(Parish[] parishes){
        //
    }

    public Village[] downloadVillages(){
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Village[]> responseEntity = restTemplate.exchange(REST_URL+"place/villages",HttpMethod.GET,getRequestEntity(),Village[].class);
            Village[] villages = responseEntity.getBody();
            Log.i("REST CLIENT:","found "+villages.length+" Villages");
            return villages;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  null;
    }

    public void uploadVillages(Village[] villages){
        //
    }

    public boolean login(String user, String pass) {
        try{
            userName = user;
            password = pass;
            RestTemplate restTemplate = getRestTemplate();
            HttpHeaders headers = getHeaders();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
            ResponseEntity<Region[]> responseEntity = restTemplate.exchange(REST_URL+"place/regions",HttpMethod.GET, requestEntity,Region[].class);
            Region[] regions = responseEntity.getBody();
            if(regions.length>0){
                return true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }


}