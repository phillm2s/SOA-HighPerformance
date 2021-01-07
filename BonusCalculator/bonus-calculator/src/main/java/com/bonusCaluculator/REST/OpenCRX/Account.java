package com.bonusCaluculator.REST.OpenCRX;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
    public String ref;  //ressource id
    @JsonProperty("fullName") public String fullName;
    @JsonProperty("firstName") public String firstName;
    @JsonProperty("lastName") public String lastName;
    @JsonProperty("organisation") public String organisation;
    @JsonProperty("accountRating")public String accountRating;

    @JsonProperty("@href") private String refURL;  //url to resource


    public static Account createFromJSON(String json) throws JsonProcessingException {
        Account acc = new ObjectMapper().readValue(json, Account.class);
        acc.ref=acc.refURL.substring(acc.refURL.lastIndexOf("account/")+8);
        return acc;
    }

}
