package sample.Java.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import sample.Java.DTO.Album;
import sample.Java.DTO.CurrentPlayList;
import sample.Java.DTO.Track;
import sample.Java.Service.MusicPlayerService;
import sample.Java.Util.EffectUtils;

import java.io.File;
import java.io.IOException;

public class TrackPane extends HBox {

    @FXML
    private Label trackNumOrder;

    @FXML
    private Label trackName;

    @FXML
    private Label trackTotalDuration;

    @FXML
    private ImageView imageView;

    private Track track;

    private Album album;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public ImageView getImageView() {
        return imageView;
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

        EffectUtils.setCusorEffect(this);
    }



    @FXML
    private void selectTrackOnPlaylist(MouseEvent mouseEvent) throws Exception {
        if(!album.getTracks().isEmpty()){
            if(mouseEvent.getClickCount() == 2) {
                ObservableList<File> files = FXCollections.observableArrayList();
                album.getTracks().forEach(t -> {
                    files.add(t.getTrackFile());
                });
                CurrentPlayList currentPlayList = new CurrentPlayList(files, this.getAlbum().getTracks());
                MusicPlayerService.getInstance().startPlaylist(currentPlayList, track.getTrackFile());
            }
        }
    }
}
