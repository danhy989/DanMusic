package sample.Java.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import sample.Java.Controller.Home.HomePane;
import sample.Java.Controller.Library.PlaylistPane;
import sample.Java.DTO.Album;
import sample.Java.DTO.CurrentPlayList;
import sample.Java.DTO.Single;
import sample.Java.DTO.Track;
import sample.Java.Service.AlbumService;
import sample.Java.Service.MusicPlayerService;
import sample.Java.Service.PlaylistService;
import sample.Java.Service.SingleService;
import sample.Java.Static.DataStaticLoader;
import sample.Java.Util.EffectUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AlbumPane extends StackPane {
    @FXML
    private ImageView albumImageView;

    @FXML
    private Label albumName,albumSingle;

    private Album album;

    public ImageView getAlbumImageView() {
        return albumImageView;
    }

    public Label getAlbumName() {
        return albumName;
    }

    public Label getAlbumSingle() {
        return albumSingle;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public AlbumPane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../Resources/Fxml/album_items.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            this.getStylesheets().add(getClass().getResource("../../Resources/Css/hover.css").toExternalForm());
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        EffectUtils.setCusorEffect(this);

    }

    @FXML
    private void selectAlbum(MouseEvent mouseEvent) throws Exception {
        if(!this.getAlbum().getTracks().isEmpty()){
            if(mouseEvent.getClickCount() == 2){
                BorderPane borderPane = (BorderPane) this.getParent().getScene().getRoot();
                if (borderPane.getCenter().getClass() != HomePane.class) {
                    borderPane.setCenter(new HomePane());
                    List<Album> albumList = AlbumService.getInstance().convertFromAlbumDao(DataStaticLoader.getInstance().getAlbumEntities());
                    AlbumService.getInstance().setContentAlbums(albumList);
                    List<Single> singles = SingleService.getInstance().convertAlbumHotTrackFromSingleDao();
                    SingleService.getInstance().setContentSingle(singles);
                }
                PlaylistService.getInstance().setContentPlaylist(this.album);
                ObservableList<File> files = FXCollections.observableArrayList();
                for (Track track:this.getAlbum().getTracks()
                ) {
                    files.add(track.getTrackFile());
                }
                CurrentPlayList currentPlayList = new CurrentPlayList(files,this.getAlbum().getTracks());
                MusicPlayerService.getInstance().startPlaylist(currentPlayList,null);
            }
        }

    }
}
