package fi.frt.domain;

import fi.frt.dao.Dao;
import fi.frt.domain.input.PurchaseInputData;
import fi.frt.domain.input.ReceiptInputData;

import java.util.List;

public class ReceiptService {

    private Dao<Receipt, Long> receiptDao;
    private List<Receipt> receiptList;

    public ReceiptService(Dao<Receipt, Long> receiptDao, List<Receipt> receiptList) {
        this.receiptDao = receiptDao;
        this.receiptList = receiptList;
        this.receiptList.addAll(receiptDao.list());
    }

    public Receipt newReceipt(ReceiptInputData rid, List<PurchaseInputData> purchaseInputs) {
        Receipt receipt = new Receipt();
        receipt.setDate(rid.getDate());
        receipt.setPlace(rid.getPlace());
        receipt.setSum(rid.getSum());
        receipt.setBuyer(rid.getBuyer());
        receipt.setId(receiptDao.create(receipt));
        receiptList.add(receipt);
        return receipt;
    }

    public void updateReceipt(Receipt receipt, ReceiptInputData rid, List<PurchaseInputData> purchaseInputs) {
        receipt.setDate(rid.getDate());
        receipt.setPlace(rid.getPlace());
        receipt.setSum(rid.getSum());
        receipt.setBuyer(rid.getBuyer());
        receiptDao.update(receipt);
    }

}
