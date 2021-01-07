package com.HighPerformance.WebInterface.REST.camunda.Model;

import java.util.List;

public class Form {
    public List<FormVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<FormVariable> variables) {
        this.variables = variables;
    }


    public List<FormVariable> variables;
}
