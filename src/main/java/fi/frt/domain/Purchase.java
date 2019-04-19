package fi.frt.domain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.util.Map;

import static fi.frt.utilities.MappingUtils.toMap;

public class Purchase {
    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleIntegerProperty quantity = new SimpleIntegerProperty();
    private final SimpleObjectProperty<BigDecimal> price = new SimpleObjectProperty<>();
    private final SimpleStringProperty type = new SimpleStringProperty();

    public long getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public String getType() {
        return type.get();
    }

    public Map<String, Object> getAttrMap() {
        return toMap(
                "id", id.get(),
                "name", name.get(),
                "quantity", quantity.get(),
                "price", price.get(),
                "type", type.get()
        );
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setPrice(BigDecimal price) {
        this.price.set(price);
    }

    public void setType(String type) {
        this.type.set(type);
    }

}
