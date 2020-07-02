package sample.Java.Dao.Impl;

import sample.Java.Dao.Dao;
import sample.Java.Util.PostgresSQLConnUtils;
import sample.Java.Util.StringUtils;
import sample.Java.entities.AlbumEntity;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlbumDaoImpl extends Dao<AlbumEntity> {
    @Override
    public List<AlbumEntity> getList() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<AlbumEntity> albumEntities = new ArrayList<>();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = "Select * from Album";
            albumEntities = this.setAlbumEntites(statement,sql);
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
        return albumEntities;
    }

    @Override
    public Optional<AlbumEntity> get(long id) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        AlbumEntity albumEntity = new AlbumEntity();

        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Select * from Album where id = %d",id);
            albumEntity = this.setAlbumEntites(statement,sql).get(0);
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
        return Optional.of(albumEntity);
    }

    @Override
    public boolean save(AlbumEntity albumEntity) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Insert into Album(name, releasetime,pathimage,album_hot_track,id_single) " +
                            "VALUES ('%s','%s','%s',%b,%d)",
                    albumEntity.getName(), albumEntity.getReleaseTime(),albumEntity.getPathImage()
                    ,albumEntity.isAlbum_hot_track(),albumEntity.getId_single());
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
    public boolean update(AlbumEntity tOld, AlbumEntity tNew) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("UPDATE album SET name = '%s', releasetime =  '%s',pathimage = '%s', " +
                            "album_hot_track = '%b' , id_single = '%d' where id=%d",
                    tNew.getName(),tNew.getReleaseTime(),tNew.getPathImage(),tNew.isAlbum_hot_track(),
                    tNew.getId_single(),tOld.getId());
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
    public boolean delete(AlbumEntity albumEntity) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("DELETE FROM album WHERE id=%d", albumEntity.getId());
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

    public List<AlbumEntity> getAlbumBySingleID(Long id) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<AlbumEntity> albumEntities = new ArrayList<>();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Select a.id,a.name,a.releasetime,a.pathimage,a.album_hot_track,a.id_single  " +
                    "from album a inner join single s on a.id_single = s.id where s.id = '%d'",id);
            albumEntities = setAlbumEntites(statement, sql);
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
        return albumEntities;
    }

    public AlbumEntity getAlbumTrackHotBySingleID(Long id) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        AlbumEntity albumEntity = null;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Select a.id,a.name,a.releasetime,a.pathimage,a.album_hot_track,a.id_single  " +
                    "from album a inner join single s on a.id_single = s.id where s.id = '%d' and a.album_hot_track = true",id);
            List<AlbumEntity> albumEntities = setAlbumEntites(statement, sql);
            if(!albumEntities.isEmpty()){
                albumEntity = albumEntities.get(0);
            }
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
        return albumEntity;
    }



    private List<AlbumEntity> setAlbumEntites(Statement statement, String sql) throws SQLException {
        ResultSet rs = statement.executeQuery(sql);
        List<AlbumEntity> albumEntities = new ArrayList<>();
        while (rs.next()){
            AlbumEntity albumEntity = new AlbumEntity();
            albumEntity.setId(rs.getLong(1));
            albumEntity.setName(rs.getString(2));
            albumEntity.setReleaseTime(rs.getString(3));
            albumEntity.setPathImage(StringUtils.getResourceString(rs.getString(4)));
            albumEntity.setAlbum_hot_track(rs.getBoolean(5));
            albumEntity.setId_single(rs.getLong(6));
            albumEntities.add(albumEntity);
        }
        return albumEntities;
    }

    public List<AlbumEntity> getRandomAlbum(int limit) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<AlbumEntity> albumEntities = new ArrayList<>();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("SELECT * FROM album ORDER BY random()  LIMIT %d",limit);
            albumEntities = setAlbumEntites(statement, sql);
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
        return albumEntities;
    }

    public List<AlbumEntity> getListBySingleName(String single) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<AlbumEntity> albumEntities = new ArrayList<>();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = "Select * from Album inner join single s on album.id_single = s.id where s.name = "+"'"+single+"'";
            albumEntities = this.setAlbumEntites(statement,sql);
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
        return albumEntities;
    }

    public AlbumEntity getByName(String name) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        AlbumEntity albumEntity = new AlbumEntity();

        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Select * from Album where name = '%s'",name);
            albumEntity = this.setAlbumEntites(statement,sql).get(0);
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
        return albumEntity;
    }


}
