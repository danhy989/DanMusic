package sample.Java.Dao.Impl;

import sample.Java.Dao.Dao;
import sample.Java.Util.PostgresSQLConnUtils;
import sample.Java.Util.StringUtils;
import sample.Java.entities.TrackEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrackDaoImpl extends Dao<TrackEntity> {
    @Override
    public List<TrackEntity> getList() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<TrackEntity> trackEntities = new ArrayList<>();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = "Select * from Track";
            trackEntities = setTrackEntities(statement, sql);
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
        return trackEntities;
    }

    @Override
    public Optional<TrackEntity> get(long id) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        TrackEntity trackEntity = new TrackEntity();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Select * from Track where id = %d",id);
            trackEntity = setTrackEntities(statement,sql).get(0);
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
        return Optional.of(trackEntity);
    }

    @Override
    public boolean save(TrackEntity trackEntity) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Insert into track(name, pathimage, pathsoundfile) VALUES ('%s','%s','%s')",
                    trackEntity.getName(),trackEntity.getPathImage(),trackEntity.getPathSoundFile());
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
    public boolean update(TrackEntity tOld, TrackEntity tNew) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("UPDATE track SET name = '%s', pathimage = '%s' , pathsoundfile = '%s' where id=%d",
                    tNew.getName(),tNew.getPathImage(),tNew.getPathSoundFile(),tOld.getId());
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
    public boolean delete(TrackEntity trackEntity) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("DELETE FROM track WHERE id=%d", trackEntity.getId());
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

    public List<TrackEntity> getAlbumTracksByAlbumID(long idAlbum) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<TrackEntity> trackEntities = new ArrayList<>();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Select t.id,t.name,t.pathimage,t.pathsoundfile " +
                    "from album_track at inner join track t on at.id_track = t.id where at.id_album = %d",idAlbum);
            trackEntities = setTrackEntities(statement, sql);
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
        return trackEntities;
    }

    private List<TrackEntity> setTrackEntities(Statement statement , String sql) throws SQLException {
        ResultSet rs = statement.executeQuery(sql);
        List<TrackEntity> trackEntities = new ArrayList<>();
        while (rs.next()){
            TrackEntity trackEntity = new TrackEntity();
            trackEntity.setId(rs.getLong(1));
            trackEntity.setName(rs.getString(2));
            trackEntity.setPathImage(StringUtils.getResourceString(rs.getString(3)));
            trackEntity.setPathSoundFile(StringUtils.getResourceString(rs.getString(4)));
            trackEntities.add(trackEntity);
        }
        return trackEntities;
    }

    public List<TrackEntity> getRandom(int limit) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<TrackEntity> trackEntities = new ArrayList<>();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("SELECT * FROM track ORDER BY random()  LIMIT %d",limit);
            trackEntities = setTrackEntities(statement, sql);
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
        return trackEntities;
    }
}
