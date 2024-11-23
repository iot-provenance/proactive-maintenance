package application;
import java.time.LocalDateTime;
import java.util.ArrayList;
import dto.*;
import enums.Enums;
import enums.Enums.Agents;
public class ActionManager {
	
	public static void addPrefault(LogMessage _m,int dataId, double label) {
		
		_m.message="anomaly detected! index :"+dataId +" label:"+label;
		
		Controller.updateResult(_m.message);
		
		LogMessage m1=new LogMessage(_m);

	m1.process=Enums.Processes.CLASSIFICATION;
		   m1.Activity=Enums.Activities.CLASSIFICATION_MODEL_GENERATION;
           m1.Entity= Enums.Entities.CLASSIFICATION_MODEL;
           m1.Relation=Enums.Relations.GENERATED_BY;
       
         
		LogManager.log(m1); 
		
		LogMessage m2=new LogMessage(_m);

		 m2.Activity=Enums.Activities.CLASSIFICATION_MODEL_GENERATION;
        m2.Entity=Enums.Entities.LABELED_DATA;
        m2.Relation=Enums.Relations.USED;
		LogManager.log(m2); 
		
		LogMessage m3=new LogMessage(_m);
		m3.process=Enums.Processes.CLASSIFICATION;
		   m3.Activity=Enums.Activities.FAILURE_PREDICTION;
        m3.Entity= Enums.Entities.PREFAULT_VALUE;
        m3.Relation=Enums.Relations.GENERATED_BY;
        m3.dataId=dataId;
		LogManager.log(m3); 
		
		LogMessage m4=new LogMessage(_m);
		
		 m4.Activity=Enums.Activities.FAILURE_PREDICTION;
         m4.Entity=Enums.Entities.PROCESSED_DATA;
         m4.Relation=Enums.Relations.USED;
         m4.dataId=dataId;
		LogManager.log(m4); 
		
		LogMessage m5=new LogMessage(_m);
		 m5.Activity=Enums.Activities.FAILURE_PREDICTION;
        m5.Entity=Enums.Entities.CLASSIFICATION_MODEL;
        m5.Relation=Enums.Relations.USED;
        m5.dataId=dataId;
		LogManager.log(m5); 
		//printRULforAnomalies(m,dataId);
	double RUL=	getRULforAnomaly(_m,dataId);
		createAction(_m,dataId, RUL);
		
	}

