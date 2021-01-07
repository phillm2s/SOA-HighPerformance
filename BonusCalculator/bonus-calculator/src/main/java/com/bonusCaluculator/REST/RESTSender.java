package com.bonusCaluculator.REST;

import com.bonusCaluculator.Application;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@RestController
public class RESTSender {
    private HttpMethod defaultMethodType;
    //private HttpEntity<String> defaultHttpEntity;
    private RestTemplate template;


    public RESTSender(){
        //Default
        defaultMethodType = HttpMethod.GET;
        template = new RestTemplate();
    }

    public HttpMethod getDefaultMethodType() {
        return defaultMethodType;
    }

    public void setDefaultMethodType(HttpMethod defaultMethodType) {
        this.defaultMethodType = defaultMethodType;
    }


    public ResponseEntity<String> sendRequest(String url, HttpMethod type, HttpEntity<String> entity){
        //Perform REST call
        ResponseEntity<String> res = template.exchange(url, type, entity, String.class);
        return res;
    }


}
