package fi.frt.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;


public class Receipt {
    private final SimpleLongProperty id = new SimpleLongProperty();
    @JsonSerialize(using = LocalDateSerializer.class)
    private final SimpleObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final SimpleStringProperty place = new SimpleStringProperty();
    private final SimpleObjectProperty<BigDecimal> sum = new SimpleObjectProperty<>();
    private final SimpleStringProperty buyer = new SimpleStringProperty();
    private ArrayList<Purchase> purchases;

    public Receipt() { }

    public Receipt(LocalDate date, String place, BigDecimal sum, String buyer, ArrayList<Purchase> purchases) {
        this.date.set(date);
        this.place.set(place);
        this.sum.set(sum);
        this.buyer.set(buyer);
        this.purchases = purchases;
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

    public ArrayList<Purchase> getPurchases() {
        return purchases;
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
