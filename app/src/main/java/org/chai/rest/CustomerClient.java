package org.chai.rest;

import android.util.Log;
import org.chai.model.Customer;
import org.chai.util.ServerResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by victor on 11/4/14.
 */
public class CustomerClient extends RestClient {

    public Customer[] downloadCustomers() {
        try {
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Customer[]> responseEntity = restTemplate.exchange(REST_URL + "customer/list?max=" + Integer.MAX_VALUE, HttpMethod.GET, getRequestEntity(), Customer[].class);
            return responseEntity.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ServerResponse uploadCustomer(Customer customer,RestTemplate restTemplate) {
        try {
//            RestTemplate restTemplate = getRestTemplate();
            HttpEntity<Customer> httpEntity = new HttpEntity<Customer>(customer, getHeaders());
            ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(REST_URL + "customer/update", HttpMethod.PUT, httpEntity, ServerResponse.class);
            Log.i("Rest Customer Response:", "" + responseEntity.getBody().getMessage());
            ServerResponse body = responseEntity.getBody();
            body.setItemRef(customer.getOutletName());
            return body;
        } catch (HttpClientErrorException ex) {
            ServerResponse serverResponse = ServerResponse.getServerErrorResponse(ex);
            if(customer!=null){
                serverResponse.setItemRef(customer.getOutletName());
            }
            return serverResponse;
        }
    }
}
