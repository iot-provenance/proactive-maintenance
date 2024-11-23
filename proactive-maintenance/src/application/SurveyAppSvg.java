package application;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import dto.UserDTO;
import dto.UserResponseDTO;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.batik.swing.JSVGCanvas;

public class SurveyAppSvg extends Application {

    private double zoomFactor = 1.0;
    private int currentCategoryIndex = 0;
    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<UserResponseDTO> userResponses = new ArrayList<>();
    private DataReaderLite dataReader = new DataReaderLite(1);

    private final String[][] categories = {
        {
            "The provenance information helps me understand the origin and history of data used in the predictive maintenance system.",
            "The provenance information enhances the explainability of the predictive maintenance system's decisions.",
            "I find the provenance information crucial for ensuring transparency in the predictive maintenance process."
        },
        {
            "Tracking provenance through logs is challenging and time-consuming.",
            "The provenance graph simplifies understanding the data lineage compared to traditional logs.",
            "Using the provenance graph makes it easier to identify the reasons behind system actions."
        },
        {
            "The extended provenance schema provides more detailed information than the standard PROV-O schema.",
            "I find the custom attributes in the extended provenance schema useful for my specific needs.",
            "The extended provenance schema improves the accountability of the predictive maintenance system.",
            "Overall, the extended provenance schema enhances my trust in the system's outputs."
        }
    };

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
        String svgPath = svgPaths[currentCategoryIndex];

        // Create a SwingNode to host the JSVGCanvas
        SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode, svgPath);

        // Create a ScrollPane to allow scrolling
        ScrollPane imageScrollPane = new ScrollPane();
        imageScrollPane.setContent(swingNode);
        
        // Create survey questions
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
                    System.out.println("User Created! Id:" + userId);
                    for (UserResponseDTO response : userResponses) {
                        response.setUserId(userId);
                        dataReader.insertUserResponse(response);
                    }
                    Alert warning = new Alert(AlertType.WARNING);
                    warning.setTitle("User Study Completed");
                    warning.setHeaderText("The user study completed! Thank you " + username + " for your contribution!");
                    warning.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                primaryStage.close(); // Close the application after the last category
            }
        });

        surveyBox.getChildren().add(nextButton);

        ScrollPane surveyScrollPane = new ScrollPane();
        surveyScrollPane.setContent(surveyBox);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(imageScrollPane, surveyScrollPane);
        splitPane.setDividerPositions(0.5);

        Scene scene = new Scene(splitPane, 1600, 1200);

        // Make the main window fullscreen and maintain fullscreen mode
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);

        primaryStage.setTitle("Survey Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createAndSetSwingContent(SwingNode swingNode, String svgPath) {
        SwingUtilities.invokeLater(() -> {
            JSVGCanvas svgCanvas = new JSVGCanvas();
            svgCanvas.setURI(svgPath);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(svgCanvas, BorderLayout.CENTER);

			/*
			 * svgCanvas.addMouseWheelListener(e -> { if (e.getPreciseWheelRotation() < 0) {
			 * svgCanvas.setScaleFactor(svgCanvas.setsc * 1.1); } else {
			 * svgCanvas.setScaleFactor(svgCanvas.getScaleFactor() / 1.1); } });
			 */

            swingNode.setContent(panel);
        });
    }

    private boolean allQuestionsAnswered(VBox surveyBox) {
        for (int i = 0; i < surveyBox.getChildren().size() - 1; i++) {
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
    	
    	
        for (int i = 0; i < surveyBox.getChildren().size() - 1; i++) {
            VBox questionBox = (VBox) surveyBox.getChildren().get(i);
            Label questionLabel = (Label) questionBox.getChildren().get(0);
            HBox optionsBox = (HBox) questionBox.getChildren().get(1);
            for (int j = 0; j < optionsBox.getChildren().size(); j++) {
                RadioButton radioButton = (RadioButton) optionsBox.getChildren().get(j);
                if (radioButton.isSelected()) {
                    userResponses.add(new UserResponseDTO(0, currentCategoryIndex * 3 + i + 1, j + 1)); // user_id will be set later
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
