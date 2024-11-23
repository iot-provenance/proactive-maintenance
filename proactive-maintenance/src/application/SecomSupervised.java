package application;


import moa.classifiers.trees.HoeffdingTree;
import moa.classifiers.Classifier;
import moa.core.TimingUtils;
import moa.streams.clustering.SimpleCSVStream;
import moa.streams.generators.RandomRBFGenerator;

import com.yahoo.labs.samoa.instances.Instance;

import dto.DatasetDTO;

import java.io.IOException;
import java.util.ArrayList;

import streams.*;


public class SecomSupervised {

        public SecomSupervised(){
        }

        public void run(int numInstances, boolean isTesting){
                Classifier learner = new HoeffdingTree();
       		    DataReader dbReader =new DataReader(1);
          
       		 DatabaseStreamSecom stream = new DatabaseStreamSecom(dbReader);
      
                stream.prepareForUse();

                learner.setModelContext(stream.getHeader());
                learner.prepareForUse();

                int numberSamplesCorrect = 0;
                int numberSamples = 0;
                boolean preciseCPUTiming = TimingUtils.enablePreciseTiming();
                long evaluateStartTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
                while (stream.hasMoreInstances() && numberSamples < numInstances) {
                        Instance trainInst = stream.nextInstance().getData();
                        if (isTesting && numberSamples>50) {
                                if (learner.correctlyClassifies(trainInst)){
                                        numberSamplesCorrect++;
                                }
                        }
                        numberSamples++;
                        learner.trainOnInstance(trainInst);
                        
                        System.out.println(numberSamples + " instances processed with " + numberSamplesCorrect + "correct.");

                }
                
                
                double accuracy = 100.0 * (double) numberSamplesCorrect/ (double) numberSamples;
                double time = TimingUtils.nanoTimeToSeconds(TimingUtils.getNanoCPUTimeOfCurrentThread()- evaluateStartTime);
                System.out.println(numberSamples + " instances processed with " + accuracy + "% accuracy in "+time+" seconds.");
        }

  
}
