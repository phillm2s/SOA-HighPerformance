package com.DataBaseRestApi.DataBaseRestApi.REST;

import java.util.HashMap;
import java.util.Map;

public class RESTException extends RuntimeException{
    public Map<String,String> messages;

    public RESTException(){
        messages= new HashMap<String,String>();
    }

    public RESTException addMessage(String key,String message){
        messages.put(key,message);
        return this;
    }
}
