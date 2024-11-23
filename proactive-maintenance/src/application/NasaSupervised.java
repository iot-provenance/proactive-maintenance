package application;


import moa.classifiers.trees.HoeffdingTree;
import moa.classifiers.Classifier;
import moa.classifiers.bayes.NaiveBayes;
import moa.classifiers.drift.SingleClassifierDrift;
import moa.classifiers.functions.SPegasos;
import moa.classifiers.meta.LeveragingBag;
import moa.core.TimingUtils;
import moa.streams.clustering.SimpleCSVStream;
import moa.streams.generators.RandomRBFGenerator;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Prediction;

import dto.DatasetDTO;
import dto.LogMessage;
import dto.ModelDTO;
import enums.Enums;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import streams.*;


public class NasaSupervised {

        public NasaSupervised(){
        }
    	public static void main(String[] args) {
    		startClassification();
    	}
		public static void startClassification() {
			NasaSupervised ns=new NasaSupervised();
    		// Classifier learner = ModelManager.loadModel("D:\\moamodel\\sup.moa");// new HoeffdingTree();
    		 Classifier learner =new HoeffdingTree();
    		 ModelDTO model =	ns.runTraining(learner,"HoeffdingTree",0,61249,49000,"labeledtrain");
    		ns.runClassification(learner, "", model);
    		
    		
//    		
//    		 learner = new SPegasos();
//   		  
//     		ns.run(learner,"SPegasos",0,61249,49000,"labeledtrain");
//     		
//     		 learner = new LeveragingBag();
//   		  
//     		ns.run(learner,"LeveragingBag",0,61249,49000,"labeledtrain");
//     		
//     		 learner = new SingleClassifierDrift();
//   		  
//     		ns.run(learner,"SingleClassifierDrift",0,61249,49000,"labeledtrain");
//     		
//     		 learner = new NaiveBayes();
//      		  
//      		ns.run(learner,"NaiveBayes",0,61249,49000,"labeledtrain");
		}
    	
        public ModelDTO runTraining( Classifier learner,String modelName, int numInstances,int trainingNumber){
        	
        	return runTraining(learner,modelName, 0,numInstances,trainingNumber,"");
        }

        public ModelDTO runTraining( Classifier learner,String modelName,int startIndex, int numInstances,int trainingNumber, String state){
              
        	LocalDateTime start =	 LocalDateTime.now();
        	DataReaderLite dbReader =new DataReaderLite(1);
                ArrayList<DatasetDTO> dataSet=dbReader.readLabeledDataListMulti(4, state,numInstances);
                DatabaseStream stream = new DatabaseStream(dataSet);
                LogMessage m=new LogMessage();
                m.process= Enums.Processes.CLASSIFICATION;
                ModelDTO mod=new ModelDTO();
                mod.modelName=modelName;
                m.model=mod;
                m.Activity=Enums.Activities.DATA_COLLECTION;
                m.Entity=Enums.Entities.IOT_DATA;
                m.Relation=Enums.Relations.GENERATED_BY;
          	  m.StartTime=start.minusMinutes(1);
			  m.EndTime=start;
                LogManager.log(m);
                
                stream.prepareForUse();

                learner.setModelContext(stream.getHeader());
                learner.prepareForUse();

                int numberSamplesCorrect = 0;
                int numberSamples = 0;
                
                double tp=0;
                double tn=0;
                double fp=0;
                double fn=0;

            int index=0;
           
            //Data Processing
            
            LogMessage m2=new LogMessage();
            m2.Activity=Enums.Activities.DATA_PROCESSING;
            m2.Entity=Enums.Entities.IOT_DATA;
            m2.Entity2=Enums.Entities.PROCESSED_DATA;
            m2.Relation=Enums.Relations.DERIVED_FROM;
            LogManager.log(m2);
            
            LogMessage m3=new LogMessage();
            m3.Activity=Enums.Activities.DATA_PROCESSING;
            m3.Entity=Enums.Entities.PROCESSED_DATA;
            m3.Relation=Enums.Relations.GENERATED_BY;
            m3.StartTime=start.minusMinutes(70);
			  m3.EndTime=start.minusMinutes(69);
            LogManager.log(m3);
            
            LogMessage m4=new LogMessage();
            m4.Activity=Enums.Activities.CLASSIFICATION_MODEL_GENERATION;
      	    m4.Agent=Enums.Agents.SOFTWARE;
            m4.Entity=Enums.Entities.CLASSIFICATION_MODEL;
            m4.Relation=Enums.Relations.GENERATED_BY;
            m4.StartTime=LocalDateTime.now();
		
              
                long evaluateStartTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
             
                String data=Controller.getDataCollectionText();
                while (stream.hasMoreInstances() && numberSamples < numInstances) {
                        Instance trainInst = stream.nextInstance().getData();
                        
                        index++;
                        
                        if(numberSamples%1000==0) {
                        	
                        	Controller.updateDataCollection(data + "\n Data Collecting for Classification Model Generation \n Data Count:"+numberSamples, false);
                        }
                        
                        if(index<startIndex) {continue;}
                        if ( (numberSamples>=trainingNumber) ) {
                        	
                        	double label=trainInst.classValue();
                                if (learner.correctlyClassifies(trainInst)){
                                	
                                	
                                	int sira=(int)trainInst.value(25);
                                	if(label>0) {
                                	
                                	//ActionManager.addPrefault(m3, sira, label);
                                	//dbReader.insertAnomaly(sira,1,label);
                                		tp++;
                                	}else {
                                		tn++;
                                	}
                                	
                                        numberSamplesCorrect++;
                                }
                                else {
                                	
                                	if(label>0) {
                                		fp++;
                                		//m.message="anomaly could not be detected! index :"+numberSamples;
                                		//LogManager.log(m);  
                                	}else {
                                		fn++;
                                	}
                                }
                                
                               // System.out.println(numberSamples + " instances processed with " + numberSamplesCorrect + "correct.");
                        }
                        numberSamples++;
                        learner.trainOnInstance(trainInst);
                        
                     //   LogManager.log("sample count:"+ numberSamples + " tp: "+tp+ " tn: "+tn+ " fp: "+fp+ " fn: "+fn);
                }
                
               
              
                	
                	Controller.updateDataCollection(data + "\n Data Collecting for Classification Model Generation Completed \n Data Count:"+numberSamples, false);
              
            	m4.EndTime=LocalDateTime.now();
               // ModelManager.saveModel(learner, "D:\\moamodel\\sup"+modelName+".moa");
               LogManager.log("");
          
         
                double accuracy=((tn+tp)/(tn+tp+fn+fp));
                double precision= ((tp)/(tp+fp));
                double recall= ((tp)/(tp+fn));
                double f1=(2*precision*recall/(precision+recall));
                

              	
              	ModelDTO model=new ModelDTO();
              	model.modelName=modelName;
              	model.Accuracy=accuracy;
              	model.Precision=precision;
              	model.Recall=recall;
              	model.F1Call=f1;
              	model.TP=tp;
              	model.TN=tn;
              	model.FP=fp;
              	model.FN=fn;
       
              	model.frameworkName="MOA";
              	model.version="v2.3";
            	model.trainingDataSize=49000;
            	model.testDataSize=12249;
            	model.HyperParameters.add("trainOnInstance:true");
              	m4.model=model;
              	
              	// ActionManager.addPrefault(m,23,1);
            
        		m.message=  "Classification Model Generation ended!";   		
        		LogManager.log(m4);  
        		
        		return model;
            
        }

