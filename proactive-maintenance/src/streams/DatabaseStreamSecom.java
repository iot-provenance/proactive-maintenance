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

import application.DataReader;

import com.yahoo.labs.samoa.instances.Instances;
import com.yahoo.labs.samoa.instances.Attribute;

import dto.DatasetDTO;

public class DatabaseStreamSecom extends  AbstractOptionHandler implements
InstanceStream, ConceptDriftGenerator, CapabilitiesHandler {

	DataReader reader;
	Instances dataset;

    int index=0;
    private com.yahoo.labs.samoa.instances.Instances datasetStructure;
   private List<Attribute> atts;
    public DatabaseStreamSecom( DataReader rs) {
        try {
            // Initialize database connection
         
            this.reader = rs;
   
         atts   =new  ArrayList<Attribute>(); 
         for(int i=0;i<595;i++) {
         atts.add(new Attribute(i+""));
         }
         
         dataset = new Instances("dataset",atts,595);
         dataset.setClassIndex(593);
         

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
            return 1567>index+1;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public InstanceExample nextInstance() {
        try {
        	
     
        	 double[] value =reader.readDataSecom();
        	 
           	 double[] values_new = new double[value.length];
        	 com.yahoo.labs.samoa.instances.Instance  instance = new DenseInstance(1.0,values_new);
        	
            // TODO: Map the result set data to the instance
            // For example:
        for(int i=0;i<value.length;i++) {
             instance.setValue(i, value[i]);
        }
        
             
   
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
