package fi.frt.domain;

import fi.frt.domain.textinput.ReceiptTextInput;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static fi.frt.utilities.DateUtils.DATE_FORMATTER;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReceiptTextInputTest {

    @Test
    public void receiptInputDataValidationWorksCorrectly() {
        Set<String> fields;
        fields = validateReceiptInputs(true, "01.02.2019", "Place", "1.25", "Buyer");
        assertThat(fields.isEmpty(), is(true));
        fields = validateReceiptInputs(false, "30.02.2019", "", "1.252", "");
        assertThat(fields.contains("date"), is(true));
        assertThat(fields.contains("place"), is(true));
        assertThat(fields.contains("sum"), is(true));
        assertThat(fields.contains("buyer"), is(true));
    }

    public Set<String> validateReceiptInputs(boolean expected, String dateStr, String placeStr, String sumStr, String buyerStr) {
        ReceiptTextInput receiptInput = new ReceiptTextInput();
        receiptInput.setDateStr(dateStr);
        receiptInput.setPlaceStr(placeStr);
        receiptInput.setSumStr(sumStr);
        receiptInput.setBuyerStr(buyerStr);
        receiptInput.validate();
        assertThat(receiptInput.isValid(), is(expected));
        if (receiptInput.isValid()) {
            LocalDate testDate = LocalDate.parse(dateStr, DATE_FORMATTER);
            BigDecimal testSum = new BigDecimal(sumStr);
            assertThat(receiptInput.getDate(), is(testDate));
            assertThat(receiptInput.getPlace(), is(placeStr));
            assertThat(receiptInput.getSum(), is(testSum));
            assertThat(receiptInput.getBuyer(), is(buyerStr));
        }
        return receiptInput.getInvalidFields();
    }
}
