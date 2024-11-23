package streams;


import moa.streams.InstanceStream;
import moa.streams.clustering.ClusterEvent;
import moa.streams.generators.cd.ConceptDriftGenerator;
import moa.tasks.TaskMonitor;
import moa.capabilities.CapabilitiesHandler;
import moa.core.InstanceExample;
import moa.core.ObjectRepository;
import moa.options.AbstractOptionHandler;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import java.util.Properties;

import com.yahoo.labs.samoa.instances.DenseInstance;
import com.yahoo.labs.samoa.instances.InstancesHeader;
import com.yahoo.labs.samoa.instances.Instances;
import com.yahoo.labs.samoa.instances.Attribute;

import dto.DatasetDTO;

public class DatabaseStream extends  AbstractOptionHandler implements
InstanceStream, ConceptDriftGenerator, CapabilitiesHandler {


    private ArrayList<DatasetDTO> resultSet;
    int index=0;
    private com.yahoo.labs.samoa.instances.Instances datasetStructure;
   private List<Attribute> atts;
    public DatabaseStream( ArrayList<DatasetDTO> rs) {
        try {
            // Initialize database connection
         
            this.resultSet = rs;
   
         atts   =new  ArrayList<Attribute>(); 
         atts.add(new Attribute("1"));
         atts.add(new Attribute("2"));
         atts.add(new Attribute("3"));
         atts.add(new Attribute("4"));
         atts.add(new Attribute("5"));
         atts.add(new Attribute("6"));
         atts.add(new Attribute("7"));
         atts.add(new Attribute("8"));
         atts.add(new Attribute("9"));
         atts.add(new Attribute("10"));
         atts.add(new Attribute("11"));
         atts.add(new Attribute("12"));
         atts.add(new Attribute("13"));
         atts.add(new Attribute("14"));
         atts.add(new Attribute("15"));
         atts.add(new Attribute("16"));
         atts.add(new Attribute("17"));
         atts.add(new Attribute("18"));
         atts.add(new Attribute("19"));
         atts.add(new Attribute("20"));
         atts.add(new Attribute("21"));
         atts.add(new Attribute("22"));
         atts.add(new Attribute("23"));
         atts.add(new Attribute("24"));
         atts.add(new Attribute("25"));
         atts.add(new Attribute("26"));

            // Assuming the dataset structure is known
            // TODO: Define the structure of your dataset here
          
        } catch (Exception e) {
            System.err.println("Error initializing database stream: " + e.getMessage());
        }
    }

    @Override
    public long estimatedRemainingInstances() {
        // TODO: Implement if possible, otherwise return -1
        return -1;
    }

    @Override
    public boolean hasMoreInstances() {
        try {
            return this.resultSet.size()>index+1;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public InstanceExample nextInstance() {
        try {
        	
        	 double[] values_new = new double[26];
        	 com.yahoo.labs.samoa.instances.Instance  instance = new DenseInstance(1.0,values_new);
        	

            // TODO: Map the result set data to the instance
            // For example:
             instance.setValue(0, this.resultSet.get(index).s1);
             instance.setValue(1, this.resultSet.get(index).s2);
             instance.setValue(2, this.resultSet.get(index).s3);
             instance.setValue(3, this.resultSet.get(index).s4);
             instance.setValue(4, this.resultSet.get(index).s5);
             instance.setValue(5, this.resultSet.get(index).s6);
             instance.setValue(6, this.resultSet.get(index).s7);
             instance.setValue(7, this.resultSet.get(index).s8);
             instance.setValue(8, this.resultSet.get(index).s9);
             instance.setValue(9, this.resultSet.get(index).s10);
             instance.setValue(10, this.resultSet.get(index).s11);
             instance.setValue(11, this.resultSet.get(index).s12);
             instance.setValue(12, this.resultSet.get(index).s13);
             instance.setValue(13, this.resultSet.get(index).s14);
             instance.setValue(14, this.resultSet.get(index).s15);
             instance.setValue(15, this.resultSet.get(index).s16);
             instance.setValue(16, this.resultSet.get(index).s17);
             instance.setValue(17, this.resultSet.get(index).s18);
             instance.setValue(18, this.resultSet.get(index).s19);
             instance.setValue(19, this.resultSet.get(index).s20);
             instance.setValue(20, this.resultSet.get(index).s21);
             instance.setValue(21, this.resultSet.get(index).s22);
             instance.setValue(22, this.resultSet.get(index).s23);
             instance.setValue(23, this.resultSet.get(index).s24);
             instance.setValue(24, this.resultSet.get(index).label);
             instance.setValue(25, this.resultSet.get(index).sira);
             
             Instances dataset = new Instances("dataset",atts,resultSet.size());
             dataset.setClassIndex(24);
             
             instance.setDataset(dataset); 
             
             index++;
            
            // ...

            return new InstanceExample(instance);
        } catch (Exception e) {
            System.err.println("Error reading instance from database: " + e.getMessage());
            return null;
        }
    }

    public void restart() {
        try {
           index=0;
        } catch (Exception e) {
            System.err.println("Error restarting stream: " + e.getMessage());
        }
    }

 
	@Override
	public InstancesHeader getHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRestartable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getDescription(StringBuilder arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<ClusterEvent> getEventsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareForUseImpl(TaskMonitor arg0, ObjectRepository arg1) {
		// TODO Auto-generated method stub
		
	}
}
