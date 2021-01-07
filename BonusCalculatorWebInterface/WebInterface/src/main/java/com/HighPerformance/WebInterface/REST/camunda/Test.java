package com.HighPerformance.WebInterface.REST.camunda;

public class Test {



    public void start(){
        try{
            CamundaAPI camunda = CamundaAPI.getInstance();

            //ProcessInstance pi = camunda.startNewProcessInstance("EvaluationProcess");
            //ArrayList<ProcessDefinition> pdList =  camunda.getProcessDefinitions();
            System.out.println("camunda API test success!");
        } catch (Exception e) {
            System.out.println("camunda API test failed!");
            e.printStackTrace();
        }
    }
}
