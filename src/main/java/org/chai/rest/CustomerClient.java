package org.chai.rest;

import org.chai.model.Customer;
import org.chai.model.Product;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    }
}
