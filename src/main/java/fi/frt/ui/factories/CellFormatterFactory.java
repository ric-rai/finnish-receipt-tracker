package fi.frt.ui.factories;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;

public class CellFormatterFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
    private Function<T, String> formatter;

    public CellFormatterFactory(Function<T, String> formatter) {
        super();
        this.formatter = formatter;
    }

    @Override
    public TableCell<S, T> call(TableColumn<S, T> arg0) {
        return new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) setGraphic(null);
                else setGraphic(new Label(formatter.apply(item)));
            }
        };
    }
}