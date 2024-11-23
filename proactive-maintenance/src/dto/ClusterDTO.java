package dto;

import java.util.List;
import java.util.ArrayList;


import clusterers.Clusterer;

public class ClusterDTO {
	
	public int Did;
	
	public int Mid;
	
	public int Percent;
	
	public int TotalDataCount;
	
	public int SelectedDataCount;
	
	public Clusterer ClusterAlgorithm;
	
	public String AlgorithmName;
	
	public double SilhouetteValue;
	
	public double VMeasureValue;
	
	public int OutlierCount;
	
	public int OutlierCountProb;
	
	public int SilhouetteKValue;
	
	public int EngineRunningCount;
	
	public int Threshold;
	
	public List<ClusterEntityDTO> Clusters=new ArrayList<ClusterEntityDTO>();
	
	
	  @Override
	  public String toString(){
	        StringBuilder dataBuilder = new StringBuilder();
	        appendFieldValue(dataBuilder, Percent+"");
	        appendFieldValue(dataBuilder, TotalDataCount+"");
	        appendFieldValue(dataBuilder, SelectedDataCount+"");
	        appendFieldValue(dataBuilder, SilhouetteValue+"");
	        
	        appendFieldValue(dataBuilder, VMeasureValue+"");
	        
	        appendFieldValue(dataBuilder, OutlierCount+"");
	        
	        appendFieldValue(dataBuilder, SilhouetteKValue+"");
	        return dataBuilder.toString();
	    }

	    private void appendFieldValue(StringBuilder dataBuilder, String fieldValue) {
	        if(fieldValue != null) {
	            dataBuilder.append(fieldValue).append(",");
	        } else {
	            dataBuilder.append("").append(",");
	        }
	    }

		public String getClusterInfor() {
			
			//StringBuilder sb=new StringBuilder();
			//int i=1;
			/*
			 * for (ClusterEntityDTO item:Clusters) { if(i<4)
			 * sb.append("Cluster Count:"+Clusters.size()+" "+
			 * i+". Centroid Radius:"+item.Radiues+" "+
			 * i+".Centroid Center dim:"+item.Dimension+" "+
			 * i+".Centroid Center dim1:"+item.Dimension1+" "+
			 * i+".Centroid Center dim2:"+item.Dimension2+" "+
			 * i+".Centroid Center dim3:"+item.Dimension3);
			 * 
			 * i++; }
			 */
		
			  return Clusters.size()+"";
		}

}

