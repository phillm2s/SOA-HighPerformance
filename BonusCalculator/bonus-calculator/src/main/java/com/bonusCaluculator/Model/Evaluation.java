package com.bonusCaluculator.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Evaluation implements Serializable {
    @JsonProperty("evaluationID") public int evaluationID;
    @JsonProperty("fullName") public String fullName;
    @JsonProperty("emplyeeID") public int emplyeeID;    //OrangeHRM
    @JsonProperty("openCRXEmployeeRef" )public String openCRXEmployeeRef;
    @JsonProperty("department") public String department;
    @JsonProperty("year") public String year;
    @JsonProperty("comment") public String comment;
    @JsonProperty("bonus") public String bonus;

    public String signatureCEO; //mocked. should be set via checkbox from claimed user. -> changes reset the signature
    public String signatureHR;

    public double totalBonusOrderEvaluation =0;
    public double totalBonusSocialEvaluation =0;

    public ArrayList<OrderEvaluation> orderEvaluations;
    public ArrayList<SocialEvaluation> socialEvaluations;




    public Evaluation setEvaluationID(int evaluationID){
        this.evaluationID = evaluationID;
        return this;
    }

    public Evaluation setOpenCRXEmployeeRef(String openCRXEmployeeRef){
        this.openCRXEmployeeRef = openCRXEmployeeRef;
        return this;
    }

    public Evaluation setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Evaluation setEmplyeeID(int emplyeeID) {
        this.emplyeeID = emplyeeID;
        return this;
    }

    public Evaluation setDepartment(String department) {
        this.department = department;
        return this;
    }

    public Evaluation setYear(String year) {
        this.year = year;
        return this;
    }

    public Evaluation setOrderEvaluations(ArrayList<OrderEvaluation> orderEvaluations) {
        this.orderEvaluations = orderEvaluations;
        return this;
    }

    public Evaluation setSocialEvaluations(ArrayList<SocialEvaluation> socialEvaluations) {
        this.socialEvaluations = socialEvaluations;
        return this;
    }

    public Evaluation setComment(String comment){
        this.comment=comment;
        return this;
    }

    public Evaluation setBonus(String bonus){
        this.bonus=bonus;
        return this;
    }


    public String serialize() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public static Evaluation createFromJSON(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Evaluation.class);
    }




    public static class Helper{

        public static JSONArray toJSONArray(ArrayList<Evaluation> evaluations) throws JsonProcessingException, JSONException {
            JSONArray ja = new JSONArray();

            for (Evaluation eval : evaluations){
                String s = eval.serialize();
                JSONObject jo = new JSONObject(s);
                ja.put(jo);
            }
            return ja;
        }

        public static ArrayList<Evaluation> toList(JSONArray evaluationsJa) throws JSONException, JsonProcessingException {
            ArrayList<Evaluation> evalautaions = new ArrayList<Evaluation>();
            for (int i=0;i<evaluationsJa.length();i++){
                JSONObject evaluationJo = evaluationsJa.getJSONObject(i);
                evalautaions.add(Evaluation.createFromJSON(evaluationJo.toString()));
            }
            return evalautaions;
        }

    }


}
