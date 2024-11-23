package evaluators;

import java.text.DecimalFormat;
import java.util.Arrays;

public class EvaluationResults{
	
	protected double[] lastEval;
	protected double[] evalMean;
	protected double evalCount;	
	protected int evalTypeCount;
	protected String[] evalNames;
	private int id;
	DecimalFormat df;
	
	public EvaluationResults(int evalTypeCount){
		this.evalTypeCount = evalTypeCount;
		this.lastEval = new double[evalTypeCount];	
		this.evalCount = 0;
		this.evalMean = new double[evalTypeCount];
		Arrays.fill(evalMean, 0);	
		this.df = new DecimalFormat();
		this.df.setMaximumFractionDigits(2);
	}
			
	public double getEvalTypeCount() {
		return evalTypeCount;
	}		
	
	public double getEvalCount() {
		return evalCount;
	}
	
	public double[] getEvalMean() {
		return evalMean;
	}
	
	public void setEvalNames(String[] evalNames) {
		this.evalNames = evalNames;
	}
	
	public int getId() {
		return id;
	}
	
	public String showEvalResults() {	
		String tmpText= "";
		for(int i=0;i<evalTypeCount;i++) {	
			tmpText += "\nEvaluation(" + this.evalNames[i] + ") : " + df.format(this.lastEval[i]) + " - Mean(" + evalNames[i] + ") : " + df.format(this.evalMean[i]);
			//System.out.print("Evaluation(" + this.evalNames[i] + ") : " + this.lastEval[i]);
			//System.out.println(" - Mean(" + evalNames[i] + ") : " + this.evalMean[i]);
		}	
		return tmpText;
	}	
	
	//////////////////////////////
	
	//NO LONGER IN USE
	public static EvaluationResults[] findBest(EvaluationResults[] list,int evalTypeIndex) {
		
		if(list[0] ==null) return new EvaluationResults[]{null,null};
		
		EvaluationResults bestResult = list[0]; 
		EvaluationResults currentBestResult = list[0];
		
		for(int i=0;i<list.length;i++) {	
			if(currentBestResult.lastEval[evalTypeIndex] < list[i].lastEval[evalTypeIndex]	) {
				currentBestResult = list[i];
				currentBestResult.id = i;
			}			
			if(bestResult.evalMean[evalTypeIndex] < list[i].evalMean[evalTypeIndex]	) {
				bestResult = list[i];
				bestResult.id = i;
			}			
		}		
		return new EvaluationResults[]{currentBestResult,bestResult};		
	}
	
	//NO LONGER IN USE
	public static EvaluationResults findElbow(EvaluationResults[] list,int evalTypeIndex) {
		
		if(list[0] ==null) return null;
		int elbow = 0;
		double maxDiff=0;
		double[] angleList = new double[list.length];
		angleList[0] = 0;
		
		for(int i=1;i<list.length-1;i++) {	
			double[] point0 = {list[i-1].evalMean[evalTypeIndex],i-1};
			double[] point1 = {list[i].evalMean[evalTypeIndex],i};
			double[] point2 = {list[i+1].evalMean[evalTypeIndex],i+1};
			angleList[i] = EvaluationResults.findAngle(point0,point1,point2);
		}
		
		for(int i=2;i<angleList.length-1;i++) {
			double a = angleList[i];

			double a1 = angleList[i-1];
			double d1 = a1-a;

			double a3 = angleList[i+1];
			double d2 = a3-a;

			double diff = d1+d2;
			if (diff > maxDiff) {
				maxDiff = diff;
				elbow = i;
			}	
		}
		
		double[] bestOfFirst2 = list[0].evalMean[evalTypeIndex] > list[1].evalMean[evalTypeIndex]?
				new double[]{list[0].evalMean[evalTypeIndex],0.0}:new double[]{list[1].evalMean[evalTypeIndex],1.0};
				
		//System.out.println("\nElbow Result(Zero) : " +  list[0].evalMean[evalTypeIndex]);
		//System.out.println("\nElbow Result(One) : " +  list[1].evalMean[evalTypeIndex]);
		
		if(bestOfFirst2[0]>list[elbow].evalMean[evalTypeIndex] ) {
			list[(int) bestOfFirst2[1]].id = (int) bestOfFirst2[1];
			return list[(int) bestOfFirst2[1]];
		}			
		list[elbow].id = elbow;
		return list[elbow];
	}
	
	//NO LONGER IN USE
	private static double findAngle(double[] point0, double[] point1, double[] point2) {
		double[] vector0 = {point1[0]- point0[0] , point1[1]- point0[1] }; //point1 - point0; 
		double[] vector1 = {point2[0]- point1[0] , point2[1]- point1[1] };	//point2 - point1;	
		return 180.0 / Math.PI * Math.atan2( vector1[1] - vector0[1], vector1[0]-vector0[0]);
	}	
	
}