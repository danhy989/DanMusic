package sample.Java.Controller;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Pair;
import sample.Java.DTO.Albums;
import sample.Java.DTO.CurrentPlayList;
import sample.Java.DTO.Track;
import sample.Java.Service.MusicPlayerService;
import sample.Java.Service.PlaylistService;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class TrackPane extends HBox {

    @FXML
    private Label trackNumOrder;

    @FXML
    private Label trackName;

    @FXML
    private Label trackTotalDuration;

    private Track track;

    private Albums albums;

    public Albums getAlbums() {
        return albums;
    }

    public void setAlbums(Albums albums) {
        this.albums = albums;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }


    public Label getTrackNumOrder() {
        return trackNumOrder;
    }

    public Label getTrackName() {
        return trackName;
    }

    public Label getTrackTotalDuration() {
        return trackTotalDuration;
    }

    public TrackPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../Resources/Fxml/track_row.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void selectTrackOnPlaylist(MouseEvent mouseEvent) throws Exception {
        ObservableList<File> files = FXCollections.observableArrayList();
        albums.getTracks().forEach(t->{
            files.add(t.getTrackFile());
        });
        CurrentPlayList currentPlayList = new CurrentPlayList(files);
        MusicPlayerService.getInstance().startPlaylist(currentPlayList,track.getTrackFile());

        VBox vboxPlaylist = (VBox) PlaylistService.getInstance().getTrackListScrollPane().getContent();
        for(int i=0;i<files.size();i++){
            TrackPane trackPane = (TrackPane) vboxPlaylist.getChildren().get(i);
            if(MusicPlayerService.getInstance().isPlayingTrack(files.get(i))){
               trackPane.getTrackNumOrder().setTextFill(Color.RED);
               trackPane.getTrackTotalDuration().setTextFill(Color.RED);
               trackPane.getTrackName().setTextFill(Color.RED);
            }else{
                trackPane.getTrackNumOrder().setTextFill(Color.BLACK);
                trackPane.getTrackTotalDuration().setTextFill(Color.BLACK);
                trackPane.getTrackName().setTextFill(Color.BLACK);
            }
        }
    }
}
