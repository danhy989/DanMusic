package sample.Java.Service;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import sample.Java.Controller.TrackPane;
import sample.Java.DTO.Album;

import java.io.File;

public class PlaylistService {
    private static final PlaylistService instance = new PlaylistService();
    private PlaylistService(){ }
    public static PlaylistService getInstance(){
        return instance;
    }

    public static void setInstance(ScrollPane trackListScrollPane){
        instance.trackListScrollPane = trackListScrollPane;
    }

    ScrollPane trackListScrollPane;

    public ScrollPane getTrackListScrollPane() {
        return trackListScrollPane;
    }

    public void setContentPlaylist(Album album){
        if(album != null){
            trackListScrollPane.setContent(null);
            VBox vBox = new VBox();
            for(int i = 0; i< album.getTracks().size(); i++){
                TrackPane trackPane = new TrackPane();
                trackPane.getTrackNumOrder().setText(String.valueOf(i+1));
                File fileImage = new File(album.getTracks().get(i).getImageTrack());
                trackPane.getImageView().setImage(new Image(fileImage.toURI().toString()));
                trackPane.getTrackName().setText(album.getTracks().get(i).getName());
                trackPane.getTrackTotalDuration().setText(album.getTracks().get(i).getDuration());
                trackPane.setTrack(album.getTracks().get(i));
                trackPane.setAlbum(album);
                vBox.getChildren().add(trackPane);
            }
            vBox.setPadding(new Insets(0,10,0,10));
            trackListScrollPane.setContent(vBox);
            trackListScrollPane.setPannable(true);
        }

    }
}
