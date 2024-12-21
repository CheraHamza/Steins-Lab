package com.example.laboratory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddPurchaseController implements Initializable {
    @FXML
    private TextField productName;
    @FXML
    private TextField montant;
    @FXML
    private ChoiceBox type;
    @FXML
    private DatePicker date;
    public String getPurchaseInfo(){
        String productName=this.productName.getText().trim();
        String type=this.type.getValue().toString().trim();
        String montant=this.montant.getText().trim();
        LocalDate date=this.date.getValue();
        return productName+":"+type+":"+montant+":"+date;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //this for make montant Text field accept only number
        montant.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                montant.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public TextField getProductName() {
        return productName;
    }

    public TextField getMontant() {
        return montant;
    }

    public DatePicker getDate() {
        return date;
    }
}
