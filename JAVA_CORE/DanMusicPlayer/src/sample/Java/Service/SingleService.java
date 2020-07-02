package sample.Java.Service;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import sample.Java.Controller.SinglePane;
import sample.Java.DTO.Album;
import sample.Java.DTO.Single;
import sample.Java.DTO.Track;
import sample.Java.Static.DataStaticLoader;
import sample.Java.Util.Mp3Utils;
import sample.Java.Util.TimeUtils;
import sample.Java.entities.AlbumEntity;
import sample.Java.entities.GenreEntity;
import sample.Java.entities.SingleEntity;
import sample.Java.entities.TrackEntity;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SingleService {
    private static final SingleService instance = new SingleService();
    private SingleService(){};
    public static SingleService getInstance(){return instance;}

    public static void setInstance(ScrollPane singleScrollPane){
        instance.singleScrollPane = singleScrollPane;
    }

    ScrollPane singleScrollPane;

    public void setContentSingle(List<Single> singles){
        if(!singles.isEmpty()){
            singleScrollPane.setContent(null);
            VBox vBox = new VBox();
            for(int i=0;i<singles.size();i++){
                SinglePane singlePane = new SinglePane();
                singlePane.getName().setText(singles.get(i).getName());
                singlePane.getGenre().setText(singles.get(i).getName());
                File fileImage = new File(singles.get(i).getPathImage());
                singlePane.getImage().setImage(new Image(fileImage.toURI().toString()));
                singlePane.setSingle(singles.get(i));
                vBox.getChildren().add(singlePane);
            }
            vBox.setPadding(new Insets(0,5,0,5));
            singleScrollPane.setContent(vBox);
            singleScrollPane.setFitToWidth(true);
            singleScrollPane.setFitToHeight(true);
            singleScrollPane.setPannable(true);
        }
    }

    public List<Single> convertFromSingleDao(List<SingleEntity> singleEntities){
        List<Single> singles = new ArrayList<>();

        singleEntities.forEach(c->{
            Single single = new Single();
            single.setName(c.getName());
            single.setInfo(c.getInfo());
            single.setPathImage(c.getPathImage());
            List<Album> albums = new ArrayList<>();
            try {
                Optional<GenreEntity> genreEntity = DataStaticLoader.getGenreEntityDao().get(c.getId_genre());
                genreEntity.ifPresent(entity->single.setGenre(entity.getName()));

                List<AlbumEntity> albumEntities = DataStaticLoader.getAlbumEntityDao().getAlbumBySingleID(c.getId());
                if(!albumEntities.isEmpty()){
                    albumEntities.forEach(a->{
                        Album album = new Album();
                        album.setName(a.getName());
                        album.setReleaseTime(a.getReleaseTime());
                        album.setPathImage(a.getPathImage());
                        album.setSingle(single.getName());
                        List<Track> tracks = new ArrayList<>();
                        List<TrackEntity> trackEntities = null;
                        try {
                            trackEntities = DataStaticLoader.getTrackEntityDao().getAlbumTracksByAlbumID(a.getId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if(!trackEntities.isEmpty()){
                            trackEntities.forEach(track->{
                                File file = new File(track.getPathSoundFile());
                                tracks.add(
                                        new Track(track.getName(),
                                                TimeUtils.convertTimeToString(Mp3Utils.getDurationWithMp3Spi(file)),
                                                file,track.getPathImage(),album.getSingle()));
                            });
                        }
                        album.setTracks(tracks);
                        albums.add(album);
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            single.setAlbums(albums);
            singles.add(single);
        });

        return singles;
    }

    public List<Single> convertAlbumHotTrackFromSingleDao(){
        List<Single> singles = new ArrayList<>();

        DataStaticLoader.getInstance().getSingleEntities().forEach(c-> {
                    Single single = new Single();
                    single.setName(c.getName());
                    single.setInfo(c.getInfo());
                    single.setPathImage(c.getPathImage());

            Optional<GenreEntity> genreEntity = null;
            AlbumEntity albumEntity = null;
            try {
                genreEntity = DataStaticLoader.getGenreEntityDao().get(c.getId_genre());
                albumEntity = DataStaticLoader.getAlbumEntityDao().getAlbumTrackHotBySingleID(c.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            genreEntity.ifPresent(entity -> single.setGenre(entity.getName()));

            if(albumEntity!=null){
                Album album = new Album();
                album.setName(albumEntity.getName());
                album.setReleaseTime(albumEntity.getReleaseTime());
                album.setPathImage(albumEntity.getPathImage());
                album.setSingle(single.getName());
                List<Track> tracks = new ArrayList<>();
                List<TrackEntity> trackEntities = null;
                try {
                    trackEntities = DataStaticLoader.getTrackEntityDao().getAlbumTracksByAlbumID(albumEntity.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (!trackEntities.isEmpty()) {
                    trackEntities.forEach(track -> {
                        File file = new File(track.getPathSoundFile());
                        tracks.add(
                                new Track(track.getName(),
                                        TimeUtils.convertTimeToString(Mp3Utils.getDurationWithMp3Spi(file)),
                                        file, track.getPathImage(), album.getSingle()));
                    });
                }
                album.setTracks(tracks);
                single.setAlbumTrackHot(album);
                singles.add(single);
            }

                });
        return singles;
    }

    public void addSingle(){

    }

}
