package com.DataBaseRestApi.DataBaseRestApi.REST;


import com.DataBaseRestApi.DataBaseRestApi.Model.Evaluation;
import com.DataBaseRestApi.DataBaseRestApi.DataBaseConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RESTListener { // handel incoming rest calls
    private final String baseURL = "/HPevaluation/api";

    //region =========GET Requests==============

    @RequestMapping(baseURL)    //handel get request and answer plain text
    public String sayHello() {
        return "<hi>Hello from the REST Listener</h1>";
    }

    @RequestMapping(baseURL+"/evaluations")
    public List<Evaluation> getAllEvaluations() {
        try {
            return DataBaseConnection.getInstance().getAllMinimalEvaluations();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RESTException().addMessage("Internal SQL Exception", e.getMessage());
        }
    }

    //endregion

    //region =========POST Requests==============
    @RequestMapping(
            value = baseURL + "/evaluation",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public Evaluation getEvaluation(@RequestParam Map<String, Object> body) {
        ParameterValidator validator = new ParameterValidator(body);

        try {
            Evaluation evaluation = DataBaseConnection.getInstance().getFullEvaluation(validator.getInt("employeeID"), validator.getInt("year")+"");
            return evaluation;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RESTException().addMessage("Internal SQL Exception",e.getMessage());
        } catch (NoSuchFieldException e) {
            throw new RESTException().addMessage("NoSuchElementException",e.getMessage());
        }
    }


    @RequestMapping(
            value = baseURL + "/evaluations/year",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public List<Evaluation> getEvaluations(@RequestParam Map<String, Object> body) {
        ParameterValidator validator = new ParameterValidator(body);

        try {
            return DataBaseConnection.getInstance().getAllMinimalEvaluations(validator.getInt("year")+"");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RESTException().addMessage("Internal SQL Exception",e.getMessage());
        }
    }



    //endregion

    //region =========PUT Requests==============

    @RequestMapping(
            value = baseURL + "/evaluations/update",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public String updateEvaluation(@RequestBody String body) throws JSONException, JsonProcessingException, SQLException {
        JSONArray ja = new JSONArray(body);
        ArrayList<Evaluation> evaluations = Evaluation.Helper.toList(ja);

        for (Evaluation evaluation : evaluations)
            DataBaseConnection.getInstance().updateFullEvaluation(evaluation);

        return "success";
    }

    //endregion

    //region =========PATCH Requests==============


    //endregion

    //region =========DELET Requests==============


    //endregion

    //region ======= Exception handling ================

    @ExceptionHandler
    public Map<String,String> handleException(RESTException e){
        return e.messages;
    }

    //endregion


}