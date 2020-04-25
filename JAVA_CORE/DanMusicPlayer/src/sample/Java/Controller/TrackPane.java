package sample.Java.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import sample.Java.DTO.Albums;
import sample.Java.DTO.CurrentPlayList;
import sample.Java.DTO.Track;
import sample.Java.Service.MusicPlayerService;

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

    @FXML
    private Track track;

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
        files.add(this.track.getTrackFile());
        CurrentPlayList currentPlayList = new CurrentPlayList(files);
        MusicPlayerService.getInstance().startPlaylist(currentPlayList);
    }
}
