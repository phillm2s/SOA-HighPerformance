package com.bonusCaluculator.REST.OpenCRX;

import com.bonusCaluculator.REST.OrangeHRM.CustomField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @JsonProperty("productNumber") public String productNumber;
    @JsonProperty("name") public String name;
    @JsonProperty("maxQuantity") public String maxQuantity;
    @JsonProperty("description") public String description;

    public static Product createFromJSON(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Product.class);
    }
}
