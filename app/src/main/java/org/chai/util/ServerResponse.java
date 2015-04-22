package org.chai.util;

import android.util.Log;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by victor on 11/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerResponse {
    public String status="";
    public String message="";
    public String itemRef="";

    public ServerResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ServerResponse(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getItemRef() {
        return itemRef;
    }

    public void setItemRef(String itemRef) {
        this.itemRef = itemRef;
    }

    public static ServerResponse getServerErrorResponse(HttpClientErrorException ex) {
        ServerResponse serverResponse = new ServerResponse(ex.getStatusCode()+"", ex.getResponseBodyAsString());
        Log.i("Error:", serverResponse.getMessage());
        ex.printStackTrace();
        return serverResponse;
    }

    public static String parseErrorMessage(String message){
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> mapObject = mapper.readValue(message,new TypeReference<Map<String, Object>>() {
            });
            return mapObject.get("message").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
