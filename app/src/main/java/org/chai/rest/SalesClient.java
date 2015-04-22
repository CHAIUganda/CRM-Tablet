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

/**
 * Created by victor on 1/3/15.
 */
public class SalesClient extends RestClient {

    public ServerResponse uploadSale(Sale sale) {
        try {
            RestTemplate restTemplate = getRestTemplate();
            HttpEntity<Sale> httpEntity = new HttpEntity<Sale>(sale, getHeaders());
            ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(REST_URL + "sale/saleOrder", HttpMethod.PUT, httpEntity, ServerResponse.class);
            Log.i("Rest Sales post Response:", "==============================================================================" + responseEntity.getBody().getMessage());

            ServerResponse body = responseEntity.getBody();
            body.setItemRef(sale.getTask().getCustomer().getOutletName());
            return body;
        } catch (HttpClientErrorException ex) {
            ServerResponse serverResponse = ServerResponse.getServerErrorResponse(ex);
            serverResponse.setItemRef(sale.getTask().getCustomer().getOutletName());
            return serverResponse;
        }
    }

    public ServerResponse uploadSaleData(SaleData saleData) {
        try {
            RestTemplate restTemplate = getRestTemplate();
            HttpEntity<SaleData> httpEntity = new HttpEntity<SaleData>(saleData, getHeaders());
            ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(REST_URL + "sale/orderSale", HttpMethod.PUT, httpEntity, ServerResponse.class);
            Log.i("Rest SalesData post Response:", "==============================================================================" + responseEntity.getBody().getMessage());
            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            ServerResponse serverResponse = ServerResponse.getServerErrorResponse(ex);
            return serverResponse;
        }
    }

    public ServerResponse uploadDirectSale(AdhockSale sale) {
        try {
            RestTemplate restTemplate = getRestTemplate();
            HttpEntity<AdhockSale> httpEntity = new HttpEntity<AdhockSale>(sale, getHeaders());
            ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(REST_URL + "sale/directSale", HttpMethod.PUT, httpEntity, ServerResponse.class);
            Log.i("Rest Sales post Response:", "==============================================================================" + responseEntity.getBody().getMessage());

            ServerResponse body = responseEntity.getBody();
            body.setItemRef(sale.getCustomer().getOutletName()+"(Adhock Sale)");
            return body;
        } catch (HttpClientErrorException ex) {
            ServerResponse serverResponse = ServerResponse.getServerErrorResponse(ex);
            serverResponse.setItemRef(sale.getCustomer().getOutletName()+"(Adhock Sale)");
            return serverResponse;
        }
    }

    public ServerResponse uploadOrder(Order order) {
        try {
            RestTemplate restTemplate = getRestTemplate();
            HttpEntity<Order> httpEntity = new HttpEntity<Order>(order, getHeaders());
            ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(REST_URL + "sale/placeOrder", HttpMethod.PUT, httpEntity, ServerResponse.class);
            Log.i("Rest Order post Response:", "==============================================================================" + responseEntity.getBody().getMessage());

            ServerResponse body = responseEntity.getBody();
            body.setItemRef(order.getCustomer()+"(Order)");
            return body;
        } catch (HttpClientErrorException ex) {
            ServerResponse serverResponse = ServerResponse.getServerErrorResponse(ex);
            serverResponse.setItemRef(order.getCustomer()+"(Order)");
            return serverResponse;
        }
    }
}
