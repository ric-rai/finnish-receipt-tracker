package fi.frt.ui;

import fi.frt.dao.PurchaseDao;
import fi.frt.dao.ReceiptDao;
import fi.frt.domain.Purchase;
import fi.frt.domain.PurchaseService;
import fi.frt.domain.Receipt;
import fi.frt.domain.ReceiptService;
import fi.frt.domain.textinput.PurchaseTextInput;
import fi.frt.domain.textinput.ReceiptTextInput;
import fi.frt.domain.textinput.TextInput;
import fi.frt.ui.components.ZoomableImageView;
import fi.frt.ui.factories.CellFormatterFactory;
import fi.frt.ui.factories.EditableCellFactory;
import fi.frt.ui.factories.LastRowStylerFactory;
import fi.frt.ui.factories.TypeSuggestionCellFactory;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static fi.frt.utilities.DateUtils.DATE_FORMATTER;
import static fi.frt.utilities.MappingUtils.setProperty;
import static fi.frt.utilities.MappingUtils.toStrMap;

@Component
public class FXMLController {

    private Stage stage;
    @FXML private String title;
    @FXML private TableView<Receipt> receiptTable;
    @FXML private Label receiptId;
    @FXML private TextField sum;
    @FXML private TextField date;
    @FXML private TextField place;
    @FXML private TextField buyer;
    @FXML private TableColumn<Receipt, LocalDate> r_date;
    @FXML private TableColumn<TextInput, String> p_name;
    @FXML private TableColumn<TextInput, String> p_quantity;
    @FXML private TableColumn<TextInput, String> p_price;
    @FXML private TableColumn<TextInput, String> p_type;
    @FXML private TableColumn<TextInput, Void> p_suggestions;
    @FXML private TableView<TextInput> purchaseInputTable;
    @FXML public ZoomableImageView imageView;

    @FXML private String newPurchaseRow;
    @FXML private String p_nameD;
    @FXML private String p_quantityD;
    @FXML private String p_priceD;
    @FXML private String p_typeD;

    @FXML private String imgAlertTitle;
    @FXML private String imgAlert;
    @FXML private String fileChooserTitle;
    @FXML private String allImagesText;

    private ReceiptService receiptService;
    private PurchaseService purchaseService;

    private SelectionModel<Receipt> receiptSelModel;
    private Map<String, TextField> receiptFields;
    private Map<String, TableColumn<TextInput, String>> purchaseColumns;
    private final List<Receipt> receiptList = FXCollections.observableArrayList(r -> new Observable[]{});
    private final List<Purchase> purchaseList = new ArrayList<>();
    private Map<String, Object> purchaseDefaults;
    private final ObservableList<TextInput> purInputList = FXCollections.observableArrayList(r -> new Observable[]{});


    public void init(Stage stage, JdbcTemplate jdbcTemplate){
        this.stage = stage;
        stage.setTitle(title);
        purchaseService = new PurchaseService(new PurchaseDao(jdbcTemplate), purchaseList);
        receiptService = new ReceiptService(new ReceiptDao(jdbcTemplate), receiptList);
        receiptFields = toStrMap("date", date, "sum", sum, "place", place, "buyer", buyer);
        purchaseColumns = toStrMap("name", p_name, "quantity", p_quantity, "price", p_price, "type", p_type);
        purchaseDefaults = toStrMap("name", p_nameD, "quantity", p_quantityD, "price", p_priceD, "type", p_typeD);
        imageView.initialize();
        initReceiptTable();
        initPurchaseTable();
    }

    private void initReceiptTable() {
        receiptSelModel = receiptTable.getSelectionModel();
        List<TableColumn<Receipt, ?>> columns = receiptTable.getColumns();
        columns.forEach(c -> c.setCellValueFactory(new PropertyValueFactory<>(c.getId().split("_")[1])));
        r_date.setCellFactory(new CellFormatterFactory<>(ld -> ld.format(DATE_FORMATTER)));
        r_date.setSortType(TableColumn.SortType.DESCENDING);
        SortedList<Receipt> sortedList = new SortedList<>((ObservableList<Receipt>)receiptList);
        sortedList.comparatorProperty().bind(receiptTable.comparatorProperty());
        receiptTable.setItems(sortedList);
        receiptTable.getSortOrder().add(r_date);
        receiptSelModel.selectedIndexProperty().addListener(this::handleReceiptTableSelectionChange);
        receiptSelModel.selectFirst();
    }

