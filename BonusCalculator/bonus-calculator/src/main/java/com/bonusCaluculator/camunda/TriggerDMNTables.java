package com.bonusCaluculator.camunda;

import com.bonusCaluculator.Model.Evaluation;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TriggerDMNTables implements JavaDelegate {


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        ArrayList<Evaluation> evaluations = (ArrayList<Evaluation>) execution.getVariable("evaluations");
        DecisionService decisionService = execution.getProcessEngineServices().getDecisionService();

        for (int i=0; i<evaluations.size();i++){
            //======  Social Evaluation ======
            for(int j=0;j<evaluations.get(i).socialEvaluations.size();j++) {
                HashMap<String, Object> socialEvaluation = new HashMap<String, Object>(1);
                socialEvaluation.put("socialEvaluation",evaluations.get(i).socialEvaluations.get(j));
                DmnDecisionResult decisionResult = decisionService.evaluateDecisionByKey("socialEvaluationRules").variables(socialEvaluation).evaluate();

                evaluations.get(i).socialEvaluations.get(j).bonus = (String)decisionResult.getFirstResult().get("bonus"); //Update evaluated bonus
            }

            //======  Order Evaluation ======
            for(int j = 0; j<evaluations.get(i).orderEvaluations.size(); j++) {
                HashMap<String, Object> orderEvaluation = new HashMap<String, Object>(1);
                orderEvaluation.put("orderEvaluation",evaluations.get(i).orderEvaluations.get(j));
                DmnDecisionResult decisionResult = decisionService.evaluateDecisionByKey("orderEvaluationRules").variables(orderEvaluation).evaluate();

                if (decisionResult.getFirstResult().get("bonus")==null){
                    evaluations.get(i).orderEvaluations.get(j).bonus = "-1";
                    evaluations.get(i).orderEvaluations.get(j).comment= "Wrong or missing DMN rule!";
                }
                evaluations.get(i).orderEvaluations.get(j).bonus = (String)decisionResult.getFirstResult().get("bonus"); //Update evaluated bonus
            }
        }
        System.out.println("Automatic bonus calculation finished.");


//        JSONArray ja = new JSONArray();
//        for(int i=0;i<evaluations.size();i++)
//            ja.put(evaluations.get(i).serialize());
        JSONArray ja = Evaluation.Helper.toJSONArray(evaluations);

        String s = ja.toString();

        execution.removeVariables();//prevent that the old object will be used
        execution.setVariable("evaluationsJSON", s); //workaround because a complex object cant get updated via REST


    }
}
