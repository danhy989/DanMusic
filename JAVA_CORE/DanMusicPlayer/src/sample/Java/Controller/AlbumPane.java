package sample.Java.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import sample.Java.DTO.Albums;
import sample.Java.DTO.CurrentPlayList;
import sample.Java.DTO.Track;
import sample.Java.Service.MusicPlayerService;
import sample.Java.Service.PlaylistService;

import java.io.File;
import java.io.IOException;

public class AlbumPane extends StackPane {
    @FXML
    private ImageView albumImageView;

    @FXML
    private Label albumName,albumSingle;

    private Albums albums;

    public ImageView getAlbumImageView() {
        return albumImageView;
    }

    public Label getAlbumName() {
        return albumName;
    }

    public Label getAlbumSingle() {
        return albumSingle;
    }

    public Albums getAlbums() {
        return albums;
    }

    public void setAlbums(Albums albums) {
        this.albums = albums;
    }

    public AlbumPane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../Resources/Fxml/album_items.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void selectAlbum(MouseEvent mouseEvent) throws Exception {
        PlaylistService.getInstance().setContentPlaylist(this.albums);

        ObservableList<File> files = FXCollections.observableArrayList();

        for (Track track:this.getAlbums().getTracks()
             ) {
            files.add(track.getTrackFile());
        }

        CurrentPlayList currentPlayList = new CurrentPlayList(files);
        MusicPlayerService.getInstance().startPlaylist(currentPlayList);
    }
}
