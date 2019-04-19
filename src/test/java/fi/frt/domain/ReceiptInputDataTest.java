package fi.frt.domain;

import fi.frt.domain.input.ReceiptInputData;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static fi.frt.utilities.DateUtils.DATE_FORMATTER;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReceiptInputDataTest {

    @Test
    public void receiptInputDataValidationWorksCorrectly(){
        Set<String> fields;
        fields = validateReceiptInputData(true,"01.02.2019", "Place", "1.25", "Buyer");
        assertThat(fields.isEmpty(), is(true));
        fields = validateReceiptInputData(false,"30.02.2019", "", "1.252", "");
        assertThat(fields.contains("date"), is(true));
        assertThat(fields.contains("place"), is(true));
        assertThat(fields.contains("sum"), is(true));
        assertThat(fields.contains("buyer"), is(true));
    }

    public Set<String> validateReceiptInputData(boolean expected, String dateStr, String placeStr, String sumStr, String buyerStr){
        ReceiptInputData receiptInputData = new ReceiptInputData();
        receiptInputData.setDateStr(dateStr);
        receiptInputData.setPlaceStr(placeStr);
        receiptInputData.setSumStr(sumStr);
        receiptInputData.setBuyerStr(buyerStr);
        receiptInputData.validate();
        assertThat(receiptInputData.isValid(), is(expected));
        if(receiptInputData.isValid()){
            LocalDate testDate = LocalDate.parse(dateStr, DATE_FORMATTER);
            BigDecimal testSum = new BigDecimal(sumStr);
            assertThat(receiptInputData.getDate(), is(testDate));
            assertThat(receiptInputData.getPlace(), is(placeStr));
            assertThat(receiptInputData.getSum(), is(testSum));
            assertThat(receiptInputData.getBuyer(), is(buyerStr));
        }
        return receiptInputData.getInvalidFields();
    }
}
