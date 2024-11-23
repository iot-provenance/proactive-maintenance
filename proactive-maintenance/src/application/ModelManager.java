package application;

import moa.classifiers.AbstractClassifier;
import moa.classifiers.Classifier;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class ModelManager {
    public static void saveModel(Classifier learner, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(learner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


public static Classifier loadModel(String filename) {
    AbstractClassifier model = null;
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
        model = (AbstractClassifier) in.readObject();
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return model;
}
}