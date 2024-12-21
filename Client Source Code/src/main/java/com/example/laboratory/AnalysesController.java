package com.example.laboratory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AnalysesController implements Initializable {
    private String pId;
    private String pNom;
    private String pPrenom;
    @FXML
    private Text name;
    @FXML
    private HBox logout;
    private Client client;
    private String userName;
    @FXML
    private GridPane analyseGrid;
    @FXML
    private GridPane currAnalyseGrid;
    @FXML
    private TextField PatientNom;
    @FXML
    private TextField BilanDate;
    @FXML
    private TextField PatientSexe;
    @FXML
    private TextField PatientDate;
    @FXML
    private Button returnButton;
    @FXML
    private Button sauvgarderButton;
    private List<String> analysesList = new ArrayList<>();
    private List<String> originalAnalysesList = new ArrayList<>();
    @FXML
    private TextField searchBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = ClientManager.getSharedClient();
        client.sendMessageToServer("getUserName:");
        this.userName=client.readMessageFromServer();
        name.setText(userName);

        returnButton.getParent().getParent().getStylesheets().add(getClass().getResource("dialogStyle.css").toExternalForm());

        returnButton.setOnAction(event -> {
            ((Stage) returnButton.getScene().getWindow()).close();
            try {
                String fxmlPath = "Reception.fxml";

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = fxmlLoader.load();
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        searchBar.setOnAction(event -> {
            String searchText = searchBar.getText().trim();
            // Perform search based on searchText
            analyseGrid.getChildren().clear();
            analyseGrid.getRowConstraints().clear();
            getAllAnalyses(searchText);
        });

        sauvgarderButton.getStyleClass().add("dialog-savebutton");

        sauvgarderButton.setOnAction(event -> {
            addBilan();
        });

        sauvgarderButton.setDisable(true);

        getAllAnalyses("");
    }

    public void setData(String pId, String pNom, String pPrenom, String date_nais, String sexe) {
        Platform.runLater(() -> {
            this.pId = pId;
            this.pNom = pNom;
            this.pPrenom = pPrenom;
            PatientNom.setText(pNom + " " + pPrenom);
            BilanDate.setText(LocalDate.now().toString());
            PatientSexe.setText(sexe);
            PatientDate.setText(date_nais);
        });
    }

    private void getAllAnalyses(String filter){
        originalAnalysesList.clear();
        client.sendMessageToServer("getAllAnalyses: ");
        String messageFromServer = client.readMessageFromServer();

        if (filter.equals("")) {
            for (String a : messageFromServer.split("\n")) {
                if (!analysesList.contains(a)) {
                    originalAnalysesList.add(a);
                }
            }
        } else {
            for (String a : messageFromServer.split("\n")) {
                if (a.split(":")[2].equals(filter) && !analysesList.contains(a)) {
                    originalAnalysesList.add(a);
                }
            }
        }

        addAnalysesToGrid(originalAnalysesList);
    }

    public void addAnalysesToGrid(List<String> analyses) {
        for(String analyse: analyses) {
            System.out.println(analyse);
            String[] parts=analyse.split(":");
            String id = parts[0];
            String nom=parts[1];
            String nom_court=parts[2];
            String prix = parts[3];
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addRow(id,nom,nom_court,prix);
                }
            });
        }
    }

    public void addAnalysesToCurrGrid(List<String> analyses) {
        for(String analyse: analyses) {
            System.out.println(analyse);
            String[] parts=analyse.split(":");
            String id = parts[0];
            String nom=parts[1];
            String nom_court=parts[2];
            String prix = parts[3];
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addRow2(id,nom,nom_court,prix);
                }
            });
        }
    }

    public void addRow(String id, String nom, String nom_court, String prix) {
        Label nameLabel = new Label(nom);
        Label nomCourtLabel = new Label(nom_court);

        // Create buttons for delete and edit with icons
        Button ajouterButton = createIconButton("images/Add.png");

        // Add event handlers for delete and edit buttons
        ajouterButton.setOnAction(event -> {
            originalAnalysesList.remove(id + ":" + nom + ":" + nom_court + ":" + prix);
            analysesList.add(id + ":" + nom + ":" + nom_court + ":" + prix);
            analyseGrid.getChildren().clear();
            analyseGrid.getRowConstraints().clear();
            currAnalyseGrid.getChildren().clear();
            currAnalyseGrid.getRowConstraints().clear();
            addAnalysesToGrid(originalAnalysesList);
            addAnalysesToCurrGrid(analysesList);

            sauvgarderButton.setDisable(false);
        });

        // Add labels to the GridPane at specific column indices
        int rowIndex = analyseGrid.getRowCount(); // Get the current row count
        analyseGrid.add(nameLabel, 0, rowIndex);
        analyseGrid.add(nomCourtLabel, 1, rowIndex);
        analyseGrid.add(ajouterButton, 2, rowIndex);

        // Update row constraints to allow for the new row
        analyseGrid.getRowConstraints().add(new RowConstraints(30));
    }

    private void addRow2(String id, String nom, String nom_court, String prix){
        Label nameLabel = new Label(nom);
        Label nomCourtLabel = new Label(nom_court);
        Label prixLabel = new Label(prix);

        // Create buttons for delete and edit with icons
        Button deleteButton = createIconButton("images/Delete.png");

        // Add labels to the GridPane at specific column indices
        int rowIndex = currAnalyseGrid.getRowCount(); // Get the current row count

        deleteButton.setOnAction(event -> {
            analysesList.remove(id + ":" + nom + ":" + nom_court + ":" + prix);
            originalAnalysesList.add(id + ":" + nom + ":" + nom_court + ":" + prix);
            analyseGrid.getChildren().clear();
            analyseGrid.getRowConstraints().clear();
            currAnalyseGrid.getChildren().clear();
            currAnalyseGrid.getRowConstraints().clear();
            addAnalysesToCurrGrid(analysesList);
            addAnalysesToGrid(originalAnalysesList);

            if(analysesList.isEmpty()) {
                sauvgarderButton.setDisable(true);
            }
        });

        currAnalyseGrid.add(nameLabel, 0, rowIndex);
        currAnalyseGrid.add(nomCourtLabel, 1, rowIndex);
        currAnalyseGrid.add(prixLabel, 2, rowIndex);
        currAnalyseGrid.add(deleteButton, 3, rowIndex);

        // Update row constraints to allow for the new row
        currAnalyseGrid.getRowConstraints().add(new RowConstraints(30));
    }

    public void addBilan() {
        String an = new String();
        String prix = new String();
        String date = BilanDate.getText();

        client.sendMessageToServer("getAllAnalyses: ");
        String list = client.readMessageFromServer();

        for(String i : list.split("\n")){
            for (String j : analysesList) {
                System.out.println(j.split(":")[2] + " = " + i.split(":")[2]);
                if (j.split(":")[2].equals(i.split(":")[2])){
                    an += i.split(":")[0] + ",";
                }
            }
        }

        for(String i : analysesList){
            prix += i.split(":")[3] + ",";
        }

        String data = pId + ":" + date + ":" + an.substring(0, an.length()-1) + ":" + prix;
        client.sendMessageToServer("addBilan:" + data);
        System.out.println(client.readMessageFromServer());

        ((Stage) BilanDate.getScene().getWindow()).close();
        try {
            String fxmlPath = "Reception.fxml";

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Button createIconButton(String iconName) {
        Button button = new Button();
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(iconName)));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        button.setGraphic(imageView);
        return button;
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
