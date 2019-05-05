package fi.frt.ui.factories;

import fi.frt.domain.textinput.TextInput;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

import static fi.frt.utilities.MappingUtils.getStrProperty;
import static fi.frt.utilities.MappingUtils.setProperty;

public class EditableCellFactory implements Callback<TableColumn<TextInput, String>, TableCell<TextInput, String>> {

    @Override
    public TableCell<TextInput, String> call(TableColumn<TextInput, String> stTableColumn) {
        return new EditableCell(stTableColumn);
    }

    public class EditableCell extends TableCell<TextInput, String> {
        boolean invalid;
        private TextField textField;
        private String propertyName;
        private TextInput input;
        private boolean initialized;

        EditableCell(TableColumn<TextInput, String> column) {
            super();
            this.propertyName = ((PropertyValueFactory)column.getCellValueFactory()).getProperty();
        }

        private void init() {
            int row = getTableRow().getIndex();
            input = getTableView().getItems().get(row);
            createTextField();
            initialized = true;
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(String.valueOf(getItem()));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void startEdit() {
            super.startEdit();
            textField.setText(getStrProperty(input, propertyName));
            setText(null);
            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.requestFocus();
        }

        @Override
        public void commitEdit(String item) {
            setProperty(input, propertyName, item);
            super.commitEdit(item);
            getTableView().refresh();
        }

        @Override
        public void updateItem(String newText, boolean empty) {
            super.updateItem(newText, empty);
            if (empty || newText == null) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) textField.setText(newText);
                    setText(null);
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(newText);
                    setGraphic(null);
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
                if (!initialized) init();
                if (initialized) {
                    boolean invalid = input.getInvalidFields().contains(propertyName.replace("Str", ""));
                    int row = getTableRow().getIndex();
                    int itemSize = getTableView().getItems().size();
                    if (invalid && row != itemSize - 1 && !(row == itemSize - 2 && newText.isEmpty())) {
                        if (!this.invalid) {
                            getStyleClass().add("invalid");
                            this.invalid = true;
                        }
                    } else {
                        if (this.invalid) {
                            getStyleClass().remove("invalid");
                            this.invalid = false;
                        }
                    }
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getItem());
            textField.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) commitEdit(textField.getText());
                else if (keyEvent.getCode() == KeyCode.ESCAPE) cancelEdit();
            });
            textField.focusedProperty().addListener((obs, hasOld, hasNew) -> {
                if (!hasNew) {
                    commitEdit(textField.getText());
                    updateItem(textField.getText(), false);
                }
            });
        }
    }
}
