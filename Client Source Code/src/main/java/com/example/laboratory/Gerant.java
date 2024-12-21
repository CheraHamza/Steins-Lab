package com.example.laboratory;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Gerant implements Initializable {
    @FXML
    private Button addUser;
    @FXML
    private ListView<String> userListView;
    private Client client;
    @FXML
    private VBox gerantUI;
    @FXML
    private VBox vboxUsers;
    @FXML
    private HBox financeHbox;
    @FXML
    private HBox stockHbox;
    @FXML
    private Text name;
    @FXML
    private HBox logout;
    @FXML
    private GridPane usersGrid;
    @FXML
    private MenuButton typeMenuButton;
    @FXML
    private TextField searchTextField;

    private String userName;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        client = ClientManager.getSharedClient();
        client.sendMessageToServer("getUserName:");
        this.userName = client.readMessageFromServer();
        name.setText(userName);


        getAllUsers();

        // Add action listener to searchTextField
        searchTextField.setOnAction(event -> {
            String searchText = searchTextField.getText().trim();
            // Perform search based on searchText
            usersGrid.getChildren().clear();
            usersGrid.getRowConstraints().clear();
            if (typeMenuButton.getText().equals("All Users")){
                getAllUsersBySearch(searchText);

            }else{
                getAllUsersBySearchByType(searchText,typeMenuButton.getText());
            }
        });

    }


    public void getAllUsers() {
        client.sendMessageToServer("getAllUsers: ");
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        String[] usersData = messageFromServer.split("\n");

        addUsersToGrid(usersData);


    }

    public void getAllUsersByType(String type) {
        client.sendMessageToServer("getAllUsersByType:"+type);
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        String[] usersData = messageFromServer.split("\n");

        addUsersToGrid(usersData);


    }


    public void getAllUsersBySearch(String search) {
        client.sendMessageToServer("getAllUsersBySearch:"+search);
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        String[] usersData = messageFromServer.split("\n");
        if(!messageFromServer.isEmpty()) {

            addUsersToGrid(usersData);
        }

    }

    public void getAllUsersBySearchByType(String search,String type) {
        client.sendMessageToServer("getAllUsersBySearchByType:"+search+":"+type);
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        String[] usersData = messageFromServer.split("\n");
        if(!messageFromServer.isEmpty()) {

            addUsersToGrid(usersData);
        }

    }
    public void addUsersToGrid(String[] userData) {

        for (String user : userData) {
            String parts[] = user.split(":", 5);
            String nom = parts[0];
            String prenom = parts[1];
            String username = parts[2];
            String password = parts[3];
            String type = parts[4];
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addRow(nom, prenom, username, password, type);
                }
            });
        }


    }


    //add a row to users Gridpane
    public void addRow(String name, String prenom, String username, String password, String type) {
        // Create labels for each cell in the row
        Label nameLabel = new Label(name);
        Label prenomLabel = new Label(prenom);
        Label userNameLabel = new Label(username);
        Label passworLable = new Label(password);
        Label typeLabel = new Label(type);

        // Create buttons for delete and edit with icons
        Button deleteButton = createIconButton("images/Delete.png");
        Button editButton = createIconButton("images/Edit.png");


        // Add event handlers for delete and edit buttons
        deleteButton.setOnAction(event -> {
            // Implement delete action here
            // You can remove the row associated with this button
            client.sendMessageToServer("deleteClient:" + username);
            String message = client.readMessageFromServer();
            System.out.println(message);

            //refresh the scene
            try {
                Parent root = FXMLLoader.load(getClass().getResource("Gestion-Personnel.fxml"));
                Stage stage = (Stage) usersGrid.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
            } catch (IOException e) {
                System.out.println("a problem in loading the gestion personnel scene in Gerant class");
            }

        });

        editButton.setOnAction(event -> {
            // Implement edit action here
            // You can open a dialog or update the row data for editing
            showEditClientDialog(name, prenom, username, password);

        });


        // Add labels to the GridPane at specific column indices
        int rowIndex = usersGrid.getRowCount(); // Get the current row count
        usersGrid.add(nameLabel, 0, rowIndex);
        usersGrid.add(prenomLabel, 1, rowIndex);
        usersGrid.add(userNameLabel, 2, rowIndex);
        usersGrid.add(passworLable, 3, rowIndex);
        usersGrid.add(typeLabel, 4, rowIndex);
        usersGrid.add(deleteButton, 5, rowIndex);
        usersGrid.add(editButton, 6, rowIndex);

        // Update row constraints to allow for the new row
        usersGrid.getRowConstraints().add(new RowConstraints(30)); // You can adjust the height as needed
    }

    // Helper method to create icon buttons
    private Button createIconButton(String iconName) {
        Button button = new Button();
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(iconName)));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        button.setGraphic(imageView);
        return button;
    }

    @FXML
    public void showAddClientDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(gerantUI.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addClientDialog.fxml"));
        try {
            Parent dialogContent = fxmlLoader.load();
            dialog.getDialogPane().setContent(dialogContent);

            // Define button types
            ButtonType addButtonType = new ButtonType("Add Client", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = ButtonType.CANCEL;

            // Add button types to dialog
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

            // Apply CSS to the dialog pane
            dialog.getDialogPane().getStylesheets().add(getClass().getResource("dialogStyle.css").toExternalForm());

            // Get buttons and set style class
            Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
            Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancelButtonType);
            addButton.getStyleClass().add("dialog-savebutton");
            cancelButton.getStyleClass().add("dialog-cancelbutton");

            // Disable the "Add Client" button initially
            addButton.setDisable(true);

            // Get the controller
            AddClientController controller = fxmlLoader.getController();

            // Create a list of text fields
            List<TextField> textFields = Arrays.asList(controller.getNom(), controller.getPrenom(), controller.getUsername(), controller.getPassword());

            // Add listeners to text fields
            textFields.forEach(textField -> textField.textProperty().addListener((observable, oldValue, newValue) ->
                    addButton.setDisable(textFields.stream().anyMatch(field -> field.getText().trim().isEmpty()))
            ));

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == addButtonType) {
                // Handle Add button pressed
                String message = "addClient:" + controller.getUserInfo();
                client.sendMessageToServer(message);
                String messageFromServer = client.readMessageFromServer();
                System.out.println(messageFromServer);
                getAllUsers();

                // Refresh the scene
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("Gestion-Personnel.fxml"));
                    Stage stage = (Stage) usersGrid.getScene().getWindow();
                    stage.setScene(new Scene(root, 800, 600));
                } catch (IOException e) {
                    System.out.println("a problem in loading the gestion personnel scene in Gerant class");
                }
            } else {
                System.out.println("cancel pressed");
            }

        } catch (IOException e) {
            System.out.println("couldn't load the AddClientDialog");
        }
    }




    @FXML
    public void showEditClientDialog(String nom, String prenom, String username, String password) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(gerantUI.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editClientDialog.fxml"));
        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());
            EditClientDialog controller = fxmlLoader.getController();
            controller.setTextFields(nom, prenom, username, password);
            // Define button types
            ButtonType addButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = ButtonType.CANCEL;

            // Add button types to dialog
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

            // Apply CSS to the dialog pane
            dialog.getDialogPane().getStylesheets().add(getClass().getResource("dialogStyle.css").toExternalForm());

            // Get buttons and set style class
            Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
            Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancelButtonType);


            addButton.getStyleClass().add("dialog-savebutton");
            cancelButton.getStyleClass().add("dialog-cancelbutton");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == addButtonType) {

                String message = "editClient:" + username + ":" + controller.getUserInfo();
                client.sendMessageToServer(message);
                String messageFromServer = client.readMessageFromServer();
                System.out.println(messageFromServer);
                getAllUsers();


                //refresh the scene
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("Gestion-Personnel.fxml"));
                    Stage stage = (Stage) usersGrid.getScene().getWindow();
                    stage.setScene(new Scene(root, 800, 600));
                } catch (IOException e) {
                    System.out.println("a problem in loading the gestion personnel scene in Gerant class");
                }
            } else {
                System.out.println("cancel pressed");
            }

        } catch (IOException e) {
            System.out.println("couldn't load the AddClientDialog");
        }

    }
    @FXML
    private void handleMenuItemAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String selectedType = menuItem.getText();
        typeMenuButton.setText(selectedType);

        if (!selectedType.equals("All Users")) {
            usersGrid.getChildren().clear();
            usersGrid.getRowConstraints().clear();
            getAllUsersByType(selectedType);
        } else {
            usersGrid.getChildren().clear();
            usersGrid.getRowConstraints().clear();
            getAllUsers();
        }
    }
    @FXML
    private void handleFinanceClick() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Gestion-finance.fxml"));

            Stage stage = (Stage) financeHbox.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleStockClick() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Gestion-Stock.fxml"));

            Stage stage = (Stage) stockHbox.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}

