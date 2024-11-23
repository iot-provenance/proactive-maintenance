package dto;

import java.util.ArrayList;

import org.openprovenance.prov.model.Attribute;

import enums.Enums;

public class ModelDTO {
	
	public Enums.ModelTypes modelType;
	
	public String modelName; 
	
	public String frameworkName; 
	
	public double MAE;
	public double MAPE;
	public double RMSE;
	public double MSE;
	
	public double TN;
	
	public double TP;
	
	public double FN;
	
	public double FP;
	
	public double Accuracy;
	
	public double F1Call;
	
	public double Precision;
	
	public double Recall;
	
	public double VMeasure;
	
	public double SilhouetteValue;
	
	public String version;
	
	public ArrayList<String> HyperParameters=new 	ArrayList<String> ();
	
	public int trainingDataSize;
	
	public int testDataSize;

}
