package com.bonusCaluculator.REST.OpenCRX;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesOrder {
    @JsonProperty("name") public String id;
    @JsonProperty("@href")private String refURL;
    public String ref;
    @JsonProperty("customer") private Map<String,String> customerRefURLList;  //url to resource
    public String customerRef;
    @JsonProperty("salesRep") private Map<String,String> salesmanRefURLList;  //url to resource
    public String salesmanRef;
    @JsonProperty("totalAmount") public String totalPrice;
    @JsonProperty("totalTaxAmount") public String totalTax;
    @JsonProperty("createdAt")  public String creationDate;

    public static SalesOrder createFromJSON(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SalesOrder instance = mapper.readValue(json, SalesOrder.class);
        try {
            instance.ref = instance.refURL.substring(instance.refURL.lastIndexOf("salesOrder/")+11);
        }catch(NullPointerException e){ }
        try {
            String customerRefUrl = instance.customerRefURLList.get("@href");
            instance.customerRef = customerRefUrl.substring(customerRefUrl.lastIndexOf("account/")+8);
        }catch(NullPointerException e){ }
        try {
            String salesmanRefUrl = instance.salesmanRefURLList.get("@href");
            instance.salesmanRef = salesmanRefUrl.substring(salesmanRefUrl.lastIndexOf("account/")+8);
        }catch(NullPointerException e){ }
        return instance;
    }


    public int getCreationYear() throws Exception {
        try{
            return Integer.parseInt(creationDate.substring(0,4));
        }catch (Exception e){
            throw new Exception("Can't determine creation Date of SalesOrder");
        }
    }

}


