package fi.frt.dao;

import fi.frt.domain.Purchase;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;

public class PurchaseDao implements Dao<Purchase, Long> {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert sji;

    public PurchaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("Purchase");
    }

    @Override
    public Long create(Purchase p) {
        return sji.usingGeneratedKeyColumns("id").executeAndReturnKey(p.getAttrMap()).longValue();
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
                "UPDATE Purchase SET name = ?, quantity = ?, price = ?, type = ? WHERE id = ?",
                p.getName(), p.getQuantity(), p.getPrice(), p.getType(), p.getId()
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
