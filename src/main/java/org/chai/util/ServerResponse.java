package org.chai.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by victor on 11/11/14.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerResponse {
    public String status;
    public String message;

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
}
