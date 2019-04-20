package fi.frt.dao;

import java.util.List;
import java.util.Map;

public interface Dao<T, K> {
    K create(T object);

    T read(K key);

    void update(T object);

    void delete(K key);

    void deleteAllWhere(Map<String, Long> map);

    List<T> list();

}
