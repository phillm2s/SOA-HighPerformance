package com.bonusCaluculator.REST.HPDatabase;

import com.bonusCaluculator.Application;
import com.bonusCaluculator.Model.Evaluation;
import com.bonusCaluculator.REST.RESTSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.appinfo.InstanceInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

public class HPDatabaseAPI {
    private static HPDatabaseAPI instance;

    private RESTSender sender;
    private String userName ="HpDbRest";
    private String password = "admin";


    private HPDatabaseAPI(){ sender = new RESTSender(); }

    public static HPDatabaseAPI getInstance(){
        if(HPDatabaseAPI.instance==null)
            HPDatabaseAPI.instance= new HPDatabaseAPI();
        return HPDatabaseAPI.instance;
    }


    public String getBaseUrl() throws Exception {
        //String baseUrl = "http://localhost:8091/HPevaluation/api";
        String url =  Application.getServiceURL("DATABASE-RESTAPI");
        String baseUrl = url + "/HPevaluation/api";
        return baseUrl;
    }

    public ArrayList<Evaluation> getAllMinimalEvaluations(int year) throws Exception {

        String url = getBaseUrl() + "/evaluations/year";
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(userName,password);
        header.set("Content-Type","application/x-www-form-urlencoded");

        String body= "year="+year;

        HttpEntity<String> entity = new HttpEntity<>(body,header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.POST, entity);

        JSONArray ja = new JSONArray(res.getBody());

        return Evaluation.Helper.toList(ja);
    }

    public Evaluation getEvaluation(int employeeID, int year) throws Exception {

        String url = getBaseUrl() + "/evaluation";
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(userName,password);
        header.set("Content-Type","application/x-www-form-urlencoded");

        String body= "employeeID="+employeeID+"&" +
                "year="+year;

        HttpEntity<String> entity = new HttpEntity<>(body,header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.POST, entity);

        return Evaluation.createFromJSON(res.getBody());
    }




    public boolean updateEvaluations(ArrayList<Evaluation> evaluations) throws Exception {
        String url = getBaseUrl() + "/evaluations/update";
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(userName,password);
        header.set("Content-Type","application/json");

        JSONArray ja =  Evaluation.Helper.toJSONArray(evaluations);
        String body = ja.toString();

        HttpEntity<String> entity = new HttpEntity<String>(body,header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.PUT, entity);

        if(res.getBody().equals("success"))
            return true;

        return false;
    }



}
