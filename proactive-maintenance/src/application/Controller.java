package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import clusterers.ClusterManager;
import dto.LogMessage;
import enums.Enums;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import provenance.ProvenanceManager;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.*;
import javafx.stage.Stage;

public class Controller {

	public static FXMLLoader currentLoader;
	public static Controller currentController;

	FXMLLoader loader;


	Button trainingStart;
	Button generateProvenance;
	Button provenanceImageButton;
	Button provenanceImageSvgButton;
	ImageView processImage;

	CheckBox trainingEvaluation;

	TextArea resultLabel;
	TextArea clusteringResultLabel;
	TextArea classificationResultLabel;
	TextArea rulResultLabel;
	TextArea actionResultLabel;
	TextArea dataCollectionResultLabel;
	//Label stateLabel;
	ImageView provenanceImage;

	int onScreenLine = 0;

	ClusterManager clusterer;


	public Controller(FXMLLoader loader) {
		try {
		this.loader = loader;
		Controller.currentLoader = this.loader;
		Controller.currentController = this;
		}
		catch(Exception e){
			
			System.out.println(e.getStackTrace());
		}
	}

	public void setUI() {
		this.setTrainingUI();
		this.setTestUI();
		this.setResultUI();
		this.setActionResultUI();
		this.setClassificationResultUI();
		this.setClusteringResultUI();
		this.setRulResultUI();
		this.setDataCollectionResultUI();
	
	}


