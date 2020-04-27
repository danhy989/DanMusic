package sample.Java.Service;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import sample.Java.Controller.TrackPane;
import sample.Java.DTO.Albums;

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

    public void setContentPlaylist(Albums albums){
        trackListScrollPane.setContent(null);
        VBox vBox = new VBox();
        for(int i=0;i<albums.getTracks().size();i++){
            TrackPane trackPane = new TrackPane();
            trackPane.getTrackNumOrder().setText(String.valueOf(i+1));
            trackPane.getTrackName().setText(albums.getTracks().get(i).getName());
            trackPane.getTrackTotalDuration().setText(albums.getTracks().get(i).getDuration());
            trackPane.setTrack(albums.getTracks().get(i));
            trackPane.setAlbums(albums);
            vBox.getChildren().add(trackPane);
        }
        vBox.setPadding(new Insets(0,10,0,10));
        trackListScrollPane.setContent(vBox);
        trackListScrollPane.setPannable(true);
    }
}
