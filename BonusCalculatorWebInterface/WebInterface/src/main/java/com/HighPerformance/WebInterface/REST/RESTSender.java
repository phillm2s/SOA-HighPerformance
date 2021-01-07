package com.HighPerformance.WebInterface.REST;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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



//    public ResponseEntity<String> sendRequest(String url, HttpEntity<String> entity){
//        return sendRequest(url, defaultMethodType, entity);
//    }

}
