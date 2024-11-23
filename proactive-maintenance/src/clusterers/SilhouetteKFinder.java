package clusterers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.DataReader;

public class SilhouetteKFinder {
    public static void main(String[] args) {
    	SilhouetteKFinder s=new SilhouetteKFinder();
       s.silhouetteScores = new ArrayList<>(); // List to store the silhouette values
       DataReader dbReader = new DataReader(1);	
       double[] dataSet = 	dbReader.readData();
        s.dataSet=dataSet;
     
        
        int optimalK = s.findOptimalK(); // Find the optimal k value
        System.out.println("Optimal value of k: " + optimalK);
    }
 
   
        public double[] dataSet;
        public List<Double> silhouetteScores;
        

        
        // Calculate the silhouette score for a given value of k
        private double calculateSilhouetteScore(double[] dataSet, int k) {
            double[] centroids = new double[k];
            for (int i = 0; i < k; i++) {
                centroids[i] = dataSet[i]; // Initialize the centroids with the first k data points
            }
            
            double[] clusterLabels = new double[dataSet.length];
            for (int i = 0; i < dataSet.length; i++) {
                double minDistance = Double.MAX_VALUE;
                int minCluster = -1;
                for (int j = 0; j < centroids.length; j++) {
                    double distance = Math.abs(dataSet[i] - centroids[j]);
                    if (distance < minDistance) {
                        minDistance = distance;
                        minCluster = j;
                    }
                }
                clusterLabels[i] = minCluster;
            }
            
            double[] clusterDistances = new double[k];
            int[] clusterSizes = new int[k];
            for (int i = 0; i < dataSet.length; i++) {
                double distanceSum = 0;
                int cluster = (int) clusterLabels[i];
                clusterSizes[cluster]++;
                for (int j = 0; j < dataSet.length; j++) {
                    if (clusterLabels[j] == cluster) {
                        distanceSum += Math.abs(dataSet[i] - dataSet[j]);
                    }
                }
                clusterDistances[cluster] += distanceSum;
            }
            
            double silhouetteSum = 0;
            for (int i = 0; i < dataSet.length; i++) {
                int cluster = (int) clusterLabels[i];
                double a = clusterDistances[cluster] / clusterSizes[cluster];
                
                double b = Double.MAX_VALUE;
                for (int j = 0; j < clusterDistances.length; j++) {
                    if (j != cluster) {
                        double distance = clusterDistances[j] / clusterSizes[j];
                        if (distance < b) {
                            b = distance;
                        }
                    }
                }
                
                double silhouetteScore = (b - a) / Math.max(a, b);
                silhouetteSum += silhouetteScore;
            }
            
            return silhouetteSum / dataSet.length;
        }
        
        // Find the optimal value of k based on the silhouette method
        public int findOptimalK() {
            for (int k = 1; k <= 10; k++) {
                double silhouetteScore = calculateSilhouetteScore(dataSet, k); // Calculate silhouette score for each value of k
                silhouetteScores.add(silhouetteScore);
                System.out.println("Silhouette score for k=" + k + ": " + silhouetteScore);
            }
            
            int optimalK = 2;
            double maxSilhouetteScore = Double.MIN_VALUE;
            for (int i = 0; i < silhouetteScores.size(); i++) {
                if (silhouetteScores.get(i) > maxSilhouetteScore) {
                    maxSilhouetteScore = silhouetteScores.get(i);
                    optimalK = i + 2; // Add 2 to get the actual value of k (k starts from 2 in this implementation)
                }
            }
            
            System.out.println("Optimal value of k="+optimalK);
            
            return optimalK;
            	}

}