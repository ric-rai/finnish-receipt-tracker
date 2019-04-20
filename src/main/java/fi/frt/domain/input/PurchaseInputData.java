package fi.frt.domain.input;

import fi.frt.utilities.MappingUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static fi.frt.utilities.CurrencyUtils.CURRENCY_PATTERN;
import static fi.frt.utilities.MappingUtils.setProperty;

public class PurchaseInputData implements InputData {
    private boolean valid;
    private boolean hasChanged;
    private String nameStr = "";
    private String quantityStr = "";
    private String priceStr = "";
    private String typeStr = "";
    private final Set<String> invalidFields = new HashSet<>();
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private String type;

    public PurchaseInputData() {
    }

    public PurchaseInputData(Map<String, Object> map) {
        setFromMap(map);
    }

    public Set<String> validate() {
        valid = true;
        hasChanged = false;
        invalidFields.clear();
        validateName();
        validateQuantity();
        validatePrice();
        validateType();
        return invalidFields;
    }

    public boolean isValid() {
        if (hasChanged) {
            validate();
        }
        return valid;
    }

    private void validateName() {
        if (nameStr.trim().isEmpty()) {
            valid = false;
            invalidFields.add("nameStr");
        } else {
            name = nameStr;
        }
    }

    private void validateQuantity() {
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (Exception e) {
            valid = false;
            invalidFields.add("quantityStr");
        }
    }

    private void validatePrice() {
        if (CURRENCY_PATTERN.matcher(priceStr).matches()) {
            try {
                price = new BigDecimal(priceStr);
            } catch (Exception e) {
                valid = false;
                invalidFields.add("priceStr");
            }
        } else {
            valid = false;
            invalidFields.add("priceStr");
        }
    }

    private void validateType() {
        if (typeStr.trim().isEmpty()) {
            valid = false;
            invalidFields.add("typeStr");
        } else {
            type = typeStr;
        }
    }

    public Set<String> getInvalidFields() {
        if (hasChanged) {
            return validate();
        }
        return invalidFields;
    }

    public String getNameStr() {
        return nameStr;
    }

    public String getQuantityStr() {
        return quantityStr;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setNameStr(String nameStr) {
        hasChanged = true;
        this.nameStr = nameStr;
    }

    public void setQuantityStr(String quantityStr) {
        hasChanged = true;
        this.quantityStr = quantityStr;
    }

    public void setPriceStr(String priceStr) {
        hasChanged = true;
        this.priceStr = priceStr;
    }

    public void setTypeStr(String typeStr) {
        hasChanged = true;
        this.typeStr = typeStr;
    }

    public void setFromMap(Map<String, Object> map) {
        hasChanged = true;
        map.forEach((k, v) -> setProperty(this, k + "Str", v.toString()));
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public Map<String, Object> getAttrMap() {
        return MappingUtils.toStrMap(
                "name", name,
                "quantity", quantity,
                "price", price,
                "type", type
        );
    }
}
