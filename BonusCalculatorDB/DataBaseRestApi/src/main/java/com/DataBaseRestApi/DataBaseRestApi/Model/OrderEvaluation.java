package com.DataBaseRestApi.DataBaseRestApi.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderEvaluation {

    @JsonProperty("orderEvaluationID") public int orderEvaluationID;
    @JsonProperty("productName") public String productName;
    @JsonProperty("clientName") public String clientName;
    @JsonProperty("clientRanking") public String clientRanking;
    @JsonProperty("itemAmount") public String amount;
    @JsonProperty("price") public String price;
    @JsonProperty("bonus") public String bonus;
    @JsonProperty("comment") public String comment;

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
}
