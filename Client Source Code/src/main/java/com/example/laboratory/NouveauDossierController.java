package com.example.laboratory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class NouveauDossierController implements Initializable {
    private Client client;
    @FXML
    private VBox dossierUI;
    @FXML
    private GridPane patientGrid;
    @FXML
    private TextField searchBar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        client = ClientManager.getSharedClient();

        searchBar.setOnAction(event -> {
            String searchText = searchBar.getText().trim();
            // Perform search based on searchText
            patientGrid.getChildren().clear();
            patientGrid.getRowConstraints().clear();
            getAllPatients(searchText);
        });

        patientGrid.getChildren().clear();
        patientGrid.getRowConstraints().clear();
        getAllPatients("");

    }

    public void getAllPatients(String filter) {
        client.sendMessageToServer("getAllPatients: ");
        String messageFromServer = client.readMessageFromServer();
        String[] patientsData = messageFromServer.split("\n");

        if (filter.equals("")) {
            addPatientsToGrid(patientsData);
        } else {
            addPatientsToGridBySearch(patientsData, filter);
        }
    }

    public void addPatientsToGrid(String[] patients) {
        for(String patient: patients) {
            System.out.println(patient);
            String[] parts = patient.split(":");
            String id = parts[0];
            String nom = parts[1];
            String prenom = parts[2];
            String date_nais = parts[3];
            String sexe = parts[4];

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addRow(id,nom,prenom,date_nais,sexe);
                }
            });
        }
    }

    public void addPatientsToGridBySearch(String[] patients, String filter) {
        for(String patient: patients) {
            System.out.println(patient);
            String[] parts=patient.split(":");
            String id=parts[0];
            String nom=parts[1];
            String prenom=parts[2];
            String date_nais=parts[3];
            String sexe = parts[4];

            if (nom.equals(filter)) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        addRow(id,nom,prenom,date_nais,sexe);
                    }
                });
            }
        }
    }

    public void addRow(String id, String nom, String prenom, String date_nais, String sexe) {
        Label nameLabel = new Label(nom);
        Label prenomLabel = new Label(prenom);
        Label dateLabel = new Label(date_nais);

        // Create buttons for delete and edit with icons
        Button ajouterButton = new Button("Ajouter");
        Button editButton = createIconButton("images/Edit.png");

        // Add event handlers for delete and edit buttons
        ajouterButton.setOnAction(event -> {
            ((Stage) ((Stage) dossierUI.getScene().getWindow()).getOwner()).close();
            openNewStage(id, nom, prenom, date_nais, sexe);
        });

        editButton.setOnAction(event -> {
            client.sendMessageToServer("getPatient:" + id);
            String data = client.readMessageFromServer();
            showEditPatientDialogue(data);
        });

        // Add labels to the GridPane at specific column indices
        int rowIndex = patientGrid.getRowCount(); // Get the current row count
        patientGrid.add(nameLabel, 0, rowIndex);
        patientGrid.add(prenomLabel, 1, rowIndex);
        patientGrid.add(dateLabel, 2, rowIndex);
        patientGrid.add(editButton, 3, rowIndex);
        patientGrid.add(ajouterButton, 4, rowIndex);

        // Update row constraints to allow for the new row
        patientGrid.getRowConstraints().add(new RowConstraints(30));
    }

    private Button createIconButton(String iconName) {
        Button button = new Button();
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(iconName)));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        button.setGraphic(imageView);
        return button;
    }

    private void openNewStage(String id, String nom, String prenom, String date_nais, String sexe) {
        try {
            String fxmlPath = "Analyses.fxml";

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            AnalysesController controller = fxmlLoader.getController();
            controller.setData(id, nom, prenom, date_nais, sexe);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showAddPatientDialogue() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(dossierUI.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Ajouter-Patient.fxml"));
        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());

            ButtonType addButtonType = new ButtonType("Sauvgarder", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = ButtonType.CANCEL;

            dialog.getDialogPane().getStylesheets().add(getClass().getResource("dialogStyle.css").toExternalForm());

            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

            Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
            Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancelButtonType);
            addButton.getStyleClass().add("dialog-savebutton");
            cancelButton.getStyleClass().add("dialog-cancelbutton");

            addButton.setDisable(true);

            AddPatientController controller = fxmlLoader.getController();

            List<TextField> textFields = Arrays.asList(controller.getNom(), controller.getPrenom(), controller.getEmail(), controller.getTel(), controller.getVille());

            // Add listeners to text fields
            textFields.forEach(textField -> textField.textProperty().addListener((observable, oldValue, newValue) ->
                    addButton.setDisable(textFields.stream().anyMatch(field -> field.getText().trim().isEmpty()))
            ));

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == addButtonType) {
                String message = "addClient:" + controller.getClientInfo();
                client.sendMessageToServer(message);
                String messageFromServer = client.readMessageFromServer();
                System.out.println(messageFromServer);

                patientGrid.getChildren().clear();
                patientGrid.getRowConstraints().clear();
                getAllPatients("");


                //refresh the scene
                /*try {
                    Parent root= FXMLLoader.load(getClass().getResource("Gestion-Stock.fxml"));
                    Stage stage=(Stage) dossierUI.getScene().getWindow();
                    stage.setScene(new Scene(root,800,600));
                } catch (IOException e) {
                    System.out.println("a problem in loading the gestion personnel scene in Gerant class");
                }*/
            } else {
                System.out.println("cancel pressed");
            }

        } catch (IOException e) {
            System.out.println("couldn't load the AddPatientDialog");
        }
    }

    public void showEditPatientDialogue(String data) {
        String[] parts = data.split(":");
        System.out.println(parts);

        String id = parts[0];
        String nom = parts[1];
        String prenom = parts[2];
        String date = parts[3];
        String sexe = parts[4];
        String tel = parts[5];
        String email = parts[6];
        String ville = parts[7];

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(dossierUI.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Ajouter-Patient.fxml"));
        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());

            ButtonType addButtonType = new ButtonType("Sauvgarder", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = ButtonType.CANCEL;

            dialog.getDialogPane().getStylesheets().add(getClass().getResource("dialogStyle.css").toExternalForm());

            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

            Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
            Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancelButtonType);
            addButton.getStyleClass().add("dialog-savebutton");
            cancelButton.getStyleClass().add("dialog-cancelbutton");

            AddPatientController controller = fxmlLoader.getController();
            controller.setText(nom, prenom, date, sexe, tel, email, ville);

            addButton.setDisable(true);

            List<TextField> textFields = Arrays.asList(controller.getNom(), controller.getPrenom(), controller.getEmail(), controller.getTel(), controller.getVille());

            // Add listeners to text fields
            textFields.forEach(textField -> textField.textProperty().addListener((observable, oldValue, newValue) ->
                    addButton.setDisable(textFields.stream().anyMatch(field -> field.getText().trim().isEmpty()))
            ));

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == addButtonType) {
                String message = "editPatient:" + id + ":" + controller.getClientInfo();
                client.sendMessageToServer(message);
                String messageFromServer = client.readMessageFromServer();
                System.out.println(messageFromServer);

                patientGrid.getChildren().clear();
                patientGrid.getRowConstraints().clear();
                getAllPatients("");
            }

        } catch (IOException e) {
            e.printStackTrace(System.out);
            System.out.println("couldn't load the AddClientDialog");
        }
    }
}
