package org.chai.rest;

import org.chai.model.Product;
import org.chai.model.ProductGroup;
import org.chai.util.Utils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by victor on 11/4/14.
 */
public class ProductClient extends RestClient{

    public ProductGroup[] downloadProductGroups(){
        Utils.log("getting product groups");
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<ProductGroup[]> responseEntity = restTemplate.exchange(getRestUrl() + "productGroup/list?max=" + Integer.MAX_VALUE, HttpMethod.GET,getRequestEntity(), ProductGroup[].class);
            return responseEntity.getBody();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public Product[] downloadProducts(){
        Utils.log("getting products");
        Utils.log(getRestUrl() + "product/list?max=" + Integer.MAX_VALUE);
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Product[]> responseEntity = restTemplate.exchange(getRestUrl() + "product/list?max=" + Integer.MAX_VALUE, HttpMethod.GET,getRequestEntity(),Product[].class);
            return responseEntity.getBody();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
