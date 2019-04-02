package org.openjfx;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReceiptService {

    private Dao receiptDao;

    ReceiptService(Dao receiptDao) {
        this.receiptDao = receiptDao;
    }

    public boolean saveReceipt(String dateStr, String placeStr, String sumStr, String buyerStr){
        if (!Utilities.getFinnishDatePattern().matcher(dateStr).matches()) return false;
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        BigDecimal sum = new BigDecimal(sumStr);
        //noinspection unchecked
        receiptDao.create(new Receipt(date, placeStr, sum, buyerStr, null));
        return true;
    }
}
