package fi.frt.dao;

import fi.frt.domain.Receipt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static fi.frt.utilities.DateUtilities.finnishDateFormatter;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class ReceiptDaoTest {
    ReceiptDao receiptDao;

    @Before
    public void before() {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
        this.receiptDao = new ReceiptDao(new JdbcTemplate(db));
    }

    @Test
    public void createAndListIsWorkingCorrectly() {
        LocalDate date = LocalDate.parse("01.01.2019", finnishDateFormatter);
        BigDecimal sum = new BigDecimal(10);
        Receipt in = new Receipt(date, "Paikka1", sum, "Ostaja1", null);
        receiptDao.create(in);
        Receipt out = receiptDao.list().get(0);
        assertThat(out.getPlace(), is("Paikka1"));
    }

}