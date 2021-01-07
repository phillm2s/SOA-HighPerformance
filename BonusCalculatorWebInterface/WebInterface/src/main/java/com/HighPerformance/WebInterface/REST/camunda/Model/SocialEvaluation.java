package com.HighPerformance.WebInterface.REST.camunda.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SocialEvaluation implements Serializable {

    @JsonProperty("socialEvaluationID") public int socialEvaluationID;
    @JsonProperty("description") public String description;
    @JsonProperty("targetValue") public String targetValue;
    @JsonProperty("actualValue") public String actualValue;
    @JsonProperty("bonus") public String bonus;
    @JsonProperty("comment") public String comment;

    public SocialEvaluation setSocialEvaluationID(int socialEvaluationID) {
        this.socialEvaluationID = socialEvaluationID;
        return this;
    }

    public SocialEvaluation setDescription(String description) {
        this.description = description;
        return this;
    }

    public SocialEvaluation setTargetValue(String targetValue) {
        this.targetValue = targetValue;
        return this;
    }

    public SocialEvaluation setActualValue(String actualValue) {
        this.actualValue = actualValue;
        return this;
    }

    public SocialEvaluation setBonus(String bonus) {
        this.bonus = bonus;
        return this;
    }

    public SocialEvaluation setComment(String comment){
        this.comment=comment;
        return this;
    }

    public int getSocialEvaluationID() {
        return socialEvaluationID;
    }

    public String getDescription() {
        return description;
    }

    public String getBonus() {
        return bonus;
    }

    public String getComment() {
        return comment;
    }

    public int getActualValue(){
        try{
            return Integer.parseInt(actualValue);
        } catch(Exception e){
            System.out.println("Can't cast "+actualValue+" to Integer.");
            return -1;
        }
    }

    public int getTargetValue(){
        try{
            return Integer.parseInt(targetValue);
        } catch(Exception e){
            System.out.println("Can't cast "+targetValue+" to Integer.");
            return -1;
        }
    }


    public static SocialEvaluation createFromJSON(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, SocialEvaluation.class);
    }

}
