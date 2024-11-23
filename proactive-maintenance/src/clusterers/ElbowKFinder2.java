package clusterers;
import java.util.ArrayList;
import java.util.List;

import application.DataReader;

public class ElbowKFinder2 {
	
	 public static void main(String[] args) {
	    	DataReader dbReader = new DataReader(1);	
	        List<double[]> dataset = 	dbReader.readData(60000);

	        int kMax = 12;
	        List<Double> sseList = new ArrayList<>();
	        for (int k = 1; k <= kMax; k++) {
	            KMeans kmeans = new KMeans(k);
	            kmeans.fit(dataset);
	            sseList.add(kmeans.getSSE());
	        }

	        int optimalK = 1;
	        double minSSE = Double.MAX_VALUE;
	        for (int i = 0; i < sseList.size(); i++) {
	          	 System.out.println(" value of k: " + i);
	        	  System.out.println("Optimal value of k: " + optimalK);
	            if (sseList.get(i) < minSSE) {
	                optimalK = i + 1;
	                minSSE = sseList.get(i);
	            }
	        }

	        System.out.println("The optimal value of k is " + optimalK);
	    }

    static class KMeans {
        private int k;
        private List<double[]> centroids;
        private List<List<double[]>> clusters;

        public KMeans(int k) {
            this.k = k;
            centroids = new ArrayList<>();
            clusters = new ArrayList<>();
        }

        public void fit(List<double[]> dataset) {
            for (int i = 0; i < k; i++) {
                centroids.add(dataset.get((int) (Math.random() * dataset.size())));
            }

            while (true) {
                clusters.clear();
                for (int i = 0; i < k; i++) {
                    clusters.add(new ArrayList<>());
                }
                for (double[] point : dataset) {
                    int closestCentroidIndex = getClosestCentroidIndex(point, centroids);
                    clusters.get(closestCentroidIndex).add(point);
                }

                List<double[]> newCentroids = new ArrayList<>();
                for (List<double[]> cluster : clusters) {
                    double[] mean = calculateMean(cluster);
                    newCentroids.add(mean);
                }

                if (areListsEqual(centroids,newCentroids)) {
                    break;
                }
                
        
                
                centroids = newCentroids;
            }
        }
        
        public  boolean areListsEqual(List<double[]> list1, List<double[]> list2) {
            if (list1 == null && list2 == null) {
                return true;
            }
            if (list1 == null || list2 == null || list1.size() != list2.size()) {
                return false;
            }
            for (int i = 0; i < list1.size(); i++) {
                double[] arr1 = list1.get(i);
                double[] arr2 = list2.get(i);
                if (arr1 == null && arr2 == null) {
                    continue;
                }
                if (arr1 == null || arr2 == null || arr1.length != arr2.length) {
                    return false;
                }
                for (int j = 0; j < arr1.length; j++) {
                    if (arr1[j] != arr2[j]) {
                        return false;
                    }
                }
            }
            return true;
        }

        private int getClosestCentroidIndex(double[] point, List<double[]> centroids) {
            int closestIndex = 0;
            double closestDistance = Double.MAX_VALUE;
            for (int i = 0; i < centroids.size(); i++) {
                double distance = calculateDistance(point, centroids.get(i));
                if (distance < closestDistance) {
                    closestIndex = i;
                    closestDistance = distance;
                }
            }
            return closestIndex;
        }

        private double calculateDistance(double[] point1, double[] point2) {
            double sum = 0;
            for (int i = 0; i < point1.length && i < point2.length; i++) {
                double diff = point1[i] - point2[i];
                sum += diff * diff;
            }
            return Math.sqrt(sum);
        }

        private double[] calculateMean(List<double[]> cluster) {
         	if(cluster.isEmpty())
        		return new double[0];
            double[] sum = new double[cluster.get(0).length];
            for (double[] point : cluster) {
                for (int i = 0; i < point.length; i++) {
                    sum[i] += point[i];
                }
            }
            for (int i = 0; i < sum.length; i++) {
                sum[i] /= cluster.size();
            }
            return sum;
        }

        public double getSSE() {
            double sse = 0;
            for (int i = 0; i < k; i++) {
                double[] centroid = centroids.get(i);
                List<double[]> cluster = clusters.get(i);
                for (double[] point : cluster) {
                    double distance = calculateDistance(point, centroid);
                    sse += distance * distance;
                }
            }
            return sse;
        }
    }

}