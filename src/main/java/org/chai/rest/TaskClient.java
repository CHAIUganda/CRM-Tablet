package org.chai.rest;

import android.util.Log;
import org.chai.model.Customer;
import org.chai.model.Task;
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
            ResponseEntity<Task[]> responseEntity = restTemplate.exchange(REST_URL+"task/list", HttpMethod.GET,getRequestEntity(),Task[].class);
            return responseEntity.getBody();
        }catch (Exception ex){
           ex.printStackTrace();
        }
        return null;
    }

    public void uploadTasks(Task[] tasks){
        try{
            for(Task task:tasks){
                RestTemplate restTemplate = getRestTemplate();
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                HttpEntity<Task> httpEntity = new HttpEntity<Task>(task,getHeaders());
                ResponseEntity<String> responseEntity = restTemplate.exchange(REST_URL + "task/update", HttpMethod.PUT,httpEntity, String.class);
                Log.i("Rest post Response:","=============================================================================="+responseEntity.getBody());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
