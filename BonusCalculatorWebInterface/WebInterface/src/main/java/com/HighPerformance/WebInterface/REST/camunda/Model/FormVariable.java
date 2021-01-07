package com.HighPerformance.WebInterface.REST.camunda.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormVariable {

    public String name;
    public String value;
    public String type;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public FormVariable setValue(String value) {
        this.value = value;
        return this;
    }

    public String getType() {
        return type;
    }

    public FormVariable setType(String type) {
        this.type = type;
        return this;
    }

    public FormVariable setName(String name){
        this.name=name;
        return this;
    }

    public int getValueAsInt(){
        return Integer.parseInt(value);
    }

    public static FormVariable createFromJSON(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, FormVariable.class);
    }
}
