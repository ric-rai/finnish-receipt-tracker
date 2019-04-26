package fi.frt.domain;

import fi.frt.domain.textinput.PurchaseTextInput;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PurchaseTextInputTest {
    @Test
    public void purchaseValidationWorksCorrectly() {
        Set<String> fields;
        fields = validatePurchaseInputs(true, "Name", "0", "0", "Type");
        assertThat(fields.isEmpty(), is(true));
        fields = validatePurchaseInputs(false, "  ", "abc", "1.123", "");
        assertThat(fields.contains("name"), is(true));
        assertThat(fields.contains("quantity"), is(true));
        assertThat(fields.contains("price"), is(true));
        assertThat(fields.contains("type"), is(true));
    }

    public Set<String> validatePurchaseInputs(boolean expected, String nameStr, String quantityStr, String priceStr, String typeStr) {
        PurchaseTextInput purchaseInput = new PurchaseTextInput();
        purchaseInput.setNameStr(nameStr);
        purchaseInput.setQuantityStr(quantityStr);
        purchaseInput.setPriceStr(priceStr);
        purchaseInput.setTypeStr(typeStr);
        assertThat(purchaseInput.isValid(), is(expected));
        if (purchaseInput.isValid()) {
            BigDecimal price = new BigDecimal(priceStr);
            assertThat(purchaseInput.getName(), is(nameStr));
            assertThat(purchaseInput.getQuantity(), is(Integer.valueOf(quantityStr)));
            assertThat(purchaseInput.getPrice(), is(price));
            assertThat(purchaseInput.getType(), is(typeStr));
        }
        return purchaseInput.getInvalidFields();
    }
}
