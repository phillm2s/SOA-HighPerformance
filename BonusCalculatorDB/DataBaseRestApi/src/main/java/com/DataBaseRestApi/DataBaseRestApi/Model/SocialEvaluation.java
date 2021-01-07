package com.DataBaseRestApi.DataBaseRestApi.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SocialEvaluation {

    public int socialEvaluationID;
    public String description;
    public String targetValue;
    public String actualValue;
    public String bonus;
    public String comment;

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

    public String serialize() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
