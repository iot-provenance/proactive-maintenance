package evaluators;

import java.util.ArrayList;

import moa.cluster.Clustering;
import moa.gui.visualization.DataPoint;


public abstract class Evaluator {
	
	protected EvaluationResults evalResult;
	
	public Evaluator(int evalTypeCount) {
		evalResult = new EvaluationResults(evalTypeCount);
	}	
	
	protected abstract EvaluationResults evaluate(Clustering c,Clustering t,ArrayList<DataPoint> dp);
	
	
	public EvaluationResults getLastResult() {
		return evalResult;
	}

	
}
