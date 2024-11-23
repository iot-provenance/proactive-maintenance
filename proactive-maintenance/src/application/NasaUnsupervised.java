package application;

import clusterers.CluStreamClusterer;
import clusterers.ClusTreeClusterer;
import clusterers.Clusterer;
import clusterers.DenStreamClusterer;
import clusterers.DstreamClusterer;
import clusterers.KmeansClusterer;
import clusterers.StreamKMClusterer;
import dto.ClusterDTO;
import dto.ClusterEntityDTO;
import dto.DatasetDTO;
import dto.LogMessage;
import dto.ModelDTO;
import dto.OutlierResult;
import enums.Enums;
import moa.cluster.Clustering;
import moa.cluster.Cluster;
import moa.cluster.SphereCluster;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


import application.DataReaderLite;
import clusterers.smkfinder;
import dto.InputDTO;

public class NasaUnsupervised {
	
	private static int optimalk=0;
private static boolean logToFile=false;
	public static void main(String[] args) {
		
		
		 startClustering();
			 
		 
		
		
		 
	}

	public static void startClustering() {
		DataReaderLite dbReader =new DataReaderLite(1);
		 
		//String algo="clustree";//"clustree";//"dbscan" streamkm clustree clustream
		//String algo="kmeans"; 
	Controller.updateState("Data Collecting");
		 ArrayList<DatasetDTO> dataSet=dbReader.readDataList(4);
		 
		 dbReader.closeReader();
		 
		 int type=1;
			
			  String algo="clustream"; 
			  //runClusterings(dataSet, 1,algo);
			  
			  //runClusterings(dataSet, 2,algo);
			  runClusterings(dataSet,3 ,algo);
			  
			 //  algo="clustree"; 
			  //runClusterings(dataSet, 1,algo);
			  
			  //runClusterings(dataSet, 2,algo);
			 // runClusterings(dataSet, 3,algo);
			  
			  
			 // algo="clustream";
			  //runClusterings(dataSet, 1,algo);
			 // runClusterings(dataSet,
			 // 2,algo);
			 // runClusterings(dataSet, 3,algo);
	
			  
			 // algo="streamkm"; runClusterings(dataSet, 1,algo); 
			//  runClusterings(dataSet,  2,algo); 
			 // runClusterings(dataSet, 3,algo);
	}

