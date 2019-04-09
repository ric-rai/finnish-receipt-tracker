package fi.frt.domain;

import fi.frt.dao.Dao;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ReceiptServiceTest {

    @Test
    public void saveReceiptReturnsCorrectValueAndCallsDaoCorrectly() {
        FakeReceiptDao fakeReceiptDao = new FakeReceiptDao();
        ReceiptService receiptService = new ReceiptService(fakeReceiptDao);
        boolean success = receiptService.saveReceipt("01.02.2019", "Place", "0", "Buyer");
        Receipt receipt = fakeReceiptDao.getReceipt();
        assertThat(success, is(true));
        assertThat(receipt.getDate().toString(), is("2019-02-01"));
        assertThat(receipt.getPlace(), is("Place"));
        assertThat(receipt.getBuyer(), is("Buyer"));
    }

    class FakeReceiptDao implements Dao<Receipt, Long> {
        Receipt receipt;

        @Override
        public void create(Receipt object) {
            this.receipt = object;
        }

        @Override
        public Receipt read(Long key) {
            return null;
        }

        @Override
        public List<Receipt> list() {
            return null;
        }

        public Receipt getReceipt() {
            return receipt;
        }
    }

}