package fi.frt;

import fi.frt.dao.PurchaseDao;
import fi.frt.dao.ReceiptDao;
import fi.frt.domain.Purchase;
import fi.frt.domain.PurchaseService;
import fi.frt.domain.Receipt;
import fi.frt.domain.ReceiptService;
import fi.frt.domain.textinput.PurchaseTextInput;
import fi.frt.domain.textinput.ReceiptTextInput;
import fi.frt.domain.textinput.TextInput;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static fi.frt.utilities.MappingUtils.toStrMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IntegrationTest {
    @Autowired private JdbcTemplate jdbcTemplate;
    private List<Receipt> receiptList = new ArrayList<>();
    private List<Purchase> purchaseList = new ArrayList<>();
    private ReceiptService receiptService;
    private ReceiptDao receiptDao;
    private PurchaseService purchaseService;
    private PurchaseDao purchaseDao;

    @Before
    public void before() {
        jdbcTemplate.execute(
                "DELETE FROM Receipt WHERE Receipt.id > 0;" +
                "ALTER TABLE Receipt ALTER COLUMN id RESTART WITH 1;"
        );
        receiptDao = new ReceiptDao(jdbcTemplate);
        receiptService = new ReceiptService(receiptDao);
        receiptService.setReceiptList(receiptList);
        receiptService.setTinifyKey((String) readPropertiesFile().get("tinify-key"));
        purchaseDao = new PurchaseDao(jdbcTemplate);
        purchaseService = new PurchaseService(purchaseDao);
        purchaseService.setActivePurchasesList(purchaseList);
    }

    @Test
    public void basicUseCases() {
        receiptDao.list().forEach(r -> System.out.println(r.getPlace()));
        Receipt receipt = createNewReceipt();
        assertThat(receiptDao.list().size(), is(1));
        assertThat(purchaseDao.list().size(), is(3));
        deleteReceipt(receipt);
        assertThat(receiptDao.list().size(), is(0));
        assertThat(purchaseDao.list().size(), is(0));
    }

    private Receipt createNewReceipt() {
        ReceiptTextInput receiptTextInput = new ReceiptTextInput();
        receiptTextInput.setDateStr("01.02.2019");
        receiptTextInput.setPlaceStr("Place");
        receiptTextInput.setSumStr("10.00");
        receiptTextInput.setBuyerStr("Buyer");
        receiptTextInput.isValid();
        List<TextInput> purchaseInputs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            purchaseInputs.add(new PurchaseTextInput(toStrMap(
                    "name", "valid name",
                    "quantity", "1",
                    "price", "100.25",
                    "type", "valid type"
            )));
        }
        purchaseInputs.add(new PurchaseTextInput());
        Receipt receipt = receiptService.newReceipt(receiptTextInput, new byte[]{});
        purchaseService.setNewPurchases(receipt.getId(), purchaseInputs);
        return receipt;
    }

    public void deleteReceipt(Receipt receipt) {
        receiptService.deleteReceipt(receipt);
    }

    private Properties readPropertiesFile() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(getResource("application.properties").getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    private URL getResource(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResource(path);
    }


}
