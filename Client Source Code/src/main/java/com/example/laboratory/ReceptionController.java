package com.example.laboratory;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.*;
import java.net.URL;
import java.util.*;

public class ReceptionController implements Initializable {
    @FXML
    private Text name;
    @FXML
    private HBox logout;
    private Client client;
    private String userName;
    @FXML
    private VBox receptionUI;
    @FXML
    private GridPane bilanGrid;
    @FXML
    private TextField searchBar;
    @FXML
    private ChoiceBox datePicker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = ClientManager.getSharedClient();
        client.sendMessageToServer("getUserName:");
        this.userName=client.readMessageFromServer();
        name.setText(userName);

        searchBar.setOnAction(event -> {
            String searchText = searchBar.getText().trim();
            // Perform search based on searchText
            bilanGrid.getChildren().clear();
            bilanGrid.getRowConstraints().clear();
            getAllBilans(searchText);
        });

        datePicker.setOnAction(event -> {
            String searchText = searchBar.getText().trim();
            // Perform search based on searchText
            bilanGrid.getChildren().clear();
            bilanGrid.getRowConstraints().clear();
            getAllBilans(searchText);
        });

        getAllBilans("");
    }

    public void getAllBilans(String filter) {
        client.sendMessageToServer("getAllBilans: ");
        String messageFromServer = client.readMessageFromServer();
        String[] bilanData = messageFromServer.split("\n");

        if(filter.equals("")) {
            addBilansToGrid(bilanData);
        } else {
            addBilansToGridBySearch(bilanData, filter);
        }
    }

    public void addBilansToGrid(String[] bilans) {
        if (bilans.length == 1) {
            return;
        }

        String idPrev = bilans[0].split(":")[0];
        ArrayList<String> analysesGroup = new ArrayList<>();
        int i = 0;

        while (i < bilans.length) {
            System.out.println(analysesGroup);
            if (bilans[i].split(":")[0].equals(idPrev)) {
                analysesGroup.add(bilans[i].split(":")[4]);
                i++;
            } else {
                idPrev = bilans[i].split(":")[0];
                System.out.println(bilans[i-1]);
                String[] parts = bilans[i-1].split(":");
                String id = parts[0];
                String id_client = parts[1];
                String nom = parts[2];
                String analyses = String.join(", ", analysesGroup);
                String montant = parts[3];
                String date = parts[5];

                if (!datePicker.getItems().contains(date)) {
                    datePicker.getItems().add(date);
                }

                if (datePicker.getValue().equals("All")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                                addRow(id, id_client, nom, montant, analyses);
                            }
                    });
                } else {
                    if (date.equals(datePicker.getValue())) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                    addRow(id, id_client, nom, montant, analyses);
                                }
                        });
                    }
                }
                analysesGroup = new ArrayList<>();
            }
        }
        System.out.println(bilans[i-1]);
        String[] parts = bilans[i-1].split(":");
        String id = parts[0];
        String id_client = parts[1];
        String nom = parts[2];
        String analyses = String.join(", ", analysesGroup);
        String montant = parts[3];
        String date = parts[5];

        if (!datePicker.getItems().contains(date)) {
            datePicker.getItems().add(date);
        }

        if (datePicker.getValue().equals("All")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addRow(id, id_client, nom, montant, analyses);
                }
            });
        } else {
            if (date.equals(datePicker.getValue())) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        addRow(id, id_client, nom, montant, analyses);
                    }
                });
            }
        }
    }

    public void addBilansToGridBySearch(String[] bilans, String filter) {
        if (bilans.length == 1) {
            return;
        }

        String idPrev = bilans[0].split(":")[0];
        ArrayList<String> analysesGroup = new ArrayList<>();
        int i = 0;

        while (i < bilans.length) {
            if (bilans[i].split(":")[0].equals(idPrev)) {
                analysesGroup.add(bilans[i].split(":")[4]);
                i++;
            } else {
                idPrev = bilans[i].split(":")[0];
                System.out.println(bilans[i-1]);
                String[] parts = bilans[i-1].split(":");
                String id = parts[0];
                String id_client = parts[1];
                String nom = parts[2];
                String analyses = String.join(", ", analysesGroup);
                String montant = parts[3];
                String date = parts[5];

                if (nom.equals(filter)) {
                    if (datePicker.getValue().equals("All")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                addRow(id, id_client, nom, montant, analyses);
                            }
                        });
                    } else {
                        if (date.equals(datePicker.getValue())) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                        addRow(id, id_client, nom, montant, analyses);
                                    }
                            });
                        }
                    }
                }
                analysesGroup = new ArrayList<>();
            }
        }

        System.out.println(bilans[i-1]);
        String[] parts = bilans[i-1].split(":");
        String id = parts[0];
        String id_client = parts[1];
        String nom = parts[2];
        String analyses = String.join(", ", analysesGroup);
        String montant = parts[3];
        String date = parts[5];

        if (nom.equals(filter)) {
            if (datePicker.getValue().equals("All")) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        addRow(id, id_client, nom, montant, analyses);
                    }
                });
            } else {
                if (date.equals(datePicker.getValue())) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            addRow(id, id_client, nom, montant, analyses);
                        }
                    });
                }
            }
        }
    }


    public void addRow(String id, String id_client, String nom, String montant, String analyses) {
        Label idLabel = new Label(id);
        Label nameLabel = new Label(nom);
        Label analysesLabel = new Label(analyses);
        Label montantLabel = new Label(montant);

        // Create buttons for delete and edit with icons
        Button deleteButton = createIconButton("images/Delete.png");
        Button printButton =createIconButton("images/Print.png");

        deleteButton.setOnAction(event -> {
            client.sendMessageToServer("deleteBilan:" + id);
            String data = client.readMessageFromServer();
            bilanGrid.getChildren().clear();
            bilanGrid.getRowConstraints().clear();
            getAllBilans(searchBar.getText().trim());
        });
        client.sendMessageToServer("getRemarque:"+id);
        String remarque=client.readMessageFromServer();
        printButton.setOnAction(event -> {
printAnalyse(id,nom,montant,analyses,remarque);
        });

        // Add labels to the GridPane at specific column indices
        int rowIndex = bilanGrid.getRowCount(); // Get the current row count
        bilanGrid.add(idLabel, 0, rowIndex);
        bilanGrid.add(nameLabel, 1, rowIndex);
        bilanGrid.add(analysesLabel, 2, rowIndex);
        bilanGrid.add(montantLabel, 3, rowIndex);
        bilanGrid.add(deleteButton, 4, rowIndex);

        if(!remarque.equals("")){
        bilanGrid.add(printButton,5,rowIndex);}

        // Update row constraints to allow for the new row
        bilanGrid.getRowConstraints().add(new RowConstraints(30));
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
            System.out.println("problem in load the login page");
        }
    }

    @FXML
    public void showNouveauDossierDialogue() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(receptionUI.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("nouveauDossierDialog.fxml"));
        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());
            ButtonType cancelButtonType = ButtonType.CANCEL;

            dialog.getDialogPane().getStylesheets().add(getClass().getResource("dialogStyle.css").toExternalForm());

            dialog.getDialogPane().getButtonTypes().addAll(cancelButtonType);

            Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancelButtonType);
            cancelButton.getStyleClass().add("dialog-cancelbutton");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                System.out.println("cancel");
            }

        } catch (IOException e) {
            e.printStackTrace(System.out);
            System.out.println("couldn't load the AddClientDialog");
        }
    }
    public void printAnalyse(String id, String name, String montant, String analyses, String remarque) {
        String src = "laboratory.pdf"; // Path to your existing PDF template
        String dest = "medical_report_filled.pdf";

        try {
            PdfReader reader = new PdfReader(src);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
            PdfContentByte content = stamper.getOverContent(1); // Assuming content goes on the first page

            // Set up fonts
            com.itextpdf.text.Font infoFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL);
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);

            // Add patient information
            ColumnText.showTextAligned(content, Element.ALIGN_LEFT, new Paragraph("Patient Information:", headerFont), 36, 700, 0);

            // Manually place each line of patient information
            String[] patientInfo = {
                    "Patient ID: " + id,
                    "Name: " + name,
                    "Montant: " + montant,
                    "Analyses: " + analyses
            };

            int yPosition = 680; // Starting y-position for the text
            for (String info : patientInfo) {
                ColumnText.showTextAligned(content, Element.ALIGN_LEFT, new Paragraph(info, infoFont), 36, yPosition, 0);
                yPosition -= 20; // Decrement y-position for the next line
            }

            // Add a blank line for spacing
            yPosition -= 20;

            // Add analysis results in a table
            PdfPTable table = new PdfPTable(3);
            table.setTotalWidth(527);
            table.setWidths(new float[]{1, 3, 1});

            // Add table header
            PdfPCell cell;
            cell = new PdfPCell(new Paragraph("Test", headerFont));
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Result", headerFont));
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Montant", headerFont));
            table.addCell(cell);

            client.sendMessageToServer("getAllAnalysesByBilan:"+id);
            String respond = client.readMessageFromServer();
            System.out.println(respond);
            String[] allAnalyse = respond.split("\n");

            for (String analyse : allAnalyse) {
                String[] info = analyse.split(":");
                String nomAnalyse = info[0];
                String resulta = info[2];
                String montant2 = info[1];
                table.addCell(nomAnalyse);
                table.addCell(resulta);
                table.addCell(montant2);
            }

            // Write the table at an absolute position
            float tableHeight = table.getTotalHeight();
            table.writeSelectedRows(0, -1, 36, yPosition, content);

            // Adjust yPosition for the next content
            yPosition -= tableHeight + 40; // Add some spacing after the table

            // Print the "remarque" text
            ColumnText.showTextAligned(content, Element.ALIGN_LEFT, new Paragraph("Remarque: " + remarque, headerFont), 36, yPosition, 0);

            // Close stamper and reader
            stamper.close();
            reader.close();

            System.out.println("PDF created successfully.");

            // Print the generated PDF
            printPDF(dest);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }


    public static void printPDF(String filePath) {
        try {
            // Load the PDF file
            File file = new File(filePath);
            InputStream fis = new FileInputStream(file);

            // Create a print job
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc doc = new SimpleDoc(fis, flavor, null);
            PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();

            // Locate a print service that can handle the document
            PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, null);
            if (services.length > 0) {
                PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
                DocPrintJob printJob = printService.createPrintJob();

                // Print the document
                printJob.print(doc, attrs);
                System.out.println("PDF printed successfully.");
            } else {
                System.out.println("No printer found.");
            }

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }

