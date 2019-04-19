package fi.frt.domain.input;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static fi.frt.utilities.CurrencyUtils.CURRENCY_PATTERN;
import static fi.frt.utilities.DateUtils.DATE_FORMATTER;
import static fi.frt.utilities.DateUtils.DATE_PATTERN;

public class ReceiptInputData implements InputData {
    private boolean valid;
    private boolean hasChanged;
    private String dateStr = "";
    private String placeStr = "";
    private String sumStr = "";
    private String buyerStr = "";
    private Set<String> invalidFields = new HashSet<>();
    private LocalDate date;
    private String place;
    private BigDecimal sum;
    private String buyer;

    public Set<String> validate() {
        valid = true;
        hasChanged = false;
        invalidFields.clear();
        validateDate();
        validatePlace();
        validateSum();
        validateBuyer();
        return invalidFields;
    }

    public boolean isValid() {
        if (hasChanged) {
            validate();
        }
        return valid;
    }

    private void validateDate() {
        if (DATE_PATTERN.matcher(dateStr).matches()) {
            try {
                date = LocalDate.parse(dateStr, DATE_FORMATTER);
            } catch (Exception e) {
                valid = false;
                invalidFields.add("date");
            }
        } else {
            valid = false;
            invalidFields.add("date");
        }
    }

    private void validatePlace() {
        if (placeStr.trim().isEmpty()) {
            valid = false;
            invalidFields.add("place");
        } else {
            place = placeStr;
        }
    }

    private void validateSum() {
        if (CURRENCY_PATTERN.matcher(sumStr).matches()) {
            try {
                sum = new BigDecimal(sumStr);
            } catch (Exception e) {
                valid = false;
                invalidFields.add("sum");
                ;
            }
        } else {
            valid = false;
            invalidFields.add("sum");
        }
    }

    private void validateBuyer() {
        if (buyerStr.trim().isEmpty()) {
            valid = false;
            invalidFields.add("buyer");
        } else {
            buyer = buyerStr;
        }
    }

    public Set<String> getInvalidFields() {
        if (hasChanged) {
            return validate();
        }
        return invalidFields;
    }

    public void setDateStr(String dateStr) {
        hasChanged = true;
        this.dateStr = dateStr;
    }

    public void setPlaceStr(String placeStr) {
        hasChanged = true;
        this.placeStr = placeStr;
    }

    public void setSumStr(String sumStr) {
        hasChanged = true;
        this.sumStr = sumStr.replace(",", ".");
    }

    public void setBuyerStr(String buyerStr) {
        hasChanged = true;
        this.buyerStr = buyerStr;
    }

    public ReceiptInputData setAllFromMap(Map<String, Object> map) {
        hasChanged = true;
        dateStr = map.get("date").toString();
        placeStr = map.get("place").toString();
        sumStr = map.get("sum").toString();
        buyerStr = map.get("buyer").toString();
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public String getBuyer() {
        return buyer;
    }
}
