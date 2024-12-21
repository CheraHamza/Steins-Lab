package com.example.laboratory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    @FXML
    private TextField productName;
    @FXML
    private TextField quantity;
    @FXML
    private ChoiceBox type;

    public String getProductInfo(){
        String username=this.productName.getText().trim();
        String quantity=this.quantity.getText().trim();
        String type=this.type.getValue().toString().trim();
        return username+":"+quantity+":"+type;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //this for make quantity Text field accept only number
        quantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantity.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

    public TextField getProductName() {
        return productName;
    }

    public TextField getQuantity() {
        return quantity;
    }
}
