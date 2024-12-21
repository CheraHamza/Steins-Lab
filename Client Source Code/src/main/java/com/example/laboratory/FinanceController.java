package com.example.laboratory;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class FinanceController implements Initializable {
    @FXML
    private HBox personnelHbox;
    @FXML
    private HBox stockHbox;
    @FXML
    private Text name;
    @FXML
    private Client client;
    @FXML
    private VBox financeUI;
    private String userName;
    @FXML
    private HBox logout;
    @FXML
    private GridPane paymentGrid;
    @FXML
    private GridPane achatsGrid;
    @FXML
    private GridPane entrantGrid;
    @FXML
    private Button AddAchats;
    @FXML
    private Label sortant;
    @FXML
    private Label entrantLabel;
    @FXML
    private Label netLabel;
    @FXML
    private MenuButton timeMenuButton;
    private int sortantMontant = 0;
    private int paymentMontantTotal = 0;
    private int purchaseMontantTotal = 0;
    private int entrantMontant=0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        client = ClientManager.getSharedClient();
        client.sendMessageToServer("getUserName:");
        this.userName = client.readMessageFromServer();
        name.setText(userName);
        paymentGrid.getChildren().clear();

        if (timeMenuButton.getText().equals("All Time")){
            getAllPayments();
            getAllPurchases();
            getAllEntrant();
        }
        sortantMontant = purchaseMontantTotal + paymentMontantTotal;
        sortant.setText("-" + sortantMontant);
        entrantLabel.setText(""+entrantMontant);
        netLabel.setText(""+(entrantMontant-sortantMontant));
if(entrantMontant-sortantMontant>=0){
    netLabel.setTextFill(Color.GREEN);
}else {
    netLabel.setTextFill(Color.RED);
}

    }

    public void getAllPayments() {
        client.sendMessageToServer("getAllPayment: ");
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        if (messageFromServer != null && !messageFromServer.isEmpty()) {
            String[] paymentsData = messageFromServer.split("\n");

            addPaymentsToGrid(paymentsData);
        }


    }

    public void getPaymentsForToday() {
        client.sendMessageToServer("getAllPaymentsForToday: ");
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        if (messageFromServer != null && !messageFromServer.isEmpty()) {
            String[] paymentsData = messageFromServer.split("\n");

            addPaymentsToGrid(paymentsData);
        }


    }

    public void getPaymentsForThisMonth() {
        client.sendMessageToServer("getAllPaymentsForThisMonth: ");
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        if (messageFromServer != null && !messageFromServer.isEmpty()) {
            String[] paymentsData = messageFromServer.split("\n");

            addPaymentsToGrid(paymentsData);
        }


    }
