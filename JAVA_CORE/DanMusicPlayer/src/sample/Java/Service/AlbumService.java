package sample.Java.Service;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import sample.Java.Controller.AlbumPane;
import sample.Java.DTO.Album;
import sample.Java.DTO.Single;
import sample.Java.DTO.Track;
import sample.Java.Static.DataStaticLoader;
import sample.Java.Util.Mp3Utils;
import sample.Java.Util.PostgresSQLConnUtils;
import sample.Java.Util.StringUtils;
import sample.Java.Util.TimeUtils;
import sample.Java.entities.AlbumEntity;
import sample.Java.entities.AlbumTrackEntity;
import sample.Java.entities.SingleEntity;
import sample.Java.entities.TrackEntity;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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


    public void addTrackToExistAlbum(String albumName,File trackFile,String to) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            // Get album_id by album_name
            String sql_1 = String.format ("Select * from Album where name = '%s'",albumName);
            resultSet = statement.executeQuery(sql_1);
            resultSet.next();
            Long albumId = resultSet.getLong("id");

            //Get imagePath of single
            String sql_2 = String.format ("select s.pathimage from single s inner join album a on s.id = a.id_single where a.name = '%s'",albumName);
            resultSet = statement.executeQuery(sql_2);
            resultSet.next();
            String pathSingleImage = resultSet.getString("pathimage");

            // Add Tracks into database
            String sql_3 = String.format ("Insert into track(name, pathimage, pathsoundfile) VALUES ('%s','%s','%s')",
                   trackFile.getName(),pathSingleImage,StringUtils.getInnerProjectResourceString(to));
            statement.executeUpdate(sql_3);

            // Add album_tracks into database
            String sql_4 = String.format("Select id from track where name = '%s' and pathimage = '%s'",trackFile.getName(),pathSingleImage);
            resultSet = statement.executeQuery(sql_4);
            resultSet.next();
            Long track_id = resultSet.getLong("id");

            String sql_5 = String.format ("Insert into album_track(id_album, id_track, createtime) VALUES (%d,%d,'%s')",
                    albumId,track_id,java.time.LocalDate.now().toString());
            statement.executeUpdate(sql_5);

            connection.commit();

        } catch (SQLException e1){
            //Handle errors for JDBC
            e1.printStackTrace();
            // If there is an error then rollback the changes.
            System.out.println("Rolling back data here....");
            try{
                if(connection!=null)
                    connection.rollback();
            }catch(SQLException se2){
                se2.printStackTrace();
            }//end try
        } catch (ClassNotFoundException e2){
            e2.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

}
