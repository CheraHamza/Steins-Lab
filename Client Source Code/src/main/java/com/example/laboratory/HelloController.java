package com.example.laboratory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button login_btn;
    private Client client;
    private String type = "";
    @FXML
    private Text errorText;
      @FXML
      private Label alert;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // try {

        //  client.receiveMessageFromServer();

        login_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                client =ClientManager.getSharedClient();

                if(client!=null){

                    client.setOnUserTypeReceived(userType -> {
                        Platform.runLater(() -> {
                            closeCurrentStage();
                            openNewStage(userType);
                        });
                    });

                    errorText.setText("");
                    String userInfo = username.getText() + ":" + password.getText();
                    client.sendMessageToServer(userInfo);
                    handleAuthentication();

                }else{
                    ClientManager.initializeClient();
                    client=ClientManager.getSharedClient();
                    alert.setText("problem in server or connection ");
                }

            }}

        );

        //  } catch (IOException e) {
        //    e.printStackTrace();
        //}

    }
    private void closeCurrentStage() {
        ((Stage) login_btn.getScene().getWindow()).close();
    }

    private void openNewStage(String userType) {
        try {
            String fxmlPath;
            if (userType.equals("gerant")) {
                fxmlPath = "Gestion-Personnel.fxml";

            }  else if(userType.equals("doctor")){
                fxmlPath = "Validation.fxml";

            } else if (userType.equals("agent")) {
                fxmlPath="Reception.fxml";

            } else if (userType.equals("biologiste")) {
                fxmlPath="Plan.fxml";

            } else {
                fxmlPath = "User.fxml";

            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleAuthentication() {
            String messageFromServer= client.readMessageFromServer();
            if(messageFromServer!=null){
            if (messageFromServer.equals("Authentication failed")) {
                errorText.setText("username ou password est fausse.");
                System.out.println("Authentication failed");
                // Handle authentication failure, show error message, etc.

            } else if (messageFromServer.equals("gerant")) {
                this.type = "gerant";
                System.out.println(this.type);
                // Notify UI controller about user type
                closeCurrentStage();
                openNewStage("gerant");

            } else if (messageFromServer.equals("agent")) {
                this.type = "agent";
                System.out.println(this.type);
                // Notify UI controller about user type
                closeCurrentStage();
                openNewStage("agent");
                
            } else if (messageFromServer.equals("doctor")) {
                this.type = "doctor";
                System.out.println(this.type);
                // Notify UI controller about user type
                closeCurrentStage();
                openNewStage("doctor");

            } else if (messageFromServer.equals("biologiste")) {
                this.type = "biologiste";
                System.out.println(this.type);
                // Notify UI controller about user type
                closeCurrentStage();
                openNewStage("biologiste");

            }

    }
    }

}


