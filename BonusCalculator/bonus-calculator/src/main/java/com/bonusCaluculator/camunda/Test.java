package com.bonusCaluculator.camunda;

public class Test {

    public void start(){

        try {
            new loadEvaluationData().execute(null);
            System.out.println("Camunda Test successful.");
        } catch (Exception e) {
            System.out.println("Camunda Test failed");
            e.printStackTrace();
        }
    }
}
