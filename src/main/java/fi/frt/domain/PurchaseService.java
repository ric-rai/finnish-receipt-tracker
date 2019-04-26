package fi.frt.domain;

import fi.frt.dao.Dao;
import fi.frt.domain.textinput.TextInput;

import java.util.*;
import java.util.stream.Collectors;

import static fi.frt.utilities.MappingUtils.toStrMap;

public class PurchaseService {
    private Dao<Purchase, Long> purchaseDao;
    private List<Purchase> activePurchasesList;
    private Map<Long, List<Purchase>> allPurchases = new HashMap<>();
    private Map<String, Set<String>> nameTypeMap = new HashMap<>();

    public PurchaseService() {
    }

    /**
     * Luo uuden PurchaseService -olion. Konstruktori lataa tietokannasta kaikki ostokset oliomuuttujaan.
     *
     * @param purchaseDao         Dao-rajapinnan toteuttava olio
     * @param activePurchasesList Lista, jossa kulloinkin aktiivisena olevan kuitin ostoksia pidetään
     */
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
            nameTypeMap.putIfAbsent(p.getName(), new HashSet<>());
            nameTypeMap.get(p.getName()).add(p.getType());
        });
    }

    /**
     * Asettaa kuittiin liittyvät ostokset activePurchaseList-listaan.
     *
     * @param receiptId Kuitin id-tunnus
     */
    public void setActivePurchases(Long receiptId) {
        activePurchasesList.clear();
        activePurchasesList.addAll(allPurchases.getOrDefault(receiptId, new ArrayList<>()));
    }

    /**
     * Asettaa uudet ostokset kuitille. Metodi poistaa ensin aiemmin kuittiin mahdollisesti liittyneet ostokset, minkä
     * jälkeen se tallentaa annetusta tekstisyötelistasta validit syötetekstit kuitin ostoksiksi.
     *
     * @param receiptId      Sen kuitin id-tunnus, jonka ostokset asetetaan
     * @param purchaseInputs Syötetekstilista, jonka syötetekstien tulee kuvata ostosten tietoja
     */
    public void setNewPurchases(Long receiptId, List<TextInput> purchaseInputs) {
        purchaseDao.deleteByValue(toStrMap("receiptId", receiptId));
        List<Purchase> purchases = purchaseInputs.stream()
                .filter(TextInput::isValid)
                .map(pid -> new Purchase(toStrMap(pid.getAttrMap(), "receiptId", receiptId)))
                .collect(Collectors.toList());
        purchases.forEach(p -> purchaseDao.create(p));
        allPurchases.put(receiptId, purchases);
    }

    /**
     * Luo listan ehdotuksia ostostyypeistä annetulle ostosnimikkeelle.
     *
     * @param purchaseName Ostoksen nimike kuitissa
     * @return Ostostyyppejä sisältävä merkkijonolista
     */
    public ArrayList<String> getTypeSuggestions(String purchaseName) {
        ArrayList<String> suggs = new ArrayList<>();
        if (nameTypeMap.containsKey(purchaseName)) {
            suggs.addAll(nameTypeMap.get(purchaseName));
        }
        return suggs;
    }
}
