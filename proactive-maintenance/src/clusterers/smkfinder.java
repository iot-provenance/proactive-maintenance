package clusterers;



import java.util.List;
import java.util.Map;

import application.DataReader;

import java.util.HashMap;
import java.util.ArrayList;


public class smkfinder {
    private List<double[]> dataset;
    private int maxK;

    public static void main(String[] args) {
    
   
       
 
       DataReader dbReader = new DataReader(1);	
       List<double[]>  dataset = 	dbReader.readData(60000);
     	smkfinder s=new smkfinder(dataset,10);
     
        
        int optimalK = s.findOptimalK(); // Find the optimal k value
     //   System.out.println("Optimal value of k: " + optimalK);
    }
    
    public smkfinder(List<double[]> dataset, int maxK) {
        this.dataset = dataset;
        this.maxK = maxK;
    }

    public int findOptimalK() {
    
    
        double maxSilhouetteCoefficient = Double.MIN_VALUE;
        int optimalK = 2; // start with k=2
        for (int k = 2; k <= maxK; k++) {
        	// System.out.println(" value of k: " + k);
        	 // System.out.println("Optimal value of k: " + optimalK);
            KMeans kMeans = new KMeans(k);
            kMeans.fit(dataset);
            List<double[]> centroids = kMeans.getCentroids();
            List<List<double[]>> clusters = kMeans.getClusters();

            double silhouetteCoefficient = calculateSilhouetteCoefficient(centroids, clusters);
            if (silhouetteCoefficient > maxSilhouetteCoefficient) {
                maxSilhouetteCoefficient = silhouetteCoefficient;
                optimalK = k;
            }
        }
        return optimalK;
    	
    	
    }

    private double calculateSilhouetteCoefficient(List<double[]> centroids, List<List<double[]>> clusters) {
        Map<double[], Double> pointToClusterDistanceMap = new HashMap<>();
        Map<double[], Double> pointToNearestClusterDistanceMap = new HashMap<>();
        for (List<double[]> cluster : clusters) {
            for (double[] point : cluster) {
                double distanceToCluster = calculateAverageDistance(point, cluster);
                pointToClusterDistanceMap.put(point, distanceToCluster);

                double minDistanceToOtherCluster = Double.MAX_VALUE;
                for (List<double[]> otherCluster : clusters) {
                    if (otherCluster != cluster) {
                        double distanceToOtherCluster = calculateAverageDistance(point, otherCluster);
                        if (distanceToOtherCluster < minDistanceToOtherCluster) {
                            minDistanceToOtherCluster = distanceToOtherCluster;
                        }
                    }
                }
                pointToNearestClusterDistanceMap.put(point, minDistanceToOtherCluster);
            }
        }

        double sum = 0;
        for (double[] point : dataset) {
            double a = pointToClusterDistanceMap.get(point);
            double b = pointToNearestClusterDistanceMap.get(point);
            double maxDistance = Math.max(a, b);
            double silhouetteCoefficient = (b - a) / maxDistance;
            sum += silhouetteCoefficient;
        }
        return sum / dataset.size();
    }

    private double calculateAverageDistance(double[] point, List<double[]> cluster) {
        double sum = 0;
        for (double[] otherPoint : cluster) {
            sum += calculateDistance(point, otherPoint);
        }
        return sum / cluster.size();
    }

    private double calculateDistance(double[] point1, double[] point2) {
        double sum = 0;
        for (int i = 0; i < point1.length; i++) {
            double diff = point1[i] - point2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }




public class KMeans {
    private int k;
    private List<double[]> centroids;
    private List<List<double[]>> clusters;

    public KMeans(int k) {
        this.k = k;
        centroids = new ArrayList<double[]>();
        clusters = new ArrayList<>();
    }

    public void fit(List<double[]> dataset) {
        for (int i = 0; i < k; i++) {
           // centroids.add(dataset.get((int) (Math.random() * dataset.size())));
            centroids.add(dataset.get(i));
        }
       int j=0;

        while (true && j<10) {
        	j++;
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

    public List<double[]> getCentroids() {
        return centroids;
    }

    public List<List<double[]>> getClusters() {
        return clusters;
    }
}}

