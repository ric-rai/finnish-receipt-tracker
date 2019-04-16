package fi.frt.domain;

import fi.frt.dao.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseService {
    private Dao<Purchase, Long> purchaseDao;
    private List<Purchase> purchaseList = new ArrayList<>();
    private Map<Long, ArrayList<Purchase>> allPurchases = new HashMap<>();

    public PurchaseService(Dao<Purchase, Long> purchaseDao) {
        this.purchaseDao = purchaseDao;
        loadAllPurchases();
    }

    private void loadAllPurchases() {
        List<Purchase> purchases = purchaseDao.list();
        purchases.forEach(p -> {
            allPurchases.putIfAbsent(p.getId(), new ArrayList<>());
            allPurchases.get(p.getId()).add(p);
        });
    }

    public void setPurchaseList(List<Purchase> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public void setPurchases(Long receiptId) {
        purchaseList.clear();
        purchaseList.addAll(allPurchases.getOrDefault(receiptId, new ArrayList<>()));
    }
}
