package application;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ImageTest extends Application {

    private static final String IMAGE_URL = "D:\\Prove\\tes1981.png"; // Örnek bir resim URL'si



        @Override
        public void start(Stage primaryStage) {
            // Resim için ImageView oluştur
            ImageView imageView = new ImageView();
            imageView.setPreserveRatio(true);

            // ScrollPane oluştur ve ImageView'i içeriğine ekle
            ScrollPane scrollPane = new ScrollPane(imageView);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);

            // Zoom in ve Zoom out butonları oluştur
            Button zoomInButton = new Button("Zoom In");
            zoomInButton.setOnAction(e -> imageView.setScaleX(imageView.getScaleX() * 1.1));

            Button zoomOutButton = new Button("Zoom Out");
            zoomOutButton.setOnAction(e -> imageView.setScaleX(imageView.getScaleX() / 1.1));

            // Layout oluştur
            HBox buttonBox = new HBox(10, zoomInButton, zoomOutButton);
            buttonBox.setAlignment(Pos.CENTER);
            BorderPane root = new BorderPane(scrollPane);
            root.setTop(buttonBox);

            // Scene oluştur ve sahneyi göster
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Resim Zoom ve Pan (Scroll ve Button ile)");
            primaryStage.show();

            // Resmi yükle
            Image image = new Image(IMAGE_URL);
            imageView.setImage(image);
        }

        public static void main(String[] args) {
            launch(args);
        }
    }
