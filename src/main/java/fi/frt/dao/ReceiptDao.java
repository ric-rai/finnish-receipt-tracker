package fi.frt.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fi.frt.domain.Receipt;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ReceiptDao implements Dao<Receipt, Long> {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert sji;
    private final ObjectMapper oMapper = new ObjectMapper();

    public ReceiptDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("Receipt");
        oMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void create(Receipt receipt) {
        Map map = oMapper.convertValue(receipt, Map.class);
        map.remove("purchases");
        sji.execute(map);
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
    public List<Receipt> list() {
        return jdbcTemplate.query(
                "SELECT * FROM Receipt ORDER BY date",
                new BeanPropertyRowMapper<>(Receipt.class)
        );
    }

}
