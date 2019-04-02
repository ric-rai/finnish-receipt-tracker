package org.openjfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FXMLController {

    @FXML private TextField date;
    @FXML private TextField place;
    @FXML private TextField buyer;

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired JdbcTemplate jdbcTemplate;

    private ReceiptService receiptService;
    private ReceiptDao receiptDao;

    public void init(){
        receiptDao = new ReceiptDao(jdbcTemplate);
        this.receiptService = new ReceiptService(receiptDao);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        receiptService.saveReceipt(date.getText(), place.getText(), "0", buyer.getText());
    }
}
