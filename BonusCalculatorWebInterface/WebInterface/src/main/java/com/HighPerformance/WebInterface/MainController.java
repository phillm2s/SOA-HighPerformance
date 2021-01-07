package com.HighPerformance.WebInterface;

import com.HighPerformance.WebInterface.REST.camunda.CamundaAPI;
import com.HighPerformance.WebInterface.REST.camunda.Model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@org.springframework.stereotype.Controller
public class MainController {

    @GetMapping("/index")
    public String index(Model model) {
        return "index"; //Name of htlm file
    }


    @GetMapping("/newInstance")
    public String newInstance(Model model) throws JsonProcessingException, JSONException {
        //perform Rest Request to camunda to get available Processes
        try {
            ArrayList<ProcessDefinition> processDefinitions = CamundaAPI.getInstance().getProcessDefinitions();
            model.addAttribute("processDefinitions",processDefinitions);
        }catch (Exception e){
            return "restFailure";
        }
        return "newInstance"; //Name of htlm file
    }


    @PostMapping("/newInstance")
    public String processFormNewInstance(@RequestParam(value = "processID", required = false) String processID, Model model) throws JsonProcessingException {

        System.out.println("Request process start with id: "+processID);
        try {
            ProcessInstance processInstance = CamundaAPI.getInstance().startNewProcessInstance(processID);
        }catch (Exception e){
            return "restFailure";
        }
        return "redirect:/activeTasks";
    }


    @GetMapping("/activeTasks")
    public String activeTasks(Model model) throws JsonProcessingException, JSONException {
        //perform Rest Request to camunda to get available Processes

        try {
            ArrayList<Task> tasks = CamundaAPI.getInstance().getActiveTasks();
            model.addAttribute("tasks",tasks);
        }catch(Exception e){
            e.printStackTrace();
            return "restFailure";
        }

        return "activeTasks"; //Name of htlm file
    }


    @GetMapping("/task")
    public String tasks(
            @RequestParam(value="taskID", required = true) String taskID,
                    Model model) throws JsonProcessingException, JSONException {

        System.out.println("Show task with ID: "+taskID);
        try {
            Task task = CamundaAPI.getInstance().getTask(taskID);
            if (task.assignee==null)
                task.assignee="nobody";
            model.addAttribute("task",task);

            String formKey = CamundaAPI.getInstance().getFormKeyFromTask(taskID);
            if(formKey!=null && formKey.equals("evaluationsList")){ //In this task evaluations have to be editable.
                //wrap this list to mak it responseable in html.
                EvaluationsForm evaluationsForm =new EvaluationsForm();
                evaluationsForm.setEvaluations(CamundaAPI.getInstance().getEvaluationsFromTask(taskID));
                model.addAttribute("evaluationsForm",evaluationsForm); //request container

                return "evaluations";
            }

            Form form= new Form();
            form.variables = CamundaAPI.getInstance().getFormVariablesFromTask(taskID);
            model.addAttribute("form",form);
        }catch(Exception e){
            return "restFailure";
        }
        return "task";
    }


    //recive Input from Task
    @PostMapping("/completeTask")
    public String processFormactiveTasks(
            @ModelAttribute Form form, Model model,
            @RequestParam(value="taskID", required = true) String taskID) throws Exception {

        try {
            if (CamundaAPI.getInstance().completeTask(taskID, form.variables))        //if camudna accept the submited form data
                return "success";
        }catch(Exception e){}

        System.out.println("Completing tasked failed by camunda rest call.");
        return "restFailure";



    }



    @PostMapping("/updateEvaluation")
    public String updateEvaluation(@ModelAttribute EvaluationsForm form, Model model,
                                   @RequestParam(value="taskID", required = true) String taskID) throws Exception {

        //the updated sub objects can get identified via the
        //request the Original Objects because the previous object with all informations is gone :/
        ArrayList<Evaluation> originalEvaluations = CamundaAPI.getInstance().getEvaluationsFromTask(taskID);


        //compare the updated object with the original and update  bonuses and commands for each subobject
        ArrayList<Evaluation> updatedEvaluations = (ArrayList<Evaluation>) form.getEvaluations();
        if (updatedEvaluations!=null)
            for(Evaluation update : updatedEvaluations){

                Evaluation original=null;

                for(Evaluation eval : originalEvaluations)            //find original evaluation with same id
                    if(eval.evaluationID==update.evaluationID)
                        original= eval;

                //update order Evaluations
                if(update.orderEvaluations!=null) //prevent from nullpointer exception in for loop
                    for(OrderEvaluation oeUpdate : update.orderEvaluations){
                        for(OrderEvaluation oeOriginal : original.orderEvaluations) //find original orderEvaluation with same id
                            if(oeOriginal.orderEvaluationID==oeUpdate.orderEvaluationID) {
                                oeOriginal.bonus = oeUpdate.bonus;
                                oeOriginal.comment = oeUpdate.comment;
                            }
                    }
                //update social Evaluation
                if(update.socialEvaluations!=null)
                    for(SocialEvaluation seUpdate : update.socialEvaluations){
                        for(SocialEvaluation seOriginal : original.socialEvaluations) //find original orderEvaluation with same id
                            if(seOriginal.socialEvaluationID==seUpdate.socialEvaluationID) {
                                seOriginal.bonus = seUpdate.bonus;
                                seOriginal.comment = seUpdate.comment;
                            }
                    }
                //update basic objects
                original.comment= update.comment;
                original.updateBonus();

                //Part of the  signature mock
                original.signatureCEO="IaMtHeCeO";
                original.signatureCEO="IaMcHanTalBanks";

            }

        //Finish the Task by sending back the updated Evaluation

        try {
            if( CamundaAPI.getInstance().completeEvaluationTask(taskID,originalEvaluations) )        //if camudna accept the submited form data
                return "success";
        }catch(Exception e){}

        System.out.println("Completing tasked failed by camunda rest call.");
        return "restFailure";

    }

}