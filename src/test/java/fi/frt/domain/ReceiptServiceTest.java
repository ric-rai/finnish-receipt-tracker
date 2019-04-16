package fi.frt.domain;

import fi.frt.dao.Dao;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static fi.frt.utilities.DateUtils.DATE_FORMATTER;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ReceiptServiceTest {
    private FakeReceiptDao fakeReceiptDao = new FakeReceiptDao();
    private ReceiptService receiptService = new ReceiptService(fakeReceiptDao);
    private LocalDate testDate = LocalDate.parse("01.02.2019", DATE_FORMATTER);
    private BigDecimal testSum = new BigDecimal("1.25");
    private ReceiptInputData fakeRID;

    @Test
    public void newReceiptWorksCorrectly() {
        List<Receipt> receiptList = new ArrayList<>();
        receiptService.setReceiptList(receiptList);
        fakeRID = new FakeReceiptInputData(true, testDate, "Place", testSum, "Buyer");
        receiptService.newReceipt(fakeRID);
        Receipt daoReceipt = fakeReceiptDao.getReceipt();
        assertThat(daoReceipt.getDate(), is(testDate));
        assertThat(daoReceipt.getPlace(), is("Place"));
        assertThat(daoReceipt.getSum(), is(testSum));
        assertThat(daoReceipt.getBuyer(), is("Buyer"));
        assertThat(receiptList.size(), is(1));
    }

    @Test
    public void updateReceiptWorksCorrectly(){
        fakeRID = new FakeReceiptInputData(true, testDate, "Place", testSum, "Buyer");
        Receipt receipt = new Receipt();
        receiptService.updateReceipt(receipt, fakeRID);
        Receipt daoReceipt = fakeReceiptDao.getReceipt();
        assertThat(receipt.getDate(), is(testDate));
        assertThat(receipt.getPlace(), is("Place"));
        assertThat(receipt.getSum(), is(testSum));
        assertThat(receipt.getBuyer(), is("Buyer"));
        assertThat(daoReceipt.getDate(), is(testDate));
        assertThat(daoReceipt.getPlace(), is("Place"));
        assertThat(daoReceipt.getSum(), is(testSum));
        assertThat(daoReceipt.getBuyer(), is("Buyer"));
    }

    class FakeReceiptDao implements Dao<Receipt, Long> {
        Receipt receipt;

        @Override
        public Long create(Receipt r) {
            this.receipt = r;
            return 0L;
        }

        @Override
        public Receipt read(Long key) {
            return null;
        }

        @Override
        public void update(Receipt r) {
            this.receipt = r;
        }

        @Override
        public List<Receipt> list() {
            return null;
        }

        public Receipt getReceipt() {
            return receipt;
        }
    }

    class FakeReceiptInputData extends ReceiptInputData{
        private boolean valid;
        private LocalDate date;
        private String place;
        private BigDecimal sum;
        private String buyer;

        public FakeReceiptInputData(boolean valid, LocalDate date, String place, BigDecimal sum, String buyer) {
            this.valid = valid;
            this.date = date;
            this.place = place;
            this.sum = sum;
            this.buyer = buyer;
        }

        @Override
        public boolean isValid() {
            return valid;
        }

        @Override
        public LocalDate getDate() {
            return date;
        }

        @Override
        public String getPlace() {
            return place;
        }

        @Override
        public BigDecimal getSum() {
            return sum;
        }

        @Override
        public String getBuyer() {
            return buyer;
        }
    }

}