
package evaluators;

import java.util.ArrayList;
import java.util.List;
import com.yahoo.labs.samoa.instances.Instance;

import moa.cluster.Clustering;
import moa.evaluation.SilhouetteCoefficient;
import moa.gui.visualization.DataPoint;

public class VMeasureEvaluator extends Evaluator {
	
	private SilhouetteCoefficient silhouetteCoefficient;

	public VMeasureEvaluator() {
		super(1);
		
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
				
				//System.out.print("Evaluation(" + evalNames[i] + ") : " + this.silhouetteCoefficient.getLastValue(i));
				//System.out.println(" - Mean(" + evalNames[i] + ") : " + this.evalResult.evalMean[i]);
			}	
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return this.evalResult;	
	}

}



 class mVMeasure {
    
    public static double computeVMeasure(List<List<Instance>> clusters, List<Instance> dataset) {
        // Count the number of instances and clusters
        int numInstances = dataset.size();
        int numClusters = clusters.size();
        
        // Compute the entropy of the dataset
        double datasetEntropy = computeEntropy(dataset, numInstances);
        
        // Compute the entropy and purity of each cluster
        double[] clusterEntropies = new double[numClusters];
        double[] clusterPurities = new double[numClusters];
        for (int i = 0; i < numClusters; i++) {
            List<Instance> cluster = clusters.get(i);
            int numClusterInstances = cluster.size();
            clusterEntropies[i] = computeEntropy(cluster, numClusterInstances);
            clusterPurities[i] = computePurity(cluster, numClusterInstances);
        }
        
        // Compute the v-measure
        double entropySum = 0.0;
        double puritySum = 0.0;
        for (int i = 0; i < numClusters; i++) {
            double weight = (double) clusters.get(i).size() / numInstances;
            entropySum += weight * clusterEntropies[i];
            puritySum += weight * clusterPurities[i];
        }
        double vMeasure = 2.0 * (puritySum * entropySum) / (puritySum + entropySum);
        
        return vMeasure;
    }
    
    private static double computeEntropy(List<Instance> instances, int numInstances) {
        double entropy = 0.0;
        int[] counts = new int[numInstances];
        for (Instance instance : instances) {
            counts[(int) instance.classValue()]++;
        }
        for (int count : counts) {
            if (count > 0) {
                double proportion = (double) count / instances.size();
                entropy -= proportion * Math.log(proportion);
            }
        }
        return entropy;
    }
    
    private static double computePurity(List<Instance> instances, int numInstances) {
        int[] counts = new int[numInstances];
        for (Instance instance : instances) {
            counts[(int) instance.classValue()]++;
        }
        int maxCount = 0;
        for (int count : counts) {
            if (count > maxCount) {
                maxCount = count;
            }
        }
        return (double) maxCount / instances.size();
    }
}

