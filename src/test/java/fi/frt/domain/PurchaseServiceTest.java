package fi.frt.domain;

import fi.frt.dao.Dao;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fi.frt.utilities.MappingUtils.toStrMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class PurchaseServiceTest {
    private PurchaseService purchaseService;
    private List<Purchase> testList = new ArrayList<>();

    public PurchaseServiceTest() {
        toTestList("Salaatti", "Elintarvike");
        toTestList("Salaatti", "Ruoka");
        toTestList("Salaatti", "Vihannes");
        toTestList("Ostos", "Ostos");
        toTestList("Ostos", "Ostos");
        toTestList("Ostos", "Ostos");

        purchaseService = new PurchaseService(new FakePurchaseDao(), new ArrayList<>());
    }

    private void toTestList(String name, String type) {
        int q = 1;
        BigDecimal p = new BigDecimal(1);
        testList.add(new Purchase(toStrMap(
                "name", name, "type", type, "quantity", q, "price", p)));
    }

    @Test
    public void getTypeSuggestionsIsWorkingCorrectly() {
        List<String> suggs = purchaseService.getTypeSuggestions("Salaatti");
        assertThat(suggs.size(), is(3));
        suggs = purchaseService.getTypeSuggestions("Ostos");
        assertThat(suggs.size(), is(1));
    }

    class FakePurchaseDao implements Dao<Purchase, Long> {

        @Override
        public Long create(Purchase object) {
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

        }

        @Override
        public void deleteByValue(Map<String, Object> map) {

        }

        @Override
        public List<Purchase> list() {
            return testList;
        }
    }
}
