package com.DataBaseRestApi.DataBaseRestApi.REST;

import java.util.List;
import java.util.Map;

public class ParameterValidator {
    private Map<String,Object> map;


    public ParameterValidator(Map<String,Object> map){
        this.map=map;
    }

    public int getInt(String key) throws RESTException{
        String value = getString(key);
        try {
            int valueInt = Integer.parseInt(value);
            return valueInt;
        }catch(NumberFormatException e){
            throw new RESTException().addMessage("NumberFormatException","'"+key+"' must be a number");
        }
    }

    public String getString(String key) throws RESTException{
        String value = (String)map.get(key);
        if(value==null || value.equals(""))
            throw new RESTException().addMessage("Missing value exception.","'"+key+"' value have to be set.");
        return value;
    }
}
