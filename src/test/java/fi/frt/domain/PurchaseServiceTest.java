package fi.frt.domain;

import fi.frt.dao.Dao;
import fi.frt.domain.textinput.TextInput;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static fi.frt.utilities.MappingUtils.getProperty;
import static fi.frt.utilities.MappingUtils.toStrMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class PurchaseServiceTest {
    private PurchaseService purchaseService;
    private List<Purchase> testList;
    private List<Purchase> activePurchasesList;

    public PurchaseServiceTest() {
        testList = new ArrayList<>();
        toTestList(1, "Salad", "Grocery", 1);
        toTestList(2, "Salad", "Food", 1);
        toTestList(3, "Salad", "Vegetable", 1);
        toTestList(4, "Purchase", "Purchase", 2);
        toTestList(5, "Purchase", "Purchase", 2);
        toTestList(6, "Purchase", "Purchase", 2);

        activePurchasesList = new ArrayList<>();
        purchaseService = new PurchaseService(new FakePurchaseDao());
        purchaseService.setActivePurchasesList(activePurchasesList);
    }

    private void toTestList(long id, String name, String type, long rId) {
        int q = 1;
        BigDecimal p = new BigDecimal(1);
        testList.add(new Purchase(
                toStrMap("id", id, "name", name, "type", type, "quantity", q, "price", p, "receiptId", rId))
        );
    }

    @Test
    public void setActivePurchasesWorksCorrectly() {
        purchaseService.setActivePurchases(1L);
        assertThat(activePurchasesList.size(), is(3));
        assertThat(activePurchasesList.get(0).getType(), is("Grocery"));
        assertThat(activePurchasesList.get(1).getType(), is("Food"));
        assertThat(activePurchasesList.get(2).getType(), is("Vegetable"));
    }
    
    @Test
    public void setNewPurchasesWorksCorrectly() {
        int initialTestListSize =  testList.size();
        List<TextInput> purchaseInputs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            purchaseInputs.add(new FakePurInput(true));
            purchaseInputs.add(new FakePurInput(false));
        }
        purchaseService.setNewPurchases(1L, purchaseInputs);
        assertThat(testList.size(), is(initialTestListSize));
        List<Purchase> list = testList.stream().filter(p -> p.getReceiptId() == 1L).collect(Collectors.toList());
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getName(), is("new name"));
        assertThat(list.get(1).getName(), is("new name"));
        assertThat(list.get(2).getName(), is("new name"));
        purchaseService.setActivePurchases(1L);
        assertThat(activePurchasesList.size(), is(3));
        assertThat(activePurchasesList.get(0).getName(), is("new name"));
        assertThat(activePurchasesList.get(1).getName(), is("new name"));
        assertThat(activePurchasesList.get(2).getName(), is("new name"));
    }

    @Test
    public void getTypeSuggestionsWorksCorrectly() {
        Set<String> suggs = purchaseService.getTypeSuggestions("Salad");
        assertThat(suggs.size(), is(3));
        suggs = purchaseService.getTypeSuggestions("Purchase");
        assertThat(suggs.size(), is(1));
    }

    class FakePurchaseDao implements Dao<Purchase, Long> {

        @Override
        public Long create(Purchase purchase) {
            testList.add(purchase);
            return null;
        }

        @Override
        public Purchase get(Long key) {
            return null;
        }

        @Override
        public List<Purchase> getByValue(Map<String, Object> map) {
            return null;
        }

        @Override
        public void update(Purchase object) {
        }

        @Override
        public void delete(Long key) {
            testList.remove(key.intValue());
        }

        @Override
        public void deleteByValue(Map<String, Object> map) {
            Map.Entry<String, Object> entry = map.entrySet().iterator().next();
            List<Purchase> matches = testList.stream().filter(p -> {
                        Object object = getProperty(p, entry.getKey());
                        Class c = entry.getValue().getClass();
                        return c.cast(object).equals(entry.getValue());
                    }).collect(Collectors.toList());
            testList.removeAll(matches);
        }

        @Override
        public List<Purchase> list() {
            return testList;
        }
    }

    class FakePurInput implements TextInput {
        private boolean valid;

        FakePurInput(boolean valid) {
            this.valid = valid;
        }

        @Override
        public boolean isValid() {
            return valid;
        }

        @Override
        public Set<String> getInvalidFields() {
            return null;
        }

        @Override
        public Map<String, Object> getAttrMap() {
            return toStrMap("name", "new name");
        }

        @Override
        public void setFromMap(Map<String, Object> map) {
        }
    }
}
