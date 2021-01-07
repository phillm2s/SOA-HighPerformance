package com.bonusCaluculator.REST.OrangeHRM;


import com.bonusCaluculator.REST.RESTSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

public class OrangeHRMAPI {
    private static OrangeHRMAPI instance;

    private RESTSender sender;
    private String currentAuthentificationToken;
    private String baseURL = "https://sepp-hrm.inf.h-brs.de";
    private String userName = "tom ";
    private String password = "tom123";

    private OrangeHRMAPI(){
        sender = new RESTSender();
    }

    public static OrangeHRMAPI getInstance(){
        if (OrangeHRMAPI.instance==null)
            OrangeHRMAPI.instance = new OrangeHRMAPI();

        return OrangeHRMAPI.instance;
    }




    public ResponseEntity<String> generateAuthentificationToken() throws JSONException {
        String url =baseURL + "/symfony/web/index.php/oauth/issueToken";
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type","application/x-www-form-urlencoded");

        String body= "client_id="+userName+"&" +
                "client_secret="+password+"&" +
                "grant_type=client_credentials";
        HttpEntity<String> entity = new HttpEntity<>(body,header);

        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.POST, entity);

        if(res.getStatusCode().value()==200){
            JSONObject obj = new JSONObject(res.getBody());
            this.currentAuthentificationToken=obj.getString("access_token");
            System.out.println("Token updated to: "+this.currentAuthentificationToken);
        }

        return res;
    }

    public Map<String, Employee> getAllemplyees() throws JSONException, JsonProcessingException, HttpClientErrorException.Unauthorized {
        String url =baseURL + "/symfony/web/index.php/api/v1/employee/search";
        HttpHeaders header = new HttpHeaders();
        ResponseEntity<String> res;
        try {
            header.set("Authorization", "Bearer " + currentAuthentificationToken);
            HttpEntity<String> entity = new HttpEntity<>("", header);
            res = sender.sendRequest(url, HttpMethod.GET, entity);
        } catch (HttpClientErrorException.Unauthorized e){
            generateAuthentificationToken();

            header.set("Authorization", "Bearer " + currentAuthentificationToken);
            HttpEntity<String> entity = new HttpEntity<>("", header);
            res = sender.sendRequest(url, HttpMethod.GET, entity);
        }


        Map<String, Employee> userMap= new HashMap<String, Employee>();
        JSONObject employeeList = new JSONObject(res.getBody());
        JSONArray ja = employeeList.getJSONArray("data");
        for (int i=0; i< ja.length();i++) {
            String employeeString = ja.get(i).toString();
            Employee employee = Employee.createFromJSON(employeeString);
            userMap.put(employee.employeeId, employee);
        }
        return userMap;
    }

    public Map<String, CustomField>getAllBonusFieldsFromUser(String userId) throws JSONException, JsonProcessingException, HttpClientErrorException.Unauthorized {
        String url =baseURL + "/symfony/web/index.php/api/v1/employee/"+userId+"/custom-field";
        HttpHeaders header = new HttpHeaders();
        ResponseEntity<String> res;
        try {
            header.set("Authorization", "Bearer " + currentAuthentificationToken);
            HttpEntity<String> entity = new HttpEntity<>("", header);
            res = sender.sendRequest(url, HttpMethod.GET, entity);
        } catch(HttpClientErrorException.Unauthorized e){
            generateAuthentificationToken();
            header.set("Authorization", "Bearer " + currentAuthentificationToken);
            HttpEntity<String> entity = new HttpEntity<>("", header);
            res = sender.sendRequest(url, HttpMethod.GET, entity);
        }


        Map<String,CustomField> bonusFieldMap= new HashMap<String,CustomField>();
        JSONObject bonusList = new JSONObject(res.getBody());
        JSONArray ja = bonusList.getJSONArray("data");
        for (int i=0; i< ja.length();i++) {
            String fieldString = ja.get(i).toString();
            CustomField field = CustomField.createFromJSON(fieldString);
            bonusFieldMap.put(field.id, field);
        }
        System.out.println(bonusFieldMap.size() + " bonus fields found.");
        return bonusFieldMap;
    }

    public ResponseEntity<String> setBonusField(String userId, CustomField cf) throws HttpClientErrorException.Unauthorized {
        String url = baseURL + "/symfony/web/index.php/api/v1/employee/"+userId+"/custom-field";
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization","Bearer "+currentAuthentificationToken);
        header.set("Content-Type","application/x-www-form-urlencoded");
        String body= "id="+userId+"&" +
                "fieldId="+cf.id+"&" +
                "value="+cf.value;
        ResponseEntity<String> res=null;
        try{
            HttpEntity<String> entity = new HttpEntity<>(body,header);
            res = sender.sendRequest(url, HttpMethod.POST, entity);
        }catch (HttpClientErrorException.Unauthorized e) {
            try {
                generateAuthentificationToken();
                header.set("Authorization","Bearer "+currentAuthentificationToken);
                HttpEntity<String> entity = new HttpEntity<>(body,header);
                res = sender.sendRequest(url, HttpMethod.POST, entity);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }

        return res;
    }


}
