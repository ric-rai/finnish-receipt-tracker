package fi.frt.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

import static fi.frt.utilities.CurrencyUtils.CURRENCY_PATTERN;
import static fi.frt.utilities.DateUtils.DATE_FORMATTER;
import static fi.frt.utilities.DateUtils.DATE_PATTERN;

public class ReceiptInputData {
    private boolean valid;
    private String dateStr;
    private String placeStr;
    private String sumStr;
    private String buyerStr;
    private Set<String> invalidFields = new HashSet<>();
    private LocalDate date;
    private String place;
    private BigDecimal sum;
    private String buyer;

    public Set<String> validate() {
        valid = true;
        if (DATE_PATTERN.matcher(dateStr).matches()) {
            try {
                date = LocalDate.parse(dateStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                invalidateDate();
            }
        } else {
            invalidateDate();
        }
        if (CURRENCY_PATTERN.matcher(sumStr).matches()) {
            try {
                sum = new BigDecimal(sumStr);
            } catch (NumberFormatException e) {
                invalidateSum();
            }
        } else {
            invalidateSum();
        }
        if (placeStr.isEmpty()) {
            invalidatePlace();
        } else {
            place = placeStr;
        }
        if (buyerStr.isEmpty()) {
            invalidateBuyer();
        } else {
            buyer = buyerStr;
        }
        return invalidFields;
    }

    public boolean isValid() {
        return valid;
    }

    private void invalidateBuyer() {
        valid = false;
        invalidFields.add("buyer");
    }

    private void invalidatePlace() {
        valid = false;
        invalidFields.add("place");
    }

    private void invalidateSum() {
        valid = false;
        invalidFields.add("sum");
    }

    private void invalidateDate() {
        valid = false;
        invalidFields.add("date");
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public void setPlaceStr(String placeStr) {
        this.placeStr = placeStr;
    }

    public void setSumStr(String sumStr) {
        this.sumStr = sumStr.replace(",", ".");
    }

    public void setBuyerStr(String buyerStr) {
        this.buyerStr = buyerStr;
    }

    public Set<String> getInvalidFields() {
        return invalidFields;
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
