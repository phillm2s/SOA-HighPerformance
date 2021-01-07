package com.HighPerformance.WebInterface.REST.camunda;

import com.HighPerformance.WebInterface.REST.RESTSender;
import com.HighPerformance.WebInterface.REST.camunda.Model.*;
import com.HighPerformance.WebInterface.WebInterfaceApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CamundaAPI {
    private static CamundaAPI instance;
    private RESTSender sender;

    //private String baseURL = "http://localhost:8090/engine-rest";

    public static CamundaAPI getInstance(){
        if(instance==null)
            instance= new CamundaAPI();
        return instance;
    }

    private CamundaAPI(){
        sender = new RESTSender();
    }

    private String getCamundaURL() throws Exception {
        return WebInterfaceApplication.getServiceURL("BONUS-CALCULATOR")+"/engine-rest";
    }

    public ProcessInstance startNewProcessInstance(String definitionID) throws Exception {
        String url = getCamundaURL()  + "/process-definition/" + definitionID + "/start";
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type","application/json");


        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.POST, entity);

        return ProcessInstance.createFromJSON(res.getBody());
    }

    public boolean completeTask(String taskID,List<FormVariable> variableList) throws Exception {
        String url = getCamundaURL()  + "/task/"+taskID+"/complete";
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type","application/json");

        JSONObject jo3 = new JSONObject();
        JSONObject jo2= new JSONObject();

        String body;
        if(variableList!=null && variableList.size()!=0) {

            for (int i = 0; i < variableList.size(); i++) {
                JSONObject jo1 = new JSONObject();
                if (variableList.get(i).type.equals("Long"))
                    jo1.put("value", variableList.get(i).getValueAsInt());
                else
                    jo1.put("value", variableList.get(i).value); //string
                jo1.put("type", variableList.get(i).type);
                jo2.put(variableList.get(i).name, jo1);
            }
            jo3.put("variables", jo2);


            body = jo3.toString();
        }else
            body="";

        HttpEntity<String> entity = new HttpEntity<>(body,header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.POST, entity);


        if( res.getBody()==null ){ //sucsess
            System.out.println("Completing task success.");
            return true;
        }
        System.out.println("Completing task failed.");
        return false;
    }

    public boolean completeEvaluationTask(String taskID,ArrayList<Evaluation> evaluations) throws Exception {

//        JSONArray ja = new JSONArray();
//        for(int i=0;i<evaluations.size();i++)
//            ja.put(evaluations.get(i).serialize());
//        String s = ja.toString();
        JSONArray ja = Evaluation.Helper.toJSONArray(evaluations);
        String evaluaitonsS = ja.toString();

        ArrayList<FormVariable> fv = new ArrayList<FormVariable>(1);
        fv.add(new FormVariable().setName("evaluationsJSON").setValue(evaluaitonsS).setType("String"));

        return completeTask(taskID,fv);
    }

    public ArrayList<ProcessDefinition> getProcessDefinitions() throws Exception {
        String url = getCamundaURL()  + "/process-definition";
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type","application/json");


        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.GET, entity);

        JSONArray ja = new JSONArray(res.getBody());
        ArrayList<ProcessDefinition> processDefinitions = new ArrayList<ProcessDefinition>();
        for(int i=0;i<ja.length();i++){
            processDefinitions.add(ProcessDefinition.createFromJSON(ja.getJSONObject(i).toString()));
        }

        return processDefinitions;
    }

    public ArrayList<Task> getActiveTasks() throws Exception {
        String url = getCamundaURL()  + "/task";
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type","application/json");

        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.GET, entity);

        JSONArray ja = new JSONArray(res.getBody());
        ArrayList<Task> tasks = new ArrayList<Task>();
        for(int i=0;i<ja.length();i++){
            tasks.add(Task.createFromJSON(ja.getJSONObject(i).toString()));
        }

        return tasks;
    }

    public Task getTask(String taskID) throws Exception {
        String url = getCamundaURL()  + "/task/"+taskID;
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type","application/json");

        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.GET, entity);

        return Task.createFromJSON(res.getBody());
    }

    public ArrayList<FormVariable> getFormVariablesFromTask(String taskID) throws Exception {
        String url = getCamundaURL()  + "/task/"+taskID+"/form-variables";
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type","application/json");

        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.GET, entity);

        JSONObject jo = new JSONObject(res.getBody());
        ArrayList<FormVariable> variables = new ArrayList<FormVariable>();

        Iterator<String> keys = jo.keys();
        while(keys.hasNext()){
            String key=keys.next();
            if(!key.equals("evaluationsJSON")) {    //part of workaround The serialized Evaluation collection
                try {
                    variables.add(FormVariable.createFromJSON(jo.get(key).toString())
                            .setName(key));
                } catch (Exception e) {
                    //save only primitive variable
                }
            }
        }
        return variables;
    }

    public String getFormKeyFromTask(String taskID) throws Exception {
        String url = getCamundaURL()  + "/task/"+taskID+"/form";
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type","application/json");

        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.GET, entity);


        String key = new JSONObject(res.getBody()).getString("key");
        return key;
    }

    public ArrayList<Evaluation> getEvaluationsFromTask(String taskID) throws Exception {
        //fetch the JSON version not the Object

        String url = getCamundaURL() + "/task/"+taskID+"/form-variables";
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type","application/json");

        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.GET, entity);


        JSONObject resJo = new JSONObject(res.getBody());
        JSONObject evalJo = resJo.getJSONObject("evaluationsJSON");
        String value = evalJo.getString("value");

        JSONArray ja = new JSONArray(value);



        ArrayList<Evaluation> evaluations = Evaluation.Helper.toList(ja);


        for(Evaluation evaluation : evaluations){
            for(int i=0;i<evaluation.orderEvaluations.size();i++)
                if (evaluation.orderEvaluations.get(i).orderEvaluationID==0)
                    evaluation.orderEvaluations.get(i).orderEvaluationID=i+1;
        }


        return evaluations;
    }

}
