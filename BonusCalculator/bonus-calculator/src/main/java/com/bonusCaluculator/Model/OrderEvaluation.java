package com.bonusCaluculator.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderEvaluation implements Serializable {

    @JsonProperty("orderEvaluationID") public int orderEvaluationID;
    @JsonProperty("productName") public String productName;
    @JsonProperty("clientName") public String clientName;
    @JsonProperty("clientRanking") public String clientRanking;
    @JsonProperty("itemAmount") public String amount;
    @JsonProperty("price") public String price;
    @JsonProperty("bonus") public String bonus;
    @JsonProperty("comment") public String comment;



    public int getClientRanking(){
        try{
            String clientRankingTmp = clientRanking; //could look like 15.000000000
            if(clientRankingTmp.indexOf(".")!= -1)
                clientRankingTmp = clientRankingTmp.substring(0,clientRankingTmp.indexOf("."));
            return Integer.parseInt(clientRankingTmp);
        }catch(Exception e){
            System.out.println("Can't parse "+clientRanking+ " to int.");
            return -1;
        }
    }

    public int getAmount(){
        try{
            String amountTmp = amount; //could look like 15.000000000
            if(amountTmp.indexOf(".")!= -1)
                amountTmp = amountTmp.substring(0,amountTmp.indexOf("."));
            return Integer.parseInt(amountTmp);
        }catch(Exception e){
            System.out.println("Can't parse "+amount+ " to int.");
            return -1;
        }
    }

    public double getPrice(){
        try{
            //could look like 422.43000000000000
            String priceTmp = price;
            if (priceTmp.indexOf(".") != -1 && priceTmp.length() > priceTmp.indexOf(".")+3)
                priceTmp = priceTmp.substring( 0 , priceTmp.indexOf(".")+3 ); //cut everything behind 2 decimal places
            return Double.parseDouble(priceTmp);
        }catch(Exception e){
            System.out.println("Can't parse "+price+ " to double.");
            return -1;
        }
    }

    public OrderEvaluation setOrderEvaluationID(int orderEvaluationID) {
        this.orderEvaluationID = orderEvaluationID;
        return this;
    }

    public OrderEvaluation setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public OrderEvaluation setPrice(String price) {
        this.price = price;
        return this;
    }

    public OrderEvaluation setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public OrderEvaluation setClientRanking(String clientRanking) {
        this.clientRanking = clientRanking;
        return this;
    }

    public OrderEvaluation setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public OrderEvaluation setBonus(String bonus) {
        this.bonus = bonus;
        return this;
    }

    public OrderEvaluation setComment(String comment){
        this.comment=comment;
        return this;
    }

    public String serialize() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }


    public static OrderEvaluation createFromJSON(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, OrderEvaluation.class);
    }
}