	private void setTrainingUI() {

		this.resultLabel = (TextArea) this.loader.getNamespace().get("resultLabel");
		this.trainingStart = (Button) this.loader.getNamespace().get("trainingStart");
		this.generateProvenance = (Button) this.loader.getNamespace().get("generateProvenance");
		this.provenanceImageButton = (Button) this.loader.getNamespace().get("provenanceImageButton");
		this.provenanceImageSvgButton = (Button) this.loader.getNamespace().get("provenanceImageSvgButton");
		this.provenanceImage = (ImageView) this.loader.getNamespace().get("provenanceImage");
		this.processImage = (ImageView) this.loader.getNamespace().get("processImage");
		this.processImage.setOnMouseClicked(event -> openImagePopup(this.processImage.getImage()));
       // this.stateLabel=(Label) this.loader.getNamespace().get("stateLabel");
      //  this.stateLabel.setText("Started");
        this.provenanceImageButton.setDisable(true);
        this.provenanceImageSvgButton.setDisable(true);
		
		this.trainingStart.setDisable(false);
		this.generateProvenance.setDisable(false);
		updateResult("\nTraining Started11!");
		this.trainingStart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
		

				Executors.newCachedThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						Controller.currentController.startSimulation();

					}
				});

			}
		});

		this.generateProvenance.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
	

				Executors.newCachedThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						Controller.currentController.generateProvenance(getSelectedDataId());

					}

					private int getSelectedDataId() {
						
						try {
						
						 for (int i = LogManager.logs.size() - 1; i >= 0; i--) {
					            LogMessage log = LogManager.logs.get(i);
					            // Check if the current LogMessage's Activity matches the specified activity
					            if (log.Activity== Enums.Activities.ALERT_GENERATION) {
					                return log.dataId; // Return the first LogMessage with the specified activity found
					            }
					        }
						}
						catch(Exception e){
							
							System.out.println(e.getStackTrace());
						}
						return 0;
					}
				});

			}
		});
		this.trainingStart.setDisable(false);
		this.generateProvenance.setDisable(true);
		
		
		  Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Welcome Message");
	        alert.setHeaderText(null);
	        alert.setContentText( "Welcome to the Simulation\n\n" +
	                "Flow Icon: Before starting the simulation, click the flow icon to review the predictive maintenance workflow.\n" +
	                "StartSimulation Button: Click the \"StartSimulation\" button to begin the simulation.\n" +
	                "Generate Provenance Button: Once the simulation is complete, the \"Generate Provenance\" button will become active. Use this button to generate the provenance.\n" +
	                "User Study Button: After generating the provenance, click the \"User Study\" button to answer the questions based on the shown graphs.\n" +
	                "Thank you for your support!");

	        // Uyarıyı göster
	        alert.showAndWait();
	}

	private Object openImagePopup(Image image) {
		
		//openImagePopup(new Image("D:\\Prove\\log.png"),new Image("D:\\Prove\\log.png"),new Image("D:\\Prove\\log.png"),null);
		
		// TODO Auto-generated method stub
	
		openImagePopupTask("dene \n me",new Image("D:\\Prove\\log2.png"));
	
		
		SurveyApp s=new SurveyApp();
		s.showImagePopup(image);
		
		return null;
	}

	@SuppressWarnings("unchecked")
	private void setTestUI() {

	}

	private void setResultUI() {

		this.resultLabel = (TextArea) this.loader.getNamespace().get("resultLabel");

	
	//	this.resultLabel.textProperty().addListener((ChangeListener<? super String>) new ChangeListener() {
     //       public void changed(ObservableValue ov, Object oldValue, Object newValue) {
               // resultLabel.setScrollTop(Double.MAX_VALUE);    //top
      //      resultLabel.setScrollTop(Double.MIN_VALUE);   //down
        //    }
                       
        //});
	}

	private void setClassificationResultUI() {
		this.classificationResultLabel = (TextArea) this.loader.getNamespace().get("classificationResultLabel");
	}

	private void setClusteringResultUI() {
		this.clusteringResultLabel = (TextArea) this.loader.getNamespace().get("clusteringResultLabel");
	}

	private void setRulResultUI() {
		this.rulResultLabel = (TextArea) this.loader.getNamespace().get("rulResultLabel");
	
	
	}

	private void setActionResultUI() {
		this.actionResultLabel =
		(TextArea)this.loader.getNamespace().get("actionResultLabel");
	}
	private void setDataCollectionResultUI() {
		this.dataCollectionResultLabel =
		(TextArea)this.loader.getNamespace().get("dataCollectionResultLabel");
	}
	
	

	private void startSimulation() {
try {

		System.out.println("Training Button Clicked");
		updateResult("\nClustering Started");
		updateClustering("\nClustering Started");
		NasaUnsupervised.startClustering();
		updateResult("\nClustering Ended");
		updateClustering("\nClustering Ended");
		updateResult("\nClassification Started");
		updateClassification("\nClassification Started");
		NasaSupervised.startClassification();
		updateResult("\nClassification Ended");
		updateClassification("\nClassification Ended");
		this.generateProvenance.setDisable(false);
		this.trainingStart.setDisable(true);
	//	generateProvenance(0);
		
}
catch(Exception e){
	
	System.out.println(e.getStackTrace());
}

	}
	
	private void generateProvenance(int dataId) {

		try {

         
		System.out.println("Provenance Generation Started");
		updateResult("\nProvenance Generation Started");
		
	String imageFile =	ProvenanceManager.GenerateProvenance(LogManager.logs,dataId, true);
	String logText =	ProvenanceManager.GenerateProvenanceLog(LogManager.logs,dataId, true);
		Image imageObject = new Image(imageFile+".png");
		
		this.provenanceImage.setImage(imageObject);

		 // Set up click event to open the image in a new window
		this.provenanceImage.setOnMouseClicked(event -> openImagePopup(new Image(imageFile+".png")));
		updateResult("\nProvenance Generation Ended");

		this.provenanceImageButton.setOnAction(event -> openImagePopupTask(logText,imageObject));
		this.provenanceImageSvgButton.setOnAction(event -> openImageFile(imageFile+".svg"));
	    this.provenanceImageButton.setDisable(false);
        this.provenanceImageSvgButton.setDisable(false);
		}
		catch(Exception e){
			
			System.out.println(e.getStackTrace());
		}

	}



	public static void updateResult(String newLine) {

		// Controller.currentController.lines.add(newLine);
		// Controller.currentController.lineLenghts.add(lineCount);

		// String tmpString = Controller.currentController.buildResultString();

		try {
		//	ImageTest imageZoomAndPan = new ImageTest();
		//	 Stage secondaryStage = new Stage();
	     //    imageZoomAndPan.start(secondaryStage);
	         
			Controller.currentController.resultLabel.appendText(newLine);
			Controller.currentController.resultLabel.appendText("\n");
			
	
			Controller.currentController.resultLabel.positionCaret(Controller.currentController.resultLabel.getLength());
		} catch (Exception e) {
			System.out.println("\n\nUI update error occured!\n");
		}

	}

	 
