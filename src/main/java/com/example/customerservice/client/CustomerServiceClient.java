
package com.example.customerservice.client;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.cxf.binding.soap.interceptor.MustUnderstandInterceptor;
import org.apache.cxf.binding.soap.interceptor.MustUnderstandInterceptor.MustUnderstandEndingInterceptor;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;
import com.example.customerservice.CustomerServiceService;

public class CustomerServiceClient {

    protected CustomerServiceClient() {
    }
    
    public static void main(String args[]) throws Exception {

        CustomerServiceService customerServiceService;
        if (args.length != 0 && args[0].length() != 0) {
            File wsdlFile = new File(args[0]);
            URL wsdlURL;
            if (wsdlFile.exists()) {
                wsdlURL = wsdlFile.toURL();
            } else {
                wsdlURL = new URL(args[0]);
            }
            customerServiceService = new CustomerServiceService(wsdlURL);
        } else {
            customerServiceService = new CustomerServiceService();
        }

        CustomerService customerService = customerServiceService.getCustomerServicePort();

        Client wsClient = ClientProxy.getClient(customerService);
        
        
        //WSS4JOutInterceptor test 
        
        
        HTTPConduit http =  (HTTPConduit) wsClient.getConduit();
        AuthorizationPolicy authPolicy = new AuthorizationPolicy();
        //authPolicy.setAuthorizationType("Digest");
        authPolicy.setUserName("foo");
        authPolicy.setPassword("bar");
        http.setAuthorization(authPolicy); 
        
        wsClient.getRequestContext().put(Message.ENDPOINT_ADDRESS, "http://localhost:9090/CustomerServicePort");
        
        wsClient.getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
        
        List<Customer> customers = customerService.getCustomersByName("Smith");
        System.out.println("Response received");
        System.out.println(customers.size());
        System.out.println(customers.get(0).getName());
        
        MustUnderstandEndingInterceptor a ;
        
        MustUnderstandInterceptor b;
        
        
        /*
        // Initialize the test class and call the tests
        CustomerServiceTester client = new CustomerServiceTester();
        client.setCustomerService(customerService);
        client.testCustomerService();
        System.exit(0); 
        */
    }
}
