package fi.frt.domain;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import static fi.frt.utilities.MappingUtils.toMap;


public class Receipt {
    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final SimpleStringProperty place = new SimpleStringProperty();
    private final SimpleObjectProperty<BigDecimal> sum = new SimpleObjectProperty<>();
    private final SimpleStringProperty buyer = new SimpleStringProperty();
    private ArrayList<Purchase> purchases;

    public Receipt() {
    }

    public Long getId() {
        return id.get();
    }

    public LocalDate getDate() {
        return date.get();
    }

    public String getPlace() {
        return place.get();
    }

    public BigDecimal getSum() {
        return sum.get();
    }

    public String getBuyer() {
        return buyer.get();
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public SimpleStringProperty placeProperty() {
        return place;
    }

    public SimpleObjectProperty<BigDecimal> sumProperty() {
        return sum;
    }

    public SimpleStringProperty buyerProperty() {
        return buyer;
    }

    public ArrayList<Purchase> getPurchases() {
        return purchases;
    }

    public Map<String, Object> getAttributeMap() {
        return toMap(
                "date", date.get(),
                "place", place.get(),
                "sum", sum.get(),
                "buyer", buyer.get()
        );
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public void setPlace(String place) {
        this.place.set(place);
    }

    public void setSum(BigDecimal sum) {
        this.sum.set(sum);
    }

    public void setBuyer(String buyer) {
        this.buyer.set(buyer);
    }

    public void setPurchases(ArrayList<Purchase> purchases) {
        this.purchases = purchases;
    }

}
