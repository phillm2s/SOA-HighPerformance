package com.bonusCaluculator.camunda;

import com.bonusCaluculator.Model.Evaluation;
import com.bonusCaluculator.REST.OrangeHRM.CustomField;
import com.bonusCaluculator.REST.OrangeHRM.OrangeHRMAPI;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveBonusInOrangeHRMCustomField implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        ArrayList<Evaluation> evaluations= (ArrayList<Evaluation>) execution.getVariable("evaluations");

        for (Evaluation evaluation : evaluations) {
            HashMap<String, CustomField> customFields = (HashMap<String, CustomField>)OrangeHRMAPI.getInstance().getAllBonusFieldsFromUser(evaluation.emplyeeID+"");
            for(String key : customFields.keySet()){
                if(customFields.get(key).name.equals("BonusGehalt_Hillmann")){
                    CustomField customField = customFields.get(key);
                    customField.setValue(evaluation.bonus);
                    OrangeHRMAPI.getInstance().setBonusField(evaluation.emplyeeID+"" , customField);
                    System.out.println("Orange HRM bonus field for "+evaluation.fullName +" updated with "+ customField.value+"€ .");
                }
            }
        }
    }
}