public  void getEntrantsForToday(){

    client.sendMessageToServer("getEntrantsForToday: ");
    String messageFromServer = client.readMessageFromServer();
    System.out.println(messageFromServer);
    if (messageFromServer != null && !messageFromServer.isEmpty()) {
        String[] entrantData = messageFromServer.split("\n");

        addEntrantsToGrid(entrantData);
    }


}

    public  void getEntrantsForThisMonth(){

        client.sendMessageToServer("getEntrantsForThisMonth: ");
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        if (messageFromServer != null && !messageFromServer.isEmpty()) {
            String[] entrantData = messageFromServer.split("\n");

            addEntrantsToGrid(entrantData);
        }


    }

    public  void getAllEntrant(){

        client.sendMessageToServer("getAllEntrant: ");
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        if (messageFromServer != null && !messageFromServer.isEmpty()) {
            String[] entrantData = messageFromServer.split("\n");

            addEntrantsToGrid(entrantData);
        }


    }

    public void getAllPurchases() {
        client.sendMessageToServer("getAllPurchase: ");
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        if (messageFromServer != null && !messageFromServer.isEmpty()) {
            String[] purchaseData = messageFromServer.split("\n");

            addPurchasesToGrid(purchaseData);
        }
        sortant.setText("-" + paymentMontantTotal);

    }

    public void getPurchasesForToday() {
        client.sendMessageToServer("getPurchasesForToday: ");
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        if (messageFromServer != null && !messageFromServer.isEmpty()) {
            String[] purchaseData = messageFromServer.split("\n");

            addPurchasesToGrid(purchaseData);
        }
        sortant.setText("-" + paymentMontantTotal);

    }

    public void getPurchasesForThisMonth() {
        client.sendMessageToServer("getPurchasesForThisMonth: ");
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        if (messageFromServer != null && !messageFromServer.isEmpty()) {
            String[] purchaseData = messageFromServer.split("\n");

            addPurchasesToGrid(purchaseData);
        }
        sortant.setText("-" + paymentMontantTotal);

    }

    public void addPaymentsToGrid(String[] userData) {

        for (String user : userData) {
            String parts[] = user.split(":", 4);
            String id = parts[0];
            String username = parts[1];
            String montant = parts[2];
            String date = parts[3];
            //calcul payment total
            paymentMontantTotal = paymentMontantTotal + Integer.parseInt(montant);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addRow(id, username, "-" + montant, date);
                }
            });
        }


    }

    public void addEntrantsToGrid(String[] entrantData) {

        for (String entrant : entrantData) {
            String parts[] = entrant.split(":", 5);
            String id = parts[0];
            String clientName = parts[1]+" "+parts[2];
            String montant = parts[3];
            String date = parts[4];
            //calcul payment total

            entrantMontant = entrantMontant + (int)Float.parseFloat(montant);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addEntrantRow(id, clientName, montant, date);
                }
            });
        }


    }


    public void addPurchasesToGrid(String[] purchaseData) {

        for (String purchase : purchaseData) {
            String parts[] = purchase.split(":", 5);
            String id = parts[0];
            String productName = parts[1];
            String type = parts[2];
            String montant = parts[3];
            String date = parts[4];
            //calcul payment total
            purchaseMontantTotal = purchaseMontantTotal + Integer.parseInt(montant);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addPurchaseRow(id, productName, type, "-" + montant, date);
                }
            });
        }


    }

    @FXML
    public void showAddPaymentDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(financeUI.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addPaymentDialog.fxml"));
        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());

            // Define button types
            ButtonType addButtonType = new ButtonType("Add Payment", ButtonBar.ButtonData.OK_DONE);
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
            //set button disable
            addButton.setDisable(true);
            AddPaymentController controller = fxmlLoader.getController();
            //this code for check that textFields and date picker is not empty

            controller.getMontant().textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if (t1.trim().isEmpty() || controller.getDate().getValue() == null) {
                        addButton.setDisable(true);
                    } else {
                        addButton.setDisable(false);
                    }
                }
            });

            controller.getDate().valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                    if (t1.toString().trim().isEmpty() || controller.getMontant().getText().trim().isEmpty()) {
                        addButton.setDisable(true);
                    } else {
                        addButton.setDisable(false);
                    }
                }
            });

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == addButtonType) {
                String message = "addPayment:" + controller.getPaymentInfo();
                client.sendMessageToServer(message);
                String messageFromServer = client.readMessageFromServer();
                System.out.println(messageFromServer);
                getAllPayments();


                //refresh the scene
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("Gestion-finance.fxml"));
                    Stage stage = (Stage) financeUI.getScene().getWindow();
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
    public void showAddPurchaseDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(financeUI.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addPurchaseDialog.fxml"));
        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());

            // Define button types
            ButtonType addButtonType = new ButtonType("Add Purchase", ButtonBar.ButtonData.OK_DONE);
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
            addButton.setDisable(true);
            AddPurchaseController controller = fxmlLoader.getController();
