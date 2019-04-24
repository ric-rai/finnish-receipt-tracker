package fi.frt.dao;

import fi.frt.domain.Receipt;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;

import java.io.ByteArrayInputStream;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import static fi.frt.utilities.MappingUtils.toLowerUnderscore;

public class ReceiptDao implements Dao<Receipt, Long> {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert sji;

    public ReceiptDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("Receipt").usingGeneratedKeyColumns("id");
    }

    @Override
    public Long create(Receipt r) {
        Map<String, ?> map = r.getAttributeMap();
        map.remove("image");
        MapSqlParameterSource sqlMap = new MapSqlParameterSource(map);
        sqlMap.addValue("image", byteArrayToLobValue(r.getImage()), Types.BLOB);
        return sji.executeAndReturnKey(sqlMap).longValue();
    }

    @Override
    public Receipt get(Long key) {
        return jdbcTemplate.query(
                "SELECT * FROM Receipt WHERE id = ?",
                new BeanPropertyRowMapper<>(Receipt.class),
                key
        ).get(0);
    }

    @Override
    public List<Receipt> getByValue(Map<String, Object> map) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        return jdbcTemplate.query(
                "SELECT * FROM Receipt WHERE "  + toLowerUnderscore(entry.getKey()) + " = ?",
                new BeanPropertyRowMapper<>(Receipt.class),
                entry.getValue()
        );
    }

    @Override
    public void update(Receipt r) {
        Map<String, ?> map = r.getAttributeMap();
        map.remove("image");
        MapSqlParameterSource sqlMap = new MapSqlParameterSource(map);
        sqlMap.addValue("image", byteArrayToLobValue(r.getImage()), Types.BLOB);
        new NamedParameterJdbcTemplate(jdbcTemplate).update(
                "UPDATE Receipt SET " +
                        "date = :date, " +
                        "place = :place, " +
                        "sum = :sum, " +
                        "buyer = :buyer, " +
                        "image = :image " +
                        "WHERE id = :id",
                sqlMap
        );
    }

    @Override
    public void delete(Long key) {
        jdbcTemplate.update(
                "DELETE FROM Receipt WHERE id = ?",
                key
        );
    }

    @Override
    public void deleteByValue(Map<String, Object> map) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        jdbcTemplate.update(
                "DELETE FROM Receipt WHERE " + toLowerUnderscore(entry.getKey()) + " = ?",
                entry.getValue()
        );
    }

    @Override
    public List<Receipt> list() {
        return jdbcTemplate.query(
                "SELECT * FROM Receipt",
                new BeanPropertyRowMapper<>(Receipt.class)
        );
    }

    private SqlLobValue byteArrayToLobValue(byte[] arr) {
        return new SqlLobValue(new ByteArrayInputStream(arr), arr.length, new DefaultLobHandler());
    }
}
