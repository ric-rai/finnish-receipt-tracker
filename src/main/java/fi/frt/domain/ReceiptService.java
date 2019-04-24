package fi.frt.domain;

import com.tinify.Source;
import com.tinify.Tinify;
import fi.frt.dao.Dao;
import fi.frt.domain.textinput.TextInput;

import java.io.File;
import java.util.List;

public class ReceiptService {

    private Dao<Receipt, Long> receiptDao;
    private final PurchaseService purchaseService;
    private List<Receipt> receiptList;

    public ReceiptService(Dao<Receipt, Long> rDao, PurchaseService pSvc, List<Receipt> rList) {
        this.receiptDao = rDao;
        this.purchaseService = pSvc;
        this.receiptList = rList;
        this.receiptList.addAll(receiptDao.list());
        Tinify.setKey("oSYQ7WOXulfGcEfPvHsF0mYvsrOlE3gB");
    }

    public Receipt newReceipt(TextInput receiptInput, List<TextInput> purInputs, byte[] img) {
        Receipt receipt = new Receipt(receiptInput.getAttrMap());
        receipt.setImage(img);
        Long receiptId = receiptDao.create(receipt);
        receipt.setId(receiptId);
        purchaseService.setNewPurchases(receiptId, purInputs);
        receiptList.add(receipt);
        return receipt;
    }

    public void updateReceipt(Receipt receipt, TextInput receiptInput, List<TextInput> purInputs, byte[] img) {
        receipt.setFromMap(receiptInput.getAttrMap());
        receipt.setImage(img);
        purchaseService.setNewPurchases(receipt.getId(), purInputs);
        receiptDao.update(receipt);
    }

    public byte[] prepareNewImage(File file) throws Exception {
        Source tinifyImg = Tinify.fromFile(file.getPath());
        return tinifyImg.toBuffer();
    }

}