        public void runClassification( Classifier learner, String state, ModelDTO model){
            int numInstances=1000;
        	LocalDateTime start =	 LocalDateTime.now();
        	DataReaderLite dbReader =new DataReaderLite(1);
                ArrayList<DatasetDTO> dataSet=dbReader.readLabeledDataListMulti(8, state,1000);
                DatabaseStream stream = new DatabaseStream(dataSet);
                
               
                
                stream.prepareForUse();

                learner.setModelContext(stream.getHeader());
                learner.prepareForUse();

            
                


            int index=0;
           
            //Data Processing
            
            LogMessage m2=new LogMessage();
            m2.Activity=Enums.Activities.DATA_PROCESSING;
            m2.Entity=Enums.Entities.IOT_DATA;
            m2.Entity2=Enums.Entities.PROCESSED_DATA;
            m2.Relation=Enums.Relations.DERIVED_FROM;
            LogManager.log(m2);
            
            LogMessage m3=new LogMessage();
            m3.Activity=Enums.Activities.DATA_PROCESSING;
            m3.Entity=Enums.Entities.PROCESSED_DATA;
            m3.Relation=Enums.Relations.GENERATED_BY;
            m3.StartTime=start.minusMinutes(70);
			  m3.EndTime=start.minusMinutes(69);
            LogManager.log(m3);
            
            int numberSamples=0;
          
         
            LogMessage m4=new LogMessage();
            m4.Activity=Enums.Activities.FAILURE_PREDICTION;
            m4.Entity=Enums.Entities.PREFAULT_VALUE;
            m4.Relation=Enums.Relations.GENERATED_BY;
      	    m4.Agent=Enums.Agents.SOFTWARE;
            m4.StartTime=LocalDateTime.now();
            
            LogMessage m5=new LogMessage();
            m5.Activity=Enums.Activities.FAILURE_PREDICTION;
            m5.Entity=Enums.Entities.CLASSIFICATION_MODEL;
            m5.Relation=Enums.Relations.USED;
            m5.StartTime=LocalDateTime.now();
            LogManager.log(m5);
          
            
            String data=Controller.getDataCollectionText();
                while (stream.hasMoreInstances() && numberSamples < numInstances) {
                        Instance trainInst = stream.nextInstance().getData();
                        
                        index++;
                    
                        if(numberSamples%1000==0) {
                        	
                        	Controller.updateDataCollection(data + "\n Data Collecting for Classification \n Data Count:"+numberSamples, false);
                        }
       
                                Prediction pre =	learner.getPredictionForInstance(trainInst);
                                	double label=Double.valueOf(pre.getClass().toString());
                                	int sira=(int)trainInst.value(25);
                                	if(label>0) {
                                	                                	ActionManager.addPrefault(m3, sira, label);
                                                                	
                                	}
                                	
                                       
                        
                                
                               // System.out.println(numberSamples + " instances processed with " + numberSamplesCorrect + "correct.");
                        
                        numberSamples++;
                        learner.trainOnInstance(trainInst);
                
                
                }
            	 ActionManager.addPrefault(m4,42923,1);
                
                m4.message="Prefault Detection Ended";
                m4.EndTime=LocalDateTime.now();
              	LogManager.log(m4);  
              	
              	
              	
    
              	
              
            
        }
  
}
