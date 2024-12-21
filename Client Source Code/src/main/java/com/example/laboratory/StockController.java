package com.example.laboratory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StockController implements Initializable {
    private Client client;
    @FXML
    private HBox personnelHbox;
    @FXML
    private HBox financeHbox;
    @FXML
    private Text name;
    @FXML
    private HBox logout;
    private String userName;
    @FXML
    private Button addProduct;
    @FXML
    private GridPane productGrid;
    @FXML
    private VBox stockUI;
    @FXML
    private MenuButton typeMenuButton;
    @FXML
    private TextField searchTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        client = ClientManager.getSharedClient();
        client.sendMessageToServer("getUserName:");
        this.userName=client.readMessageFromServer();
        name.setText(userName);
        productGrid.getChildren().clear();
        getAllProducts();

        // Add action listener to searchTextField
        searchTextField.setOnAction(event -> {
            String searchText = searchTextField.getText().trim();

            // Perform search based on searchText

            productGrid.getChildren().clear();
            productGrid.getRowConstraints().clear();
            if (typeMenuButton.getText().equals("All Products")){
                getAllProductsBySearch(searchText);

           }else{
                getAllProductsBySearchByType(searchText,typeMenuButton.getText());
            }
        });


    }

    private void getAllProducts() {
        client.sendMessageToServer("getAllProduct: ");
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        if(messageFromServer!=null&& !messageFromServer.isEmpty()) {
            String[] productsData = messageFromServer.split("\n");

            addProductsToGrid(productsData);
        }
    }

    private void getAllProductsByType(String type) {
        client.sendMessageToServer("getAllProductsByType:"+type);
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        if(messageFromServer!=null&& !messageFromServer.isEmpty()) {
            String[] productsData = messageFromServer.split("\n");

            addProductsToGrid(productsData);
        }
    }
    public void getAllProductsBySearch(String search) {
        client.sendMessageToServer("getAllProductsBySearch:"+search);
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        String[] usersData = messageFromServer.split("\n");
if(!messageFromServer.isEmpty()) {
    addProductsToGrid(usersData);
}

    }
    public void getAllProductsBySearchByType(String search,String type) {
        client.sendMessageToServer("getAllProductsBySearchByType:"+search+":"+type);
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        String[] usersData = messageFromServer.split("\n");
        if(!messageFromServer.isEmpty()) {

            addProductsToGrid(usersData);
        }

    }
    private void addProductsToGrid(String[] productsData) {
        for(String user:productsData) {
            String parts[]=user.split(":",4);
            String id=parts[0];
            String productName=parts[1];
            String quantity=parts[2];
            String type=parts[3];

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addRow( id,  productName,  quantity,type);
                }
            });
        }

    }

    private void addRow(String id, String productName, String quantity,String type) {

        // Create labels for each cell in the row
        Label idLabel = new Label(id);
        Label productNameLabel = new Label(productName);
        Label quantityLabel = new Label(quantity);
        Label typeLabel=new Label(type);

        // Create buttons for delete and edit with icons
        Button deleteButton = createIconButton("images/Delete.png");
        Button editButton = createIconButton("images/Edit.png");

        // Add event handlers for delete and edit buttons
        deleteButton.setOnAction(event -> {
            // Implement delete action here
            // You can remove the row associated with this button
            client.sendMessageToServer("deleteProduct:"+id);
            String message=client.readMessageFromServer();
            System.out.println(message);

            //refresh the scene
            try {
                Parent root= FXMLLoader.load(getClass().getResource("Gestion-Stock.fxml"));
                Stage stage=(Stage) productGrid.getScene().getWindow();
                stage.setScene(new Scene(root,800,600));
            } catch (IOException e) {
                System.out.println("a problem in loading the gestion personnel scene in Gerant class");
            }

        });

        editButton.setOnAction(event -> {
            // Implement edit action here
            // You can open a dialog or update the row data for editing
          showEditProductDialog(id,productName,quantity,type);

        });


        idLabel.setFont(Font.font("System ", FontWeight.BOLD,10));
        productNameLabel.setFont(Font.font("System ",FontWeight.BOLD,10));
        quantityLabel.setFont(Font.font("System ",FontWeight.BOLD,10));
        typeLabel.setFont(Font.font("System ",FontWeight.BOLD,10));

        idLabel.setTextFill(Color.GRAY);
        productNameLabel.setTextFill(Color.GRAY);
        quantityLabel.setTextFill(Color.GRAY);
        typeLabel.setTextFill(Color.GRAY);


        // Add labels to the GridPane at specific column indices
        int rowIndex = productGrid.getRowCount(); // Get the current row count

        productGrid.add(idLabel, 0, rowIndex);
        productGrid.add(productNameLabel, 1, rowIndex);
        productGrid.add(quantityLabel, 2, rowIndex);
        productGrid.add(typeLabel,3,rowIndex);
        productGrid.add(editButton,4,rowIndex);
        productGrid.add(deleteButton,5,rowIndex);

        // Update row constraints to allow for the new row
        productGrid.getRowConstraints().add(new RowConstraints(30)); // You can adjust the height as needed

    }

    private Button createIconButton(String iconName) {
        Button button = new Button();
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(iconName)));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        button.setGraphic(imageView);        return button;
    }

    @FXML
    public void showAddProductDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(stockUI.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addProductDialog.fxml"));
        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());

            // Define button types
            ButtonType addButtonType = new ButtonType("Add Product", ButtonBar.ButtonData.OK_DONE);
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
            AddProductController controller = fxmlLoader.getController();

            // Create a list of text fields
            List<TextField> textFields = Arrays.asList(controller.getProductName(), controller.getQuantity());

            // Add listeners to text fields
            textFields.forEach(textField -> textField.textProperty().addListener((observable, oldValue, newValue) ->
                    addButton.setDisable(textFields.stream().anyMatch(field -> field.getText().trim().isEmpty()))
            ));

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == addButtonType) {
                String message = "addProduct:" + controller.getProductInfo();
                client.sendMessageToServer(message);
                String messageFromServer = client.readMessageFromServer();
                System.out.println(messageFromServer);
                getAllProducts();


                //refresh the scene
                try {
                    Parent root= FXMLLoader.load(getClass().getResource("Gestion-Stock.fxml"));
                    Stage stage=(Stage) stockUI.getScene().getWindow();
                    stage.setScene(new Scene(root,800,600));
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
    public void showEditProductDialog(String id,String productName,String quantity,String type) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(stockUI.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editProductDialog.fxml"));
        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());
            EditProductDialog controller = fxmlLoader.getController();
            controller.setTextFields(productName, quantity,type);
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

                String message = "editProduct:" +id+":"+ controller.getUserInfo();
                client.sendMessageToServer(message);
                String messageFromServer = client.readMessageFromServer();
                System.out.println(messageFromServer);
                getAllProducts();


                //refresh the scene
                try {
                    Parent root= FXMLLoader.load(getClass().getResource("Gestion-Stock.fxml"));
                    Stage stage=(Stage) productGrid.getScene().getWindow();
                    stage.setScene(new Scene(root,800,600));
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
    private void handlePersonnelClick(){
        try {
            Parent root= FXMLLoader.load(getClass().getResource("Gestion-Personnel.fxml"));
            Stage stage=(Stage) personnelHbox.getScene().getWindow();
            stage.setScene(new Scene(root,800,600));
        } catch (IOException e) {
            System.out.println("a problem in loading the gestion personnel scene");
        }
    }
    @FXML
    private void handleMenuItemAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String selectedType = menuItem.getText();
        typeMenuButton.setText(selectedType);

        if (!selectedType.equals("All Products")) {
            productGrid.getChildren().clear();
            productGrid.getRowConstraints().clear();
            getAllProductsByType(selectedType);
        } else {
            productGrid.getChildren().clear();
            productGrid.getRowConstraints().clear();
            getAllProducts();
        }
    }

    @FXML
    private void handleFinanceClick() {
        try {
            Parent root=FXMLLoader.load(getClass().getResource("Gestion-finance.fxml"));

            Stage stage=(Stage) financeHbox.getScene().getWindow();
            stage.setScene(new Scene(root,800,600));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void handleLogout(){
        try {
            client.sendMessageToServer("logout:");
            Parent root=FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage=(Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            System.out.println("problem in load the login page");        }
    }

}
