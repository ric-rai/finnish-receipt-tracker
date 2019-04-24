package fi.frt.dao;

import java.util.List;
import java.util.Map;

public interface Dao<T, K> {
    K create(T object);

    T get(K key);

    List<T> getByValue(Map<String, Object> map);

    void update(T object);

    void delete(K key);

    void deleteByValue(Map<String, Object> map);

    List<T> list();

}
