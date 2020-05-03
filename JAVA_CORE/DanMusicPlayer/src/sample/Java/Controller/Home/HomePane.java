package sample.Java.Controller.Home;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import sample.Java.Service.AlbumService;
import sample.Java.Service.PlaylistService;
import sample.Java.Service.SingleService;
import sample.Java.Util.EffectUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePane extends VBox implements Initializable {
    @FXML
    private ScrollPane trackListScrollPane,albumListScrollPane,singleScrollPane;
    public HomePane(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../Resources/Fxml/Home/homePane.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PlaylistService.setInstance(trackListScrollPane);
        AlbumService.setInstance(albumListScrollPane);
        SingleService.setInstance(singleScrollPane);
    }
}