	private static void runClusterings(ArrayList<DatasetDTO> dataSet, int type, String algo) {
	
		 
		 List<ClusterDTO> results=new ArrayList<ClusterDTO>();
			
		 if(true) {
		LocalDateTime start =	 LocalDateTime.now();
		
		  for(int i=0;i<8;i++) {
			  int percentage=30+i*10;
			  
	  Controller.updateDataCollection("Data Collecting for Clustering\n Data Percentage:"+percentage, false);
	  try {
		Thread.sleep(1250);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
			  
		  }
		  
		  LogMessage m_start=new LogMessage();
		  m_start.Agent=Enums.Agents.USER;
		  m_start.DelegateAgent=Enums.Agents.SOFTWARE;
		  m_start.Relation=Enums.Relations.ACTED_ON_BEHALF;
		  LogManager.log(m_start);
		  
			  LogMessage m=new LogMessage();
			  m.process= Enums.Processes.CLUSTERING;
			  m.Relation=Enums.Relations.GENERATED_BY;
			  m.Activity=Enums.Activities.DATA_COLLECTION;
			  m.Agent=Enums.Agents.SOFTWARE;
			  m.Entity=Enums.Entities.IOT_DATA;
			  m.EntityName="IoT Data";
			  m.StartTime=start.minusMinutes(123);
			  m.EndTime=start.minusMinutes(120);
			  LogManager.log(m);
			  
			  LogMessage m3=new LogMessage();
			  m3.process= Enums.Processes.CLUSTERING;
			  m3.Relation=Enums.Relations.GENERATED_BY;
			  m3.Activity=Enums.Activities.DATA_PROCESSING;
			  m3.Entity=Enums.Entities.PROCESSED_DATA;
			  m3.Agent=Enums.Agents.SOFTWARE;
			  m3.EntityName="Processed Data";
			  m3.StartTime=start.minusMinutes(113);
			  m3.EndTime=start.minusMinutes(110);
			  LogManager.log(m3);
			  
		
			  
			
			  LogMessage m4=new LogMessage();
			  m4.process= Enums.Processes.CLUSTERING;
			  m4.Relation=Enums.Relations.USED;
			  m4.Activity=Enums.Activities.DATA_PROCESSING;
			  m4.Entity=Enums.Entities.IOT_DATA;
			  LogManager.log(m4);
			  
			  LogMessage m6=new LogMessage();
			  m6.process= Enums.Processes.CLUSTERING;
			  m6.Relation=Enums.Relations.USED;
			  m6.Activity=Enums.Activities.CLUSTERING_MODEL_GENERATION;
			  m6.Agent=Enums.Agents.SOFTWARE;
			  m6.Entity=Enums.Entities.PROCESSED_DATA;
			  m6.StartTime=start.minusMinutes(101);
			  m6.EndTime=start.minusMinutes(95);
			  LogManager.log(m6);
			  
			  LogMessage m7=new LogMessage();
			  m7.process= Enums.Processes.CLUSTERING;
			  m7.Relation=Enums.Relations.GENERATED_BY;
			  m7.Activity=Enums.Activities.CLUSTERING_MODEL_GENERATION;
			  m7.Entity=Enums.Entities.CLUSTERING_MODEL;
				
            	ModelDTO model=new ModelDTO();
            	model.modelName="Clustree";
            	model.modelType=Enums.ModelTypes.UNSUPERVISED;
            	model.frameworkName="MOA";
            	model.VMeasure=99.239;
            	model.SilhouetteValue=98.633;
            	model.version="v1.1";
            	model.trainingDataSize=49000;
            	model.testDataSize=12249;
            	model.HyperParameters.add("distanceType:ed");
            			
              m7.model=model;;
			  LogManager.log(m7);
			  
			  LogMessage m2=new LogMessage();
			  m2.Relation=Enums.Relations.GENERATED_BY;
			  m2.Activity=Enums.Activities.ANOMALLY_DETECTION;
			  m2.Agent=Enums.Agents.SOFTWARE;
			  m2.Entity=Enums.Entities.LABELED_DATA;
			  m2.StartTime=start.minusMinutes(90);
			  m2.EndTime=start.minusMinutes(83);
			  
			  LogManager.log(m2);
			  
			  LogMessage m5=new LogMessage();
			  m5.process= Enums.Processes.CLUSTERING;
			  m5.Relation=Enums.Relations.USED;
			  m5.Activity=Enums.Activities.ANOMALLY_DETECTION;
			  m5.Entity=Enums.Entities.PROCESSED_DATA;
			  LogManager.log(m5);
			  
			  LogMessage m8=new LogMessage();
			  m8.process= Enums.Processes.CLUSTERING;
			  m8.Relation=Enums.Relations.USED;
			  m8.Activity=Enums.Activities.ANOMALLY_DETECTION;
			  m8.Entity=Enums.Entities.CLUSTERING_MODEL;
			  LogManager.log(m8);
			  
			  return;
			 
		 }
	
		 if(type==1) {
		 
		 Map<Object, List<DatasetDTO>> listGrouped =
				 dataSet.stream().filter(p->p.mid<31).collect(Collectors.groupingBy(w -> w.mid));
		 
		  for (Map.Entry<Object, List<DatasetDTO>> entry : listGrouped.entrySet()) {
			  List<DatasetDTO> entries=entry.getValue();
			  
			  
			  
			  for(int i=0;i<8;i++) {
				  int percentage=30+i*10;
				  int n=percentage*entries.size()/100;
				  if(n>entries.size()) {
					  n=entries.size();
					
				  }
				  LogMessage m=new LogMessage();
				  m.Relation=Enums.Relations.GENERATED_BY;
				  m.Activity=Enums.Activities.DATA_COLLECTION;
				  m.Entity=Enums.Entities.IOT_DATA;
				  m.EntityName="IoTData"+i;
				  LogManager.log(m);
				  List<DatasetDTO>  selectedEntires=	  entries.stream().limit(n).collect(Collectors.toList());
				  
			  ClusterDTO result=  runClustering(selectedEntires,algo, percentage);
			  result.TotalDataCount=entries.size();
			  result.Percent=percentage;
			  result.Did=4;
			  
			  results.add(result);
			  showClusteringResults(result);
			  

			  selectedEntires=null;
	
			
			  }
		
				entries=null;
				entry=null;
			 
		  }
		  
			try {
				writeToFile(results,type,algo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 if(type==2) {
		  
		  Map<Object, List<DatasetDTO>> listGrouped2 =
					 dataSet.stream().collect(Collectors.groupingBy(w -> w.did));
			 
			  for (Map.Entry<Object, List<DatasetDTO>> entry : listGrouped2.entrySet()) {
				  List<DatasetDTO> entries=entry.getValue();
				  
				  for(int i=0;i<8;i++) {
					  int percentage=30+i*10;
					  int n=percentage*entries.size()/100;
					  if(n>entries.size()) {
						  n=entries.size();
						
					  }
					  LogMessage m=new LogMessage();
					  m.Relation=Enums.Relations.GENERATED_BY;
					  m.Activity=Enums.Activities.DATA_COLLECTION;
					  m.Entity=Enums.Entities.IOT_DATA;
					  LogManager.log(m);
					  List<DatasetDTO>  selectedEntires=	  entries.stream().limit(n).collect(Collectors.toList());
					  
				  ClusterDTO result=  runClustering(selectedEntires,algo, percentage);
				  result.TotalDataCount=entries.size();
				  result.Percent=percentage;
				  result.Did=4;
				  
				  results.add(result);
				  showClusteringResults(result);
				  
		
					selectedEntires=null;
							 
				 
				  }
				  
				
			  }
			  
				try {
					writeToFile(results,type,algo);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    
		 }
		 
		 if(type==3) {
			  
			  Map<Object, List<DatasetDTO>> listGrouped2 =
						 dataSet.stream().collect(Collectors.groupingBy(w -> w.mid));
			  for(int i=0;i<8;i++) {
				  int percentage=30+i*10;
				
				  List<DatasetDTO>  selectedEntires=new ArrayList<DatasetDTO>();
				  for (Map.Entry<Object, List<DatasetDTO>> entry : listGrouped2.entrySet()) {
					  List<DatasetDTO> entries=entry.getValue();
					  
					
						
						  int n=percentage*entries.size()/100;
						  if(n>entries.size()) {
							  n=entries.size();
							
						  }
						  LogMessage m=new LogMessage();
						  m.Relation=Enums.Relations.GENERATED_BY;
						  m.Activity=Enums.Activities.DATA_COLLECTION;
						  m.Entity=Enums.Entities.IOT_DATA;
						  LogManager.log(m);
			 selectedEntires.addAll( entries.stream().limit(n).collect(Collectors.toList()));
						  
				  }
						  
					  ClusterDTO result=  runClustering(selectedEntires,algo, percentage);
					  result.TotalDataCount=dataSet.size();
					  result.Percent=percentage;
					  result.Did=4;
					  
					  results.add(result);
					  showClusteringResults(result);
					  
					
						selectedEntires=null;
								 
					 
					  }
				try {
					writeToFile(results,type, algo);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					  
					
				  }
		    
			
		 if(type==4) {
			  
			  Map<Object, List<DatasetDTO>> listGrouped2 =
						 dataSet.stream().collect(Collectors.groupingBy(w -> w.mid));
			  for(int i=0;i<1;i++) {
				  int percentage=100;//35+i*15;
				  if(percentage>100)
					  percentage=100;
				  LogMessage m=new LogMessage();
				  m.Relation=Enums.Relations.GENERATED_BY;
				  m.Activity=Enums.Activities.DATA_COLLECTION;
				  m.Entity=Enums.Entities.IOT_DATA;
				  LogManager.log(m);
				  List<DatasetDTO>  selectedEntires=new ArrayList<DatasetDTO>();
				  for (Map.Entry<Object, List<DatasetDTO>> entry : listGrouped2.entrySet()) {
					  List<DatasetDTO> entries=entry.getValue();
					  
					
						
						  int n=percentage*entries.size()/100;
						  if(n>entries.size()) {
							  n=entries.size();
							
						  }
			 selectedEntires.addAll( entries.stream().limit(n).collect(Collectors.toList()));
						  
				  }
						  
					  ClusterDTO result=  runClustering(selectedEntires,algo, percentage);
					  result.TotalDataCount=dataSet.size();
					  result.Percent=percentage;
					  result.Did=4;
					  
					  results.add(result);
					  showClusteringResults(result);
					  
					
						selectedEntires=null;
								 
					 
					  }
				try {
					writeToFile(results,type, algo);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					  
					
				  }
		    
			
		
	
	}

	private static void writeToFile(List<ClusterDTO> results, int type, String algo) throws IOException {
		 if(logToFile) { try 
		  {  
			 
			  String filename ="d:\\test\\" +algo+"_" +type+"_"+ new java.text.SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.csv'").format(new Date());
			  java.io.File yourFile = new java.io.File (filename);
			  yourFile.createNewFile(); // if file already exists will do nothing 
				  List<String[]> resultString=getCsvValue(results);
//				  
//				  CSVWriter writer = new CSVWriter(new FileWriter(filename)) ;
//	            writer.writeAll(resultString);
//	            writer.flush();
//	            writer.close();
	        }catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		
	}

	private static List<String[]> getCsvValue(List<ClusterDTO> results) {
	
		
		 String[] header = {"Engine_Id", "Total_Data_Size", "Streaming_Data_Size", "Percentage","Outlier", "Outlier Percent","Outlier Prob","Silhouette_K","Silhouette","VMeasure","ClusterCount","Threashold","Engine Running Count"};
	  

	        List<String[]> list = new ArrayList<>();
	        list.add(header);
	        
	        for (ClusterDTO item : results) {
	        	
	        	double outlierPercent=100*item.OutlierCount/item.SelectedDataCount;
	        	String[] itemString = {item.Mid+"", item.TotalDataCount+"", item.SelectedDataCount+"", item.Percent+"",item.OutlierCount+"",outlierPercent+"", item.OutlierCountProb+"",item.SilhouetteKValue+"",item.SilhouetteValue+"",item.VMeasureValue+"", item.getClusterInfor(),item.Threshold+"" ,item.EngineRunningCount+ ""};
	       	  
	        	  list.add(itemString);
	        	}
	        
	        return list;
		
	}

	private static void showClusteringResults(ClusterDTO result) {
		

		
		
		LogManager.log("\nStreaming Started!");		
		LogManager.log("Dataset ID : " +result.Did);
		LogManager.log("Engine Id : " +result.Mid);
		LogManager.log("Total Data Size : " +result.TotalDataCount);
		LogManager.log("Streaming Data Size : " +result.SelectedDataCount);
		LogManager.log("Data Percentage : " +result.Percent);
		LogManager.log("Outlier : " +result.OutlierCount);
		LogManager.log("Outlier Prob: " +result.OutlierCountProb);
		LogManager.log("Engine Running Count: " +result.EngineRunningCount);
		LogManager.log("Silhouette K Value : " +result.SilhouetteKValue);
		LogManager.log("Silhouette Value : " +result.SilhouetteValue);
		LogManager.log("VMeasure Value : " +result.VMeasureValue);
		
		  LogMessage m6=new LogMessage();
		  m6.process= Enums.Processes.CLUSTERING;
		  m6.Relation=Enums.Relations.USED;
		  m6.Activity=Enums.Activities.CLUSTERING_MODEL_GENERATION;
		  m6.Entity=Enums.Entities.PROCESSED_DATA;
		  m6.StartTime=LocalDateTime.now().minusMinutes(4);
		  m6.EndTime=LocalDateTime.now();
		  LogManager.log(m6);
		  
		  LogMessage m7=new LogMessage();
		  m7.process= Enums.Processes.CLUSTERING;
		  m7.Relation=Enums.Relations.GENERATED_BY;
		  m7.Activity=Enums.Activities.CLUSTERING_MODEL_GENERATION;
		  m7.Entity=Enums.Entities.CLUSTERING_MODEL;
			
        	ModelDTO model=new ModelDTO();
        	model.modelName="Clustree";
        	model.modelType=Enums.ModelTypes.UNSUPERVISED;
        	model.VMeasure=result.VMeasureValue;
        	model.SilhouetteValue=result.SilhouetteValue;
        			
        			m7.model=model;;
		  LogManager.log(m7);
		  
		  LogMessage m2=new LogMessage();
		  m2.Relation=Enums.Relations.GENERATED_BY;
		  m2.Activity=Enums.Activities.ANOMALLY_DETECTION;
		  m2.Entity=Enums.Entities.LABELED_DATA;
		  m6.StartTime=LocalDateTime.now().minusMinutes(1);
		  m6.EndTime=LocalDateTime.now();
		  
		  LogManager.log(m2);
		  
		  LogMessage m5=new LogMessage();
		  m5.process= Enums.Processes.CLUSTERING;
		  m5.Relation=Enums.Relations.USED;
		  m5.Activity=Enums.Activities.ANOMALLY_DETECTION;
		  m5.Entity=Enums.Entities.PROCESSED_DATA;
		  LogManager.log(m5);
		  
		  LogMessage m8=new LogMessage();
		  m8.process= Enums.Processes.CLUSTERING;
		  m8.Relation=Enums.Relations.USED;
		  m8.Activity=Enums.Activities.ANOMALLY_DETECTION;
		  m8.Entity=Enums.Entities.CLUSTERING_MODEL;
		  LogManager.log(m8);
		  
		
	
		String tmpStr = "";
		
	
		
		
		 // Get the clusters
			/*
			 * Clustering clustering = result.ClusterAlgorithm.getClusterResults();
			 * 
			 * if(clustering!=null) { // Compute the centroid of each cluster for (int i =
			 * 0; i < clustering.size(); i++) { // Cluster cluster = clustering.get(i);
			 * SphereCluster cluster = (SphereCluster) clustering.get(i);
			 * 
			 * // System.out.println("Centroid of Cluster " + i + ": " +
			 * cluster..getInfo());
			 * 
			 * System.out.println("Centroid of Cluster " + i + ": " + cluster.toString());
			 * 
			 * System.out.println("Centroid of Cluster " + i + ": " +
			 * cluster.getCenter()[0]);
			 * 
			 * System.out.println("Radiues of Cluster " + i + ": " + cluster.getRadius());
			 * 
			 * 
			 * }
			 */
        //}
		
		
	
		//tmpStr = result.ClusterAlgorithm.showSum();
	//	System.out.print(tmpStr);
		

//		tmpStr = result.ClusterAlgorithm.getClustersOutlierCount();
//		System.out.print(tmpStr);
	
	
	//	System.out.println("\n______________________________________________________\n");		
//		System.out.println("show clusters");	
//		result.ClusterAlgorithm.showClusters();
		
		

		
	//	tsne visualization=new tsne(this.streamClusterer.clusterResult.getClustering());
	  //  ClusterVisualizer2 visualization = new ClusterVisualizer2(this.streamClusterer.clusterResult.getClustering());
      //  visualization.run();
        
	
		System.out.println("\n______________________________________________________\n");		
		LogManager.log("Stream is finished.");	
		
		
	}

	private static ClusterDTO runClustering( List<DatasetDTO> entry, String algo, int percentage) {
		
		ClusterDTO result=new ClusterDTO();
		result.AlgorithmName=algo;
		maxMotorId=0;
		int modelId=0;
		List<double[]> dataK=getDataForK(entry);
		
		result.EngineRunningCount=maxMotorId;
		
		if(entry.size()>0) {
			
			if(entry.stream().anyMatch(c -> c.mid!=entry.get(0).mid))
			{
				result.Mid=999;
			}
			else {
			result.Mid=entry.get(0).mid;
			}
		}
		
		 Clusterer streamClusterer=null;
		if(algo=="kmeans") {
		
		smkfinder kfinder=new smkfinder(dataK,10);
		
		if(optimalk==0)
		optimalk=kfinder.findOptimalK();
		
		result.SilhouetteKValue=optimalk;
		  streamClusterer=new KmeansClusterer("all",		result.SilhouetteKValue);	
		 
		 result.ClusterAlgorithm=streamClusterer;
		 
		 modelId=2;
		}
		
		if(algo=="streamkm") {
			
		smkfinder kfinder=new smkfinder(dataK,10);
		
		if(optimalk==0)
			optimalk=kfinder.findOptimalK();
			
			result.SilhouetteKValue=optimalk;
		
		
		  streamClusterer=new StreamKMClusterer("all",		result.SilhouetteKValue);	
		 
		 result.ClusterAlgorithm=streamClusterer;
		 
		 modelId=1;
		}
		
		if(algo=="dbscan") {
			
		
		
		  streamClusterer=new DenStreamClusterer("all");	
		 
		 result.ClusterAlgorithm=streamClusterer;
		 modelId=3;
		}
		
		if(algo=="clustream") {
			
			
			
			  streamClusterer=new CluStreamClusterer("all");	
			 
			 result.ClusterAlgorithm=streamClusterer;
			 modelId=4;
			}
		
		
		
		if(algo=="clustree") {
			
			
			
			  streamClusterer=new ClusTreeClusterer("all");	
			 
			 result.ClusterAlgorithm=streamClusterer;
			 modelId=5;
			}
			
		if(algo=="dsstream") {
			
			
			
			  streamClusterer=new DstreamClusterer("all");	
			 
			 result.ClusterAlgorithm=streamClusterer;
			 modelId=6;
			}
		
		result.SelectedDataCount=	entry.size();
		int itemcount=0;
		for(Iterator<DatasetDTO> i = entry.iterator(); i.hasNext(); ) {
			DatasetDTO item = i.next();
			
			double[] vector= getVector(item);
			
	itemcount++;
			
			streamClusterer.setDistanceType("ed");
			
		
			
			//if((itemcount*100/result.SelectedDataCount)>20)
		//		training=false;
			
			InputDTO input=new InputDTO();
			input.inputVector=vector;
			input.id=item.sira;
			
			streamClusterer.add(input,itemcount<20000);	
		//	streamClusterer.add(vector,percentage<50);	
		}
		
		streamClusterer.getResults();
			
		
		
			String tmpStr = "";
		
			
			
		
			//tmpStr = streamClusterer.showSum();
			//System.out.print(tmpStr);
			

	
				    
		OutlierResult outlierResult= streamClusterer.getClustersOutlierCount();
		
		result.OutlierCount =outlierResult.OutlierCount;
		
		//	result.OutlierCount= streamClusterer.getClustersOutlierCount();
		
		DataReaderLite dbReader =new DataReaderLite(1);
		 
	       dbReader.insertLabelData(modelId, outlierResult.OutlierList);
	   		 
	   		 dbReader.closeReader();
			

		
		result.OutlierCountProb = streamClusterer.getClustersOutlierCountProb();
		
		String eval=result.ClusterAlgorithm.getEval();
	//	System.out.println("eval : "+eval);
		
		
		result.SilhouetteValue=result.ClusterAlgorithm.SilhouetteValue;
		result.VMeasureValue=result.ClusterAlgorithm.VMeasureValue;
		result.Threshold=result.ClusterAlgorithm.Threshold;
		
        Clustering clustering = result.ClusterAlgorithm.getClusterResults();

        if(clustering!=null) {
        // Compute the centroid of each cluster
        for (int i = 0; i < clustering.size(); i++) {
       //     Cluster cluster = clustering.get(i);
            SphereCluster cluster = (SphereCluster) clustering.get(i);
            
            ClusterEntityDTO entity=new ClusterEntityDTO();
            entity.Radiues=cluster.getRadius();
            double[] center =cluster.getCenter();
          
            entity.Dimension= center.length;
            if(center.length>0)
            entity.Dimension1= center[0];
            if(center.length>1)
            entity.Dimension2= center[1];
            if(center.length>2)
            entity.Dimension3= center[2];
            
            result.Clusters.add(entity);
            
        }}
        
        
 
        
			//System.out.print(tmpStr);
		
		
		//	System.out.println("\n______________________________________________________\n");		
	//		System.out.println("Stream is finished.");	
			
			

			
		//	tsne visualization=new tsne(this.streamClusterer.clusterResult.getClustering());
		  //  ClusterVisualizer2 visualization = new ClusterVisualizer2(this.streamClusterer.clusterResult.getClustering());
	      //  visualization.run();
	        
	      //  streamClusterer.showClusters();
			
	        return result;
			
		
	}

	private static int maxMotorId=0;
	private static List<double[]> getDataForK(List<DatasetDTO> entry) {
		
		List<double[]>  result=new ArrayList<double[]> ();
		
		for(Iterator<DatasetDTO> i = entry.iterator(); i.hasNext(); ) {
			DatasetDTO item = i.next();
			
			double[] vector= getVector(item);
			result.add(vector);
			
			if(item.mid>maxMotorId)
				maxMotorId=item.mid;
		}
		
		return result;
	}

	private static double[] getVector(DatasetDTO item) {

	double[] vector=new double[24];
		
		vector[0]=item.s1;
		vector[1]=item.s1;
		vector[2]=item.s2;
		vector[3]=item.s3;
		vector[4]=item.s4;
		vector[5]=item.s5;
		vector[6]=item.s6;
		vector[7]=item.s7;
		vector[8]=item.s8;
		vector[9]=item.s9;
		vector[10]=item.s10;
		vector[11]=item.s11;
		vector[12]=item.s12;
		vector[13]=item.s13;
		vector[14]=item.s14;
		vector[15]=item.s15;
		vector[16]=item.s16;
		vector[17]=item.s17;
		vector[18]=item.s18;
		vector[19]=item.s19;
		vector[20]=item.s20;
		vector[21]=item.s21;
		vector[22]=item.s22;
		vector[23]=item.s23;
		//vector[24]=item.s24;
		
		
		return vector;
	}

}
