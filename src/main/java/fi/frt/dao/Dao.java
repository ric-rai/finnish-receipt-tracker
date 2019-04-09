package fi.frt.dao;

import java.util.List;

public interface Dao<T, K> {
    void create(T object);

    T read(K key);
    //T update(T object) throws SQLException;
    //void delete(K key) throws SQLException;
    List<T> list();

}
