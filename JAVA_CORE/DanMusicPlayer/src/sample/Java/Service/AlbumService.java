package sample.Java.Service;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import sample.Java.Controller.AlbumPane;
import sample.Java.DTO.Album;
import sample.Java.DTO.Track;
import sample.Java.Static.DataStaticLoader;
import sample.Java.Util.Mp3Utils;
import sample.Java.Util.TimeUtils;
import sample.Java.entities.AlbumEntity;
import sample.Java.entities.AlbumTrackEntity;
import sample.Java.entities.SingleEntity;
import sample.Java.entities.TrackEntity;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public  void setContentAlbums(List<Album> albums){
        if(!albums.isEmpty()){
            albumListScrollPane.setContent(null);
            HBox hBox = new HBox();

            for(int i=0;i<albums.size();i++){
                AlbumPane albumPane = new AlbumPane();
                File fileImage = new File(albums.get(i).getPathImage());
                albumPane.getAlbumImageView().setImage(new Image(fileImage.toURI().toString()));
                albumPane.getAlbumName().setText(albums.get(i).getName());
                albumPane.getAlbumSingle().setText(albums.get(i).getSingle());
                albumPane.setAlbum(albums.get(i));
                System.out.println(albumPane.getPrefHeight());
                hBox.getChildren().add(albumPane);
            }

            albumListScrollPane.setFitToHeight(true);
            albumListScrollPane.setContent(hBox);
            albumListScrollPane.setPannable(true);

            hBox.setSpacing(200);
            hBox.setPadding(new Insets(5,5,5,5));

        }
    }

    public List<Album> convertFromAlbumDao(List<AlbumEntity> albumEntities) {
        List<Album> albumList = new ArrayList<>();
        albumEntities.forEach(a->{
            Album album = new Album();
            album.setName(a.getName());
            album.setReleaseTime(a.getReleaseTime());
            album.setPathImage(a.getPathImage());
            Optional<SingleEntity> singleEntity;
            List<Track> tracks = new ArrayList<>();
            try {
                singleEntity = DataStaticLoader.getSingleEntityDao().get(a.getId_single());
                singleEntity.ifPresent(entity -> album.setSingle(entity.getName()));

                List<TrackEntity> trackEntities = DataStaticLoader.getTrackEntityDao().getAlbumTracksByAlbumID(a.getId());
                if(!trackEntities.isEmpty()){
                    trackEntities.forEach(track->{
                        File file = new File(track.getPathSoundFile());
                        tracks.add(
                                new Track(track.getName(),
                                        TimeUtils.convertTimeToString(Mp3Utils.getDurationWithMp3Spi(file)),
                                        file,track.getPathImage(),album.getSingle()));
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            album.setTracks(tracks);

            albumList.add(album);

        });
        return albumList;
    }

}
