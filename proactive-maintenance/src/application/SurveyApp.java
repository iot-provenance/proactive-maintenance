package application;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.StackPane;
import dto.UserDTO;
import dto.UserResponseDTO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import provenance.ProvenanceManager;

public class SurveyApp extends Application {

    private double zoomFactor = 1.0;
    private int currentCategoryIndex = 0;
    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<UserResponseDTO> userResponses = new ArrayList<>();
    private DataReaderLite dataReader = new DataReaderLite(1);

    private final String[][] categories = {
    	    {
    	    	"The displayed information shows the activities in this system.",
    	    	"The displayed information shows thbe agents and relations in this system.",
    	    	"The displayed information monitors the overall operation of the predictive maintenance process.",
    	    	"The displayed information shows the data relationships and connections in the predictive maintenance process.",
    	    	"The displayed information tracks data movements in the predictive maintenance process.",
    	    	"The displayed information shows the source of the data used in the predictive maintenance process.",
    	    	"The displayed information shows the history of the data used in the predictive maintenance process.",
    	    	"The displayed information provides an overall assessment of the system's performance and effectiveness.",
    	    	"The displayed information provides information about the start and end times of the activities.",
    	    	"The displayed information informs about the algorithm (modelName) used in creating the CLUSTERING MODEL.",
    	    	"The displayed information informs about the algorithm (modelName) used in creating the CLASSIFICATION MODEL.",
    	    	"The displayed information informs about the algorithm (modelName) used in creating the RUL MODEL.",
    	    	"The displayed information provides the framework, model type, and version used in creating the CLUSTERING MODEL.",
    	    	"The displayed information provides the framework, model type, and version used in creating the CLASSIFICATION MODEL.",
    	    	"The displayed information provides the framework, model type, and version used in creating the RUL MODEL.",
    	    	"The displayed information informs about the training data size and testing data size used in creating the CLUSTERING MODEL.",
    	    	"The displayed information informs about the training data size and testing data size used in creating the CLASSIFICATION MODEL.",
    	    	"The displayed information informs about the training data size and testing data size used in creating the RUL MODEL.",
    	    	"The displayed information provides information about the hyperparameters used in the models.",
    	    	"The displayed information informs about the success values (vMeasure, Silhouette) of the CLUSTERING MODEL creation activity.",
    	    	"The displayed information informs about the success values (accuracy, precision, f1, recall) of the CLASSIFICATION MODEL creation activity.",
    	    	"The displayed information informs about the success values (MSE, RMSE, MAE, MAPE) of the RUL MODEL creation activity.",
    	    	"The displayed information provides information about the action to be taken, action target, action time, risk assessment, and who will take the action.",
    	    	"The displayed information identifies the reasons behind the system's actions.",
    	    	"The displayed information ensures transparency and traceability in the predictive maintenance process."    },
    	    {
    	    		"The displayed information shows the activities in this system.",
        	    	"The displayed information shows the agents and relations in this system.",
        	    	"The displayed information monitors the overall operation of the predictive maintenance process.",
        	    	"The displayed information shows the data relationships and connections in the predictive maintenance process.",
        	    	"The displayed information tracks data movements in the predictive maintenance process.",
        	    	"The displayed information shows the source of the data used in the predictive maintenance process.",
        	    	"The displayed information shows the history of the data used in the predictive maintenance process.",
        	    	"The displayed information provides an overall assessment of the system's performance and effectiveness.",
        	    	"The displayed information provides information about the start and end times of the activities.",
        	    	"The displayed information informs about the algorithm (modelName) used in creating the CLUSTERING MODEL.",
        	    	"The displayed information informs about the algorithm (modelName) used in creating the CLASSIFICATION MODEL.",
        	    	"The displayed information informs about the algorithm (modelName) used in creating the RUL MODEL.",
        	    	"The displayed information provides the framework, model type, and version used in creating the CLUSTERING MODEL.",
        	    	"The displayed information provides the framework, model type, and version used in creating the CLASSIFICATION MODEL.",
        	    	"The displayed information provides the framework, model type, and version used in creating the RUL MODEL.",
        	    	"The displayed information informs about the training data size and testing data size used in creating the CLUSTERING MODEL.",
        	    	"The displayed information informs about the training data size and testing data size used in creating the CLASSIFICATION MODEL.",
        	    	"The displayed information informs about the training data size and testing data size used in creating the RUL MODEL.",
        	    	"The displayed information provides information about the hyperparameters used in the models.",
        	    	"The displayed information informs about the success values (vMeasure, Silhouette) of the CLUSTERING MODEL creation activity.",
        	    	"The displayed information informs about the success values (accuracy, precision, f1, recall) of the CLASSIFICATION MODEL creation activity.",
        	    	"The displayed information informs about the success values (MSE, RMSE, MAE, MAPE) of the RUL MODEL creation activity.",
        	    	"The displayed information provides information about the action to be taken, action target, action time, risk assessment, and who will take the action.",
        	    	"The displayed information identifies the reasons behind the system's actions.",
        	    	"The displayed information ensures transparency and traceability in the predictive maintenance process."  
    	    },
    	    {
    	    	"The displayed information shows the activities in this system.",
    	    	"The displayed information shows the agents and relations in this system.",
    	    	"The displayed information monitors the overall operation of the predictive maintenance process.",
    	    	"The displayed information shows the data relationships and connections in the predictive maintenance process.",
    	    	"The displayed information tracks data movements in the predictive maintenance process.",
    	    	"The displayed information shows the source of the data used in the predictive maintenance process.",
    	    	"The displayed information shows the history of the data used in the predictive maintenance process.",
    	    	"The displayed information provides an overall assessment of the system's performance and effectiveness.",
    	    	"The displayed information provides information about the start and end times of the activities.",
    	    	"The displayed information informs about the algorithm (modelName) used in creating the CLUSTERING MODEL.",
    	    	"The displayed information informs about the algorithm (modelName) used in creating the CLASSIFICATION MODEL.",
    	    	"The displayed information informs about the algorithm (modelName) used in creating the RUL MODEL.",
    	    	"The displayed information provides the framework, model type, and version used in creating the CLUSTERING MODEL.",
    	    	"The displayed information provides the framework, model type, and version used in creating the CLASSIFICATION MODEL.",
    	    	"The displayed information provides the framework, model type, and version used in creating the RUL MODEL.",
    	    	"The displayed information informs about the training data size and testing data size used in creating the CLUSTERING MODEL.",
    	    	"The displayed information informs about the training data size and testing data size used in creating the CLASSIFICATION MODEL.",
    	    	"The displayed information informs about the training data size and testing data size used in creating the RUL MODEL.",
    	    	"The displayed information provides information about the hyperparameters used in the models.",
    	    	"The displayed information informs about the success values (vMeasure, Silhouette) of the CLUSTERING MODEL creation activity.",
    	    	"The displayed information informs about the success values (accuracy, precision, f1, recall) of the CLASSIFICATION MODEL creation activity.",
    	    	"The displayed information informs about the success values (MSE, RMSE, MAE, MAPE) of the RUL MODEL creation activity.",
    	    	"The displayed information provides information about the action to be taken, action target, action time, risk assessment, and who will take the action.",
    	    	"The displayed information identifies the reasons behind the system's actions.",
    	    	"The displayed information ensures transparency and traceability in the predictive maintenance process."  
    	    }
    	};

