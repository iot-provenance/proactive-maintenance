package evaluators;

import java.util.ArrayList;

import moa.cluster.Clustering;
import moa.evaluation.SilhouetteCoefficient;
import moa.gui.visualization.DataPoint;

public class SCEvaluator extends Evaluator {
	
	private SilhouetteCoefficient silhouetteCoefficient;

	public SCEvaluator() {
		super(1);
		this.silhouetteCoefficient = new SilhouetteCoefficient();	
		this.evalResult.evalNames = this.silhouetteCoefficient.getNames();
	}
	
	
	@Override
	protected EvaluationResults evaluate(Clustering c, Clustering t, ArrayList<DataPoint> dp) {		
		try {
			this.silhouetteCoefficient.evaluateClustering(c, t, dp);
			for(int i=0;i<this.evalResult.evalNames.length;i++) {				
				this.evalResult.lastEval[i] = this.silhouetteCoefficient.getLastValue(i);	
				
				this.evalResult.evalMean[i] *= this.evalResult.evalCount++;
				this.evalResult.evalMean[i] += this.silhouetteCoefficient.getLastValue(i);
				this.evalResult.evalMean[i] /= this.evalResult.evalCount;
				
				System.out.print("Evaluation(" + this.evalResult.evalNames[i] + ") : " + this.silhouetteCoefficient.getLastValue(i));
				System.out.println(" - Mean(" + this.evalResult.evalNames[i] + ") : " + this.evalResult.evalMean[i]);
			}	
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return this.evalResult;	
	}

}
