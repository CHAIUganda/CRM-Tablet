package org.chai.rest;

import android.util.Log;

import org.chai.model.DetailerCall;
import org.chai.model.MalariaDetail;
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
        Utils.log("TaskClient -> downloadTasks");
        try {
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Task[]> responseEntity = restTemplate.exchange(getRestUrl() + "task/list?max=" + Integer.MAX_VALUE, HttpMethod.GET, getRequestEntity(), Task[].class);
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
            HttpEntity<Task> httpEntity = new HttpEntity<>(task, getHeaders());
            String url = getRestUrl() + "task/update";
            if(task.getType().equalsIgnoreCase("malaria")){
                url = getRestUrl() + "task/malariaUpdate";
                Utils.log("Syncing to -> " + url);
            }
            ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, ServerResponse.class);
            ServerResponse body = responseEntity.getBody();
            body.setItemRef(task.getDescription());
            return body;
        } catch (HttpClientErrorException ex) {
            ServerResponse serverResponse = ServerResponse.getServerErrorResponse(ex);
            serverResponse.setItemRef(task.getDescription());
            return serverResponse;
        }
    }

    public DetailerCall[] downloadDiarrheaHistory(){
        try {
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<DetailerCall[]> responseEntity = restTemplate.exchange(getRestUrl() + "task/listCompleteDiarrhoea?max=" + Integer.MAX_VALUE, HttpMethod.GET, getRequestEntity(), DetailerCall[].class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException se) {
            Log.i("Server Error:", se.getResponseBodyAsString());
            Utils.log("HttpClientErrorException -> " + se.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.log("Exception -> " + ex.getMessage());
        }
        return null;
    }

    public MalariaDetail[] downloadMalariaHistory(){
        try {
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<MalariaDetail[]> responseEntity = restTemplate.exchange(getRestUrl() + "task/listCompleteMalaria?max=" + Integer.MAX_VALUE, HttpMethod.GET, getRequestEntity(), MalariaDetail[].class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException se) {
            Log.i("Server Error:", se.getResponseBodyAsString());
            Utils.log("HttpClientErrorException -> " + se.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.log("Exception -> " + ex.getMessage());
        }
        return null;
    }
}
