package com.bonusCaluculator.REST.OrangeHRM;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomField {
    @JsonProperty("id") public String id;
    @JsonProperty("name") public String name;
    @JsonProperty("type") public String type;
    @JsonProperty("screen") public String screen;
    @JsonProperty("value") public String value;

    public CustomField(){}

    public CustomField setId(String id){
        this.id = id;
        return this;
    }
    public CustomField setValue(String value){
        this.value=value;
        return this;
    }

    public static CustomField createFromJSON(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, CustomField.class);
    }
}
