package fi.frt.domain;

import fi.frt.dao.Dao;
import fi.frt.domain.input.InputData;

import java.util.List;

public class ReceiptService {

    private final PurchaseService purchaseService;
    private Dao<Receipt, Long> receiptDao;
    private List<Receipt> receiptList;

    public ReceiptService(Dao<Receipt, Long> receiptDao, PurchaseService purchaseService, List<Receipt> receiptList) {
        this.receiptDao = receiptDao;
        this.purchaseService = purchaseService;
        this.receiptList = receiptList;
        this.receiptList.addAll(receiptDao.list());
    }

    public Receipt newReceipt(InputData receiptInput, List<InputData> purchaseInputs) {
        Receipt receipt = new Receipt(receiptInput.getAttrMap());
        Long receiptId = receiptDao.create(receipt);
        receipt.setId(receiptId);
        purchaseService.setNewPurchases(receiptId, purchaseInputs);
        receiptList.add(receipt);
        return receipt;
    }


    public void updateReceipt(Receipt receipt, InputData receiptInput, List<InputData> purchaseInputs) {
        receipt.setFromMap(receiptInput.getAttrMap());
        purchaseService.setNewPurchases(receipt.getId(), purchaseInputs);
        receiptDao.update(receipt);
    }

}
