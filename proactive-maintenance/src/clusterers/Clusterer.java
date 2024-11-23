package clusterers;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

import com.yahoo.labs.samoa.instances.Attribute;
import com.yahoo.labs.samoa.instances.DenseInstance;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import com.yahoo.labs.samoa.instances.InstancesHeader;

import dto.InputDTO;
import dto.OutlierResult;
import evaluators.EvaluationResults;
import evaluators.EvaluatorManager;
import moa.cluster.Cluster;
import moa.cluster.Clustering;
import moa.core.AutoExpandVector;
import moa.gui.visualization.DataPoint;

public abstract class Clusterer {

	
	private double[] thresholdList;
	protected int[] catchedOutlierList;
	protected int[] falsePositiveList ;
	protected double outlierPercentage;	
	public int OutlierCountNew=0;

	protected EvaluatorManager evaluator;
	protected EvaluatorManager[] evaluators;
	
	public Clustering clusterResult;
	public Clustering microClusterResult;

	
	protected boolean resultUpToDate;			
	protected ArrayList<DataPoint> dataPoints;
	protected ArrayList<InputDTO> inputs;
	protected ArrayList<DenseInstance> instancePoints;
	protected ArrayList<double[]> dataIndex;
	protected ArrayList<double[]> vectorForm;
	protected int keepTime;
	
	protected int totalOutlierCount;
	protected double threshold;
	protected boolean useMD;
	protected boolean useED;
	protected boolean useDefault;

	
	protected int outlierCount[];
	protected int dataCount[];
	protected int catchedOutliers;
	protected int falsePositives ;
	
	protected double minOutlierDistance;
	protected double avgOutlierDistance;	

	protected RealMatrix invcov = null;	
	
	public double SilhouetteValue;
	public double VMeasureValue;
	public int Threshold=35;
		
	public Clusterer(String evaluationName) {
		evaluator = new EvaluatorManager(evaluationName);
		this.resultUpToDate = false;		
				
		this.dataPoints = new ArrayList<DataPoint>();
		this.inputs = new ArrayList<InputDTO>();
		this.instancePoints = new ArrayList<DenseInstance>();
		this.dataIndex = new ArrayList<double[]>();
		this.vectorForm = new ArrayList<double[]>();
	
		this.minOutlierDistance = Double.MAX_VALUE;
		
		this.outlierCount = new int[9999];
		this.dataCount = new int[9999];
		this.catchedOutliers = 0;
		this.falsePositives = 0;
		this.avgOutlierDistance = 0;
		
		this.thresholdList = new double[601];
		for (int i = 0; i < this.thresholdList.length; i++) {
			this.thresholdList[i] = (double)i / 10.0;
		}
		
		this.catchedOutlierList = new int[thresholdList.length];
		this.falsePositiveList = new int[thresholdList.length];			
	
	}
	
	public Clusterer(String evaluationName , int streamCount , boolean multi) {	
		this.evaluators = new EvaluatorManager[streamCount];
		for(int i=0;i<streamCount;i++) {
			this.evaluators[i] = new EvaluatorManager(evaluationName);
		}		
		this.resultUpToDate = false;
		this.dataPoints = new ArrayList<DataPoint>();
		this.inputs = new ArrayList<InputDTO>();
	}
	
	public abstract void add(double[] vector,boolean training);
	public abstract void add(InputDTO input,boolean training);
	public abstract void getResults();
	
	protected InstancesHeader genInstanceHead(int size) {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		for (int i = 0; i < size; i++) {
			attributes.add(new Attribute("feature_" + i));
		}
		InstancesHeader streamHeader = new InstancesHeader(	new Instances("Test Header",attributes, size));
		return streamHeader;
	}	


	public String getEval() {		
		if(!this.resultUpToDate) this.getResults();	
		String eval="";
		EvaluationResults evalRes = this.evaluator.evaluate(clusterResult, microClusterResult, dataPoints);		
		if(evalRes!=null) {
			eval="Eval1:"+ evalRes.showEvalResults();
			SilhouetteValue=evalRes.getEvalMean()[0];
		}
		
		EvaluationResults evalRes2 = this.evaluator.evaluate2(clusterResult, microClusterResult, dataPoints);		
		if(evalRes2!=null) {
			eval=eval+ "\n Eval2 : "+ evalRes2.showEvalResults();	
			VMeasureValue=evalRes2.getEvalMean()[0];
		}
		
		return eval;
	}	

	public int getClustersOutlierCountProb() {	
		int outlier=0;
		
		double[] tmpVector;
		double distance ;
				
		for(int i=0;i<this.instancePoints.size();i++) {

			moa.cluster.Clustering clustering = this.clusterResult;//.getMicroClusteringResult();
            double clusterIndex = clustering.getMaxInclusionProbability(this.instancePoints.get(i));
      
            if (clusterIndex <0.1) {
                // Instance does not belong to any cluster, so it is an outlier
                OutlierCountNew++;
                outlier++;
                
              //  System.out.println("yeni outlier:"+OutlierCountNew);
            }
		
		}	

		
		return outlier;
	}
	