//this code for check that textFields and date picker is not empty
            controller.getProductName().textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if (t1.trim().isEmpty() || controller.getDate().getValue() == null || controller.getMontant().getText().trim().isEmpty()) {
                        addButton.setDisable(true);
                    } else {
                        addButton.setDisable(false);
                    }
                }
            });

            controller.getMontant().textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if (t1.trim().isEmpty() || controller.getDate().getValue() == null || controller.getProductName().getText().trim().isEmpty()) {
                        addButton.setDisable(true);
                    } else {
                        addButton.setDisable(false);
                    }
                }
            });

            controller.getDate().valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                    if (t1.toString().trim().isEmpty() || controller.getMontant().getText().trim().isEmpty() ||
                            controller.getProductName().getText().trim().isEmpty()) {
                        addButton.setDisable(true);
                    } else {
                        addButton.setDisable(false);
                    }
                }
            });
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == addButtonType) {
                String message = "addPurchase:" + controller.getPurchaseInfo();
                client.sendMessageToServer(message);
                String messageFromServer = client.readMessageFromServer();
                System.out.println(messageFromServer);
                getAllPayments();


                //refresh the scene
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("Gestion-finance.fxml"));
                    Stage stage = (Stage) financeUI.getScene().getWindow();
                    stage.setScene(new Scene(root, 800, 600));
                } catch (IOException e) {
                    System.out.println("a problem in loading the gestion finance scene in Gerant class");
                }
            } else {
                System.out.println("cancel pressed");
            }

        } catch (IOException e) {
            System.out.println("couldn't load the AddClientDialog");
        }

    }


    @FXML
    private void handlePersonnelClick() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Gestion-Personnel.fxml"));
            Stage stage = (Stage) personnelHbox.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            System.out.println("a problem in loading the gestion personnel scene");
        }
    }

    @FXML
    private void handleStockClick() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Gestion-Stock.fxml"));

            Stage stage = (Stage) stockHbox.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            System.out.println("a problem in loadin the gestion personnel scene");
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


    public void addRow(String id, String userName, String montant, String date) {
        // Create labels for each cell in the row
        Label idLabel = new Label(id);
        Label userNameLabel = new Label(userName);
        Label montantLabel = new Label(montant);
        Label dateLabel = new Label(date);
        montantLabel.setTextFill(Color.RED);
        idLabel.setTextFill(Color.GRAY);
        montantLabel.setTextFill(Color.RED);
        userNameLabel.setTextFill(Color.GRAY);
        dateLabel.setTextFill(Color.GRAY);
        montantLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));
        idLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));
        userNameLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));
        dateLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));

        // Add labels to the GridPane at specific column indices
        int rowIndex = paymentGrid.getRowCount(); // Get the current row count

        paymentGrid.add(idLabel, 0, rowIndex);
        paymentGrid.add(userNameLabel, 1, rowIndex);
        paymentGrid.add(montantLabel, 2, rowIndex);
        paymentGrid.add(dateLabel, 3, rowIndex);

        // Update row constraints to allow for the new row
        paymentGrid.getRowConstraints().add(new RowConstraints(30)); // You can adjust the height as needed
    }


    public void addPurchaseRow(String id, String productName, String type, String montant, String date) {
        // Create labels for each cell in the row
        Label idLabel = new Label(id);
        Label ProductLabel = new Label(productName);
        Label typeLabel = new Label(type);
        Label montantLabel = new Label(montant);
        Label dateLabel = new Label(date);

        montantLabel.setTextFill(Color.RED);
        idLabel.setTextFill(Color.GRAY);

        ProductLabel.setTextFill(Color.GRAY);
        typeLabel.setTextFill(Color.GRAY);
        dateLabel.setTextFill(Color.GRAY);
        montantLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));
        idLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));
        ProductLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));
        dateLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));
        typeLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));

        // Add labels to the GridPane at specific column indices
        int rowIndex = achatsGrid.getRowCount(); // Get the current row count

        achatsGrid.add(idLabel, 0, rowIndex);
        achatsGrid.add(ProductLabel, 1, rowIndex);
        achatsGrid.add(typeLabel, 2, rowIndex);
        achatsGrid.add(montantLabel, 3, rowIndex);
        achatsGrid.add(dateLabel, 4, rowIndex);

        // Update row constraints to allow for the new row
        achatsGrid.getRowConstraints().add(new RowConstraints(30)); // You can adjust the height as needed
    }
    public void addEntrantRow(String id, String clientName, String montant, String date) {
        // Create labels for each cell in the row
        Label idLabel = new Label(id);
        Label clientLabel = new Label(clientName);
        Label montantLabel = new Label(montant);
        Label dateLabel = new Label(date);

        montantLabel.setTextFill(Color.GREEN);
        idLabel.setTextFill(Color.GRAY);

        clientLabel.setTextFill(Color.GRAY);
        dateLabel.setTextFill(Color.GRAY);
        montantLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));
        idLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));
        clientLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));
        dateLabel.setFont(Font.font("System ", FontWeight.BOLD, 10));

        // Add labels to the GridPane at specific column indices
        int rowIndex = entrantGrid.getRowCount(); // Get the current row count

        entrantGrid.add(idLabel, 0, rowIndex);
        entrantGrid.add(clientLabel, 1, rowIndex);
        entrantGrid.add(montantLabel, 2, rowIndex);
        entrantGrid.add(dateLabel, 3, rowIndex);

        // Update row constraints to allow for the new row
        entrantGrid.getRowConstraints().add(new RowConstraints(30)); // You can adjust the height as needed
    }

    @FXML
    private void handleMenuItemAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String selectedType = menuItem.getText();
        timeMenuButton.setText(selectedType);

        if (selectedType.equals("All Time")) {
            entrantGrid.getChildren().clear();
            entrantGrid.getRowConstraints().clear();

            paymentGrid.getChildren().clear();
            paymentGrid.getRowConstraints().clear();

            achatsGrid.getChildren().clear();
            achatsGrid.getRowConstraints().clear();

            sortantMontant=0;
            entrantMontant=0;
            paymentMontantTotal=0;
            purchaseMontantTotal=0;

            getAllEntrant();
            getAllPayments();
            getAllPurchases();

            sortantMontant = purchaseMontantTotal + paymentMontantTotal;
            sortant.setText("-" + sortantMontant);
            entrantLabel.setText(""+entrantMontant);
            netLabel.setText(""+(entrantMontant-sortantMontant));
            if(entrantMontant-sortantMontant>=0){
                netLabel.setTextFill(Color.GREEN);
            }else {
                netLabel.setTextFill(Color.RED);
            }

        } else if(selectedType.equals("Today")){
            entrantGrid.getChildren().clear();
            entrantGrid.getRowConstraints().clear();

            paymentGrid.getChildren().clear();
            paymentGrid.getRowConstraints().clear();

            achatsGrid.getChildren().clear();
            achatsGrid.getRowConstraints().clear();
            sortantMontant=0;
            entrantMontant=0;
            paymentMontantTotal=0;
            purchaseMontantTotal=0;


            getPurchasesForToday();
            getEntrantsForToday();
            getPaymentsForToday();
            sortantMontant = purchaseMontantTotal + paymentMontantTotal;
            sortant.setText("-" + sortantMontant);
            entrantLabel.setText(""+entrantMontant);
            netLabel.setText(""+(entrantMontant-sortantMontant));
            if(entrantMontant-sortantMontant>=0){
                netLabel.setTextFill(Color.GREEN);
            }else {
                netLabel.setTextFill(Color.RED);
            }
        } else if (selectedType.equals("this Month")) {
            entrantGrid.getChildren().clear();
            entrantGrid.getRowConstraints().clear();

            paymentGrid.getChildren().clear();
            paymentGrid.getRowConstraints().clear();

            achatsGrid.getChildren().clear();
            achatsGrid.getRowConstraints().clear();

            sortantMontant=0;
            entrantMontant=0;
            paymentMontantTotal=0;
            purchaseMontantTotal=0;

            getPaymentsForThisMonth();
            getPurchasesForThisMonth();
            getEntrantsForThisMonth();
            sortantMontant = purchaseMontantTotal + paymentMontantTotal;
            sortant.setText("-" + sortantMontant);
            entrantLabel.setText(""+entrantMontant);
            netLabel.setText(""+(entrantMontant-sortantMontant));
            if(entrantMontant-sortantMontant>=0){
                netLabel.setTextFill(Color.GREEN);
            }else {
                netLabel.setTextFill(Color.RED);
            }
        }

    }

}

