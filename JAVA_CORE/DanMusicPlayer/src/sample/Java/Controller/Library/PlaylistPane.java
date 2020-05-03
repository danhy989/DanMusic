package sample.Java.Controller.Library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import sample.Java.Controller.AlbumPane;
import sample.Java.DTO.Album;
import sample.Java.Service.AlbumService;
import sample.Java.Static.DataStaticLoader;
import sample.Java.entities.AlbumEntity;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class PlaylistPane extends BorderPane implements Initializable {

    private static final int MAX_ITEMS_ON_ONE_ROW = 4;

    @FXML
    private ScrollPane scrollPane;

    public PlaylistPane(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../Resources/Fxml/Library/LibListPane.fxml"));

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
        List<AlbumEntity> albumEntityList = null;
        try {
            albumEntityList = DataStaticLoader.getAlbumEntityDao().getRandomAlbum(12);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        double countRow = 0;
        if(albumEntityList.size()<MAX_ITEMS_ON_ONE_ROW) {
            countRow += 1;
        }else{
            countRow = (double) albumEntityList.size()/MAX_ITEMS_ON_ONE_ROW;
            double check = countRow - (int)countRow;
            if(check!=0){
                countRow = (int)countRow+1;
            }
        }
        List<Album> albumList = AlbumService.getInstance().convertFromAlbumDao(albumEntityList);
        int numAlbum = 0;
        VBox vBox = new VBox();
        for(int row=0;row<countRow;row++){
            ObservableList<AlbumPane> albumPanes = FXCollections.observableArrayList();
            for(int col=0;col<MAX_ITEMS_ON_ONE_ROW;col++){
                if(numAlbum >= albumList.size()){
                    break;
                }
                AlbumPane albumPane = new AlbumPane();
                File fileImage = new File(albumList.get(numAlbum).getPathImage());
                albumPane.getAlbumImageView().setImage( new Image(fileImage.toURI().toString()));
                albumPane.getAlbumSingle().setText(albumList.get(numAlbum).getSingle());
                albumPane.getAlbumName().setText(albumList.get(numAlbum).getName());
                albumPane.setAlbum(albumList.get(numAlbum));
                albumPanes.add(albumPane);
                numAlbum++;
            }
            vBox.getChildren().add(new GridPaneItem(albumPanes,row));
        }
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(10,10,10,10));
        scrollPane.setContent(vBox);
    }
}
