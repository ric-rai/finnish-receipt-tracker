package fi.frt.ui;

import fi.frt.dao.Dao;
import fi.frt.dao.PurchaseDao;
import fi.frt.dao.ReceiptDao;
import fi.frt.domain.*;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static fi.frt.utilities.DateUtils.DATE_FORMATTER;
import static fi.frt.utilities.MappingUtils.setProperty;
import static fi.frt.utilities.MappingUtils.toMap;

@Component
public class FXMLController {

    @FXML private TableView purchaseTable;
    @FXML private TableView<Receipt> receiptTable;
    @FXML private Label receiptId;
    @FXML private TextField sum;
    @FXML private TextField date;
    @FXML private TextField place;
    @FXML private TextField buyer;

    @Autowired JdbcTemplate jdbcTemplate;
    private ReceiptService receiptService;
    private PurchaseService purchaseService;
    private Dao<Receipt, Long> receiptDao;

    private SelectionModel<Receipt> receiptSelModel;
    private Map<String, TextField> receiptFields;
    private ObservableList<Receipt> receiptList;
    private ObservableList<Purchase> purchaseList;

    public void init(){
        receiptDao = new ReceiptDao(jdbcTemplate);
        receiptService = new ReceiptService(receiptDao);
        purchaseService = new PurchaseService(new PurchaseDao(jdbcTemplate));
        receiptFields = toMap("date", date, "sum", sum, "place", place, "buyer", buyer);
        initReceiptTable();
        initPurchaseTable();
    }

    private void initReceiptTable(){
        receiptSelModel = receiptTable.getSelectionModel();
        List<TableColumn<Receipt, ?>> columns = receiptTable.getColumns();
        TableColumn<Receipt, LocalDate> dateCol = (TableColumn)columns.get(0);
        dateCol.setCellFactory(new ColumnFormatter<>(ld -> ld.format(DATE_FORMATTER)));
        dateCol.setSortType(TableColumn.SortType.DESCENDING);
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("date"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("place"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("sum"));
        columns.get(3).setCellValueFactory(new PropertyValueFactory<>("buyer"));
        receiptList = FXCollections.observableArrayList(r -> new Observable[]{});
        receiptList.addAll(receiptDao.list());
        receiptService.setReceiptList(receiptList);
        SortedList<Receipt> sortedList = new SortedList<>(receiptList);
        sortedList.comparatorProperty().bind(receiptTable.comparatorProperty());
        receiptTable.setItems(sortedList);
        receiptTable.getSortOrder().add(dateCol);
        receiptSelModel.selectedIndexProperty().addListener(this::handleReceiptTableSelectionChange);
        receiptSelModel.selectFirst();
    }

    private void handleReceiptTableSelectionChange(Observable obs, Number old, Number newSelection) {
        if (newSelection.intValue() != -1) {
            Receipt selectedReceipt = receiptSelModel.getSelectedItem();
            receiptId.setText(String.format("%7s", selectedReceipt.getId()).replace(" ", "0"));
            date.setText(selectedReceipt.getDate().format(DATE_FORMATTER));
            place.setText(selectedReceipt.getPlace());
            buyer.setText(selectedReceipt.getBuyer());
            purchaseService.setPurchases(selectedReceipt.getId());
        }
    }

    private void initPurchaseTable(){
        List<TableColumn<Purchase, ?>> columns = purchaseTable.getColumns();
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("price"));
        columns.get(3).setCellValueFactory(new PropertyValueFactory<>("type"));
        purchaseList = FXCollections.observableArrayList(r -> new Observable[]{});
        purchaseService.setPurchaseList(purchaseList);
        purchaseService.setPurchases(receiptSelModel.getSelectedItem().getId());
        purchaseTable.setItems(purchaseList);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        ReceiptInputData inputData = new ReceiptInputData();
        receiptFields.forEach((fName, field) -> setProperty(inputData, fName + "Str", field.getText()));
        Set<String> invalidFields = inputData.validate();
        invalidFields.forEach(fName -> receiptFields.get(fName).getStyleClass().add("invalid"));
        if(!inputData.isValid()) {
            return;
        }
        receiptFields.values().forEach(field -> field.getStyleClass().remove("invalid"));
        sendReceiptInputData(inputData);
    }

    private void sendReceiptInputData(ReceiptInputData inputData){
        Receipt selectedReceipt = receiptSelModel.getSelectedItem();
        if (selectedReceipt == null) {
            receiptSelModel.select(receiptService.newReceipt(inputData));
        } else {
            receiptService.updateReceipt(selectedReceipt, inputData);
        }
    }

    @FXML
    private void handleNewReceipt(ActionEvent actionEvent) {
        receiptSelModel.clearSelection();
        receiptId.setText("");
        receiptFields.values().forEach(TextInputControl::clear);
        sum.setText("0.00");
        purchaseList.clear();
    }

}
