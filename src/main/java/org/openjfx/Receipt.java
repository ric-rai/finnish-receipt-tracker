package org.openjfx;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;


public class Receipt {
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    private String place;
    private BigDecimal sum;
    private String buyer;
    private ArrayList<Purchase> purchases;

    public Receipt(LocalDate date, String place, BigDecimal sum, String buyer, ArrayList<Purchase> purchases) {
        this.date = date;
        this.place = place;
        this.sum = sum;
        this.buyer = buyer;
        this.purchases = purchases;
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

    public ArrayList<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(ArrayList<Purchase> purchases) {
        this.purchases = purchases;
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
}
