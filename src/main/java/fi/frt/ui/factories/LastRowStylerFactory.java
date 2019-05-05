package fi.frt.ui.factories;

import javafx.css.PseudoClass;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class LastRowStylerFactory<S> implements Callback<TableView<S>, TableRow<S>> {

    @Override
    public TableRow<S> call(TableView<S> tableView) {
        PseudoClass lastRow = PseudoClass.getPseudoClass("last-row");
        return new TableRow() {
            @Override
            public void updateIndex(int index) {
                super.updateIndex(index);
                pseudoClassStateChanged(lastRow,
                        index >= 0 && index == tableView.getItems().size() - 1);
            }
        };
    }
}
