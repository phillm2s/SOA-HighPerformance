package com.bonusCaluculator.camunda;

import com.bonusCaluculator.Model.Evaluation;
import com.bonusCaluculator.Model.OrderEvaluation;
import com.bonusCaluculator.REST.HPDatabase.HPDatabaseAPI;
import com.bonusCaluculator.REST.OpenCRX.*;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import java.util.ArrayList;
import java.util.List;

public class loadEvaluationData implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        int year = ( (Long)delegateExecution.getVariable("year_input") ).intValue();

        delegateExecution.removeVariable("year_input");

        System.out.println("\nStart loading evaluation data for "+year+" ...\n");

        //getData From MySqlDB
        HPDatabaseAPI db = HPDatabaseAPI.getInstance();
        ArrayList<Evaluation> minimalEvaluations = db.getAllMinimalEvaluations(year); // to get a list of employees with Evaluations

        //for each evaluation load socialPerformance Data and basic evaluation data
        ArrayList<Evaluation> evaluations = new ArrayList<Evaluation>();
        for (int i=0;i< minimalEvaluations.size();i++){
            evaluations.add( db.getEvaluation(minimalEvaluations.get(i).emplyeeID,year) );
        }

        System.out.println(evaluations.size() +" evaluations loaded from HighPerformance Evaluation DB.");

        //getData from OpenCRX reference via openCRXRef field from mySqlDb
        OpenCRXAPI openCRX = OpenCRXAPI.getInstance();

        List<SalesOrder> salesOrders = openCRX.getAllSalesOrders();
        for(int i=0;i < evaluations.size();i++){
            try {
                ArrayList<OrderEvaluation> orderEvaluations= new ArrayList<OrderEvaluation>();
                for (int j=0;j<salesOrders.size();j++){ //check if SalesOrder refers to current Evaluation/Employee
                    if(salesOrders.get(j).salesmanRef.equals(evaluations.get(i).openCRXEmployeeRef)
                            && salesOrders.get(j).getCreationYear()==year){
                        //get Orderpositions for the matching Salesorder
                        List<Position> positions = openCRX.getOrderPositions(salesOrders.get(j).ref);
                        try {
                            Account client = openCRX.getAccount(salesOrders.get(j).customerRef);
                            for(int k=0;k<positions.size();k++) {
                                //map position to -> OrderEvaluation
                                orderEvaluations.add(new OrderEvaluation()
                                        .setProductName(openCRX.getProduct(positions.get(k).productRef).name)    //get Product Information via reference
                                        .setPrice(positions.get(k).pricePerUnit)
                                        .setAmount(positions.get(k).amount)
                                        .setClientName(client.fullName)
                                        .setClientRanking(client.accountRating));
                            }
                        }catch(Exception e){
                            System.out.println("Exception while loading orderEvalautions from OpenCRX");
                            System.out.println(e.getMessage());
                        }
                    }
                }
                evaluations.get(i).setOrderEvaluations(orderEvaluations);
                System.out.println(orderEvaluations.size() + " orderEvaluations set for Evaluation from: " + evaluations.get(i).fullName);

            }catch(Exception e){
                System.out.println("OrderEvaluation for: "+evaluations.get(i).fullName+" failed!");
                System.out.println(e.getMessage());
            }
        }


        if(evaluations.size()<1) {
            System.out.println("No matching Evaluations found.");
        }
        else {
            System.out.println(evaluations.size()+ " evaluations loaded.");
        }


        //-----set--------
        delegateExecution.setVariable("evaluations", evaluations);

    }

}
