package evaluators;

import java.util.ArrayList;

import moa.cluster.Clustering;
import moa.evaluation.EntropyCollection;
import moa.evaluation.SilhouetteCoefficient;
import moa.gui.visualization.DataPoint;

public class VMEvaluator extends Evaluator {
	
	private EntropyCollection eval;

	public VMEvaluator() {
		super(1);
		this.eval = new EntropyCollection();
	    String[] names = {"V-Measures"};
		this.evalResult.evalNames = names;
	}
	
	
	@Override
	protected EvaluationResults evaluate(Clustering c, Clustering t, ArrayList<DataPoint> dp) {		
		try {
			this.eval.evaluateClustering(c, t, dp);
			for(int i=0;i<this.evalResult.evalNames.length;i++) {				
				this.evalResult.lastEval[i] = this.eval.getLastValue(i);	
				
				this.evalResult.evalMean[i] *= this.evalResult.evalCount++;
				this.evalResult.evalMean[i] += this.eval.getLastValue(i);
				this.evalResult.evalMean[i] /= this.evalResult.evalCount;
				
				System.out.print("Evaluation(" + this.evalResult.evalNames[i] + ") : " + this.eval.getLastValue(i));
				System.out.println(" - Mean(" + this.evalResult.evalNames[i] + ") : " + this.evalResult.evalMean[i]);
			}	
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return this.evalResult;	
	}

}
