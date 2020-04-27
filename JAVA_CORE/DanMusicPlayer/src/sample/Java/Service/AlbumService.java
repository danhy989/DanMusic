package sample.Java.Service;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sample.Java.Controller.AlbumPane;
import sample.Java.Controller.TrackPane;
import sample.Java.DTO.Albums;

import java.util.List;

public class AlbumService {
    private static final AlbumService instance = new AlbumService();
    private AlbumService(){ }
    public static AlbumService getInstance(){
        return instance;
    }

    public static void setInstance(ScrollPane albumListScrollPane){
        instance.albumListScrollPane = albumListScrollPane;
    }

    ScrollPane albumListScrollPane;

    public  void setContentAlbums(List<Albums> albums){
        albumListScrollPane.setContent(null);
        HBox hBox = new HBox();

        for(int i=0;i<albums.size();i++){
            AlbumPane albumPane = new AlbumPane();
            albumPane.getAlbumImageView().setImage(new Image(albums.get(i).getPathImage()));
            albumPane.getAlbumName().setText(albums.get(i).getName());
            albumPane.getAlbumSingle().setText(albums.get(i).getSingle());
            albumPane.setAlbums(albums.get(i));
            hBox.getChildren().add(albumPane);
        }
        albumListScrollPane.setPadding(new Insets(5,5,5,5));
        albumListScrollPane.setFitToHeight(true);
        albumListScrollPane.setContent(hBox);
        albumListScrollPane.setPannable(true);
    }

}
