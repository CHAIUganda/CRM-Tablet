package org.chai.rest;

import android.util.Log;

import org.chai.model.District;
import org.chai.model.Parish;
import org.chai.model.Region;
import org.chai.model.Subcounty;
import org.chai.model.SummaryReport;
import org.chai.model.User;
import org.chai.model.Village;
import org.chai.util.Utils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by victor on 11/3/14.
 */
public class Place extends RestClient {

    public Place(){
    }

    public Region[] downloadRegions(){
        Utils.log("Place: Downloading regions");
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Region[]> responseEntity = restTemplate.exchange(REST_URL+"place/regions",HttpMethod.GET,getRequestEntity(),Region[].class);
            Region[] regions = responseEntity.getBody();
            Utils.log("REST CLIENT: found " + regions.length+" Regions");
            return regions;
        }catch (HttpClientErrorException ex){
            Utils.log("Error downloading regions -> " + ex.getMessage());
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

    public User login(String user, String pass) {
        try{
            userName = user;
            password = pass;

            RestTemplate restTemplate = getRestTemplate();
            HttpHeaders headers = getHeaders();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
            ResponseEntity<User> responseEntity = restTemplate.exchange(REST_URL + "info", HttpMethod.GET, requestEntity, User.class);
            User user1 = responseEntity.getBody();
            if(user1 != null){
                Utils.log("user1 is not empty");
                return user1;
            }else{
                Utils.log("User is empty");
            }
        }catch (Exception ex){
            Utils.log("Error login user -> " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public SummaryReport[] getSummaryReports(){
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<SummaryReport[]> responseEntity = restTemplate.exchange(REST_URL+"dashboard",HttpMethod.GET,getRequestEntity(),SummaryReport[].class);
            SummaryReport[] summaryReports = responseEntity.getBody();
            Log.i("REST CLIENT:","found "+summaryReports.length+" Reports");
            return summaryReports;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  null;
    }


}