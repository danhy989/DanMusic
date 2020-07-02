package sample.Java.Dao.Impl;

import sample.Java.Dao.Dao;
import sample.Java.Util.StringUtils;
import sample.Java.entities.SingleEntity;
import sample.Java.Util.PostgresSQLConnUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SingleDaoImpl extends Dao<SingleEntity> {

    @Override
    public List<SingleEntity> getList() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<SingleEntity> singleEntities = new ArrayList<>();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = "Select * from Single";
            singleEntities = this.setEntites(statement,sql);
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
        return singleEntities;
    }

    @Override
    public Optional<SingleEntity> get(long id) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        SingleEntity singleEntity = new SingleEntity();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Select * from Single where id = %d",id);
            singleEntity = this.setEntites(statement,sql).get(0);
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
        return Optional.of(singleEntity);
    }

    @Override
    public boolean save(SingleEntity singleEntity) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Insert into single(name, info, pathimage,id_genre) VALUES ('%s','%s','%s','%d')",
                    singleEntity.getName(), singleEntity.getInfo(), singleEntity.getPathImage(),singleEntity.getId_genre());
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
    public boolean update(SingleEntity tOld, SingleEntity tNew) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("UPDATE single SET name = '%s', info =  '%s',pathimage = '%s' , id_genre = %d where id=%d",
                    tNew.getName(),tNew.getInfo(),tNew.getPathImage(),tNew.getId_genre(),tOld.getId());
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
    public boolean delete(SingleEntity singleEntity) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int rowCount = 0;
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("DELETE FROM single WHERE id=%d", singleEntity.getId());
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

    private List<SingleEntity> setEntites(Statement statement, String sql) throws SQLException {
        ResultSet rs = statement.executeQuery(sql);
        List<SingleEntity> singleEntities = new ArrayList<>();
        while (rs.next()){
            SingleEntity singleEntity = new SingleEntity();
            singleEntity.setId(rs.getLong(1));
            singleEntity.setName(rs.getString(2));
            singleEntity.setInfo(rs.getString(3));
            singleEntity.setPathImage(StringUtils.getResourceString(rs.getString(4)));
            singleEntity.setId_genre(rs.getLong(5));
            singleEntities.add(singleEntity);
        }
        return singleEntities;
    }

    public List<SingleEntity> getRandomSingle(int limit) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<SingleEntity> singleEntities = new ArrayList<>();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("SELECT * FROM single ORDER BY random()  LIMIT %d",limit);
            singleEntities = setEntites(statement, sql);
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
        return singleEntities;
    }

    public List<SingleEntity> getListByGenreName(String genre) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<SingleEntity> singleEntities = new ArrayList<>();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = "Select * from Single s inner join genre g on s.id_genre = g.id where g.name = "+"'"+genre+"'";
            singleEntities = this.setEntites(statement,sql);
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
        return singleEntities;
    }

    public Optional<SingleEntity> getByName(String name) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        SingleEntity singleEntity = new SingleEntity();
        try {
            connection = PostgresSQLConnUtils.getConnection();
            statement = connection.createStatement();
            String sql = String.format ("Select * from Single where name = '%s'",name);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                singleEntity.setId(rs.getLong(1));
                singleEntity.setName(rs.getString(2));
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
        return Optional.of(singleEntity);
    }

}
