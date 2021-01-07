package com.bonusCaluculator.REST.OpenCRX;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Position {
    public String ref;
    @JsonProperty("@href") private String refURL;
    @JsonProperty("quantity") public String amount;
    @JsonProperty("pricePerUnit") public String pricePerUnit;

    @JsonProperty("product")  private Map<String,String> productRefMap;
    public String productRef;

    public static Position createFromJSON(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Position instance = mapper.readValue(json, Position.class);
        try {
            String productRefURL = instance.productRefMap.get("@href");
            instance.productRef = productRefURL.substring(productRefURL.lastIndexOf("product/")+8);
        } catch (NullPointerException e) {}
        try {
            instance.ref = instance.refURL.substring(instance.refURL.lastIndexOf("position/")+9);
        } catch (NullPointerException e) {}

        return instance;
    }

}
