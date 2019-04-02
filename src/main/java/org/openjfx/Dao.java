package org.openjfx;

import java.util.List;

public interface Dao<T, K> {
        void create(T object);
        //T read(K key) throws SQLException;
        //T update(T object) throws SQLException;
        //void delete(K key) throws SQLException;
        List<T> list();

}
