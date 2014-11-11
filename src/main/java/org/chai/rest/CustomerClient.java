package org.chai.rest;

import android.util.Log;
import org.chai.model.Customer;
import org.chai.model.Product;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by victor on 11/4/14.
 */
public class CustomerClient extends RestClient {

    public Customer[] downloadCustomers() {
        try {
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Customer[]> responseEntity = restTemplate.exchange(REST_URL + "customer/list?max="+Integer.MAX_VALUE, HttpMethod.GET, getRequestEntity(), Customer[].class);
            return responseEntity.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void uploadCustomers(Customer[] customers){
        try{
            for(Customer customer:customers){
                RestTemplate restTemplate = getRestTemplate();
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                HttpEntity<Customer> httpEntity = new HttpEntity<Customer>(customer,getHeaders());
                ResponseEntity<String> responseEntity = restTemplate.exchange(REST_URL + "customer/update", HttpMethod.PUT,httpEntity, String.class);
                Log.i("Rest post Response:","=============================================================================="+responseEntity.getBody());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
