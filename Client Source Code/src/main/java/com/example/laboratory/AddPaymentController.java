package com.example.laboratory;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddPaymentController implements Initializable {

    @FXML
    private ChoiceBox usernameChoice;
    @FXML
    private TextField montant;
    @FXML
    private DatePicker date;
    private Client client;
    private List<String>usernameList=new ArrayList<>();
    public String getPaymentInfo(){
        String username=this.usernameChoice.getValue().toString().trim();
        String montant=this.montant.getText().trim();
        LocalDate date=this.date.getValue();
        return username+":"+montant+":"+date;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //this code is for add usernames to choiceBox
        client=ClientManager.getSharedClient();
        client.sendMessageToServer("getAllUsers: ");
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        String[] usersData = messageFromServer.split("\n");
        for(String user:usersData) {
            String parts[]=user.split(":",5);
            String username=parts[2];
            usernameList.add(username);


        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                usernameChoice.setItems(FXCollections.observableArrayList(usernameList));
usernameChoice.setValue(usernameList.get(0));
            }
        });







        //this for make montat Text field accept only number
        montant.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                montant.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });




    }

    public TextField getMontant() {
        return montant;
    }

    public DatePicker getDate() {
        return date;
    }
}
