package fi.frt.ui.factories;

import fi.frt.domain.PurchaseService;
import fi.frt.domain.textinput.PurchaseTextInput;
import fi.frt.domain.textinput.TextInput;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class TypeSuggestionCellFactory implements Callback<TableColumn<TextInput, Void>, TableCell<TextInput, Void>> {
    private PurchaseService purchaseService;

    public TypeSuggestionCellFactory(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @Override
    public TableCell<TextInput, Void> call(TableColumn<TextInput, Void> stTableColumn) {
        return new SuggestionCell();
    }

    public class SuggestionCell extends TableCell<TextInput, Void> {
        private final HBox container = new HBox();
        private final Button btn1 = new Button();
        private final Button btn2 = new Button();
        private final Button btn3 = new Button();
        private ArrayList<Button> buttons = new ArrayList<>();
        private PurchaseTextInput input;
        private boolean initialized;

        SuggestionCell() {
            super();
            container.getChildren().addAll(btn1, btn2, btn3);
            buttons.add(btn1); buttons.add(btn2); buttons.add(btn3);
            btn1.setOnAction(event -> setType(btn1.getText()));
            btn2.setOnAction(event -> setType(btn2.getText()));
            btn3.setOnAction(event -> setType(btn3.getText()));
        }

        private void setType(String text) {
            input.setTypeStr(text);
            getTableView().refresh();
        }

        private void init() {
            int row = getTableRow().getIndex();
            input = (PurchaseTextInput) getTableView().getItems().get(row);
            initialized = true;
        }

        @Override
        public void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                this.setGraphic(null);
            } else {
                if (!initialized) init();
                if (initialized && getTableRow().getIndex() != getTableView().getItems().size() - 1) {
                    Set<String> suggs = purchaseService.getTypeSuggestions(input.getNameStr());
                    Iterator<String> iter = suggs.iterator();
                    int size = suggs.size();
                    for (int i = 0; i < 3; i++) {
                        if (size > i) {
                            buttons.get(i).setText(iter.next());
                            buttons.get(i).setVisible(true);
                        } else {
                            buttons.get(i).setVisible(false);
                        }
                    }
                    setGraphic(container);
                } else {
                    setGraphic(null);
                }
            }
        }
    }

}
