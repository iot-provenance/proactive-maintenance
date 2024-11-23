package clusterers;

import java.util.Date;
import com.yahoo.labs.samoa.instances.DenseInstance;

import dto.InputDTO;
import moa.clusterers.denstream.WithDBSCAN;
import moa.gui.visualization.DataPoint;

public class DenStreamClusterer extends Clusterer{
	
	private WithDBSCAN denStream;	
	
	public DenStreamClusterer(String evaluationName) {	
		
		super(evaluationName);
		this.denStream = new WithDBSCAN();
		this.denStream.resetLearning();	
		this.denStream.initialDBScan();	
		
	}
	
	@Override
	public void add(double[] vector,boolean training) {
		this.resultUpToDate = false;
		DenseInstance instance = new DenseInstance(vector.length);		 
		 		 
		for (int i = 0; i < vector.length; i++) {
			instance.setValue(i,vector[i]);
		}
		instance.setDataset(genInstanceHead(vector.length));				
		this.denStream.trainOnInstanceImpl(instance);
		if(!training) {
			this.dataPoints.add(new DataPoint(instance, (int) (new Date().getTime()/1000)));
			this.instancePoints.add(instance);
			//this.dataIndex.add(new double[]{vector[vector.length-2],vector[vector.length-1]});
			//this.vectorForm.add(new double[]{vector[0],vector[1],vector[2],vector[3],vector[4],vector[5],vector[6]});
			this.vectorForm.add(new double[]{vector[0],vector[1],vector[2],vector[3],vector[4],vector[5],vector[6], vector[7],vector[8],vector[9],vector[10],vector[11],vector[12],vector[13],vector[14],vector[15], vector[16], vector[17],vector[18],vector[19],vector[20],vector[21],vector[22],vector[23]});

		}	
		
	}	
		
	@Override
	public void getResults() {
		this.resultUpToDate = true;
		this.microClusterResult = this.denStream.getMicroClusteringResult();		
		this.clusterResult = this.denStream.getClusteringResult();	
	}

	@Override
	public void add(InputDTO input, boolean training) {
		this.resultUpToDate = false;
		DenseInstance instance = new DenseInstance(input.inputVector.length);		 
		 		 
		for (int i = 0; i < input.inputVector.length; i++) {
			instance.setValue(i,input.inputVector[i]);
		}
		instance.setDataset(genInstanceHead(input.inputVector.length));	
	
		this.denStream.trainOnInstanceImpl(instance);
		
		//instance.setValue(0,vector[0]);
	
		if(!training) {
		
			
			this.dataPoints.add(new DataPoint(instance, (int) (new Date().getTime()/1000)));
			this.instancePoints.add(instance);
			this.inputs.add(input);
			//this.dataIndex.add(new double[]{vector[vector.length-2],vector[vector.length-1]});
			//this.vectorForm.add(new double[]{vector[0],vector[1],vector[2],vector[3],vector[4],vector[5],vector[6]});
		//	this.vectorForm.add(new double[]{vector[0],vector[1],vector[2],vector[3],vector[4],vector[5],vector[6], vector[7],vector[8],vector[9],vector[10],vector[11],vector[12],vector[13],vector[14],vector[15], vector[16], vector[17],vector[18],vector[19],vector[20],vector[21],vector[22],vector[23]});
			this.vectorForm.add(new double[]{input.inputVector[0],input.inputVector[1],input.inputVector[2],input.inputVector[3],input.inputVector[4],input.inputVector[5],input.inputVector[6], input.inputVector[7],input.inputVector[8],input.inputVector[9],input.inputVector[10],input.inputVector[11],input.inputVector[12],input.inputVector[13],input.inputVector[14],input.inputVector[15], input.inputVector[16], input.inputVector[17],input.inputVector[18],input.inputVector[19],input.inputVector[20],input.inputVector[21],input.inputVector[22],input.inputVector[23]});

		}			
		
		
	}
	
}
