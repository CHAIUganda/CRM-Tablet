package org.chai.util;

import android.util.Log;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Created by victor on 11/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerResponse {
    public String status;
    public String message;
    public String itemRef;

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
        ServerResponse serverResponse = new ServerResponse("500", ex.getResponseBodyAsString());
        Log.i("Error:", serverResponse.getMessage());
        ex.printStackTrace();
        return serverResponse;
    }
}
