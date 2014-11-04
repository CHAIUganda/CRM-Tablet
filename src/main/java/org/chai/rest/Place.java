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
            return regions;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public District[] downloadDistricts(Long regionId){
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<District[]> responseEntity = restTemplate.exchange(REST_URL+"place/districts",HttpMethod.GET,getRequestEntity(),District[].class);
            return responseEntity.getBody();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  null;
    }

    public Subcounty[] downloadSubcounties(Long districtId){
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Subcounty[]> responseEntity = restTemplate.exchange(REST_URL+"place/subcounties",HttpMethod.GET,getRequestEntity(),Subcounty[].class);
            return responseEntity.getBody();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  null;
    }

    public void uploadSubcounties(Subcounty[] subcounties){
        //
    }

    public Parish[] downloadParishes(Long subcountyId){
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Parish[]> responseEntity = restTemplate.exchange(REST_URL+"place/subcounties",HttpMethod.GET,getRequestEntity(),Parish[].class);
            return responseEntity.getBody();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  null;
    }

    public void uploadParishes(Parish[] parishes){
        //
    }

    public Village[] downloadVillages(Long parishId){
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Village[]> responseEntity = restTemplate.exchange(REST_URL+"place/villages",HttpMethod.GET,getRequestEntity(),Village[].class);
            return responseEntity.getBody();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  null;
    }

    public void uploadVillages(Village[] villages){
        //
    }

}
