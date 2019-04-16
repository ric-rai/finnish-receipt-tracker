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
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ReceiptDaoTest {
    private LocalDate testDate = LocalDate.parse("01.01.2019", DATE_FORMATTER);
    private BigDecimal testSum = new BigDecimal(1.25);
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
    public void createIsWorkingCorrectly() {
        Receipt in = new Receipt();
        in.setDate(testDate);
        in.setPlace("Place");
        in.setSum(testSum);
        in.setBuyer("Buyer");
        receiptDao.create(in);
        Receipt out = jdbcTemplate.query(
                "SELECT * FROM receipt WHERE id = 1",
                new BeanPropertyRowMapper<>(Receipt.class)
        ).get(0);
        assertThat(out.getDate(), is(testDate));
        assertThat(out.getPlace(), is("Place"));
        assertThat(out.getSum(), is(testSum));
        assertThat(out.getBuyer(), is("Buyer"));
    }

    @Test
    public void readIsWorkingCorrectly(){
        jdbcTemplate.update(
                "INSERT INTO Receipt (date, place, sum, buyer)" +
                        " VALUES ( ?, 'Place', ?, 'Buyer' )",
                testDate, testSum
        );
        Receipt out = receiptDao.read(1L);
        assertThat(out.getDate(), is(testDate));
        assertThat(out.getPlace(), is("Place"));
        assertThat(out.getSum(), is(testSum));
        assertThat(out.getBuyer(), is("Buyer"));
    }

    @Test
    public void listIsWorkingCorrectly(){
        jdbcTemplate.update(
                "INSERT INTO Receipt (date, place, sum, buyer) VALUES" +
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