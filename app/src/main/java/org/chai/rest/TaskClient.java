package org.chai.rest;

import android.util.Log;

import org.chai.model.Task;
import org.chai.util.ServerResponse;
import org.chai.util.Utils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by victor on 11/4/14.
 */
public class TaskClient extends RestClient {

    public Task[] downloadTasks() {
        try {
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Task[]> responseEntity = restTemplate.exchange(REST_URL + "task/list?max=" + Integer.MAX_VALUE, HttpMethod.GET, getRequestEntity(), Task[].class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException se) {
            Log.i("Server Error:", se.getResponseBodyAsString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ServerResponse uploadTask(Task task) {
        try {
            Utils.log("Uploading task -> " + task.getDescription());
            RestTemplate restTemplate = getRestTemplate();
            HttpEntity<Task> httpEntity = new HttpEntity<Task>(task, getHeaders());
            Utils.log("HTTPEntity -> " + httpEntity.getBody());
            ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(REST_URL + "task/update", HttpMethod.PUT, httpEntity, ServerResponse.class);
            ServerResponse body = responseEntity.getBody();
            Utils.log("After getting body -> " + body.toString());
            body.setItemRef(task.getDescription());
            return body;
        } catch (HttpClientErrorException ex) {
            ServerResponse serverResponse = ServerResponse.getServerErrorResponse(ex);
            serverResponse.setItemRef(task.getDescription());
            return serverResponse;
        }
    }
}