	public OutlierResult getClustersOutlierCount() {	
		
		OutlierResult result=new OutlierResult();
		
		if(this.useMD) {
			this.setInvcov();
		}
		int outlier=0;
		double[] tmpVector;
		double distance ;
				
		for(int i=0;i<this.dataPoints.size();i++) {

			tmpVector = this.getOutlierClusterDistanceId(i);
			distance = tmpVector[1];	
			
			//for(int j=0;j<thresholdList.length;j++) {
				if(distance>=Threshold) {
					//if(dataIndex.get(i)[0]== 1.0 ) {
					//	this.catchedOutlierList[j]++;
					//}else if( this.dataIndex.get(i)[0]== 0.0 ) {
					//	this.falsePositiveList[j]++;
					//}
					outlier++;
					result.OutlierList.add(getInputIdFromDataPoint(this.dataPoints.get(i)));
				//}
			}
		
		}	
	
		//int index = this.catchedOutlierList.length-1;
		//while((float)catchedOutlierList[index]/(float)catchedOutlierList[0] < this.outlierPercentage) {			
		//	index--;
		//}
	//	String tmpReturn = "\nTotal Outlier : " + catchedOutlierList[0] + 
		//		"\nCatched Outlier : " + catchedOutlierList[index] + 
			//	"\nFalse Positive : " + falsePositiveList[index];

		//this.catchedOutlierList = new int[thresholdList.length];
	//	this.falsePositiveList = new int[thresholdList.length];	

		
		//if(catchedOutlierList.length>index)
		//	outlier= catchedOutlierList[index];
		
        result.OutlierCount=outlier;
		
		return result;
	}
	
	private int getInputIdFromDataPoint(DataPoint p) {
		for(int i=0;i<inputs.size();i++) {
			double[] v=inputs.get(i).inputVector;
			if(p.value(0)==v[0] && p.value(1)==v[1] && p.value(2)==v[2] && p.value(3)==v[3] && p.value(4)==v[4] && p.value(5)==v[5]
					&&p.value(6)==v[6] && p.value(7)==v[7] && p.value(8)==v[8] && p.value(9)==v[9] && p.value(10)==v[10] && p.value(11)==v[11]
						&&	p.value(12)==v[12] && p.value(13)==v[13] && p.value(14)==v[14] && p.value(15)==v[15] && p.value(16)==v[16] && p.value(17)==v[17]
							&&		p.value(18)==v[18] && p.value(19)==v[19] && p.value(20)==v[20] &&		p.value(21)==v[21] && p.value(22)==v[22] && p.value(23)==v[23]  )
			{
				return inputs.get(i).id;
			}
			
		}
		return 0;
	}
	public double[] getOutlierClusterDistanceId(int dataNo) {	
		double minDistance = Double.MAX_VALUE;
		if(this.useDefault) {
			minDistance = -1;
		}		
		double currentDistance;
		double dataPointClusterId = -1;
		double clusterNo = -1;
		//getInclusionProbability

		if(this.clusterResult!=null)
		{for(Object testClust:this.clusterResult.getClustering().toArray()) {		
			//System.out.print("Cluster "+ clustNo++ + " Info : " + ((Cluster) testClust).getInfo());
			clusterNo++;
			if(this.useMD) {
				currentDistance =  this.mDistance(((Cluster) testClust).getCenter(), this.vectorForm.get(dataNo));
			}else if(this.useED) {
				currentDistance =  this.eDistance(((Cluster) testClust).getCenter(), this.vectorForm.get(dataNo));
			}else {
				currentDistance =  this.showLastMaxProbability(this.instancePoints.get(dataNo));
			}
			
			if(this.useDefault) {				
				if(currentDistance>minDistance) {
					minDistance = currentDistance;
					//dataPointClusterId = ((Cluster) testClust).getId();
					dataPointClusterId = clusterNo;
				}
			}else {
				if(currentDistance<minDistance) {
					minDistance = currentDistance;
					//dataPointClusterId = ((Cluster) testClust).getId();
					dataPointClusterId = clusterNo;
				}
			}			
			//currentDistance = ((Cluster) testClust).getInclusionProbability();						
		}
	}
		return new double[] {dataPointClusterId,minDistance};		
	}
	
	private double mDistance(double[] centroid, double[] data) {  
	     

        if (Arrays.equals(centroid, data)) {
            return 0.0;
        }
        
        double [] diff = new double[centroid.length];
        
        for (int i = 0; i < centroid.length; i++) {
            diff[i] = centroid[i] - data[i];
        }       
        
        double [] left = invcov.preMultiply(diff);
        
        double res = 0.0;
        for (int i = 0; i < diff.length; i++) {
            res += left[i] * diff[i];
        }        
        return Math.sqrt(res);        
    }
	
