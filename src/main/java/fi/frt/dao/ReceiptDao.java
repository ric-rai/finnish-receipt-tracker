package fi.frt.dao;

import fi.frt.domain.Receipt;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;

public class ReceiptDao implements Dao<Receipt, Long> {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert sji;

    public ReceiptDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("Receipt");
    }

    @Override
    public Long create(Receipt r) {
        return sji.usingGeneratedKeyColumns("id").executeAndReturnKey(r.getAttributeMap()).longValue();
    }

    @Override
    public Receipt read(Long key) {
        return jdbcTemplate.query(
                "SELECT * FROM Receipt WHERE id = ?",
                new BeanPropertyRowMapper<>(Receipt.class),
                key
        ).get(0);
    }

    @Override
    public void update(Receipt r) {
        jdbcTemplate.update(
                "UPDATE Receipt SET date = ?, place = ?, sum = ?, buyer = ? WHERE id = ?",
                r.getDate(), r.getPlace(), r.getSum(), r.getBuyer(), r.getId()
        );
    }

    @Override
    public List<Receipt> list() {
        return jdbcTemplate.query(
                "SELECT * FROM Receipt",
                new BeanPropertyRowMapper<>(Receipt.class)
        );
    }

}
