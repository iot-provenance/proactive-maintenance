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
import dto.InputDTO;
import dto.OutlierResult;
import moa.cluster.Clustering;
import moa.classifiers.Classifier;
import moa.classifiers.functions.SPegasos;
import moa.classifiers.trees.HoeffdingTree;
import moa.cluster.Cluster;
import moa.cluster.SphereCluster;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


import clusterers.smkfinder;

public class ConsoleManager {
	
	private static int optimalk=0;

	public static void main(String[] args) {
		
		//startSimulation();
		// DataReader dbReader =new DataReader(1);
		 
		//String algo="clustree";//"clustree";//"dbscan" streamkm clustree clustream
		//String algo="kmeans"; 
		
		// ArrayList<DatasetDTO> dataSet=dbReader.readDataList(4);
		 
		// dbReader.closeReader();
		 
		// int type=1;
			
			//  String algo="streamkm"; 
			  //runClusterings(dataSet, 1,algo);
			  
			  //runClusterings(dataSet, 2,algo);
			  //runClusterings(dataSet, 3,algo);
			  
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

	private static void startSimulation() {
		
		LogManager.log("Batch Data Collected");
		LogManager.log("Batch Data Processing");
		LogManager.log("Batch Data Processed");
		LogManager.log("Batch Data Clustering");
		LogManager.log("Batch Data Clustered");
		LogManager.log("Batch Data Labelling");
		LogManager.log("Batch Data Labeled");
		
		LogManager.log("Batch Data Classification Started");
		LogManager.log("HoeffdingTree model selected to training 40000 record");
		NasaSupervised ns=new NasaSupervised();
		
		Classifier learner = new HoeffdingTree();
		
		ns.runTraining(learner,"HoeffdingTree",0,50000,40000,"labeledtrain");
		
		LogManager.log("Batch Data Classification completed");
		boolean continueSimulation=true;
	
		while(continueSimulation) {
		learner = ModelManager.loadModel("D:\\moamodel\\sup.moa");// new HoeffdingTree();
		  
		ns.runTraining(learner,"HoeffdingTree",0,3,0, "labeledtest");
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
		
	}

	private static void runClusterings(ArrayList<DatasetDTO> dataSet, int type, String algo) {
	
		 
		 List<ClusterDTO> results=new ArrayList<ClusterDTO>();
			
		 
	
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
				  List<DatasetDTO>  selectedEntires=	  entries.stream().limit(n).collect(Collectors.toList());
				  
			  ClusterDTO result=  runClustering(selectedEntires,algo);
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
					  List<DatasetDTO>  selectedEntires=	  entries.stream().limit(n).collect(Collectors.toList());
					  
				  ClusterDTO result=  runClustering(selectedEntires,algo);
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
			  for(int i=0;i<6;i++) {
				  int percentage=35+i*15;
				  if(percentage>100)
					  percentage=100;
				  List<DatasetDTO>  selectedEntires=new ArrayList<DatasetDTO>();
				  for (Map.Entry<Object, List<DatasetDTO>> entry : listGrouped2.entrySet()) {
					  List<DatasetDTO> entries=entry.getValue();
					  
					
						
						  int n=percentage*entries.size()/100;
						  if(n>entries.size()) {
							  n=entries.size();
							
						  }
			 selectedEntires.addAll( entries.stream().limit(n).collect(Collectors.toList()));
						  
				  }
						  
					  ClusterDTO result=  runClustering(selectedEntires,algo);
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
		  try 
		  {  
			 
			  String filename ="d:\\test\\" +algo+"_" +type+"_"+ new java.text.SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.csv'").format(new Date());
			  java.io.File yourFile = new java.io.File (filename);
			  yourFile.createNewFile(); // if file already exists will do nothing 
				  List<String[]> resultString=getCsvValue(results);
				  
//				  CSVWriter writer = new CSVWriter(new FileWriter(filename)) ;
//	            writer.writeAll(resultString);
//	            writer.flush();
//	            writer.close();
	        }catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
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
		

		
		
		System.out.println("\nStreaming Started!");		
		System.out.println("Dataset ID : " +result.Did);
		System.out.println("Engine Id : " +result.Mid);
		System.out.println("Total Data Size : " +result.TotalDataCount);
		System.out.println("Streaming Data Size : " +result.SelectedDataCount);
		System.out.println("Data Percentage : " +result.Percent);
		System.out.println("Outlier : " +result.OutlierCount);
		System.out.println("Outlier Prob: " +result.OutlierCountProb);
		System.out.println("Engine Running Count: " +result.EngineRunningCount);
		System.out.println("Silhouette K Value : " +result.SilhouetteKValue);
		System.out.println("Silhouette Value : " +result.SilhouetteValue);
		System.out.println("VMeasure Value : " +result.VMeasureValue);
		
	
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
		System.out.println("Stream is finished.");	
		
		
	}

	private static ClusterDTO runClustering( List<DatasetDTO> entry, String algo) {
		
		ClusterDTO result=new ClusterDTO();
		result.AlgorithmName=algo;
		maxMotorId=0;
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
		}
		
		if(algo=="streamkm") {
			
		smkfinder kfinder=new smkfinder(dataK,10);
		
		if(optimalk==0)
			optimalk=kfinder.findOptimalK();
			
			result.SilhouetteKValue=optimalk;
		
		
		  streamClusterer=new StreamKMClusterer("all",		result.SilhouetteKValue);	
		 
		 result.ClusterAlgorithm=streamClusterer;
		}
		
		if(algo=="dbscan") {
			
		
		
		  streamClusterer=new DenStreamClusterer("all");	
		 
		 result.ClusterAlgorithm=streamClusterer;
		}
		
		if(algo=="clustream") {
			
			
			
			  streamClusterer=new CluStreamClusterer("all");	
			 
			 result.ClusterAlgorithm=streamClusterer;
			}
		
		
		
		if(algo=="clustree") {
			
			
			
			  streamClusterer=new ClusTreeClusterer("all");	
			 
			 result.ClusterAlgorithm=streamClusterer;
			}
			
		if(algo=="dsstream") {
			
			
			
			  streamClusterer=new DstreamClusterer("all");	
			 
			 result.ClusterAlgorithm=streamClusterer;
			}
		
		result.SelectedDataCount=	entry.size();
		int itemcount=0;
		for(Iterator<DatasetDTO> i = entry.iterator(); i.hasNext(); ) {
			DatasetDTO item = i.next();
			
			double[] vector= getVector(item);
			
	itemcount++;
			
			streamClusterer.setDistanceType("ed");
			
			boolean training=false;
			
			//if((itemcount*100/result.SelectedDataCount)>20)
		//		training=false;
			
			InputDTO input=new InputDTO();
			input.inputVector=vector;
			input.id=item.sira;
			
			streamClusterer.add(input,training);
			
		//	streamClusterer.add(vector,training);	
		}
		
		streamClusterer.getResults();
			
		
		
			String tmpStr = "";
		
			
			
		
			//tmpStr = streamClusterer.showSum();
			//System.out.print(tmpStr);
			

			OutlierResult outlierResult= streamClusterer.getClustersOutlierCount();
			
			result.OutlierCount =outlierResult.OutlierCount;
			
				    
			
			//result.OutlierCount = streamClusterer.getClustersOutlierCount();

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
		vector[1]=item.s2;
		vector[2]=item.s3;
		vector[3]=item.s4;
		vector[4]=item.s5;
		vector[5]=item.s6;
		vector[6]=item.s7;
		vector[7]=item.s8;
		vector[8]=item.s9;
		vector[9]=item.s10;
		vector[10]=item.s11;
		vector[11]=item.s12;
		vector[12]=item.s13;
		vector[13]=item.s14;
		vector[14]=item.s15;
		vector[15]=item.s16;
		vector[16]=item.s17;
		vector[17]=item.s18;
		vector[18]=item.s19;
		vector[19]=item.s20;
		vector[20]=item.s21;
		vector[21]=item.s22;
		vector[22]=item.s23;
		vector[23]=item.s24;
		//vector[24]=item.s1;
		
		return vector;
	}

}
