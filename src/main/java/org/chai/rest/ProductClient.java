package org.chai.rest;

import org.chai.model.Product;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by victor on 11/4/14.
 */
public class ProductClient extends RestClient{

    public Product[] downloadProducts(){
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Product[]> responseEntity = restTemplate.exchange(REST_URL+"product/list", HttpMethod.GET,getRequestEntity(),Product[].class);
            return responseEntity.getBody();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
