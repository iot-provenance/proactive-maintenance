package application;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.StackPane;
import dto.UserDTO;
import dto.UserResponseDTO;
import dto.UserTaskDTO;
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

public class SurveyAppTask extends Application {

    private double zoomFactor = 1.0;
    private int currentTaskIndex = 0;
    private int currentTaskQuestionIndex = 0;
    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<UserResponseDTO> userResponses = new ArrayList<>();
    private List<UserTaskDTO> userTasks = new ArrayList<>();
    private DataReaderLite dataReader = new DataReaderLite(1);
    private LocalDateTime taskStartTime;
    private LocalDateTime taskEndTime;
    private boolean isLogTask = true; // Flag to switch between log and provenance tasks
    public Image provenance = null;
    VBox questionBox;
    public String logString;

    private final String[][] taskCategories = {
        {
            "Traceability Task: Identify the origin of Processed Data 1",
            "Traceability Task: Trace the movement of data from its source to its final usage in a Classification Model Generation Activity.",
            "Traceability Task: Determine which agents interacted during the Action Execution Activity.",
            "Traceability Task: Find the sequence of activities that led to Stop Machine Action",
            "Traceability Task: Identify the start and end times of all activities involved in the Stop Machine Action."
        },
        {
            "Visualization Task: See/Find the entire history of data usage for Stop Machine Action.",
            "Visualization Task: See/Find when and how each piece of data was used in Stop Machine Action",
            "Visualization Task: See/Finda ll maintenance actions taken.",
            "Visualization Task: See/Find the connections and relationships between different entities in the Rul Model Generation Activity.",
            "Visualization Task: See/Find the data flow and transformations during the Failure Prediction Activity."
        },
        {
            "Explainability Task: Find the AI model name used for creating the latest clustering model in the maintenance process.",
            "Explainability Task: Describe the framework, model type, and version used for the last classification model.",
            "Explainability Task: Describe the training and testing data sizes for the latest RUL model.",
            "Explainability Task: List the hyperparameters used in the latest AI models for predictive maintenance.",
            "Explainability Task: Explain the performance metrics (accuracy, precision, f1, recall) for the last classification model."
        },
        {
            "Please answer the Questions"
        }
    };

    private final String[] likertQuestions = {
        "The presented information is easy to understand, and I was able to complete the given tasks easily and quickly.",
        "The presented information helps me track the data flow clearly.",
        "I can easily identify the reasons for actions taken by the system.",
        "The system's decision-making process is transparent.",
        "I can easily see the performance information of the generated models.",
        "I can easily track activities and data changes in the system.",
        "I can quickly find the source of any data or decision.",
        "I have confidence in the system's accountability.",
        "The presented information enhances my trust in the system.",
        "Overall, the presented information improves my experience with the proactive maintenance system."
    };

