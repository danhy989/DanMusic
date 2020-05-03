package sample.Java.Dao.Impl;

import sample.Java.Dao.Dao;
import sample.Java.Util.PostgresSQLConnUtils;
import sample.Java.entities.AlbumTrackEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlbumTrackDaoImpl extends Dao<AlbumTrackEntity> {
    @Override
    public List<AlbumTrackEntity> getList() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<AlbumTrackEntity> albumTrackEntities = new ArrayList<>();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = "Select * from album_track";
            albumTrackEntities = setAlbumTrackEntity(statement, sql);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return albumTrackEntities;
    }

    @Override
    public Optional<AlbumTrackEntity> get(long id) throws SQLException {
        return Optional.empty();
    }

    private List<AlbumTrackEntity> setAlbumTrackEntity(Statement statement, String sql) throws SQLException {
        ResultSet rs = statement.executeQuery(sql);
        List<AlbumTrackEntity> albumTrackEntities = new ArrayList<>();
        while (rs.next()){
            AlbumTrackEntity albumTrackEntity = new AlbumTrackEntity();
            albumTrackEntity.setId_album(rs.getLong(1));
            albumTrackEntity.setId_track(rs.getLong(2));
            albumTrackEntity.setCreateTime(rs.getString(3));
            albumTrackEntities.add(albumTrackEntity);
        }
        return albumTrackEntities;
    }

    @Override
    public boolean save(AlbumTrackEntity albumTrackEntity) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Insert into ablum_track(id_album, id_track, createtime) VALUES ('%d','%d','%s')",
                    albumTrackEntity.getId_album(),albumTrackEntity.getId_track(),albumTrackEntity.getCreateTime());
            rowCount = statement.executeUpdate(sql);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        if(rowCount>0)return true;
        return false;
    }

    @Override
    public boolean update(AlbumTrackEntity tOld, AlbumTrackEntity tNew) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("UPDATE ablum_track SET id_album = '%d' , id_track = '%d' , createtime = '%s' " +
                            "where id_album = '%d' and id_track = '%d'",
                    tOld.getId_album(),tOld.getId_track(),tOld.getCreateTime(),tNew.getId_album(),tNew.getId_track());
            rowCount = statement.executeUpdate(sql);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        if(rowCount>0)return true;
        return false;
    }

    @Override
    public boolean delete(AlbumTrackEntity albumTrackEntity) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("DELETE FROM ablum_track WHERE id_album = '%d' and id_track = '%d'",
                    albumTrackEntity.getId_album(),albumTrackEntity.getId_track());
            rowCount = statement.executeUpdate(sql);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        if(rowCount>0)return true;
        return false;
    }

    void a(){

    }
}