private void openImagePopupSvg(String image,String log,String imageNoExt) {
	  String[] svgPaths = {
		       image,
		        log,
		       imageNoExt
		    };
	SurveyAppSvg app=new SurveyAppSvg();
	Stage popupStage = new Stage();


	app.SetPaths(svgPaths);

	app.start(popupStage);
	/*
	 * // Create a new stage for the popup Stage popupStage = new Stage();
	 * 
	 * // Create an ImageView for the popup ImageView popupImageView = new
	 * ImageView(image); popupImageView.setPreserveRatio(true);
	 * 
	 * // Enable zooming on the popup image popupImageView.setOnScroll(event ->
	 * handleZoom(event, popupImageView));
	 * 
	 * // Set up the layout for the popup StackPane popupRoot = new
	 * StackPane(popupImageView); Scene popupScene = new Scene(popupRoot, 800, 600);
	 * 
	 * // Set the popup stage popupStage.setTitle("Image Popup");
	 * popupStage.setScene(popupScene); popupStage.show();
	 */
}
private void openImagePopupTask(String log,Image image) {
	
	SurveyAppTask app=new SurveyAppTask();
	Stage popupStage = new Stage();

	   
	   app.logString=log;
	   app.provenance=image;


	app.start(popupStage);}

private void openImagePopup(Image log,Image imageNoExt,Image image, String[] paths) {
	
	SurveyApp app=new SurveyApp();
	Stage popupStage = new Stage();
	   final Image[] images = {log,
			   imageNoExt,
		       image
		    };
	   
	   

	app.SetImages(images);
	app.SetPaths(paths);

	app.start(popupStage);
	/*
	 * // Create a new stage for the popup Stage popupStage = new Stage();
	 * 
	 * // Create an ImageView for the popup ImageView popupImageView = new
	 * ImageView(image); popupImageView.setPreserveRatio(true);
	 * 
	 * // Enable zooming on the popup image popupImageView.setOnScroll(event ->
	 * handleZoom(event, popupImageView));
	 * 
	 * // Set up the layout for the popup StackPane popupRoot = new
	 * StackPane(popupImageView); Scene popupScene = new Scene(popupRoot, 800, 600);
	 * 
	 * // Set the popup stage popupStage.setTitle("Image Popup");
	 * popupStage.setScene(popupScene); popupStage.show();
	 */
}


private void openImageFile(String file) {
    ProvenanceManager.OpenFile(file);
}
private void handleZoom(ScrollEvent event, ImageView imageView) {
    double zoomFactor = 1.05;
    double deltaY = event.getDeltaY();
    
    if (deltaY < 0) {
        zoomFactor = 0.95;
    }
    
    imageView.setScaleX(imageView.getScaleX() * zoomFactor);
    imageView.setScaleY(imageView.getScaleY() * zoomFactor);
}
	public static void updateClustering(String newLine) {

	

		try {
			Controller.currentController.clusteringResultLabel.appendText(newLine);
			Controller.currentController.clusteringResultLabel.appendText("\n");
		} catch (Exception e) {
			System.out.println("\n\nUI update error occured!\n");
		}

	}

	public static void updateClassification(String newLine) {



		try {
			Controller.currentController.classificationResultLabel.appendText(newLine);
			Controller.currentController.classificationResultLabel.appendText("\n");
		} catch (Exception e) {
			System.out.println("\n\nUI update error occured!\n");
		}

	}
	
	public static void updateState(String state) {



		try {
			//Controller.currentController.stateLabel.setText(state);
			
		} catch (Exception e) {
			System.out.println("\n\nUI update error occured!\n");
		}

	}

	public static void updateRUL(String newLine) {

		
		try {
			Controller.currentController.rulResultLabel.appendText(newLine);
			Controller.currentController.rulResultLabel.appendText("\n");
		} catch (Exception e) {
			System.out.println("\n\nUI update error occured!\n");
		}

	}

	public static void updateAction(String newLine) {



		try {
			Controller.currentController.actionResultLabel.appendText(newLine);
			Controller.currentController.actionResultLabel.appendText("\n");
		} catch (Exception e) {
			System.out.println("\n\nUI update error occured!\n");
		}

	}
	
	public static void updateDataCollection(String newLine, boolean add) {



		try {
			if(add) {
			Controller.currentController.dataCollectionResultLabel.appendText(newLine);
			}else {
				
				Controller.currentController.dataCollectionResultLabel.setText(newLine);
			}

		} catch (Exception e) {
			System.out.println("\n\nUI update error occured!\n");
		}

	}
	
	public static String getDataCollectionText() {



		try {
			return			Controller.currentController.dataCollectionResultLabel.getText();
			
		} catch (Exception e) {
			System.out.println("\n\nUI update error occured!\n");
			return "";
		}

	}
	
	


}
