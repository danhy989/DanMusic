package sample.Java.Controller.Library;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import sample.Java.Controller.AlbumPane;

import java.io.IOException;

public class GridPaneItem extends GridPane {

    GridPaneItem(ObservableList<AlbumPane> albumPane, int row){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../Resources/Fxml/Library/GridPaneItem.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

            for(int col=0;col<albumPane.size();col++){
                this.add(albumPane.get(col),col,row);
            }

            this.setPrefHeight(albumPane.get(0).getAlbumImageView().getFitHeight());
            System.out.println(this.getPrefHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
