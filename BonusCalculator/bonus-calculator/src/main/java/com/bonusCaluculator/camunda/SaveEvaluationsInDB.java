package com.bonusCaluculator.camunda;

import com.bonusCaluculator.Model.Evaluation;
import com.bonusCaluculator.REST.HPDatabase.HPDatabaseAPI;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONArray;

import java.util.ArrayList;

public class SaveEvaluationsInDB implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String evaluationsS = (String)execution.getVariable("evaluationsJSON");

        JSONArray evaluationsJA = new JSONArray(evaluationsS);
        ArrayList<Evaluation> evaluations = new ArrayList<Evaluation>();
        for (int i=0;i<evaluationsJA.length();i++){
            String evaluationS= evaluationsJA.getString(i); //jo.toString();
            Evaluation evaluation = Evaluation.createFromJSON(evaluationS);
            evaluations.add(evaluation);
        }


        if ( !HPDatabaseAPI.getInstance().updateEvaluations(evaluations))
            throw new Exception();

        execution.setVariable("evaluations",evaluations); //part of workaround
    }
}
