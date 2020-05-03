package sample.Java.Dao.Impl;

import sample.Java.Dao.Dao;
import sample.Java.Util.PostgresSQLConnUtils;
import sample.Java.entities.GenreEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenreDaoImpl extends Dao<GenreEntity> {
    @Override
    public List<GenreEntity> getList() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<GenreEntity> genreEntities = new ArrayList<>();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = "Select * from Genre";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                GenreEntity genreEntity = new GenreEntity();
                genreEntity.setId(rs.getLong(1));
                genreEntity.setName(rs.getString(2));
                genreEntities.add(genreEntity);
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
        return genreEntities;
    }

    @Override
    public Optional<GenreEntity> get(long id) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        GenreEntity genreEntity = new GenreEntity();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Select * from Genre where id = %d",id);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                genreEntity.setId(rs.getLong(1));
                genreEntity.setName(rs.getString(2));
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
        return Optional.of(genreEntity);
    }

    @Override
    public boolean save(GenreEntity genreEntity) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Insert into genre(name) VALUES ('%s')",
                    genreEntity.getName());
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
    public boolean update(GenreEntity tOld, GenreEntity tNew) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("UPDATE genre SET name = '%s' where id=%d",
                    tNew.getName(),tOld.getId());
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
    public boolean delete(GenreEntity genreEntity) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("DELETE FROM genre WHERE id=%d", genreEntity.getId());
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
}
