import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private DatabaseConnector databaseConnector;
    private boolean isLoggedIn = false;
    private String type;
    private String userName;

   private String[] parts;
   private String controller;
   private String data;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            databaseConnector = DatabaseConnector.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!isLoggedIn) {
                    String receivedMessage = dis.readUTF();
                    String[] parts = receivedMessage.split(":", 2);
                    String username = parts[0];
                    String password = parts[1];

                    if (databaseConnector.authenticate(username, password)) {
                        this.type = databaseConnector.getUserType(username, password);
                        System.out.println("authentication succsfuly");
                        this.userName=username;

                        dos.writeUTF(type);
                        System.out.println("Write UTF of the type");
                        isLoggedIn = true;
                        // Continue handling client requests...
                    } else {
                        dos.writeUTF("Authentication failed");
                        System.out.println("authentication failed");

                    }

                } else {
                    // Client is logged in, proceed with handling client's requests
                    String receivedMessage = dis.readUTF();
                    // Implement protocol to handle client's requests
                    // handleRequest(receivedMessage);

                    switch (type) {
                        case "gerant":
                            // Handle doctor's requests
                            //  System.out.println(receivedMessage);
                            System.out.println(receivedMessage);
                             parts = receivedMessage.split(":", 2);
                           controller = parts[0];
                            data = parts[1];

                            if (controller.equals("addClient")) {
                                addClientHandler(data);
                                dos.writeUTF("addClientSucced");
                            }
                            if (controller.equals("deleteClient")){
                                deleteClientHandler(data);
                            }
                            if(controller.equals("deleteProduct")){
                                deleteProductHandler(data);
                            }
                            if(controller.equals("editClient")){
                                editClientHandler(data);
                            }
                            if(controller.equals("editProduct")){
                                editProductHandler(data);
                            }
                            if (controller.equals("addPayment")){
                                addPaymentHandler(data);

                            }
                            if (controller.equals("addPurchase")){
                                addPurchaseHandler(data);
                            }
                            if (controller.equals("addProduct")){
                                addProductHandler(data);
                            }
                            if (controller.equals("getAllUsers")) {
                                sendAllUsersToClient();
                            }
                            if (controller.equals("getAllUsersByType")){
                                sendAllUsersByTypeToClient(data);
                            }
                            if (controller.equals("getAllUsersBySearch")){
                                sendAllUsersBySearchToClient(data);
                            }
                            if (controller.equals("getAllUsersBySearchByType")){
                                sendAllUsersBySearchByTypeToClient(data);
                            }
                            if (controller.equals("getAllPayment")){
                                sendAllPaymentToClient();
                            }
                            if(controller.equals("getAllPaymentsForToday")){
                                sendAllPaymentsForTodayToClient();
                            }
                            if (controller.equals("getAllPaymentsForThisMonth")){
                                sendAllPaymentsForThisMonthToClient();
                            }
                            if(controller.equals("getAllProduct")){
                                sendAllProductToClient();
                            }
                            if (controller.equals("getAllProductsByType")){
                                sendAllProductsByTypeToClient();
                            }
                            if (controller.equals("getAllProductsBySearch")){
                                sendAllProductsBySearchToClient();
                            }
                            if (controller.equals("getAllProductsBySearchByType")){
                                sendAllProductsBySearchByTypeToClient();
                            }
                            if (controller.equals("getAllPurchase")){
                                sendAllPurchaseToClient();
                            }
                            if (controller.equals("getPurchasesForToday")){
                                sendAllPurchasesForTodayToClient();
                            }
                            if(controller.equals("getPurchasesForThisMonth")){
                                sendPurchasesForThisMonthToClient();
                            }
                            if (controller.equals("getAllEntrant")){
                                sendAllEntrantToClient();
                            }
                            if (controller.equals("getEntrantsForToday")){
                                sendEntrantsForTodayToClient();
                            }
                            if(controller.equals("getEntrantsForThisMonth")){
                                sendEntrantsForThisMonthToClient();
                            }
                            if(controller.equals("getUserName")){
                                dos.writeUTF(userName);
                            }
                            if (controller.equals("logout")){
                                this.isLoggedIn=false;
                            }

                            break;
                        case "doctor":
                            // Handle reception agent's requests
                            System.out.println(receivedMessage);
                            parts = receivedMessage.split(":", 2);
                            controller = parts[0];
                            data = parts[1];

                            if(controller.equals("getUserName")){
                                dos.writeUTF(userName);
                            }
                            if (controller.equals("getAnalyses")) {
                                sendAllAnalyseToClient(data);
                            }
                            if(controller.equals("getDateBilan")) {
                                getDateBilanHandler(data);
                            }
                            if(controller.equals("editState")) {
                                editStateHandler(data);
                            }
                            if(controller.equals("getIdResultat")) {
                                getIdResultatHandler(data);
                            }
                            if (controller.equals("logout")){
                                this.isLoggedIn=false;
                            }

                            break;
                        case "agent":
                            // Handle manager's requests
                            System.out.println(receivedMessage);
                            parts = receivedMessage.split(":", 2);
                            controller = parts[0];
                            data = parts[1];

                            if(controller.equals("addClient")){
                                addPatientHandeler(data);
                            }
                            if (controller.equals("editPatient")) {
                                editPatientHandler(data);
                            }
                            if (controller.equals("getPatient")) {
                                getPatientHandler(data);
                            }
                            if(controller.equals("getAllPatients")){
                                sendAllPatientsToClient();
                            }
                            if(controller.equals("getUserName")){
                                dos.writeUTF(userName);
                            }
                            if(controller.equals("getAllAnalyses")){
                                sendAllAnalysesToClient();
                            }
                            if(controller.equals("getAllAnalysesByBilan")){
                                sendAllAnalysesByBilanToClient(data);
                            }
                            if(controller.equals("getAllBilans")) {
                                sendAllBilansToClient();
                            }
                            if(controller.equals("addBilan")){
                                addBilanHandeler(data);
                            }
                            if(controller.equals("deleteBilan")){
                                deleteBilanHandler(data);
                            }
                            if(controller.equals("getIdResultat")) {
                                getIdResultatHandler(data);
                            }
                            if(controller.equals("getRemarque")){
                                getRemarqueHandler(data);
                            }
                            if (controller.equals("logout")){
                                this.isLoggedIn=false;
                            }
                            break;
                        case "biologiste":
                            // Handle technician's requests
                            System.out.println(receivedMessage);
                            parts = receivedMessage.split(":", 2);
                            controller = parts[0];
                            data = parts[1];

                            if(controller.equals("getUserName")){
                                dos.writeUTF(userName);
                            }
                            if (controller.equals("logout")){
                                this.isLoggedIn=false;
                            }
                            if (controller.equals("getAnalyses")) {
                                sendAllAnalyseToClient(data);
                            }
                            if(controller.equals("addResult")){
                                addResultHandler(data);
                            }
                            if(controller.equals("getEtat")){
                                getEtatHandler(data);
                            }

                            break;
                        default:
                            // Handle unrecognized role
                            break;
                    }
                }

            }

        } catch (IOException | SQLException e) {
            closeEverything(socket, dis, dos);
        }

    }

    private void getRemarqueHandler(String data) {
        try {
            String remarque = databaseConnector.getRemarque(data);

            if(remarque == null) {
                dos.writeUTF("null");
            } else {
                dos.writeUTF(remarque);
            }
        } catch (SQLException | IOException e2) {
            e2.printStackTrace();
        }
    }

    private void sendAllAnalysesByBilanToClient(String data) {
        try {
            // Retrieve all users from the database
            List<Analyse> analysesList = databaseConnector.getAnalysesWithDetails(data);
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Analyse analyse : analysesList) {
                messageBuilder.append(analyse.getNom()).append(":").append(analyse.getPrix()).append(":").append(analyse.getResult()).append("\n");
            }
            System.out.println("message to send"+messageBuilder);
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    private void sendEntrantsForThisMonthToClient() {
        try {
            // Retrieve all purchase from the database
            List<Entrant> entrantList = databaseConnector.getEntrantsForThisMonth();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Entrant entrant : entrantList) {
                messageBuilder.append(entrant.getId()).append(":").append(entrant.getName()).append(":").append(entrant.getPrenom()).append(":").append(entrant.getMontant()).append(":").append(entrant.getDate()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
    }

    private void sendEntrantsForTodayToClient() {
        try {
            // Retrieve all purchase from the database
            List<Entrant> entrantList = databaseConnector.getEntrantsForToday();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Entrant entrant : entrantList) {
                messageBuilder.append(entrant.getId()).append(":").append(entrant.getName()).append(":").append(entrant.getPrenom()).append(":").append(entrant.getMontant()).append(":").append(entrant.getDate()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
    }

    private void sendPurchasesForThisMonthToClient() {
        try {
            // Retrieve all purchase from the database
            List<Purchase> purchaseList = databaseConnector.getPurchasesForThisMonth();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Purchase purchase : purchaseList) {
                messageBuilder.append(purchase.getId()).append(":").append(purchase.getProductName()).append(":").append(purchase.getType()).append(":").append(purchase.getMontant()).append(":").append(purchase.getDate()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
    }

    private void sendAllPurchasesForTodayToClient() {
        try {
            // Retrieve all purchase from the database
            List<Purchase> purchaseList = databaseConnector.getPurchasesForToday();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Purchase purchase : purchaseList) {
                messageBuilder.append(purchase.getId()).append(":").append(purchase.getProductName()).append(":").append(purchase.getType()).append(":").append(purchase.getMontant()).append(":").append(purchase.getDate()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
    }

    private void sendAllPaymentsForThisMonthToClient() {
        try {
            // Retrieve all users from the database
            List<Payment> paymentsList = databaseConnector.getAllPaymentsForThisMonth();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Payment payment : paymentsList) {
                messageBuilder.append(payment.getId()).append(":").append(payment.getUsername()).append(":").append(payment.getMontant()).append(":").append(payment.getDate()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
    }

    private void sendAllPaymentsForTodayToClient() {
        try {
            // Retrieve all users from the database
            List<Payment> paymentsList = databaseConnector.getAllPaymentsForToday();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Payment payment : paymentsList) {
                messageBuilder.append(payment.getId()).append(":").append(payment.getUsername()).append(":").append(payment.getMontant()).append(":").append(payment.getDate()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
    }

    private void sendAllEntrantToClient() {
        try {
            // Retrieve all purchase from the database
            List<Entrant> entrantList = databaseConnector.getAllEntrant();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Entrant entrant : entrantList) {
                messageBuilder.append(entrant.getId()).append(":").append(entrant.getName()).append(":").append(entrant.getPrenom()).append(":").append(entrant.getMontant()).append(":").append(entrant.getDate()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }

    }

    private void sendAllProductsBySearchByTypeToClient() {
        try {
            String parts[]=data.split(":",2);
            String searchQuery=parts[0];
            String productType=parts[1];
            // Retrieve all Products from the database
            List<Product> productList = databaseConnector.getAllProductsBySearchByType(searchQuery,productType);
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Product product : productList) {
                messageBuilder.append(product.getId()).append(":").append(product.getProductNom()).append(":").append(product.getQuantity()).append(":").append(product.getType()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAllProductsBySearchToClient() {
        try {
            // Retrieve all users from the database
            List<Product> productList = databaseConnector.getAllProductsBySearch(data);
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Product product : productList) {
                messageBuilder.append(product.getId()).append(":").append(product.getProductNom()).append(":").append(product.getQuantity()).append(":").append(product.getType()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAllProductsByTypeToClient() {
        try {
            // Retrieve all users from the database
            List<Product> productList = databaseConnector.getAllProductsByType(data);
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Product product : productList) {
                messageBuilder.append(product.getId()).append(":").append(product.getProductNom()).append(":").append(product.getQuantity()).append(":").append(product.getType()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    private void sendAllUsersBySearchByTypeToClient(String data) {
        try {
            String parts[]=data.split(":",2);
            String searchQuery=parts[0];
            String typeUser=parts[1];
            // Retrieve all users from the database
            List<User> userList = databaseConnector.getAllUsersBySearchByType(searchQuery,typeUser);
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (User user : userList) {
                messageBuilder.append(user.getNom()).append(":").append(user.getPrenom()).append(":").append(user.getUsername()).append(":").append(user.getPassword()).append(":").append(user.getType()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAllUsersBySearchToClient(String data) {
        try {
            // Retrieve all users from the database
            List<User> userList = databaseConnector.getAllUsersBySearch(data);
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (User user : userList) {
                messageBuilder.append(user.getNom()).append(":").append(user.getPrenom()).append(":").append(user.getUsername()).append(":").append(user.getPassword()).append(":").append(user.getType()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAllUsersByTypeToClient(String data) {
        try {
            // Retrieve all users from the database
            List<User> userList = databaseConnector.getAllUsersByType(data);
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (User user : userList) {
                messageBuilder.append(user.getNom()).append(":").append(user.getPrenom()).append(":").append(user.getUsername()).append(":").append(user.getPassword()).append(":").append(user.getType()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    private void addPurchaseHandler(String data) {
        String parts[] = data.split(":", 4);
        String productName = parts[0];
        String type = parts[1];
        String montant=parts[2];
        String date=parts[3];

        try {
            if (databaseConnector.addPurchase(productName,type,montant,date)) {
                System.out.println("purchase added succefuly");
                dos.writeUTF("purchase added succefuly");

            } else {
                System.out.println("purchase don't added");
                dos.writeUTF("purchase don't added");

            }        } catch (SQLException e) {
            System.out.println("error in addpurchase");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void sendAllPurchaseToClient() {
        try {
            // Retrieve all purchase from the database
            List<Purchase> purchaseList = databaseConnector.getAllPurchases();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Purchase purchase : purchaseList) {
                messageBuilder.append(purchase.getId()).append(":").append(purchase.getProductName()).append(":").append(purchase.getType()).append(":").append(purchase.getMontant()).append(":").append(purchase.getDate()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
    }
    private void editProductHandler(String data) {
        String parts[] = data.split(":", 4);
        String id=parts[0];
        String productName = parts[1];
        String quantity = parts[2];
        String type=parts[3];

        try {
            if (databaseConnector.editProduct(id,productName,quantity,type)) {
                System.out.println("product edit succefuly");
                dos.writeUTF("product edit succefuly");
            } else {
                System.out.println("product  edit failed");
                dos.writeUTF("product edit failed");
            }        } catch (SQLException e) {
            System.out.println("error in editProduct");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteProductHandler(String data) {
        try {
            if (databaseConnector.removeProduct(data)) {
                System.out.println("product removed succefuly");
                dos.writeUTF("product removed succefuly");
            } else {
                System.out.println("product don't removed");
                dos.writeUTF("product don't removed");

            }
        } catch (SQLException e) {
            System.out.println("a problem in deleteing product");
        } catch (IOException e) {
            System.out.println(" a problem in in write message to product");        }

    }

    private void sendAllProductToClient() {
        try {
            // Retrieve all users from the database
            List<Product> productsList = databaseConnector.getAllProduct();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Product product : productsList) {
                messageBuilder.append(product.getId()).append(":").append(product.getProductNom()).append(":").append(product.getQuantity()).append(":").append(product.getType()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
    }

    private void addProductHandler(String data) {
        String parts[] = data.split(":", 3);
        String productName = parts[0];
        String quantity = parts[1];
        String type=parts[2];

        try {
            if (databaseConnector.addProduct(productName, quantity,type)) {
                System.out.println("product added succefuly");
                dos.writeUTF("product added succefuly");

            } else {
                System.out.println("product don't added");
                dos.writeUTF("product don't added");

            }        } catch (SQLException e) {
            System.out.println("error in addproduct");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendAllPaymentToClient() {
            try {
                // Retrieve all users from the database
                List<Payment> paymentsList = databaseConnector.getAllPayment();
                // Create a StringBuilder to construct the message to send
                StringBuilder messageBuilder = new StringBuilder();
                for (Payment payment : paymentsList) {
                    messageBuilder.append(payment.getId()).append(":").append(payment.getUsername()).append(":").append(payment.getMontant()).append(":").append(payment.getDate()).append("\n");

                }
                // Send the message to the client
                dos.writeUTF(messageBuilder.toString());
            } catch (SQLException | IOException e) {
                e.printStackTrace();

        }
    }

    private void addPaymentHandler(String data) {
        String parts[] = data.split(":", 3);
        String username = parts[0];
        String montant = parts[1];
        String date = parts[2];
        try {
            if (databaseConnector.addPayment(username, montant, date)) {
                System.out.println("payment added succefuly");
                dos.writeUTF("payment added succefuly");

            } else {
                System.out.println("payment don't added");
                dos.writeUTF("payment don't added");

            }        } catch (SQLException e) {
            System.out.println("error in addpayment");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void editClientHandler(String data) {

        String parts[] = data.split(":", 5);
        String oldUsername=parts[0];
        String nom = parts[1];
        String prenom = parts[2];
        String username = parts[3];
        String password = parts[4];
        try {
            if (databaseConnector.editUser(oldUsername,username,nom,prenom,password)) {
                System.out.println("client edit succefuly");
                dos.writeUTF("client edit succefuly");
            } else {
                System.out.println("client  edit failed");
                dos.writeUTF("client edit failed");
            }        } catch (SQLException e) {
            System.out.println("error in editClient");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteClientHandler(String data) {
        try {
            if (databaseConnector.removeUser(data)) {
                System.out.println("client removed succefuly");
                dos.writeUTF("client removed succefuly");
            } else {
                System.out.println("client don't removed");
                dos.writeUTF("client don't removed");

            }
        } catch (SQLException e) {
            System.out.println("a problem in deleteing user");
        } catch (IOException e) {
            System.out.println(" a problem in in write message to client");        }

    }

    public void closeEverything(Socket socket, DataInputStream dis, DataOutputStream dos) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (dis != null)
                dis.close();
            if (dos != null)
                dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addClient(String nom, String prenom, String username, String password, String type) throws SQLException {
        if (databaseConnector.addClient(nom, prenom, username, password, type)) {
            System.out.println("client added succefuly");
        } else {
            System.out.println("client don't added");
        }
    }

    private void addClientHandler(String data) {
        String parts[] = data.split(":", 5);
        String nom = parts[0];
        String prenom = parts[1];
        String username = parts[2];
        String password = parts[3];
        String type = parts[4];
        try {
            addClient(nom, prenom, username, password, type);
        } catch (SQLException e) {
            System.out.println("error in addClient");
        }

    }

    private void sendAllUsersToClient() {
        try {
            // Retrieve all users from the database
            List<User> userList = databaseConnector.getAllUsers();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (User user : userList) {
                messageBuilder.append(user.getNom()).append(":").append(user.getPrenom()).append(":").append(user.getUsername()).append(":").append(user.getPassword()).append(":").append(user.getType()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addPatientHandeler(String data) {
        String parts[] = data.split(":", 7);
        String nom = parts[0];
        String prenom = parts[1];
        String sexe = parts[2];
        String date = parts[3];
        String tel = parts[4];
        String email = parts[5];
        String ville = parts[6];

        try {
            if (databaseConnector.addPatient(nom, prenom, sexe, date, tel, email, ville)) {
                System.out.println("product added succefuly");
                dos.writeUTF("product added succefuly");

            } else {
                System.out.println("product don't added");
                dos.writeUTF("product don't added");

            }        } catch (SQLException e) {
            System.out.println("error in addproduct");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void editPatientHandler(String data) {

        String parts[] = data.split(":");
        String id = parts[0];
        String nom = parts[1];
        String prenom = parts[2];
        String sexe = parts[3];
        String date = parts[4];
        String tel = parts[5];
        String email = parts[6];
        String ville = parts[7];

        try {
            if (databaseConnector.editPatient(id, nom, prenom, sexe, date, tel, email, ville)) {
                System.out.println("patient edit succefuly");
                dos.writeUTF("patient edit succefuly");
            } else {
                System.out.println("patient edit failed");
                dos.writeUTF("patient edit failed");
            }        } catch (SQLException e) {
            System.out.println("error in editPatient");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getPatientHandler(String data) {
        try {
            Patient patient = databaseConnector.getPatient(data);

            try {
                StringBuilder messageBuilder = new StringBuilder();
                messageBuilder.append(patient.getId()).append(":").append(patient.getNom()).append(":").append(patient.getPrenom()).append(":").append(patient.getDate_nais()).append(":").append(patient.getSexe()).append(":").append(patient.getTelephone()).append(":").append(patient.getEmail()).append(":").append(patient.getVille()).append("\n");
                dos.writeUTF(messageBuilder.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    private void sendAllPatientsToClient() {
        try {
            // Retrieve all users from the database
            List<Patient> patientList = databaseConnector.getAllPatients();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Patient patient : patientList) {
                messageBuilder.append(Integer.toString(patient.getId())).append(":").append(patient.getNom()).append(":").append(patient.getPrenom()).append(":").append(patient.getDate_nais()).append(":").append(patient.getSexe()).append("\n");
            }

            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAllAnalysesToClient() {
        try {
            // Retrieve all users from the database
            List<Analyse> analysesList = databaseConnector.getAllAnalyses();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Analyse analyse : analysesList) {
                messageBuilder.append(Integer.toString(analyse.getId())).append(":").append(analyse.getNom()).append(":").append(analyse.getNom_cours()).append(":").append(analyse.getPrix()).append("\n");
            }

            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAllBilansToClient() {
        try {
            // Retrieve all users from the database
            List<Bilan> bilanList = databaseConnector.getAllBilans();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Bilan bilan : bilanList) {
                messageBuilder.append(bilan.getId()).append(":").append(bilan.getId_client()).append(":").append(bilan.getNom()).append(":").append(bilan.getMontant()).append(":").append(bilan.getAnalyses()).append(":").append(bilan.getDate()).append("\n");
            }

            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addBilanHandeler(String data) {
        String parts[] = data.split(":");
        String id = parts[0];
        String date = parts[1];
        String analyses = parts[2];
        String prix = parts[3];

        String AlphaNumericString = "0123456789";

        // create StringBuffer size of AlphaNumericString
        StringBuilder code = new StringBuilder(8);
        StringBuilder factureId = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            code.append(AlphaNumericString.charAt(index));
        }

        for (int i = 0; i < 8; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            factureId.append(AlphaNumericString.charAt(index));
        }

        Double montant = 0.0;
        for(String i : prix.split(",")){
            montant += Double.parseDouble(i);
        }

        try {
            if(databaseConnector.addFacture(factureId.toString(), montant)) {
                if (databaseConnector.addBilan(id, factureId.toString(), date, code.toString())) {
                    String id_bilan = databaseConnector.getBilanByCode(code.toString());
                    System.out.println(id_bilan);

                    System.out.println(analyses);
                    System.out.println(analyses.split(","));
                    for(String an: analyses.split(",")) {
                        databaseConnector.addTube(id_bilan, an);
                    }

                    System.out.println("bilan added");
                    dos.writeUTF("bilan added");
                } else {
                    System.out.println("bilan not added");
                    dos.writeUTF("bilan not added");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //this is code of mohcine

    private void sendAllAnalyseToClient(String data) {
        try {
            List<Analyse> analyses = databaseConnector.getAllAnalyses(data);

            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (Analyse bilan : analyses) {
                messageBuilder.append(bilan.getCode()).append(":").append(bilan.getNom()).append(":").append(bilan.getNom_cours()).append(":").append(bilan.getResult()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
    }

    public void addResultHandler(String data){
        try {
            String numericString = "0123456789";
            StringBuilder id = new StringBuilder(8);

            for (int i = 0; i < 8; i++) {
                int index = (int) (numericString.length() * Math.random());
                id.append(numericString.charAt(index));
            }

            System.out.println(data);
            databaseConnector.addResult(id.toString(), data.split(":")[0]);
            databaseConnector.modifyBilanIdResultat(id.toString(),data.split(":")[1]);

            dos.writeUTF("Result added");
        } catch (SQLException | IOException e) {
            System.out.println("error in addResult");
        }
    }

    private void deleteBilanHandler(String data) {
        try {
            String id_resultat = databaseConnector.getBilanResultatId(data);

            if (databaseConnector.removeBilanAssociation(data)) {
                if (databaseConnector.removeBilan(data)) {
                    if (id_resultat != null) {
                        databaseConnector.removeResultat(id_resultat);
                    }
                    System.out.println("bilan removed succefuly");
                    dos.writeUTF("bilan removed succefuly");
                }
            } else {
                System.out.println("bilan don't removed");
                dos.writeUTF("bilan don't removed");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("a problem in deleteing bilan");
        } catch (IOException e) {
            System.out.println(" a problem in in write message to client");        }

    }

    private void getIdResultatHandler(String code) {
        try {
            String id = databaseConnector.getIdResultat(code);
            dos.writeUTF(id);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void editStateHandler(String data) {
        String remarque = data.split(":")[0];
        String state = data.split(":")[1];
        String code = data.split(":")[2];

        try {
            if(databaseConnector.editState(code, remarque, state)) {
                dos.writeUTF("edited state");
            } else {
                dos.writeUTF("failed to edit state");
            }
        } catch (SQLException | IOException e2) {
            e2.printStackTrace();
        }
    }

    private void getDateBilanHandler(String data) {
        try {
            String info = databaseConnector.getDateBilan(data);
            dos.writeUTF(info);
        } catch (SQLException | IOException e2) {
            e2.printStackTrace();
        }
    }

    private void getEtatHandler(String data) {
        try {
            String info = databaseConnector.getEtat(data);

            if(info == null) {
                dos.writeUTF("null");
            } else {
                dos.writeUTF(info);
            }
        } catch (SQLException | IOException e2) {
            e2.printStackTrace();
        }
    }
}
