package fi.frt.domain;

import fi.frt.utilities.MappingUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static fi.frt.utilities.MappingUtils.setProperty;


public class Receipt {
    private long id;
    private LocalDate date;
    private String place;
    private BigDecimal sum;
    private String buyer;

    public Receipt() {
    }

    public Receipt(Map<String, Object> map) {
        setFromMap(map);
    }

    public long getId() {
        return id;
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

    public Map<String, Object> getAttributeMap() {
        return MappingUtils.toStrMap(
                "date", date,
                "place", place,
                "sum", sum,
                "buyer", buyer
        );
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public void setFromMap(Map<String, Object> map) {
        map.forEach((k, v) -> setProperty(this, k, v));
    }


}
