package fi.frt.ui;

import fi.frt.dao.PurchaseDao;
import fi.frt.dao.ReceiptDao;
import fi.frt.domain.Purchase;
import fi.frt.domain.PurchaseService;
import fi.frt.domain.Receipt;
import fi.frt.domain.ReceiptService;
import fi.frt.domain.input.InputData;
import fi.frt.domain.input.PurchaseInputData;
import fi.frt.domain.input.ReceiptInputData;
import fi.frt.ui.factories.CellFormatterFactory;
import fi.frt.ui.factories.EditableCellFactory;
import fi.frt.ui.factories.LastRowStylerFactory;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static fi.frt.utilities.DateUtils.DATE_FORMATTER;
import static fi.frt.utilities.MappingUtils.setProperty;
import static fi.frt.utilities.MappingUtils.toMap;

@Component
public class FXMLController {

    @FXML private TableView<Receipt> receiptTable;
    @FXML private Label receiptId;
    @FXML private TextField sum;
    @FXML private TextField date;
    @FXML private TextField place;
    @FXML private TextField buyer;
    @FXML private TableColumn<Receipt, LocalDate> r_date;
    @FXML private TableColumn<InputData, String> p_name;
    @FXML private TableColumn<InputData, String> p_quantity;
    @FXML private TableColumn<InputData, String> p_price;
    @FXML private TableColumn<InputData, String> p_type;
    @FXML private TableView<PurchaseInputData> purchaseInputTable;
    @FXML private String textForNewPurchaseRow;
    @FXML private String p_nameD;
    @FXML private String p_quantityD;
    @FXML private String p_priceD;
    @FXML private String p_typeD;

    @Autowired JdbcTemplate jdbcTemplate;
    private ReceiptService receiptService;
    private PurchaseService purchaseService;

    private SelectionModel<Receipt> receiptSelModel;
    private Map<String, TextField> receiptFields;
    private Map<String, TableColumn<InputData, String>> purchaseColumns;
    private final List<Receipt> receiptList = FXCollections.observableArrayList(r -> new Observable[]{});
    private final List<Purchase> purchaseList = new ArrayList<>();
    private Map<String, Object> purchaseDefaults;
    private final ObservableList<PurchaseInputData> purchaseInputList =
            FXCollections.observableArrayList(r -> new Observable[]{});

    public void init(){
        receiptService = new ReceiptService(new ReceiptDao(jdbcTemplate), receiptList);
        purchaseService = new PurchaseService(new PurchaseDao(jdbcTemplate), purchaseList);
        receiptFields = toMap("date", date, "sum", sum, "place", place, "buyer", buyer);
        purchaseColumns = toMap("name", p_name, "quantity", p_quantity, "price", p_price, "type", p_type);
        purchaseDefaults = toMap("name", p_nameD, "quantity", p_quantityD, "price", p_priceD, "type", p_typeD);
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
            column.setCellValueFactory(new PropertyValueFactory<>(column.getId().split("_")[1] + "Str"));
            column.setCellFactory(new EditableCellFactory());
            column.setOnEditStart(this::handlePurchaseCellEditStart);
            column.setOnEditCommit(this::handlePurchaseCellEditCommitAndCancel);
            column.setOnEditCancel(this::handlePurchaseCellEditCommitAndCancel);
        });
        purchaseInputTable.setRowFactory(new LastRowStylerFactory<>());
        setPurchaseInputList();
        purchaseInputTable.setItems(purchaseInputList);
        purchaseInputTable.setEditable(true);
    }

    private void handlePurchaseCellEditCommitAndCancel(CellEditEvent<InputData, String> cellEvent) {
        calculatePurchaseInputSum();
    }

    private void calculatePurchaseInputSum() {
        BigDecimal s = purchaseInputList.stream()
                .filter(PurchaseInputData::isValid)
                .map(PurchaseInputData::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        sum.setText(s.toString());
    }

    private void handlePurchaseCellEditStart(CellEditEvent<InputData, String> cellEvent) {
        int row = cellEvent.getTablePosition().getRow();
        if (row == purchaseInputTable.getItems().size() - 1) {
            purchaseInputList.get(row).setFromMap(purchaseDefaults);
            purchaseInputTable.getItems().add(new PurchaseInputData(toMap("name", textForNewPurchaseRow)));
            purchaseInputTable.refresh();
        }
    }

    private void setPurchaseInputList(){
        Receipt receipt = receiptSelModel.getSelectedItem();
        purchaseService.setActivePurchases(receipt != null ? receipt.getId() : null);
        purchaseInputList.setAll(purchaseList.stream()
                .map(p -> new PurchaseInputData(p.getAttrMap())).collect(Collectors.toList()));
        purchaseInputList.add(new PurchaseInputData(toMap("name", textForNewPurchaseRow)));
        calculatePurchaseInputSum();
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
        purchaseInputTable.refresh();
    }

    @FXML
    private void handleSave(ActionEvent event) {
        ReceiptInputData inputData = new ReceiptInputData();
        receiptFields.forEach((fName, field) -> setProperty(inputData, fName + "Str", field.getText()));
        receiptFields.values().forEach(field -> field.getStyleClass().remove("invalid"));
        inputData.validate().forEach(fName -> receiptFields.get(fName).getStyleClass().add("invalid"));
        if(!inputData.isValid()) return;
        sendReceiptInputData(inputData);
        receiptTable.refresh();
    }

    private void sendReceiptInputData(ReceiptInputData RID){
        Receipt selected = receiptSelModel.getSelectedItem();
        if (selected == null) receiptSelModel.select(receiptService.newReceipt(RID, purchaseInputList));
        else receiptService.updateReceipt(selected, RID, purchaseInputList);
    }

    @FXML
    private void handleNewReceipt(ActionEvent actionEvent) {
        receiptSelModel.clearSelection();
        receiptId.setText("");
        receiptFields.values().forEach(TextInputControl::clear);
        setPurchaseInputList();
    }

}
