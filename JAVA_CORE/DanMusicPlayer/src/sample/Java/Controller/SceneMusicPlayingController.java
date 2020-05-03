package sample.Java.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SceneMusicPlayingController extends BorderPane implements Initializable {

    @FXML
    private ImageView imageView;

    public SceneMusicPlayingController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Resources/Fxml/SceneMusicPlaying.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            this.getStylesheets().add(getClass().getResource("../../Resources/Css/musicPlayingTransfer.css").toExternalForm());
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //File fileImage = new File();
        int a = 3;

    }
}
