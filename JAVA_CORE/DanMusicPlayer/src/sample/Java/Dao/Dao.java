package sample.Java.Dao;

import sample.Java.entities.TrackEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

abstract public class Dao<T> {
    abstract public List<T> getList() throws SQLException;
    abstract public Optional<T> get(long id) throws SQLException;
    abstract public boolean save(T t) throws SQLException;
    abstract public boolean update(T tOld, T tNew) throws SQLException;
    abstract public boolean delete(T t) throws SQLException;
}
