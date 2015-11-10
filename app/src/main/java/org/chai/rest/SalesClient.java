package org.chai.rest;

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
            HttpEntity<Sale> httpEntity = new HttpEntity<>(sale, getHeaders());
            ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(getRestUrl() + "sale/saleOrder", HttpMethod.PUT, httpEntity, ServerResponse.class);

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
            HttpEntity<SaleData> httpEntity = new HttpEntity<>(saleData, getHeaders());
            ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(getRestUrl() + "sale/orderSale", HttpMethod.PUT, httpEntity, ServerResponse.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            ServerResponse serverResponse = ServerResponse.getServerErrorResponse(ex);
            return serverResponse;
        }
    }

    public ServerResponse uploadDirectSale(AdhockSale sale) {
        try {
            RestTemplate restTemplate = getRestTemplate();
            HttpEntity<AdhockSale> httpEntity = new HttpEntity<>(sale, getHeaders());
            ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(getRestUrl() + "sale/directSale", HttpMethod.PUT, httpEntity, ServerResponse.class);

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
            HttpEntity<Order> httpEntity = new HttpEntity<>(order, getHeaders());
            ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(getRestUrl() + "sale/placeOrder", HttpMethod.PUT, httpEntity, ServerResponse.class);

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
