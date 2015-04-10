package org.chai;

import org.chai.model.Customer;
import org.chai.rest.CustomerClient;
import org.chai.util.ServerResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Created by victor on 08-Apr-15.
 */
@RunWith(RobolectricTestRunner.class)
public class CustomerClientTest {
    MockRestServiceServer mockRestServiceServer;
    RestTemplate restTemplate;
    DaoInitForTest daoInitForTest;
    @Before
    public void setUp(){
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        daoInitForTest = new DaoInitForTest();
    }

    @Test
    public void testUploadCustomerSuccess(){
        Customer customer=daoInitForTest.setUpCustomer("0f48f620-b15b-4df3-a0ef-3462c665a17a");
        CustomerClient customerClient = new CustomerClient();
        mockRestServiceServer.expect(requestTo("http://178.79.178.121:8080/test-web-crm/rest/customer/update"))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withSuccess("{status: OK, message:Success}", MediaType.APPLICATION_JSON));
        ServerResponse serverResponse = customerClient.uploadCustomer(customer,restTemplate);
        mockRestServiceServer.verify();

        assertEquals(serverResponse.getStatus(),"OK");

    }
    @Test
    public void test_that_a_bad_request_is_handled_correctly(){
        Customer customer=null;
        CustomerClient customerClient = new CustomerClient();

        mockRestServiceServer.expect(requestTo("http://178.79.178.121:8080/test-web-crm/rest/customer/update"))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));
        ServerResponse serverResponse = customerClient.uploadCustomer(customer,restTemplate);
        mockRestServiceServer.verify();

        assertEquals(serverResponse.getStatus(),"400");
    }
}
