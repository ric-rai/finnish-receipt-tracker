package fi.frt.dao;

import java.util.List;

public interface Dao<T, K> {
    K create(T object);
    T read(K key);
    void update(T object);
    //void delete(K key) throws SQLException;
    List<T> list();

}
