package fi.frt.dao;

import fi.frt.domain.Receipt;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static fi.frt.utilities.DateUtils.DATE_FORMATTER;
import static fi.frt.utilities.MappingUtils.toStrMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ReceiptDaoTest {
    private LocalDate testDate = LocalDate.parse("01.01.2019", DATE_FORMATTER);
    private BigDecimal testSum = new BigDecimal(1.25);
    private byte[] testImg = new byte[]{};
    private EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("receipt.sql").build();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
    private ReceiptDao receiptDao = new ReceiptDao(jdbcTemplate);


    @Before
    public void before() {
        jdbcTemplate.execute(
                "TRUNCATE TABLE Receipt; " +
                        "ALTER TABLE Receipt ALTER COLUMN id RESTART WITH 1;"
        );
    }

    @Test
    public void createWorksCorrectly() {
        Receipt in = new Receipt();
        in.setDate(testDate);
        in.setPlace("Place");
        in.setSum(testSum);
        in.setBuyer("Buyer");
        in.setImage(testImg);
        receiptDao.create(in);
        Receipt out = jdbcTemplate.query(
                "SELECT * FROM receipt WHERE id = 1",
                new BeanPropertyRowMapper<>(Receipt.class)
        ).get(0);
        assertThat(out.getDate(), is(testDate));
        assertThat(out.getPlace(), is("Place"));
        assertThat(out.getSum(), is(testSum));
        assertThat(out.getBuyer(), is("Buyer"));
        assertThat(out.getImage().length, is(0));
    }

    @Test
    public void getWorksCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO receipt (date, place, sum, buyer, image)" +
                        " VALUES ( ?, 'Place', ?, 'Buyer', X'01FF' )",
                testDate, testSum
        );
        Receipt out = receiptDao.get(1L);
        assertThat(out.getDate(), is(testDate));
        assertThat(out.getPlace(), is("Place"));
        assertThat(out.getSum(), is(testSum));
        assertThat(out.getBuyer(), is("Buyer"));
        assertThat(out.getImage().length, is(2));
    }

    @Test
    public void getByValueWorksCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO receipt (date, place, sum, buyer, image)" +
                        " VALUES ( ?, 'test', ?, 'Buyer', X'01FF' )",
                testDate, testSum
        );
        List<Receipt> list = receiptDao.getByValue(toStrMap("place", "test"));
        assertThat(list.size(), is(1));
        Receipt out = list.get(0);
        assertThat(out.getDate(), is(testDate));
        assertThat(out.getPlace(), is("test"));
        assertThat(out.getSum(), is(testSum));
        assertThat(out.getBuyer(), is("Buyer"));
        assertThat(out.getImage().length, is(2));
    }

    @Test
    public void updateWorksCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO receipt (date, place, sum, buyer, image)" +
                        " VALUES ( ?, 'Place', ?, 'Buyer', X'01FF' )",
                testDate, testSum
        );
        LocalDate newDate = LocalDate.parse("02.02.2019", DATE_FORMATTER);
        String newPlace = "New Place";
        BigDecimal newSum = new BigDecimal(0);
        String newBuyer = "New Buyer";
        byte[] newImg = new byte[]{};
        Receipt receipt = new Receipt();
        receipt.setId(1L);
        receipt.setDate(newDate);
        receipt.setPlace(newPlace);
        receipt.setSum(newSum);
        receipt.setBuyer(newBuyer);
        receipt.setImage(newImg);
        receiptDao.update(receipt);
        Receipt out = jdbcTemplate.query(
                "SELECT * FROM receipt WHERE id = 1",
                new BeanPropertyRowMapper<>(Receipt.class)
        ).get(0);
        assertThat(out.getDate(), is(newDate));
        assertThat(out.getPlace(), is(newPlace));
        assertThat(out.getSum(), is(newSum));
        assertThat(out.getBuyer(), is(newBuyer));
        assertThat(out.getImage().length, is(newImg.length));
    }

    @Test
    public void deleteWorksCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO receipt (date, place, sum, buyer, image)" +
                        " VALUES ( ?, 'Place', ?, 'Buyer', X'01FF' )",
                testDate, testSum
        );
        receiptDao.delete(1L);
        List<Receipt> out = jdbcTemplate.query(
                "SELECT * FROM receipt WHERE id = 1",
                new BeanPropertyRowMapper<>(Receipt.class)
        );
        assertThat(out.size(), is(0));
    }

    @Test
    public void deleteByValueWorksCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO receipt (date, place, sum, buyer, image)" +
                        " VALUES ( ?, 'test', ?, 'Buyer', X'01FF' )",
                testDate, testSum
        );
        receiptDao.deleteByValue(toStrMap("place", "test"));
        List<Receipt> out = jdbcTemplate.query(
                "SELECT * FROM receipt WHERE id = 1",
                new BeanPropertyRowMapper<>(Receipt.class)
        );
        assertThat(out.size(), is(0));
    }

    @Test
    public void listWorksCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO receipt (date, place, sum, buyer) VALUES" +
                        " ( '2019-01-01', 'Place', 1, 'Buyer' )," +
                        " ( '2019-01-01', 'Place', 2, 'Buyer' )," +
                        " ( '2019-01-01', 'Place', 3, 'Buyer' )"
        );
        List<Receipt> list = receiptDao.list();
        assertThat(list.get(0).getId(), is(1L));
        assertThat(list.get(0).getSum().intValue(), is(1));
        assertThat(list.get(2).getId(), is(3L));
        assertThat(list.get(2).getSum().intValue(), is(3));
    }

}