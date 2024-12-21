package com.example.laboratory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Biologiste implements Initializable {
    @FXML
    private Text name;
    @FXML
    private HBox logout;
    private Client client;
    private String userName;
    @FXML
    private TextField searchTextField;
    @FXML
    private GridPane analyseInfoGrid;
    @FXML
    private Button save;
    @FXML
    private Text errorText;
    @FXML
    private Text statusText;
    @FXML
    private TextArea textarea;
    @FXML
    private List<TextArea> textAreas = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = ClientManager.getSharedClient();
        client.sendMessageToServer("getUserName:");
        this.userName = client.readMessageFromServer();
        name.setText(userName);
        save.getParent().getStylesheets().add(getClass().getResource("dialogStyle.css").toExternalForm());
        save.getStyleClass().add("dialog-savebutton");
        save.setDisable(true);

        searchTextField.setOnAction(even -> {
            errorText.setText("");
            String searchText = searchTextField.getText().trim();
            save.setDisable(true);
            analyseInfoGrid.getChildren().clear();
            analyseInfoGrid.getRowConstraints().clear();
            getAllAnalyses(searchText);
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

    private TextArea createDynamicTextArea() {
        TextArea textArea = new TextArea();
        // You can customize the TextArea properties here if needed
        return textArea;
    }

    public void addAnalyseToGrid(String[] analyseData) {
        client.sendMessageToServer("getEtat:" + analyseData[0].split(":")[0]);
        String messageFromServer = client.readMessageFromServer();

        if (messageFromServer.equals("1")) {
            statusText.setText("status: valider");
        } else if (messageFromServer.equals("0")) {
            statusText.setText("status: refuser");
        } else {
            statusText.setText("status: vide");
        }

        int i = 0;
        for (String user : analyseData) {
            String[] parts = user.split(":");
            String code = parts[0];
            String nom = parts[1];
            String nomCourt = parts[2];
            String[] resultat = parts[3].split(",");

            addRow(code, nom, nomCourt, resultat[i]);
            i++;
            if (i == resultat.length) {
                i = 0;
            }
        }
    }

    public void addRow(String code, String analyse, String nomCourt, String resultat) {
        // Create labels for each cell in the row
        Label codeLabel = new Label(code);
        Label analyseLabel = new Label(analyse);
        Label nomCourtLabel = new Label(nomCourt);
        TextArea resultatArea = createDynamicTextArea();
        if (!resultat.equals("null")) {
            resultatArea.appendText(resultat);
        }

        textAreas.add(resultatArea);

        // Add listeners to text fields
        textAreas.forEach(textField -> textField.textProperty().addListener((observable, oldValue, newValue) ->
                save.setDisable(textAreas.stream().anyMatch(field -> field.getText().trim().isEmpty()))
        ));

        // Add labels to the GridPane at specific column indices
        int rowIndex = analyseInfoGrid.getRowCount(); // Get the current row count
        analyseInfoGrid.add(codeLabel, 0, rowIndex);
        analyseInfoGrid.add(analyseLabel, 1, rowIndex);
        analyseInfoGrid.add(nomCourtLabel, 2, rowIndex);
        analyseInfoGrid.add(resultatArea, 3, rowIndex);

        // Update row constraints to allow for the new row
        analyseInfoGrid.getRowConstraints().add(new RowConstraints(50)); // You can adjust the height as needed
    }

    @FXML
    private void handleLogout() {
        try {
            client.sendMessageToServer("logout:");
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            System.out.println("problem in load the login page");
        }
    }

    public void saveresult() {
        String result = "";
        int rowCount = analyseInfoGrid.getRowCount();
        System.out.println("Total number of rows: " + rowCount);

        boolean allFieldsFilled = true;  // Flag to check if all fields are filled

        // Initialize the button as disabled
        save.setDisable(true);

        // Iterate through the rows
        for (int i = 0; i < rowCount; i++) {
            // Iterate through the nodes in the GridPane
            for (Node node : analyseInfoGrid.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(node);
                Integer colIndex = GridPane.getColumnIndex(node);

                // Check if this node is in the third column (index 2) and in the current row
                if (colIndex != null && colIndex == 3 && rowIndex != null && rowIndex == i) {
                    if (node instanceof TextArea) {
                        TextArea textarea = (TextArea) node;
                        String val = textarea.getText();

                        if (val.trim().isEmpty()) {  // Check if the field is empty
                            allFieldsFilled = false;
                        }

                        result = result + val + ",";
                    }
                }
            }
        }

        result = result.trim();  // Remove trailing spaces and commas
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        System.out.println("The result is " + result);

        if (allFieldsFilled && result!="") {
            save.setDisable(false);  // Enable the save button
            String message = "addResult:" + result + ":" + searchTextField.getText().trim();
            client.sendMessageToServer(message);
            String messageFromServer = client.readMessageFromServer();
            System.out.println(messageFromServer);
        } else {
            System.out.println("All fields must be filled.");
        }
    }
}