package clusterers;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import application.Controller;
import application.DataReader;


public class ClusterManager {	

	private int trainingPercentage;
	private int checkTime;
	
	private DataReader dbReader;	
	private Clusterer streamClusterer;		
	private String clusterName;
	private String dataSetName;
	private int currentTime;
	private int lastCheck;
	
	
	public ClusterManager(String clusterName,String evaluationName,int dataStart, String dataSetName){
		
		this.dbReader = new DataReader(dataStart);	
		
		this.clusterName = clusterName;
		this.dataSetName=dataSetName;
		

	}

	public void train(int trainingDataSize, boolean isIteration) {
		System.out.println("\nOffset cluster data feed is started!");
		Controller.updateResult("\nOffset cluster data feed is started!");
				
		for(int i=0;i<trainingDataSize;i++) {	
			if(i%(trainingDataSize*0.01)==0) {				
				if((int) Math.round(( (float)i/(float)trainingDataSize)*100) % 20 == 0) {
					System.out.println("Process : %" + (int) Math.round(( (float)i/(float)trainingDataSize)*100)  );
					Controller.updateResult("\nProcess : %" + (int) Math.round(( (float)i/(float)trainingDataSize)*100));
				}				
			}
			this.addData(i,true,isIteration);
		}	
		System.out.println("\nProcess : %" + 100 );
		Controller.updateResult("\nProcess : %" + 100);
		Controller.updateResult(this.streamClusterer.showSum());
		Controller.updateResult("\nOffset clusters are constructed!");
		Controller.updateResult("\n______________________________________________________");		
		System.out.println("\nOffset clusters are constructed!");	
		System.out.println("\n______________________________________________________\n");		
			
	}
	
	
	public void stream(int testSize,String distanceType,boolean isIteration,int sleepTime) {
		this.lastCheck = (int) (new Date().getTime()) ;
		streamClusterer.setDistanceType(distanceType);
		System.out.println("\nStreaming Started!");		
		System.out.println("Streaming Data Size : " + ((int)((float)testSize*(1- (float)this.trainingPercentage/100 )))  );
		for(int i=0;i<testSize;i++) {			
			this.addData(i,false,isIteration);
			if(sleepTime !=0) {
				sleep(sleepTime);
			}			
		}	
		String tmpStr = "";
		Controller.updateResult("\nData Count : " + testSize);
		
		tmpStr = this.streamClusterer.getEval();
		System.out.print(tmpStr);
		Controller.updateResult(tmpStr);
		
	
		tmpStr = this.streamClusterer.showSum();
		System.out.print(tmpStr);
		Controller.updateResult(tmpStr);			

		tmpStr = this.streamClusterer.getClustersOutlierCount()+"";
		System.out.print(tmpStr);
		Controller.updateResult(tmpStr);
		
		Controller.updateResult("\n______________________________________________________");
		Controller.updateResult("\nStream is finished.");		
		System.out.println("\n______________________________________________________\n");		
		System.out.println("Stream is finished.");	
		dbReader.closeReader();	
		

		
	//	tsne visualization=new tsne(this.streamClusterer.clusterResult.getClustering());
	  //  ClusterVisualizer2 visualization = new ClusterVisualizer2(this.streamClusterer.clusterResult.getClustering());
      //  visualization.run();
        
        this.streamClusterer.showClusters();

	}
	
	private void addData(int i,boolean training,boolean iteration) {
		
		double[] vector = null;
		
		if(this.dataSetName=="Secom") {
			vector =dbReader.readDataSecom();
			
		}
		else {		
			vector =dbReader.readData();
		}
		this.streamClusterer.add(vector,training);	
		boolean trainingProc = false;
		
		if( !training ) {	
			
			if(iteration ) {
				if(i % this.checkTime == 0 && i != 0) {
					trainingProc=true;
				}
			}else {				
				this.currentTime = (int) (new Date().getTime()) ;
				if(this.currentTime-this.lastCheck >= this.checkTime) {
					this.lastCheck = this.currentTime;
					trainingProc=true;
				}
			}
			
			if(trainingProc) {
				System.out.println("Data Count : " + (i));	
				Controller.updateResult("\nData Count : " + (i));
				String tmpStr = "";
				
				tmpStr = this.streamClusterer.getEval();
				System.out.print(tmpStr);
				Controller.updateResult(tmpStr);
				
				tmpStr = this.streamClusterer.showSum();
				Controller.updateResult(tmpStr);
				
				tmpStr = this.streamClusterer.getClustersOutlierCount()+"";
				System.out.print(tmpStr);
				Controller.updateResult(tmpStr);
				
				this.streamClusterer.clearDataIndex();
				System.out.println("\n_________________________");
				Controller.updateResult("\n_________________________");
				streamClusterer.clearDataPoints();
			}		
						
		}
		
	}
	
	public void setValues(int testOutlierPercentage,int checkTime) {
		//this.dataSize = dataSize;		
		//this.interval = interval;		
		this.checkTime =checkTime;
		this.streamClusterer.setOutlierPercentage(testOutlierPercentage);
	}
	
	
	
	private void sleep(int interval) {
		try {	
			TimeUnit.MILLISECONDS.sleep(interval);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}	

	public String getClusterName() {
		return clusterName;
	}

}
