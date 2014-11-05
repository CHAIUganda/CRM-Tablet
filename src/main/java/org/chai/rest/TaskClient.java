package org.chai.rest;

import org.chai.model.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    public void uploadTasks(Task[] tasks){}
}
