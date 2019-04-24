package fi.frt.dao;

import fi.frt.domain.Purchase;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static fi.frt.utilities.MappingUtils.keysToLowerUnderscore;
import static fi.frt.utilities.MappingUtils.toLowerUnderscore;

public class PurchaseDao implements Dao<Purchase, Long> {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert sji;

    public PurchaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("Purchase").usingGeneratedKeyColumns("id");
    }

    @Override
    public Long create(Purchase p) {
        return sji.executeAndReturnKey(keysToLowerUnderscore(p.getAttrMap())).longValue();
    }

    @Override
    public Purchase get(Long key) {
        return jdbcTemplate.query(
                "SELECT * FROM Purchase WHERE id = ?",
                new BeanPropertyRowMapper<>(Purchase.class),
                key
        ).get(0);
    }

    @Override
    public List<Purchase> getByValue(Map<String, Object> map) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        return jdbcTemplate.query(
                "SELECT * FROM Purchase WHERE "  + toLowerUnderscore(entry.getKey()) + " = ?",
                new BeanPropertyRowMapper<>(Purchase.class),
                entry.getValue()
        );
    }

    @Override
    public void update(Purchase p) {
        StringJoiner str = new StringJoiner(" = ?, ", "UPDATE Purchase SET ", " = ? WHERE id = ?");
        Map<String, Object> map = p.getAttrMap();
        map.remove("id");
        ArrayList<Object> valueList = new ArrayList<>();
        map.forEach((k, v) -> {
            str.add(k);
            valueList.add(v);
        });
        valueList.add(p.getId());
        jdbcTemplate.update(str.toString(), valueList.toArray());
    }

    @Override
    public void delete(Long key) {
        jdbcTemplate.update(
                "DELETE FROM Purchase WHERE id = ?",
                key
        );
    }

    @Override
    public void deleteByValue(Map<String, Object> map) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        jdbcTemplate.update(
                "DELETE FROM Purchase WHERE " + toLowerUnderscore(entry.getKey()) + " = ?",
                entry.getValue()
        );
    }

    @Override
    public List<Purchase> list() {
        return jdbcTemplate.query(
                "SELECT * FROM Purchase",
                new BeanPropertyRowMapper<>(Purchase.class)
        );
    }

}
