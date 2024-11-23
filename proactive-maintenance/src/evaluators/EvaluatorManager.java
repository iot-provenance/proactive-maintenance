package evaluators;

import java.util.ArrayList;


import moa.cluster.Clustering;
import moa.gui.visualization.DataPoint;

public class EvaluatorManager {
	private String evaluationName;
	private Evaluator evaluator;
	private Evaluator evaluator2;
	
	public EvaluatorManager(String evaluationName){
		this.evaluationName = evaluationName;
		switch (this.evaluationName.toLowerCase()) {
			case "sc": {
				evaluator = new SCEvaluator();
				
				break;
			}
			case "vm": {
				evaluator = new VMEvaluator();
				break;
			}
			case "all": {
				evaluator = new SCEvaluator();
				evaluator2 = new VMEvaluator();
				break;
			}
			case "off": {
				evaluator = null;
				break;
			}
			default:{
					throw new IllegalArgumentException("Unexpected value: " + evaluationName);
			}
					
		}		
	}
	
	public EvaluationResults evaluate(Clustering c,Clustering t,ArrayList<DataPoint> dp) {
		if(this.evaluator==null) return null;
		return this.evaluator.evaluate(c,t,dp);		
	}
	
	public EvaluationResults evaluate2(Clustering c,Clustering t,ArrayList<DataPoint> dp) {
		if(this.evaluator2==null) return null;
		return this.evaluator2.evaluate(c,t,dp);		
	}


	public String getEvaluationName() {
		return evaluationName;
	}
	
	
}