    private Image[] images = {
            new Image("file:path_to_provenance_image.jpg"),
            new Image("file:path_to_logs_image.jpg"),
            new Image("file:path_to_extended_schema_image.jpg")
        };

        public void SetImages(Image[] _images) {
        	images=_images;
        }

        private String[] svgPaths = {
                "file:path_to_provenance_image.svg",
                "file:path_to_logs_image.svg",
                "file:path_to_extended_schema_image.svg"
            };

        public void SetPaths(String[] paths) {
        	
        	svgPaths=paths;
        }
    @Override
    public void start(Stage primaryStage) {
        showInitialPopup(primaryStage);
    }

    private void showInitialPopup(Stage primaryStage) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Welcome to the Survey");
        alert.setResizable(true);
        alert.setHeaderText("Please read the following instructions:");

        // Create a GridPane to hold the instruction text and the username field
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Add instruction text
        Label instructionLabel = new Label("Please answer the survey questions based on the simulation and the graphical information provided. Please be objective in your responses.");
        grid.add(instructionLabel, 0, 0, 2, 1);

        // Add username field
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);

        alert.getDialogPane().setContent(grid);

        // Make the alert dialog fullscreen
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setMaximized(true);

        alert.showAndWait();

        username = usernameField.getText();
        if (username == null || username.isEmpty()) {
            showInitialPopup(primaryStage); // Show the popup again if username is empty
        } else {
            startTime = LocalDateTime.now();
            showCategory(primaryStage);
        }
    }

    private void showCategory(Stage primaryStage) {
       

        String[] questions = categories[currentCategoryIndex];
        Image image = images[currentCategoryIndex];

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        // Allow zooming
        imageView.setOnScroll((ScrollEvent event) -> {
            if (event.getDeltaY() > 0) {
                zoomFactor *= 1.1;
            } else {
                zoomFactor /= 1.1;
            }
            imageView.setFitWidth(image.getWidth() * zoomFactor);
        });
        
        

        ScrollPane imageScrollPane = new ScrollPane();
        imageScrollPane.setContent(imageView);

        VBox surveyBox = new VBox(10);
        surveyBox.setPadding(new Insets(10));

        for (String question : questions) {
            Label questionLabel = new Label(question);
            ToggleGroup group = new ToggleGroup();
            HBox optionsBox = new HBox(10);

            String[] options = {"Strongly Disagree", "Disagree", "Neutral", "Agree", "Strongly Agree"};
            for (String option : options) {
                RadioButton radioButton = new RadioButton(option);
                radioButton.setToggleGroup(group);
                optionsBox.getChildren().add(radioButton);
            }

            VBox questionBox = new VBox(5, questionLabel, optionsBox);
            surveyBox.getChildren().add(questionBox);
        }

        Button nextButton = new Button(currentCategoryIndex < categories.length - 1 ? "Next" : "Complete");
        nextButton.setOnAction(event -> {
            if (!allQuestionsAnswered(surveyBox)) {
                Alert warning = new Alert(AlertType.WARNING);
                warning.setTitle("Incomplete Survey");
                warning.setHeaderText("Please answer all questions before proceeding.");
                warning.showAndWait();
                return;
            }

            saveResponses(surveyBox);

            currentCategoryIndex++;
            if (currentCategoryIndex < categories.length) {
                showCategory(primaryStage);
            } else {
            	
     
                    endTime = LocalDateTime.now();
                    try {
                        int userId = dataReader.insertUser(new UserDTO(username, startTime, endTime));
                        System.out.println("User Created! Id:"+ userId);
                   
                        for (UserResponseDTO response : userResponses) {
                            response.setUserId(userId);
                            dataReader.insertUserResponse(response);
                      
                        }
                        
                        Alert warning = new Alert(AlertType.WARNING);
                        warning.setTitle("User Study Completed");
                        warning.setHeaderText("The user study completed! Thank you "+username+" for your contribution!");
                        warning.showAndWait();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                   
                
                primaryStage.close(); // Close the application after the last category
            }
        });

        surveyBox.getChildren().add(nextButton);
        
        Button openSvgButton = new Button( "Open File in New Window");
        openSvgButton.setOnAction(event -> {
        	 ProvenanceManager.OpenFile(svgPaths[currentCategoryIndex]);
            });
        
        surveyBox.getChildren().add(openSvgButton);

        ScrollPane surveyScrollPane = new ScrollPane();
        surveyScrollPane.setContent(surveyBox);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(imageScrollPane, surveyScrollPane);
        splitPane.setDividerPositions(0.5);

        Scene scene = new Scene(splitPane, 1600, 1200);

        // Make the main window fullscreen and maintain fullscreen mode
        primaryStage.setMaximized(true);
        //primaryStage.setFullScreen(true);

        primaryStage.setTitle("Survey Application");
        primaryStage.setScene(scene);
        primaryStage.show();
        

    }

    private boolean allQuestionsAnswered(VBox surveyBox) {
        for (int i = 0; i < surveyBox.getChildren().size() - 2; i++) {
    
            VBox questionBox = (VBox) surveyBox.getChildren().get(i);
            HBox optionsBox = (HBox) questionBox.getChildren().get(1);
            boolean answered = false;
            for (int j = 0; j < optionsBox.getChildren().size(); j++) {
                RadioButton radioButton = (RadioButton) optionsBox.getChildren().get(j);
                if (radioButton.isSelected()) {
                    answered = true;
                    break;
                }
            }
            if (!answered) {
                return false;
            }
        }
        return true;
    }

    private void saveResponses(VBox surveyBox) {
    	
    	
        for (int i = 0; i < surveyBox.getChildren().size() - 2; i++) {
            VBox questionBox = (VBox) surveyBox.getChildren().get(i);
            Label questionLabel = (Label) questionBox.getChildren().get(0);
            HBox optionsBox = (HBox) questionBox.getChildren().get(1);
            for (int j = 0; j < optionsBox.getChildren().size(); j++) {
                RadioButton radioButton = (RadioButton) optionsBox.getChildren().get(j);
                if (radioButton.isSelected()) {
                    userResponses.add(new UserResponseDTO(currentCategoryIndex, i + 1, j + 1)); // user_id will be set later
                    break;
                }
            }
        }
    }
    
    public void showImagePopup(Image image) {
        // Resmi bir ImageView'e ekle
        ImageView imageView = new ImageView(image);
        
        // Bir StackPane oluştur ve ImageView'i ekle
        StackPane root = new StackPane();
        root.getChildren().add(imageView);
        
        // Popup penceresi oluştur
        Stage popupStage = new Stage();
        popupStage.setTitle("Image Popup");
        popupStage.setScene(new Scene(root, image.getWidth(), image.getHeight()));
        popupStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
