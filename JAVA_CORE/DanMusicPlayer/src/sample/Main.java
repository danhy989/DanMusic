package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Java.Controller.MainController;
import sample.Java.Controller.SceneMusicPlayingController;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application  {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Resources/Fxml/Main.fxml"));
        MainController.setMainStage(primaryStage);

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX()-xOffset);
                primaryStage.setY(event.getScreenY()-yOffset);
            }
        });
        root.getStylesheets().add(getClass().getResource("Resources/Css/colors.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("Resources/Css/sliderbar.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("Resources/Css/hover.css").toExternalForm());
        primaryStage.setTitle("DanMusic Player");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
