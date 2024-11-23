package clusterers;
import java.util.ArrayList;
import java.util.List;

import application.DataReader;

public class ElbowKFinder {
    public static void main(String[] args) {
    	DataReader dbReader = new DataReader(1);	
        double[] dataSet = 	dbReader.readData();
        List<Double> distortions = new ArrayList<>(); // List to store the distortions
        
        for (int k = 1; k <= 10; k++) {
            double distortion = calculateDistortion(dataSet, k); // Calculate distortion for each value of k
            distortions.add(distortion);
            System.out.println("Distortion for k=" + k + ": " + distortion);
        }
        
        int optimalK = findOptimalK(distortions); // Find the optimal k value
        System.out.println("Optimal value of k: " + optimalK);
    }
    
    // Calculate the distortion for a given value of k
    private static double calculateDistortion(double[] dataSet, int k) {
        double[] centroids = new double[k];
        for (int i = 0; i < k; i++) {
            centroids[i] = dataSet[i]; // Initialize the centroids with the first k data points
        }
        
        double distortion = 0;
        for (double data : dataSet) {
            double minDistance = Double.MAX_VALUE;
            for (double centroid : centroids) {
                double distance = Math.abs(data - centroid);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
            distortion += minDistance * minDistance;
        }
        
        return distortion;
    }
    
    // Find the optimal value of k based on the elbow method
    private static int findOptimalK(List<Double> distortions) {
        int optimalK = 1;
        double maxSlope = Double.MIN_VALUE;
        for (int i = 1; i < distortions.size() - 1; i++) {
            double slope = (distortions.get(i+1) - distortions.get(i)) / (i+1);
            if (slope > maxSlope) {
                maxSlope = slope;
                optimalK = i+1;
            }
        }
        
        return optimalK;
    }
}