package fi.frt.domain;

import fi.frt.dao.Dao;
import fi.frt.domain.input.InputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fi.frt.utilities.MappingUtils.toStrMap;

public class PurchaseService {
    private Dao<Purchase, Long> purchaseDao;
    private List<Purchase> activePurchasesList;
    private Map<Long, List<Purchase>> allPurchases = new HashMap<>();

    public PurchaseService(Dao<Purchase, Long> purchaseDao, List<Purchase> activePurchasesList) {
        this.purchaseDao = purchaseDao;
        this.activePurchasesList = activePurchasesList;
        loadAllPurchases();
    }

    private void loadAllPurchases() {
        List<Purchase> purchases = purchaseDao.list();
        purchases.forEach(p -> {
            allPurchases.putIfAbsent(p.getReceiptId(), new ArrayList<>());
            allPurchases.get(p.getReceiptId()).add(p);
        });
    }

    public void setActivePurchases(Long receiptId) {
        activePurchasesList.clear();
        activePurchasesList.addAll(allPurchases.getOrDefault(receiptId, new ArrayList<>()));
    }

    public void setNewPurchases(Long receiptId, List<InputData> purchaseInputs) {
        deleteReceiptPurchases(receiptId);
        List<Purchase> purchases = purchaseInputs.stream()
                .filter(InputData::isValid)
                .map(pid -> new Purchase(toStrMap(pid.getAttrMap(), "receiptId", receiptId)))
                .collect(Collectors.toList());
        purchases.forEach(p -> purchaseDao.create(p));
        allPurchases.put(receiptId, purchases);
    }

    public void deleteReceiptPurchases(Long receiptId) {
        purchaseDao.deleteAllWhere(toStrMap("receiptId", receiptId));
    }

}
