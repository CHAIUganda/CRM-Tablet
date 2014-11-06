package org.chai.rest;

import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

/**
 * Created by victor on 11/3/14.
 */
public class RestClient {
    public static String REST_URL = "http://23.239.27.196:8080/web-crm/rest/";
//    public static String REST_URL = "http://192.168.1.109:8080/chai-crm/rest/";

    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(new MediaType("application", "json")));
        headers.set("device-imei","Samsung Galaxy S3");
        HttpAuthentication authHeader = new HttpBasicAuthentication("root", "pass");
        headers.setAuthorization(authHeader);
        return headers;
    }

    public HttpHeaders getHeaders(String user,String password){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(new MediaType("application", "json")));
        headers.set("device-imei","Samsung Galaxy S3");
        HttpAuthentication authHeader = new HttpBasicAuthentication(user, password);
        headers.setAuthorization(authHeader);
        return headers;
    }

    public HttpEntity<?> getRequestEntity() {
        HttpEntity<?> requestEntity = new HttpEntity<Object>(getHeaders());
        return requestEntity;
    }

    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }
}
