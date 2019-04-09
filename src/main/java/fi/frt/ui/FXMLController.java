package fi.frt.ui;

import fi.frt.dao.Dao;
import fi.frt.dao.ReceiptDao;
import fi.frt.domain.Receipt;
import fi.frt.domain.ReceiptService;
import fi.frt.utilities.DateUtilities;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class FXMLController {

    @FXML public TableView<Receipt> receiptTable;
    public Label receiptId;
    @FXML private TextField date;
    @FXML private TextField place;
    @FXML private TextField buyer;

    @Autowired JdbcTemplate jdbcTemplate;
    private ReceiptService receiptService;
    private Dao<Receipt, Long> receiptDao;

    private ObservableList<Receipt> receiptList;

    public void init(){
        receiptDao = new ReceiptDao(jdbcTemplate);
        this.receiptService = new ReceiptService(receiptDao);
        initReceiptTable();
    }

    private void initReceiptTable(){
        List<TableColumn<Receipt, ?>> columns = receiptTable.getColumns();
        TableColumn<Receipt, LocalDate> dateCol = (TableColumn)columns.get(0);
        dateCol.setCellFactory(new ColumnFormatter<>(ld -> ld.format(DateUtilities.finnishDateFormatter)));
        dateCol.setSortType(TableColumn.SortType.DESCENDING);
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("date"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("place"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("sum"));
        columns.get(3).setCellValueFactory(new PropertyValueFactory<>("buyer"));
        receiptList = FXCollections.observableArrayList(r -> new Observable[]{});
        receiptList.addAll(receiptDao.list());
        receiptService.setReceiptList(receiptList);
        receiptList.addListener(this::handleReceiptListChange);
        SortedList<Receipt> sortedList = new SortedList<>(receiptList);
        sortedList.comparatorProperty().bind(receiptTable.comparatorProperty());
        receiptTable.setItems(sortedList);
        receiptTable.getSortOrder().add(dateCol);
        TableView.TableViewSelectionModel sm = receiptTable.getSelectionModel();
        sm.selectedIndexProperty().addListener(this::handleReceiptTableSelectionChange);
        sm.selectFirst();
    }

    private void handleReceiptTableSelectionChange(Observable obs, Number old, Number newSelection) {
        if (newSelection != null) {
            Receipt selectedReceipt = receiptTable.getSelectionModel().getSelectedItem();
            receiptId.setText(selectedReceipt.getId().toString());
            date.setText(selectedReceipt.getDate().format(DateUtilities.finnishDateFormatter));
            place.setText(selectedReceipt.getPlace());
            buyer.setText(selectedReceipt.getBuyer());
        }
    }

    private void handleReceiptListChange(ListChangeListener.Change<? extends Receipt> change){
        while (change.next()){
            if (change.wasAdded() && change.getAddedSize() == 1)
                receiptTable.getSelectionModel().select(change.getFrom());
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Receipt selectedReceipt = receiptTable.getSelectionModel().getSelectedItem();
        if (selectedReceipt == null) receiptService.saveReceipt(date.getText(), place.getText(), "0", buyer.getText());
        else receiptService.saveReceipt(selectedReceipt, date.getText(), place.getText(), "0", buyer.getText());
    }
}
