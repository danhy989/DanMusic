package sample.Java.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import sample.Java.DTO.Albums;
import sample.Java.DTO.CurrentPlayList;
import sample.Java.DTO.Single;
import sample.Java.DTO.Track;
import sample.Java.Service.MusicPlayerService;
import sample.Java.Service.PlaylistService;

import java.io.File;
import java.io.IOException;

public class SinglePane extends HBox {
    @FXML
    private ImageView image;

    @FXML
    private Label name;

    @FXML
    private Label genre;

    private Single single;

    public ImageView getImage() {
        return image;
    }

    public Label getName() {
        return name;
    }

    public Label getGenre() {
        return genre;
    }

    public SinglePane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../Resources/Fxml/single_items.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public Single getSingle() {
        return single;
    }

    public void setSingle(Single single) {
        this.single = single;
    }

    @FXML
    private void selectPLaylistOfSingle(MouseEvent mouseEvent) throws Exception {
        PlaylistService.getInstance().setContentPlaylist(this.getSingle().getAlbumsTrackHot());

        ObservableList<File> files = FXCollections.observableArrayList();

        for (Track track:this.getSingle().getAlbumsTrackHot().getTracks()
        ) {
            files.add(track.getTrackFile());
        }

        CurrentPlayList currentPlayList = new CurrentPlayList(files);
        MusicPlayerService.getInstance().startPlaylist(currentPlayList,null);
    }
}