    private void initPurchaseTable() {
        purchaseColumns.values().forEach(column -> {
            column.setSortable(false);
            column.setCellValueFactory(new PropertyValueFactory<>(column.getId().split("_")[1] + "Str"));
            column.setCellFactory(new EditableCellFactory());
            column.setOnEditStart(this::handlePurchaseCellEditStart);
            column.setOnEditCommit(this::handlePurchaseCellCommitAndCancel);
            column.setOnEditCancel(this::handlePurchaseCellCommitAndCancel);
        });
        p_suggestions.setCellFactory(new TypeSuggestionCellFactory(purchaseService));
        purchaseInputTable.setRowFactory(new LastRowStylerFactory<>());
        setPurchaseInputList();
        purchaseInputTable.setItems(purInputList);
        purchaseInputTable.setEditable(true);
    }

    private void setPurchaseInputList(){
        Receipt receipt = receiptSelModel.getSelectedItem();
        purchaseService.setActivePurchases(receipt != null ? receipt.getId() : null);
        purInputList.setAll(purchaseList.stream()
                .map(p -> new PurchaseTextInput(p.getAttrMap())).collect(Collectors.toList()));
        purInputList.add(new PurchaseTextInput(toStrMap("name", newPurchaseRow)));
        purchaseInputTable.refresh();
        calculateSum();
    }

    private void handlePurchaseCellCommitAndCancel(CellEditEvent<TextInput, String> cellEvent) {
        calculateSum();
    }

    private void calculateSum() {
        BigDecimal s = purInputList.stream()
                .filter(TextInput::isValid)
                .map(inputData -> (PurchaseTextInput) inputData)
                .map(pid -> pid.getPrice().multiply(new BigDecimal(pid.getQuantity())))
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        sum.setText(s.toString());
    }

    private void handlePurchaseCellEditStart(CellEditEvent<TextInput, String> cellEvent) {
        int row = cellEvent.getTablePosition().getRow();
        if (row == purchaseInputTable.getItems().size() - 1) {
            purInputList.get(row).setFromMap(purchaseDefaults);
            purchaseInputTable.getItems().add(new PurchaseTextInput(toStrMap("name", newPurchaseRow)));
            purchaseInputTable.refresh();
        }
    }

    private void handleReceiptTableSelectionChange(Observable obs, Number old, Number newSelection) {
        if (newSelection.intValue() == -1) return;
        Receipt selectedReceipt = receiptSelModel.getSelectedItem();
        receiptId.setText(String.format("%7s", selectedReceipt.getId()).replace(" ", "0"));
        date.setText(selectedReceipt.getDate().format(DATE_FORMATTER));
        place.setText(selectedReceipt.getPlace());
        buyer.setText(selectedReceipt.getBuyer());
        receiptFields.values().forEach(field -> field.getStyleClass().remove("invalid"));
        setPurchaseInputList();
        if (selectedReceipt.getImage() != null) {
            imageView.setZoomableImage(selectedReceipt.getImage());
        } else {
            imageView.setZoomableImage(new byte[]{});
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        TextInput textInput = new ReceiptTextInput();
        receiptFields.forEach((fName, field) -> setProperty(textInput, fName + "Str", field.getText()));
        receiptFields.values().forEach(field -> field.getStyleClass().remove("invalid"));
        textInput.getInvalidFields().forEach(fName -> receiptFields.get(fName).getStyleClass().add("invalid"));
        if(!textInput.isValid()) return;
        sendReceiptInputData(textInput);
        receiptTable.refresh();
    }

    private void sendReceiptInputData(TextInput rcptInput){
        Receipt selected = receiptSelModel.getSelectedItem();
        if (selected != null) {
            receiptService.updateReceipt(selected, rcptInput, imageView.getImage());
            purchaseService.setNewPurchases(selected.getId(), purInputList);
        } else {
            Receipt newReceipt = receiptService.newReceipt(rcptInput, imageView.getImage());
            purchaseService.setNewPurchases(newReceipt.getId(), purInputList);
            receiptSelModel.select(newReceipt);
        }
        setPurchaseInputList();
    }

    @FXML
    private void handleNewReceipt(ActionEvent actionEvent) {
        receiptSelModel.clearSelection();
        receiptId.setText("");
        receiptFields.values().forEach(TextInputControl::clear);
        setPurchaseInputList();
        imageView.setZoomableImage(new byte[]{});
    }

    @FXML
    public void addImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(allImagesText, "*.png*", "*.jpg"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        fileChooser.setTitle(fileChooserTitle);
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) return;
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            try {
                imageView.setZoomableImage(receiptService.prepareNewImage(file));
                return true;
            } catch (Exception ignored) {
                return false;
            }
        }, Platform::runLater);
        future.thenAccept(success -> {
            if (!success) alert();
        });
        future.exceptionally(throwable -> false);
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(imgAlertTitle);
        alert.setHeaderText(null);
        alert.setContentText(imgAlert);
        alert.showAndWait();
    }

}
