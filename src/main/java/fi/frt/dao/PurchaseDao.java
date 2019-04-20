package fi.frt.dao;

import fi.frt.domain.Purchase;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;
import java.util.Map;

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
    public Purchase read(Long key) {
        return jdbcTemplate.query(
                "SELECT * FROM Purchase WHERE id = ?",
                new BeanPropertyRowMapper<>(Purchase.class),
                key
        ).get(0);
    }

    @Override
    public void update(Purchase p) {
        jdbcTemplate.update(
                "UPDATE Purchase SET name = ?, quantity = ?, price = ?, type = ?, receipt_id = ? WHERE id = ?",
                p.getName(), p.getQuantity(), p.getPrice(), p.getType(), p.getReceiptId(), p.getId()
        );
    }

    @Override
    public void delete(Long key) {
        jdbcTemplate.update(
                "DELETE FROM Purchase WHERE id = ?",
                key
        );
    }

    @Override
    public void deleteAllWhere(Map<String, Long> map) {
        Map.Entry<String, Long> entry = map.entrySet().iterator().next();
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
