package application;

import java.util.ArrayList;

import dto.LogMessage;
import enums.Enums;

public  class LogManager {
	
	public static ArrayList<LogMessage> logs=new ArrayList<LogMessage>();
	
	public static void log(String message) {
		System.out.println(message);
		Controller.updateResult(message);
		
	}
	
	public static void log(LogMessage message) {
		System.out.println(message.getMessageSummary());
		
		if(logs.contains(message))
			return;
		
		logs.add(message);
		
		
		
		if(message.message==null) {
			message.message=message.getProvSummary();
		}
		else
		{
			message.message=message.message+"\n"+ message.getProvSummary();
		}
		
		Controller.updateResult(message.getMessageSummary());
		Controller.updateResult(message.message);
		
		if(message.process==Enums.Processes.DATA_COLLECTION) {
			
			Controller.updateDataCollection(message.message,true);
			//Controller.updateState("Data Collection");
		}
		
		if(message.process==Enums.Processes.CLUSTERING) {
			
			Controller.updateClustering(message.message);
		//	Controller.updateState("Clustering");
		}
		
	  if(message.process==Enums.Processes.CLASSIFICATION) {
			
			Controller.updateClassification(message.message);
			//Controller.updateState("Classification");
		}
	
	if(message.process==Enums.Processes.RUL) {
		
		Controller.updateRUL(message.message);
		//Controller.updateState("RUL Calculation");
	}
	
	if(message.process==Enums.Processes.ACTION) {
		
		Controller.updateAction(message.message);
		//Controller.updateState("Action Generation");
	}
		
	}

}