	private static double getRULforAnomaly(	LogMessage m,int dataId) {
		try {
			Thread.sleep(15*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataReaderLite reader=new DataReaderLite(1);
		//ArrayList<AnomalyDTO> a=reader.getAnomaly(dataId);
		AnomalyDTO d=new AnomalyDTO();
		d.dataId=1;d.label=1;d.rul=3;
		ArrayList<AnomalyDTO> a=new 	ArrayList<AnomalyDTO> ();
		a.add(d);
		boolean modelGenerated=false;
		
		if(a.size()>0) {
			if(!modelGenerated) {
			LogMessage m2=new LogMessage();
	
		m2.process=Enums.Processes.RUL;
		m2.Activity=Enums.Activities.RUL_MODEL_GENERATION;
        m2.Entity=Enums.Entities.PROCESSED_DATA;
        m2.Relation=Enums.Relations.USED;
		LogManager.log(m2);
		
		ModelDTO model=new ModelDTO();
		model.modelName="LSTM";
		model.frameworkName="TensorFlow";
		model.MAE=32.09;
		model.MAPE=37.73;
		model.MSE=1885.77;
		model.RMSE=43.43;
     	model.version="v2.3";
    	model.trainingDataSize=49000;
    	model.testDataSize=12249;
    	model.HyperParameters.add("validationPercentage:20");
		
		LogMessage m3=new LogMessage();
		m3.model=m.model;
		 m3.Activity=Enums.Activities.RUL_MODEL_GENERATION;
        	m3.Entity=Enums.Entities.RUL_MODEL;
        	
         m3.Relation=Enums.Relations.GENERATED_BY;
   m3.model=model;
		LogManager.log(m3);
		
		modelGenerated=true;
			}
		
		LogMessage m4=new LogMessage();
		m4.message=a.get(0).rul+" RUL calculated for data:"+a.get(0).dataId ;
		m4.process=Enums.Processes.RUL;
		m4.Activity=Enums.Activities.RUL_CALCULATION;
        m4.Entity=Enums.Entities.PREFAULT_VALUE;
        m4.Relation=Enums.Relations.USED;
        m4.dataId=dataId;
		LogManager.log(m4);
		
		LogMessage m5=new LogMessage();
		 m5.Activity=Enums.Activities.RUL_CALCULATION;
        	m5.Entity=Enums.Entities.RUL_VALUE;
         m5.Relation=Enums.Relations.GENERATED_BY;
         m5.dataId=dataId;
         double rul= a.get(0).rul;
         m5.rulValue=rul;
 
		LogManager.log(m5);
		

		LogMessage m6=new LogMessage();
		m6.process=Enums.Processes.RUL;
		m6.Activity=Enums.Activities.RUL_CALCULATION;
        m6.Entity=Enums.Entities.RUL_MODEL;
        m6.Relation=Enums.Relations.USED;
        m6.dataId=dataId;
		LogManager.log(m6);
		
		
		return rul;
		}
		
		return 0;
	}
	
	private static void printRULforAnomalies(	LogMessage m,int dataId) {
		try {
			Thread.sleep(3*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataReaderLite reader=new DataReaderLite(1);
		ArrayList<AnomalyDTO> a=reader.getAnomaliesRUL();
		
		if(a==null)
			return;
		boolean modelGenerated=false;
		for(int i=0;i<a.size();i++) {
			
			if(!modelGenerated) {
			LogMessage m2=new LogMessage();
			
			m2.process=Enums.Processes.RUL;
			m2.Activity=Enums.Activities.RUL_MODEL_GENERATION;
	        m2.Entity=Enums.Entities.PROCESSED_DATA;
	        m2.Relation=Enums.Relations.USED;
			LogManager.log(m2);
			
			LogMessage m3=new LogMessage();
			 m3.Activity=Enums.Activities.RUL_MODEL_GENERATION;
			  m3.Agent=Enums.Agents.SOFTWARE;
	        	m3.Entity=Enums.Entities.RUL_MODEL;
	         m3.Relation=Enums.Relations.GENERATED_BY;
			LogManager.log(m3);
			
			modelGenerated=true;
				}
			
			LogMessage m4=new LogMessage();
			m4.message=a.get(0).rul+" RUL calculated for data:"+a.get(0).dataId ;
			m4.process=Enums.Processes.RUL;
			m4.Activity=Enums.Activities.RUL_CALCULATION;
	        m4.Entity=Enums.Entities.PREFAULT_VALUE;
	        m4.Relation=Enums.Relations.USED;
	        m4.dataId=dataId;
			LogManager.log(m4);
			
			LogMessage m5=new LogMessage();
			 m5.Activity=Enums.Activities.RUL_CALCULATION;
	        	m5.Entity=Enums.Entities.RUL_VALUE;
	         m5.Relation=Enums.Relations.GENERATED_BY;
	   	  m5.Agent=Enums.Agents.SOFTWARE;
	         m5.dataId=dataId;
			LogManager.log(m5);
			
			LogMessage m6=new LogMessage();
			m6.process=Enums.Processes.RUL;
			m6.Activity=Enums.Activities.RUL_CALCULATION;
	        m6.Entity=Enums.Entities.RUL_MODEL;
	        m6.Relation=Enums.Relations.USED;
	        m6.dataId=dataId;
			LogManager.log(m6);
			
		createAction(m,dataId,a.get(i).rul);
		
		}
		
		
		
	}

	private static void createAction(	LogMessage m,int dataId, double RUL) {
		// TODO Auto-generated method stub
	
		LogMessage m2=new LogMessage();
		m2.process=Enums.Processes.ACTION;
		 m2.Activity=Enums.Activities.ALERT_GENERATION;
      
         m2.Entity=Enums.Entities.ACTION_RECORD;
   	  m2.Agent=Enums.Agents.SOFTWARE;
         m2.Relation=Enums.Relations.GENERATED_BY;
         m2.dataId=dataId;
         ActionDTO action=new ActionDTO();
         action.ActionType=Enums.ActionTypes.STOP_MACHINE;
         action.ActionTime=LocalDateTime.now();
         action.ActionTarget="Machine7";
         action.riskAssesment="high";
       m2.Actions.add(action);
		m2.message="Action created for data:"+dataId +" to be taken in "+RUL+"  hours" ;
		LogManager.log(m2);
		
//		LogMessage m21=new LogMessage();
//		m21.process=Enums.Processes.ACTION;
//		 m21.Activity=Enums.Activities.ALERT_GENERATION;
//      
//         m21.Entity=Enums.Entities.ACTION_RECORD;
//         
//         m21.Relation=Enums.Relations.GENERATED_BY;
//         m21.dataId=dataId;
//         ActionDTO action2=new ActionDTO();
//         action2.ActionType=Enums.ActionTypes.INFORM_OPERATOR;
//         action2.ActionTime=LocalDateTime.now();
//         action2.ActionTarget="Operator3";
//       m21.Actions.add(action2);
//		m21.message="Action created for data:"+dataId +" to be taken in "+RUL+"  hours" ;
//		LogManager.log(m21);
		
		LogMessage m3=new LogMessage();
		
		 m3.Activity=Enums.Activities.ALERT_GENERATION;
	      
         m3.Entity=Enums.Entities.RUL_VALUE;
         m3.Relation=Enums.Relations.USED;
         m3.dataId=dataId;

		LogManager.log(m3); 
		
		LogMessage m4=new LogMessage();
		
		 m4.Activity=Enums.Activities.ALERT_GENERATION;
	      
        m4.Entity=Enums.Entities.PREFAULT_VALUE;
        m4.Relation=Enums.Relations.USED;

		LogManager.log(m4); 
		takeAction(m,dataId);
	}
	
	private static void takeAction(LogMessage m,int dataId) {
	
		LogMessage m2=new LogMessage();
		 m2.Activity=Enums.Activities.ACTION_EXECUTION;
         m2.Agent=Enums.Agents.SYSTEM_ADMINISTRATORS;
         m2.Entity=Enums.Entities.ACTION_RECORD;
         m2.Relation=Enums.Relations.USED; //"used;informedBy;wasGeneratedBy;wasAssociatedWith;wasAttributedTo";
		m2.message=" Action 1 taken for data:"+dataId ;
		  m2.dataId=dataId;
		LogManager.log(m2); 
		
		LogMessage m3=new LogMessage();
		 m3.Activity=Enums.Activities.ACTION_EXECUTION;
         m3.Agent=Enums.Agents.SYSTEM_ADMINISTRATORS;
         m3.Entity=Enums.Entities.MAINTENANCE_RECORD;
         m3.Relation=Enums.Relations.GENERATED_BY; //"used;informedBy;wasGeneratedBy;wasAssociatedWith;wasAttributedTo";
         m3.dataId=dataId;
		LogManager.log(m3); 
		
		LogMessage m4=new LogMessage();
		 m4.Activity=Enums.Activities.ACTION_EXECUTION;
         m4.Agent=Enums.Agents.SYSTEM_ADMINISTRATORS;
         m4.Entity=Enums.Entities.MAINTENANCE_RECORD;
         m4.Relation=Enums.Relations.ASSOCIATED_WITH; //"used;informedBy;wasGeneratedBy;wasAssociatedWith;wasAttributedTo";
         m4.dataId=dataId;
         LogManager.log(m4);
         
         LogMessage m5=new LogMessage();
		 m5.Activity=Enums.Activities.ACTION_EXECUTION;
         m5.Agent=Agents.SYSTEM_ADMINISTRATORS;
         m5.Entity=Enums.Entities.MAINTENANCE_RECORD;
         m5.Relation=Enums.Relations.ATTRIBUTED_TO;
         m5.dataId=dataId;
         LogManager.log(m5); 
		feedBackAnalysis(m,dataId);
	
	}
	
	
	private static void feedBackAnalysis(LogMessage m,int dataId) {
		
		
		// m.Activity= Enums.Activities.FEEDBACK_ANALYSIS;
       // m.Agent="System Administrators";
       // m.Entity="System Configration";
       // m.Relation="used;informedBy;wasGeneratedBy;wasAssociatedWith;wasAttributedTo";
	//	m.message=" Action 1 taken for data:"+dataId ;
	//	LogManager.log(m); 
		PerformansEvalutaion(m,dataId);
	
	}
	
	private static void PerformansEvalutaion(LogMessage m,int dataId) {
		
		
	//	 m.Activity="Performance Evaluation";
     //  m.Agent="Process 5, System Administrators";
     //  m.Entity="Performance Metrics,MaintenanceRcord,System Configration";
     //  m.Relation="used;wasGeneratedBy;wasAssociatedWith;wasAttributedTo";
	//	m.message=" Action 1 taken for data:"+dataId ;
	//	LogManager.log(m); 
	
	}

}
