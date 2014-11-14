package org.chai.rest;

import android.util.Log;
import org.chai.model.Customer;
import org.chai.model.Task;
import org.chai.util.ServerResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by victor on 11/4/14.
 */
public class TaskClient extends RestClient {

    public Task[] downloadTasks(){
        try{
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<Task[]> responseEntity = restTemplate.exchange(REST_URL+"task/list?max="+Integer.MAX_VALUE, HttpMethod.GET,getRequestEntity(),Task[].class);
            return responseEntity.getBody();
        }catch (Exception ex){
           ex.printStackTrace();
        }
        return null;
    }

    public boolean uploadTask(Task task) {
        try{
            RestTemplate restTemplate = getRestTemplate();
            HttpEntity<Task> httpEntity = new HttpEntity<Task>(task,getHeaders());
            ResponseEntity<ServerResponse> responseEntity = restTemplate.exchange(REST_URL + "task/update", HttpMethod.PUT, httpEntity, ServerResponse.class);
            Log.i("Rest Task post Response:", "==============================================================================" + responseEntity.getBody().getMessage());
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
