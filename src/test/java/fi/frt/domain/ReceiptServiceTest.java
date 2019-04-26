package fi.frt.domain;

import fi.frt.dao.Dao;
import fi.frt.domain.textinput.TextInput;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static fi.frt.utilities.DateUtils.DATE_FORMATTER;
import static fi.frt.utilities.MappingUtils.toStrMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ReceiptServiceTest {
    private FakeReceiptDao fakeReceiptDao = new FakeReceiptDao();
    private List<Receipt> receiptList = new ArrayList<>();
    private ReceiptService receiptService = new ReceiptService(fakeReceiptDao, receiptList);
    private LocalDate testDate = LocalDate.parse("01.02.2019", DATE_FORMATTER);
    private BigDecimal testSum = new BigDecimal("1.25");
    private TextInput fakeRcptTextInput;
    private byte[] testImage = new byte[]{};

    @Test
    public void newReceiptWorksCorrectly() {
        fakeRcptTextInput = new FakeReceiptTextInput(true, testDate, "Place", testSum, "Buyer");
        receiptService.newReceipt(fakeRcptTextInput, testImage);
        Receipt daoReceipt = fakeReceiptDao.getReceipt();
        assertThat(daoReceipt.getDate(), is(testDate));
        assertThat(daoReceipt.getPlace(), is("Place"));
        assertThat(daoReceipt.getSum(), is(testSum));
        assertThat(daoReceipt.getBuyer(), is("Buyer"));
        assertThat(daoReceipt.getImage().length, is(testImage.length));
        assertThat(receiptList.size(), is(1));
    }

    @Test
    public void updateReceiptWorksCorrectly(){
        fakeRcptTextInput = new FakeReceiptTextInput(true, testDate, "Place", testSum, "Buyer");
        Receipt receipt = new Receipt();
        receiptService.updateReceipt(receipt, fakeRcptTextInput, testImage);
        Receipt daoReceipt = fakeReceiptDao.getReceipt();
        assertThat(receipt.getDate(), is(testDate));
        assertThat(receipt.getPlace(), is("Place"));
        assertThat(receipt.getSum(), is(testSum));
        assertThat(receipt.getBuyer(), is("Buyer"));
        assertThat(receipt.getImage().length, is(testImage.length));
        assertThat(daoReceipt.getDate(), is(testDate));
        assertThat(daoReceipt.getPlace(), is("Place"));
        assertThat(daoReceipt.getSum(), is(testSum));
        assertThat(daoReceipt.getBuyer(), is("Buyer"));
        assertThat(daoReceipt.getImage().length, is(testImage.length));
    }

    @Test
    public void prepareNewImageIsWorkingCorrectly() {
        BufferedImage img = new BufferedImage(400, 700, BufferedImage.TYPE_INT_RGB);
        File file = new File("src/test/resources/_test_image.jpg");
        try {
            ImageIO.write(img, "JPEG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] testArray = new byte[]{};
        try {
            testArray = receiptService.prepareNewImage(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(testArray.length, is(1991));
        file.delete();
    }

    class FakeReceiptDao implements Dao<Receipt, Long> {
        Receipt receipt;

        @Override
        public Long create(Receipt r) {
            this.receipt = r;
            return 0L;
        }

        @Override
        public Receipt get(Long key) {
            return null;
        }

        @Override
        public List<Receipt> getByValue(Map<String, Object> map) {
            return null;
        }

        @Override
        public void update(Receipt r) {
            this.receipt = r;
        }

        @Override
        public void delete(Long key) {

        }

        @Override
        public void deleteByValue(Map<String, Object> map) {

        }

        @Override
        public List<Receipt> list() {
            return new ArrayList<>();
        }

        public Receipt getReceipt() {
            return receipt;
        }
    }

    class FakeReceiptTextInput implements TextInput {
        private boolean valid;
        private LocalDate date;
        private String place;
        private BigDecimal sum;
        private String buyer;

        public FakeReceiptTextInput(boolean valid, LocalDate date, String place, BigDecimal sum, String buyer) {
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
        public Set<String> getInvalidFields() {
            return null;
        }

        @Override
        public Map<String, Object> getAttrMap() {
            return toStrMap(
                    "date", date,
                    "place", place,
                    "sum", sum,
                    "buyer", buyer
            );
        }

        @Override
        public void setFromMap(Map<String, Object> map) {

        }

    }

}