package sample.Java.Static;

import sample.Java.Dao.*;
import sample.Java.Dao.Impl.*;
import sample.Java.entities.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataStaticLoader {
    private static final DataStaticLoader instance = new DataStaticLoader();
    private DataStaticLoader(){}

    public static DataStaticLoader getInstance(){
        return instance;
    }
    private static final AlbumDaoImpl albumEntityDao = new AlbumDaoImpl();
    private static final GenreDaoImpl genreEntityDao = new GenreDaoImpl();
    private static final SingleDaoImpl singleEntityDao = new SingleDaoImpl();
    private static final TrackDaoImpl trackEntityDao = new TrackDaoImpl();
    private static final AlbumTrackDaoImpl albumTrackEntityDao = new AlbumTrackDaoImpl();

    private List<AlbumEntity> albumEntities = new ArrayList<>();
    private List<GenreEntity> genreEntities = new ArrayList<>();
    private List<SingleEntity> singleEntities = new ArrayList<>();
    private List<TrackEntity> trackEntities = new ArrayList<>();
    private List<AlbumTrackEntity> albumTrackEntities = new ArrayList<>();
    static{
        try {
            instance.albumEntities = albumEntityDao.getList();
            instance.genreEntities = genreEntityDao.getList();
            instance.singleEntities = singleEntityDao.getList();
            instance.trackEntities = trackEntityDao.getList();
            instance.albumTrackEntities = albumTrackEntityDao.getList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static AlbumDaoImpl getAlbumEntityDao() {
        return albumEntityDao;
    }

    public static GenreDaoImpl getGenreEntityDao() {
        return genreEntityDao;
    }

    public static SingleDaoImpl getSingleEntityDao() {
        return singleEntityDao;
    }

    public static TrackDaoImpl getTrackEntityDao() {
        return trackEntityDao;
    }

    public static AlbumTrackDaoImpl getAlbumTrackEntityDao() {
        return albumTrackEntityDao;
    }

    public List<AlbumEntity> getAlbumEntities() {
        return albumEntities;
    }

    public List<GenreEntity> getGenreEntities() {
        return genreEntities;
    }

    public List<SingleEntity> getSingleEntities() {
        return singleEntities;
    }

    public List<TrackEntity> getTrackEntities() {
        return trackEntities;
    }

    public List<AlbumTrackEntity> getAlbumTrackEntities() {
        return albumTrackEntities;
    }
}
