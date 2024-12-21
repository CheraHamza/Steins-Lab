package com.example.laboratory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Doctor implements Initializable {
    @FXML
    private Text name;
    @FXML
    private HBox logout;
    private Client client;
    private String userName;
    @FXML
    private GridPane analyseInfoGrid;
    @FXML
    private TextField searchTextField;
    @FXML
    private TextField nomPatient;
    @FXML
    private TextField codeTube;
    @FXML
    private TextField nomMedcine;
    @FXML
    private TextField date;
    @FXML
    private Button valider;
    @FXML
    private Button refuser;
    @FXML
    private TextArea remarque;
    @FXML
    private Text errorText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = ClientManager.getSharedClient();
        client.sendMessageToServer("getUserName:");
        this.userName=client.readMessageFromServer();

        valider.setDisable(true);
        refuser.setDisable(true);

        remarque.textProperty().addListener((observable, oldValue, newValue) -> {
            valider.setDisable(remarque.getText().trim().isEmpty());
            refuser.setDisable(remarque.getText().trim().isEmpty());
        });

        name.setText(userName);searchTextField.setOnAction(even -> {
            errorText.setText("");
            remarque.setDisable(true);
            remarque.setText("");
            String searchText = searchTextField.getText().trim();
            analyseInfoGrid.getChildren().clear();
            analyseInfoGrid.getRowConstraints().clear();
            getAllAnalyses(searchText);
        });

        valider.setOnAction(event -> {
            sendRemarque(true);
        });

        refuser.setOnAction(event -> {
            sendRemarque(false);
        });
    }

    public void getAllAnalyses(String valeur) {
        client.sendMessageToServer("getAnalyses:" + valeur);
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        String[] analyses = messageFromServer.split("\n");

        if (!messageFromServer.equals("")) {
            addAnalyseToGrid(analyses);
        } else {
            errorText.setText("Code n'existe pas.");
        }
    }

    public void addAnalyseToGrid(String[] analyseData) {
        int i = 0;
        for (String user : analyseData) {
            String[] parts = user.split(":");
            String code = parts[0];
            String nom = parts[1];
            String nomCourt = parts[2];
            String resultat = parts[3].split(",")[i];

            if(resultat.equals("null")) {
                errorText.setText("Code n'a pas des resultats.");
                return;
            }
            remarque.setDisable(false);

            addRow(code, nom, nomCourt, resultat);
            i++;
        }

        nomMedcine.setText(userName);
        codeTube.setText(searchTextField.getText().trim());
        client.sendMessageToServer("getDateBilan:" + searchTextField.getText().trim());
        String data = client.readMessageFromServer();
        nomPatient.setText(data.split(":")[0]);
        date.setText(data.split(":")[1]);
    }

    public void addRow(String code, String analyse, String nomCourt, String resultat) {
        // Create labels for each cell in the row
        Label codeLabel = new Label(code);
        Label analyseLabel = new Label(analyse);
        Label nomCourtLabel = new Label(nomCourt);
        Label resultatLabel = new Label(resultat);


        // Create buttons for delete and edit with icons


        // Add labels to the GridPane at specific column indices
        int rowIndex = analyseInfoGrid.getRowCount(); // Get the current row count
        analyseInfoGrid.add(codeLabel, 0, rowIndex);
        analyseInfoGrid.add(analyseLabel, 1, rowIndex);
        analyseInfoGrid.add(nomCourtLabel, 2, rowIndex);
        analyseInfoGrid.add(resultatLabel, 3, rowIndex);

        // Update row constraints to allow for the new row
        analyseInfoGrid.getRowConstraints().add(new RowConstraints(50)); // You can adjust the height as needed
    }

    private void sendRemarque(boolean state) {
        client.sendMessageToServer("getIdResultat:" + codeTube.getText().trim());
        String code = client.readMessageFromServer();
        client.sendMessageToServer("editState:" + remarque.getText() + ":" + state + ":" + code);
        System.out.println(client.readMessageFromServer());
    }

    @FXML
    private void handleLogout(){
        try {
            client.sendMessageToServer("logout:");
            Parent root= FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage=(Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            System.out.println("problem in load the login page");        }
    }
}
