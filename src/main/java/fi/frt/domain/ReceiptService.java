package fi.frt.domain;

import fi.frt.utilities.DateUtilities;
import fi.frt.dao.Dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReceiptService {

    private Dao<Receipt, Long> receiptDao;
    private List<Receipt> receiptList;

    public ReceiptService(Dao<Receipt, Long> receiptDao) {
        this.receiptList = new ArrayList<>();
        this.receiptDao = receiptDao;
    }

    public boolean saveReceipt(String dateStr, String placeStr, String sumStr, String buyerStr) {
        if (!DateUtilities.getFinnishDatePattern().matcher(dateStr).matches()) return false;
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        BigDecimal sum = new BigDecimal(sumStr);
        Receipt receipt = new Receipt(date, placeStr, sum, buyerStr, null);
        receiptDao.create(receipt);
        receiptList.add(receipt);
        return true;
    }

    public boolean saveReceipt(Receipt selectedReceipt, String dateStr, String placeStr, String sumStr, String buyerStr) {
        if (!DateUtilities.getFinnishDatePattern().matcher(dateStr).matches()) return false;
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        BigDecimal sum = new BigDecimal(sumStr);
        selectedReceipt.setDate(date);
        selectedReceipt.setPlace(placeStr);
        selectedReceipt.setSum(sum);
        selectedReceipt.setBuyer(buyerStr);
        return true;
    }

    public void setReceiptList(List<Receipt> receiptList) {
        this.receiptList = receiptList;
    }
}