	private void setInvcov() {
	     
		double[][] dataVectors = new double[this.vectorForm.size()][7];
		
		for(int i=0;i<this.vectorForm.size();i++) {			
			dataVectors[i] = this.vectorForm.get(i);
		}
		
	     RealMatrix mx = MatrixUtils.createRealMatrix(dataVectors);
	     RealMatrix cov = new Covariance(mx).getCovarianceMatrix();
	     this.invcov = new LUDecomposition(cov).getSolver().getInverse();	     
	}
	
	
	 @SuppressWarnings("unused")
	private double eDistance(double[] a, double[] b) {
	        double diff_square_sum = 0.0;
	        int length= a.length;
	      if(length>b.length)
	      	length=b.length;
	        for (int i = 0; i <length; i++) {
	            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
	        }
	        return Math.sqrt(diff_square_sum);
	}
	
	public void showResults() {		
		System.out.println("\n*TOTAL RESULTS*\n");
		this.showSum();
		this.showClusters();
		this.showMicroClusters();
	}
	
	public String showSum() {		
		
		int mcsize=0;
		if(this.microClusterResult!=null)
		mcsize=this.microClusterResult.size() ;
		if(!this.resultUpToDate) this.getResults();		
		//System.out.println("\nOutlier Count : " + this.outliers.size() );
		System.out.println("\nCluster Count = " + this.clusterResult.size());	
		System.out.println("Micro Cluster Count = " + mcsize);	
		return "\nCluster Count : " + this.clusterResult.size() + "\nMicro Cluster Count : " + mcsize ;		
	
	}
	
	public void showClusters() {		
		System.out.print("\nCluster Centers : \n");
		int clustNo = 0;		
		for(Object testClust:this.clusterResult.getClustering().toArray()) {
			System.out.print("Cluster "+ clustNo++ +" - Cluster ID : "+ ((Cluster) testClust).getId() + " - Center : ");
			for(int i=0;i<this.clusterResult.dimension();i++) {
				System.out.print( ((Cluster) testClust).getCenter()[i]);
				if( i < this.clusterResult.dimension()-1) {
					System.out.print(", ");
				}
			}
			System.out.print("\n");
		}			
		//System.out.println("\nOutlier Count : " + this.outliers.size() );
	}
	
	public void showMicroClusters() {		
		System.out.print("\nMicro Cluster Centers : \n");
		int clustNo = 0;		
		for(Object testClust:this.microClusterResult.getClustering().toArray()) {
			System.out.print("Cluster "+ clustNo++ + " Center : ");
			for(int i=0;i<this.microClusterResult.dimension();i++) {
				System.out.print( ((Cluster) testClust).getCenter()[i]);
				if( i < this.microClusterResult.dimension()-1) {
					System.out.print(", ");
				}
			}
			System.out.print("\n");
		}
	}
	
	public double showLastMaxProbability(DenseInstance instance) {				
		double probability = this.clusterResult.getMaxInclusionProbability(instance);
		//System.out.print("Inclusion Probability : " + probability);		
		return probability;
	}	
	
	public double getMaxInclusionProbabilityId(Instance point) {
        double maxInclusion = 0.0;
        double currentInclusion = 0.0;
        double maxId = -1;
        AutoExpandVector<Cluster> clusters = clusterResult.getClustering();
        for (int i = 0; i < clusters.size(); i++) {
        	currentInclusion = clusters.get(i).getInclusionProbability(point);
        	if(currentInclusion>maxInclusion) {
        		maxId = clusters.get(i).getId();
        	}
        }
        return maxId;
    }
	
	
	public void setDistanceType(String distance) {
		this.useMD = distance.toLowerCase() == "md";
		this.useED = distance.toLowerCase() == "ed";
		this.useDefault = distance.toLowerCase() == "moa";
		
	}
	
	public void setOutlierPercentage(int outlierPercentage) {
		this.outlierPercentage = (double)outlierPercentage / 100.0;
	}
	
	
	public void clearDataPoints() {		
		this.getDataPoints().clear();	
		this.getInstancePoints().clear();		
	}
	
	public void clearDataIndex() {		
		this.getDataIndex().clear();	
		this.getVectorForm().clear();
	}
	
	
	public ArrayList<double[]> getVectorForm() {		
		return this.vectorForm;
	}
	
	public ArrayList<double[]> getDataIndex() {		
		return this.dataIndex;
	}
	
	public ArrayList<DataPoint> getDataPoints() {		
		return this.dataPoints;
	}
	
	public ArrayList<DenseInstance> getInstancePoints() {		
		return this.instancePoints;
	}
	
		

	public Clustering getClusterResults() {		
		return clusterResult;
	}

	public Clustering getMicroClusterResults() {		
		return microClusterResult;
	}

	
}
