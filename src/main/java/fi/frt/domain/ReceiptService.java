package fi.frt.domain;

import fi.frt.dao.Dao;

import java.util.List;

public class ReceiptService {

    private Dao<Receipt, Long> receiptDao;
    private List<Receipt> receiptList;

    public ReceiptService(Dao<Receipt, Long> receiptDao) {
        this.receiptDao = receiptDao;
    }

    public Receipt newReceipt(ReceiptInputData receiptInputData) {
        Receipt receipt = new Receipt();
        receipt.setDate(receiptInputData.getDate());
        receipt.setPlace(receiptInputData.getPlace());
        receipt.setSum(receiptInputData.getSum());
        receipt.setBuyer(receiptInputData.getBuyer());
        receipt.setId(receiptDao.create(receipt));
        receiptList.add(receipt);
        return receipt;
    }

    public void updateReceipt(Receipt selectedReceipt, ReceiptInputData receiptInputData) {
        selectedReceipt.setDate(receiptInputData.getDate());
        selectedReceipt.setPlace(receiptInputData.getPlace());
        selectedReceipt.setSum(receiptInputData.getSum());
        selectedReceipt.setBuyer(receiptInputData.getBuyer());
        receiptDao.update(selectedReceipt);
    }

    public void setReceiptList(List<Receipt> receiptList) {
        this.receiptList = receiptList;
    }
}
