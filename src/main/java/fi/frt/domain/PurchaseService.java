package fi.frt.domain;

import fi.frt.dao.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseService {
    private Dao<Purchase, Long> purchaseDao;
    private List<Purchase> activePurchasesList;
    private Map<Long, ArrayList<Purchase>> allPurchases = new HashMap<>();

    public PurchaseService(Dao<Purchase, Long> purchaseDao, List<Purchase> activePurchasesList) {
        this.purchaseDao = purchaseDao;
        this.activePurchasesList = activePurchasesList;
        loadAllPurchases();
    }

    private void loadAllPurchases() {
        List<Purchase> purchases = purchaseDao.list();
        purchases.forEach(p -> {
            allPurchases.putIfAbsent(p.getId(), new ArrayList<>());
            allPurchases.get(p.getId()).add(p);
        });
    }

    public void setActivePurchases(Long receiptId) {
        activePurchasesList.clear();
        activePurchasesList.addAll(allPurchases.getOrDefault(receiptId, new ArrayList<>()));
    }
}
