package com.example.laboratory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditClientDialog implements Initializable {
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField username;
    @FXML
    private TextField password;


    public String getUserInfo(){
        String nom=this.nom.getText().trim();
        String prenom=this.prenom.getText().trim();
        String username=this.username.getText().trim();
        String password=this.password.getText().trim();
        return nom+":"+prenom+":"+username+":"+password;
    }

    @FXML
    public void setTextFields(String nom,String prenom,String username,String password){
        System.out.println("setTExtFields method called");
        this.nom.setText(nom);
        this.prenom.setText(prenom);
        this.username.setText(username);
        this.password.setText(password);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
