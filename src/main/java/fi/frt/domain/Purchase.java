package fi.frt.domain;

import java.math.BigDecimal;
import java.util.Map;

import static fi.frt.utilities.MappingUtils.setProperty;
import static fi.frt.utilities.MappingUtils.toStrMap;

public class Purchase {
    private Long id;
    private Long receiptId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private String type;

    public Purchase() {
    }

    public Purchase(Map<String, Object> map) {
        setFromMap(map);
    }

    public Long getId() {
        return id;
    }

    public Long getReceiptId() {
        return receiptId;
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
        return toStrMap(
                "id", id,
                "receiptId", receiptId,
                "name", name,
                "quantity", quantity,
                "price", price,
                "type", type
        );
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFromMap(Map<String, Object> map) {
        map.forEach((k, v) -> setProperty(this, k, v));
    }

}
