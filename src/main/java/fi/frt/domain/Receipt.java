package fi.frt.domain;

import fi.frt.utilities.MappingUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static fi.frt.utilities.MappingUtils.setProperty;


public class Receipt {
    private Long id;
    private LocalDate date;
    private String place;
    private BigDecimal sum;
    private String buyer;
    private byte[] image = new byte[0];

    public Receipt() {
    }

    public Receipt(Map<String, Object> map) {
        setFromMap(map);
    }

    public Long getId() {
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

    public byte[] getImage() {
        return image;
    }

    /**
     * Metodi palauttaa olion attribuuttien arvot Map-olioon määriteltyinä.
     *
     * @return Map - {"nameOfAttr1", valueOfAttr1}, {"nameOfAttr2", valueOfAttr2} ...
     */
    public Map<String, Object> getAttributeMap() {
        return MappingUtils.toStrMap("id", id, "date", date, "place", place, "sum", sum, "buyer", buyer, "image", image);
    }

    public void setId(Long id) {
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

    public void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * Metodi asettaa olion attribuutit annetun Map-olion mukaisiin arvoihin. Map-oliossa tulee olla avain-arvo -pareina
     * asetettavan olion attribuuttien nimet ja arvot.
     *
     * @param map Map-olio, johon on määritelty attribuuttien uusia arvoja
     */
    public void setFromMap(Map<String, Object> map) {
        map.forEach((k, v) -> setProperty(this, k, v));
    }


}