    private Image logImage = new Image("file:path_to_logs_image.jpg");
    private Image provenanceImage = new Image("D:\\Prove\\prov5.png");

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
        Label instructionLabel = new Label("Please perform the tasks based on the simulation and the graphical information provided. Please be objective in your responses.");
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
            taskStartTime = LocalDateTime.now();
            showTask(primaryStage);
        }
    }

    private void showTask(Stage primaryStage) {
        if (currentTaskIndex < taskCategories.length * taskCategories[0].length) {
            String task = taskCategories[currentTaskIndex / taskCategories[0].length][currentTaskIndex % taskCategories[0].length];
            Image image = provenance != null ? provenance : provenanceImage;

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
            if (isLogTask) {

                Label text = new Label();
                text.setText(logString);
                imageScrollPane.setContent(text);
            } else {
                imageScrollPane.setContent(imageView);
            }
            VBox taskBox = new VBox(10);
            taskBox.setPadding(new Insets(10));

            Label taskLabel = new Label(task);
            taskBox.getChildren().add(taskLabel);

            Button nextButton = new Button("Next");
            nextButton.setOnAction(event -> {
                taskEndTime = LocalDateTime.now();
                Duration duration = Duration.between(taskStartTime, taskEndTime);
                currentTaskQuestionIndex++;
                saveTaskTime(task, duration);
                taskStartTime = LocalDateTime.now();
                currentTaskIndex++;

                if (currentTaskQuestionIndex == 15) {
                    addLikertQuestions(taskBox);
                } else if (currentTaskQuestionIndex > 15) {
                    if (!allQuestionsAnswered(taskBox)) {
                        Alert warning = new Alert(AlertType.WARNING);
                        warning.setTitle("Incomplete Survey");
                        warning.setHeaderText("Please answer all questions before proceeding.");
                        warning.showAndWait();
                        return;
                    }

                    saveResponses(taskBox);

                    if (isLogTask) {
                        isLogTask = false;
                        currentTaskIndex = 0;
                        currentTaskQuestionIndex = 0;
                        showTask(primaryStage);
                    } else {
                        completeSurvey(primaryStage);
                    }
                } else {
                    showTask(primaryStage);
                }
            });

            taskBox.getChildren().add(nextButton);

            ScrollPane taskScrollPane = new ScrollPane();
            taskScrollPane.setContent(taskBox);

            SplitPane splitPane = new SplitPane();
            splitPane.getItems().addAll(imageScrollPane, taskScrollPane);
            splitPane.setDividerPositions(0.5);

            Scene scene = new Scene(splitPane);

            // Make the main window fullscreen and maintain fullscreen mode
            primaryStage.setMaximized(false);

            primaryStage.setTitle("Survey Application");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        }
    }

    private void saveTaskTime(String task, Duration duration) {
        UserTaskDTO d = new UserTaskDTO();
        d.setTaskId(currentTaskIndex);
        d.setResponseTime(duration.toMillis());
        d.setTaskQuestionNumber(currentTaskQuestionIndex);
        userTasks.add(d);
    }

    private void addLikertQuestions(VBox taskBox) {
        for (String question : likertQuestions) {
            Label questionLabel = new Label(question);
            ToggleGroup group = new ToggleGroup();
            HBox optionsBox = new HBox(10);

            String[] options = {"Strongly Disagree", "Disagree", "Neutral", "Agree", "Strongly Agree"};
            for (String option : options) {
                RadioButton radioButton = new RadioButton(option);
                radioButton.setToggleGroup(group);
                optionsBox.getChildren().add(radioButton);
            }

            questionBox = new VBox(5, questionLabel, optionsBox);
            taskBox.getChildren().add(questionBox);
        }
    }

    private boolean allQuestionsAnswered(VBox surveyBox) {
        for (int i = 0; i < surveyBox.getChildren().size(); i++) {
            if (surveyBox.getChildren().get(i) instanceof VBox) {
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
        }
        return true;
    }

    private void saveResponses(VBox surveyBox) {
    	int nonQuestion=0;
        for (int i = 0; i < surveyBox.getChildren().size(); i++) {
            if (surveyBox.getChildren().get(i) instanceof VBox) {
                VBox questionBox = (VBox) surveyBox.getChildren().get(i);
                HBox optionsBox = (HBox) questionBox.getChildren().get(1);
                for (int j = 0; j < optionsBox.getChildren().size(); j++) {
                    RadioButton radioButton = (RadioButton) optionsBox.getChildren().get(j);
                    if (radioButton.isSelected()) {
                        userResponses.add(new UserResponseDTO(isLogTask ? 1 : 2, i + 1-nonQuestion, j + 1)); // user_id will be set later
                        break;
                    }
                }
            }else {nonQuestion++;}
        }
    }

    private void completeSurvey(Stage primaryStage) {
        endTime = LocalDateTime.now();
        try {
            int userId = dataReader.insertUser(new UserDTO(username, startTime, endTime));
            System.out.println("User Created! Id:" + userId);

            for (UserResponseDTO response : userResponses) {
                response.setUserId(userId);
                dataReader.insertUserResponse(response);
            }

            for (UserTaskDTO task : userTasks) {
                task.setUserId(userId);
                dataReader.insertUserTask(task);
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("User Study Completed");
            alert.setHeaderText("The user study is completed! Thank you " + username + " for your contribution!");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        primaryStage.close(); // Close the application after the last task
    }

    public static void main(String[] args) {
        launch(args);
    }
}
