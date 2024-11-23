package dto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.openprovenance.prov.model.Attribute;

import enums.Enums;

public class LogMessage {
	
	public LogMessage(LogMessage _base) {
		
		this.model=_base.model;
	}
	
	public LogMessage() {
	
	}
	public Enums.Processes process;
	
	public int dataId;
	
	public double rulValue;

	public String message;
	
	public Enums.Agents Agent;
	
	public Enums.Agents DelegateAgent;
	
	public Enums.Entities Entity;
	
	public Enums.Entities Entity2;
	
	public String AgentName;
	
	public String EntityName;
	
	public LocalDateTime StartTime;
	
	public LocalDateTime EndTime;
	
	public Enums.Activities Activity;
	
	public Enums.Relations Relation;
	
	public ModelDTO model;
	
	public ArrayList<Attribute> Attributes=new 	ArrayList<Attribute> ();
	
	public ArrayList<ActionDTO> Actions=new 	ArrayList<ActionDTO> ();	

	public ArrayList<Attribute> ActivityAttributes=new 	ArrayList<Attribute> ();
	
	public ArrayList<Attribute> AgentAttributes=new 	ArrayList<Attribute> ();
	
public String getMessageSummary() {
	
	return "process: "+ process +" model: "+model+  " Activity: "+Activity+" startTime:"+ getDate(StartTime) + " endTime:"+getDate(EndTime) +"\n"+getProvSummary();
}

private String getDate(LocalDateTime time) {

	if(time!=null)
		return time.toString();
	return "";
}

public String getProvSummary() {
	
	return "Agent: "+ Agent +" Activity: "+Activity+ " Entity: "+Entity+" Entity2: "+Entity2+" Relation: "+Relation +" " + getAttributesSummary();
}

private String getAttributesSummary() {
	StringBuilder sb=new StringBuilder();
	
	if(model!=null) {

		sb.append(" modelName:"+ model.modelName);
		sb.append(" modelType:"+ model.modelType);
		
		sb.append(" frameworkName:"+ model.frameworkName);
		sb.append(" version:"+ model.version);
		sb.append(" accuracy:"+ model.Accuracy); 
		sb.append(" F1Call:"+ model.F1Call);
		sb.append(" Precision:"+ model.Precision);
		sb.append(" Recall:"+ model.Recall);
		sb.append(" MAE:"+ model.MAE);
		sb.append(" MAPE:"+ model.MAPE);
		sb.append(" RMSE:"+ model.RMSE);
		sb.append(" MSE:"+ model.MSE);
		sb.append(" SilhouetteValue:"+ model.SilhouetteValue);
	
	
		sb.append(" VMeasure:"+ model.VMeasure);
	
		sb.append(" testDataSize:"+ model.testDataSize);
		sb.append(" trainingDataSize:"+ model.trainingDataSize);
		
		for(int i=0;i<model.HyperParameters.size();i++) {
			
			sb.append(" hyperparameter"+i+1+":"+model.HyperParameters.get(i));
		}
		
		
	}
	
	
	return sb.toString();
}

}
