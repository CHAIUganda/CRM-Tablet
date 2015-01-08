package org.chai.rest;

import android.util.Log;
import org.chai.model.AdhockSale;
import org.chai.model.Order;
import org.chai.model.Sale;
import org.chai.model.SaleData;
import org.chai.util.ServerResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by victor on 1/3/15.
 */
public class SalesClient extends RestClient {

    public boolean uploadSales(Sale[] sales){
        try{
            for(Sale sale:sales){
                RestTemplate restTemplate = getRestTemplate();
                HttpEntity<Sale> httpEntity = new HttpEntity<Sale>(sale,getHeaders());
                ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(REST_URL+"sale/saleOrder", HttpMethod.PUT,httpEntity,ServerResponse.class);
                Log.i("Rest Sales post Response:", "==============================================================================" + responseEntity.getBody().getMessage());
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public boolean uploadSaleData(SaleData[] saleDatas){
        try{
            for(SaleData saleData:saleDatas){
                RestTemplate restTemplate = getRestTemplate();
                HttpEntity<SaleData> httpEntity = new HttpEntity<SaleData>(saleData,getHeaders());
                ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(REST_URL+"sale/orderSale", HttpMethod.PUT,httpEntity,ServerResponse.class);
                Log.i("Rest SalesData post Response:", "==============================================================================" + responseEntity.getBody().getMessage());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    public boolean uploadDirectSale(AdhockSale[] sales){
        try{
            for(AdhockSale sale:sales){
                RestTemplate restTemplate = getRestTemplate();
                HttpEntity<AdhockSale> httpEntity = new HttpEntity<AdhockSale>(sale,getHeaders());
                ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(REST_URL+"sale/directSale", HttpMethod.PUT,httpEntity,ServerResponse.class);
                Log.i("Rest Sales post Response:", "==============================================================================" + responseEntity.getBody().getMessage());
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public boolean uploadOrders(Order[] orders){
        try{
            for(Order order:orders){
                RestTemplate restTemplate = getRestTemplate();
                HttpEntity<Order> httpEntity = new HttpEntity<Order>(order,getHeaders());
                ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(REST_URL+"sale/placeOrder",HttpMethod.PUT,httpEntity,ServerResponse.class);
                Log.i("Rest Order post Response:", "==============================================================================" + responseEntity.getBody().getMessage());
            }
            return true;

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
