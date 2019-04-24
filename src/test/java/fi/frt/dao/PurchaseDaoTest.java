package fi.frt.dao;

import fi.frt.domain.Purchase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.util.List;

import static fi.frt.utilities.MappingUtils.toStrMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PurchaseDaoTest {
    private String testName = "Name";
    private Integer testQuantity = 1;
    private BigDecimal testPrice = new BigDecimal(1.25);
    private String testType = "Type";
    private EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("purchase.sql").build();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
    private PurchaseDao purchaseDao = new PurchaseDao(jdbcTemplate);


    @Before
    public void before() {
        jdbcTemplate.execute(
                "TRUNCATE TABLE Purchase; " +
                        "ALTER TABLE Purchase ALTER COLUMN id RESTART WITH 1;"
        );
    }

    @Test
    public void createIsWorkingCorrectly() {
        Purchase in = new Purchase();
        in.setName(testName);
        in.setQuantity(testQuantity);
        in.setPrice(testPrice);
        in.setType(testType);
        purchaseDao.create(in);
        Purchase out = jdbcTemplate.query(
                "SELECT * FROM Purchase WHERE id = 1",
                new BeanPropertyRowMapper<>(Purchase.class)
        ).get(0);
        assertThat(out.getName(), is(testName));
        assertThat(out.getQuantity(), is(testQuantity));
        assertThat(out.getPrice(), is(testPrice));
        assertThat(out.getType(), is(testType));
    }

    @Test
    public void getIsWorkingCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO Purchase (name, quantity, price, type) VALUES ( ?, ?, ?, ? )",
                testName, testQuantity, testPrice, testType
        );
        Purchase out = purchaseDao.get(1L);
        assertThat(out.getName(), is(testName));
        assertThat(out.getQuantity(), is(testQuantity));
        assertThat(out.getPrice(), is(testPrice));
        assertThat(out.getType(), is(testType));
    }

    @Test
    public void getByValueIsWorkingCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO Purchase (name, quantity, price, type) VALUES ( ?, ?, ?, ? )",
                testName, testQuantity, testPrice, testType
        );
        List<Purchase> list = purchaseDao.getByValue(toStrMap("price", testPrice));
        assertThat(list.size(), is(1));
        Purchase out = list.get(0);
        assertThat(out.getName(), is(testName));
        assertThat(out.getQuantity(), is(testQuantity));
        assertThat(out.getPrice(), is(testPrice));
        assertThat(out.getType(), is(testType));
    }

    @Test
    public void updateIsWorkingCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO Purchase (name, quantity, price, type) VALUES ( ?, ?, ?, ? )",
                "Old Name", 0, 0, "Old Type"
        );
        Purchase in = new Purchase();
        in.setId(1L);
        in.setName(testName);
        in.setQuantity(testQuantity);
        in.setPrice(testPrice);
        in.setType(testType);
        purchaseDao.update(in);
        Purchase out = jdbcTemplate.query(
                "SELECT * FROM Purchase WHERE id = 1",
                new BeanPropertyRowMapper<>(Purchase.class)
        ).get(0);
        assertThat(out.getName(), is(testName));
        assertThat(out.getQuantity(), is(testQuantity));
        assertThat(out.getPrice(), is(testPrice));
        assertThat(out.getType(), is(testType));
    }

    @Test
    public void deleteIsWorkingCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO Purchase (name, quantity, price, type) VALUES ( ?, ?, ?, ? )",
                testName, testQuantity, testPrice, testType
        );
        purchaseDao.delete(1L);
        List<Purchase> out = jdbcTemplate.query(
                "SELECT * FROM Purchase WHERE id = 1",
                new BeanPropertyRowMapper<>(Purchase.class)
        );
        assertThat(out.size(), is(0));
    }

    @Test
    public void deleteByValueIsWorkingCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO Purchase (name, quantity, price, type) VALUES ( ?, ?, ?, ? )",
                testName, testQuantity, testPrice, testType
        );
        purchaseDao.deleteByValue(toStrMap("quantity", 1));
        List<Purchase> out = jdbcTemplate.query(
                "SELECT * FROM Purchase WHERE id = 1",
                new BeanPropertyRowMapper<>(Purchase.class)
        );
        assertThat(out.size(), is(0));
    }

    @Test
    public void listIsWorkingCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO Purchase (name, quantity, price, type) VALUES" +
                        " ( 'Name', 1, 0, 'Type' )," +
                        " ( 'Name', 2, 0, 'Type' )," +
                        " ( 'Name', 3, 0, 'Type' )"
        );
        List<Purchase> list = purchaseDao.list();
        assertThat(list.get(0).getId(), is(1L));
        assertThat(list.get(0).getQuantity().intValue(), is(1));
        assertThat(list.get(2).getId(), is(3L));
        assertThat(list.get(2).getQuantity().intValue(), is(3));
    }
}
