package com.example.laboratory;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class EditProductDialog {
    @FXML
    private TextField productName;
    @FXML
    private TextField quantity;
    @FXML
    private ChoiceBox type;

    public String getUserInfo(){
        String productName=this.productName.getText().trim();
        String quantity=this.quantity.getText().trim();
        String type=this.type.getValue().toString().trim();

        return productName+":"+quantity+":"+type;
    }

    @FXML
    public void setTextFields(String productName,String quantity,String type){
        System.out.println("setTExtFields method called");
        this.productName.setText(productName);
        this.quantity.setText(quantity);
        this.type.setValue(type);

    }
}
