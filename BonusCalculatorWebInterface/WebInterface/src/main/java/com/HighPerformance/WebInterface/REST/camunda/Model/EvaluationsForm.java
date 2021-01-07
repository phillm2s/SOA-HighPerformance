package com.HighPerformance.WebInterface.REST.camunda.Model;

import java.util.List;

public class EvaluationsForm {

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> variables) {
        this.evaluations = variables;
    }

    public List<Evaluation> evaluations;
}
