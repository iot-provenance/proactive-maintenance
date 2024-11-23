package application;
	

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		System.out.println("Main Started");
		try {
			//AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("StartPage.fxml"));		
			Parent root = loader.load();
			
			
		        loader.setController(new Controller(loader));
			
			 // Controller'ı doğrudan FXML loader'dan al
		    Controller controller = loader.getController();
		    if (controller != null) {
		        controller.setUI();
		    } else {
		        System.err.println("Controller is null");
		    }

		    // Parent tipinden AnchorPane'e güvenli bir şekilde cast et
		    if (root instanceof AnchorPane) {
		        Scene scene = new Scene((AnchorPane) root, 1200, 800);
		        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		        primaryStage.setResizable(true);
		        primaryStage.setTitle("Proactive Maintenance Simulation");
		        primaryStage.setScene(scene);
		        primaryStage.show();
		    } else {
		        System.err.println("Root is not an instance of AnchorPane");
		    }
		} catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

