package com.bonusCaluculator.REST.OrangeHRM;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {
    @JsonProperty("employeeId")public String employeeId;
    @JsonProperty("firstName") public String firstName;
    @JsonProperty("middleName") public String middleName;
    @JsonProperty("lastName") public String lastName;
    @JsonProperty("unit")public String unit;
    @JsonProperty("jobTitle")public String jobTitle;

    public static Employee createFromJSON(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Employee.class);
    }
}
