import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseConnector {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/stein";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static Connection connection;
    private static DatabaseConnector instance;

    // Private constructor to prevent instantiation from outside
    private DatabaseConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get instance of DatabaseConnector (Singleton pattern)
    public static synchronized DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    // Method to authenticate user (synchronized)
    public synchronized boolean authenticate(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If a row is returned, authentication is successful
        }
    }

    // Method to get user type (synchronized)
    public synchronized String getUserType(String username, String password) throws SQLException {
        String userType = null;
        String query = "SELECT type FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userType = resultSet.getString("type");
            }
        }
        return userType;
    }

    // Method to add a new client (synchronized)
    public synchronized boolean addClient(String nom, String prenom, String username, String password, String type) throws SQLException {
        String query = "INSERT INTO users (nom, prenom, username, password, type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, type);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if insertion was successful)
            return rowsAffected > 0;
        }
    }

//    public synchronized boolean addPayment( String username, String montant, String date) throws SQLException {
//        String query = "INSERT INTO payment ( id_user, montant, date) VALUES (?, ?, ?)";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setString(1, username);
//            preparedStatement.setString(2, montant);
//            preparedStatement.setString(3, date);
//
//            int rowsAffected = preparedStatement.executeUpdate();
//            // Check if any rows were affected (1 row should be affected if insertion was successful)
//            return rowsAffected > 0;
//        }
//    }

    public synchronized boolean addPayment(String username, String montant, String date) throws SQLException {
        // Query to find the id_user from username
        String findUserIdQuery = "SELECT id FROM users WHERE username = ?";
        // Query to insert the payment
        String insertPaymentQuery = "INSERT INTO payment (id_user, montant, date) VALUES (?, ?, ?)";

        try (PreparedStatement findUserIdStatement = connection.prepareStatement(findUserIdQuery)) {
            // Set the username parameter
            findUserIdStatement.setString(1, username);
            ResultSet resultSet = findUserIdStatement.executeQuery();

            // Check if user exists and get the id_user
            if (resultSet.next()) {
                int idUser = resultSet.getInt("id");

                // Now insert the payment using the found id_user
                try (PreparedStatement insertPaymentStatement = connection.prepareStatement(insertPaymentQuery)) {
                    insertPaymentStatement.setInt(1, idUser);
                    insertPaymentStatement.setString(2, montant);
                    insertPaymentStatement.setString(3, date);

                    int rowsAffected = insertPaymentStatement.executeUpdate();
                    // Check if any rows were affected (1 row should be affected if insertion was successful)
                    return rowsAffected > 0;
                }
            } else {
                // Username not found
                return false;
            }
        }
    }



    public synchronized boolean addProduct( String productName, String quantity,String type) throws SQLException {
        String query = "INSERT INTO product ( productName, quantity, type) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productName);
            preparedStatement.setString(2, quantity);
            preparedStatement.setString(3,type);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if insertion was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized boolean addPurchase(String productName, String type, String montant, String date) throws SQLException {
        String query = "INSERT INTO achats (productName, type, montant, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productName);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, montant);
            preparedStatement.setString(4, date);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if insertion was successful)
            return rowsAffected > 0;
        }
    }
//    public synchronized boolean removeUser(String username) throws SQLException {
//        String query = "DELETE FROM users WHERE username = ?";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setString(1, username);
//            int rowsAffected = preparedStatement.executeUpdate();
//            // Check if any rows were affected (1 row should be affected if deletion was successful)
//            return rowsAffected > 0;
//        }
//    }


    public synchronized boolean removeUser(String username) throws SQLException {
        // Query to find the id_user from username
        String findUserIdQuery = "SELECT id FROM users WHERE username = ?";
        // Query to delete related payments
        String deletePaymentsQuery = "DELETE FROM payment WHERE id_user = ?";
        // Query to delete the user
        String deleteUserQuery = "DELETE FROM users WHERE username = ?";

        try (PreparedStatement findUserIdStatement = connection.prepareStatement(findUserIdQuery)) {
            // Set the username parameter
            findUserIdStatement.setString(1, username);
            ResultSet resultSet = findUserIdStatement.executeQuery();

            // Check if user exists and get the id_user
            if (resultSet.next()) {
                int idUser = resultSet.getInt("id");

                // Delete related payments first
                try (PreparedStatement deletePaymentsStatement = connection.prepareStatement(deletePaymentsQuery)) {
                    deletePaymentsStatement.setInt(1, idUser);
                    deletePaymentsStatement.executeUpdate();
                }

                // Now delete the user
                try (PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUserQuery)) {
                    deleteUserStatement.setString(1, username);
                    int rowsAffected = deleteUserStatement.executeUpdate();
                    // Check if any rows were affected (1 row should be affected if deletion was successful)
                    return rowsAffected > 0;
                }
            } else {
                // Username not found
                return false;
            }
        }
    }

    public synchronized boolean removeProduct(String username) throws SQLException {
        String query = "DELETE FROM product WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if deletion was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized boolean removePurchase(int id) throws SQLException {
        String query = "DELETE FROM achats WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if deletion was successful)
            return rowsAffected > 0;
        }
    }
    // Method to edit an existing user (synchronized)
    public synchronized boolean editUser(String oldUsername, String newUsername, String newName, String newPrenom, String newPassword) throws SQLException {
        String query = "UPDATE users SET nom = ?, prenom = ?, username = ?, password = ?  WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newPrenom);
            preparedStatement.setString(3, newUsername);
            preparedStatement.setString(4, newPassword);
            preparedStatement.setString(5, oldUsername);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if update was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized boolean editProduct(String id, String productName, String quantity,String type) throws SQLException {
        String query = "UPDATE product SET productName = ?, quantity = ?,type = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productName);
            preparedStatement.setString(2, quantity);
            preparedStatement.setString(3,type);
            preparedStatement.setString(4, id);


            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if update was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized boolean editPurchase(String id, String productName, String type, String montant, String date) throws SQLException {
        String query = "UPDATE achats SET productName = ?, type = ?, montant = ?, date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productName);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, montant);
            preparedStatement.setString(4, date);
            preparedStatement.setString(5, id);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if update was successful)
            return rowsAffected > 0;
        }
    }
    public synchronized List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM users ORDER BY type ASC, nom ASC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("type")
                );
                userList.add(user);
            }
        }
        return userList;
    }
    public synchronized List<User> getAllUsersBySearch(String searchQuery) throws SQLException {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM users WHERE nom LIKE ? OR prenom LIKE ? OR username LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + searchQuery + "%");
            preparedStatement.setString(2, "%" + searchQuery + "%");
            preparedStatement.setString(3, "%" + searchQuery + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("type")
                );
                userList.add(user);
            }
        }
        return userList;
    }

    public synchronized List<User> getAllUsersBySearchByType(String searchQuery, String userType) throws SQLException {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM users WHERE (nom LIKE ? OR prenom LIKE ? OR username LIKE ?) AND type = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + searchQuery + "%");
            preparedStatement.setString(2, "%" + searchQuery + "%");
            preparedStatement.setString(3, "%" + searchQuery + "%");
            preparedStatement.setString(4, userType);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("type")
                );
                userList.add(user);
            }
        }
        return userList;
    }

    public synchronized List<User> getAllUsersByType(String type) throws SQLException {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM users WHERE type = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, type);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("type")
                );
                userList.add(user);
            }
        }
        return userList;
    }


    public synchronized List<Product> getAllProductsByType(String type) throws SQLException {
        List<Product> productsList = new ArrayList<>();
        String query = "SELECT * FROM product WHERE type = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, type);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("productName"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("type")

                );
                productsList.add(product);
            }
        }
        return productsList;
    }

    public synchronized List<Product> getAllProductsBySearch(String searchQuery) throws SQLException {
        List<Product> productsList = new ArrayList<>();
        String query = "SELECT * FROM product WHERE productName LIKE ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + searchQuery + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("productName"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("type")

                );
                productsList.add(product);
            }
        }
        return productsList;
    }

    public synchronized List<Product> getAllProductsBySearchByType(String searchQuery,String productType) throws SQLException {
        List<Product> productsList = new ArrayList<>();
        String query = "SELECT * FROM product WHERE (productName LIKE ?) AND type = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + searchQuery + "%");
            preparedStatement.setString(2, productType);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("productName"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("type")

                );
                productsList.add(product);
            }
        }
        return productsList;
    }


//    public synchronized List<Payment> getAllPayment() throws SQLException {
//        List<Payment> paymentsList = new ArrayList<>();
//        String query = "SELECT * FROM payment ORDER BY date DESC, id DESC";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                Payment payment = new Payment(
//                        resultSet.getInt("id"),
//                        resultSet.getString("id_user"),
//                        resultSet.getString("montant"),
//                        resultSet.getString("date")
//                );
//                paymentsList.add(payment);
//            }
//        }
//        return paymentsList;
//    }

    public synchronized List<Payment> getAllPayment() throws SQLException {
        List<Payment> paymentsList = new ArrayList<>();
        String query = "SELECT payment.id, users.username, payment.montant, payment.date " +
                "FROM payment " +
                "JOIN users ON payment.id_user = users.id " +
                "ORDER BY payment.date DESC, payment.id DESC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Payment payment = new Payment(
                        resultSet.getInt("id"),
                        resultSet.getString("username"), // Extracting the username
                        resultSet.getString("montant"),
                        resultSet.getString("date")
                );
                paymentsList.add(payment);
            }
        }
        return paymentsList;
    }


    public synchronized List<Payment> getAllPaymentsForToday() throws SQLException {
        List<Payment> paymentsList = new ArrayList<>();
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date(calendar.getTimeInMillis());

        // SQL query to select payments for today
        String query = "SELECT payment.id, users.username, payment.montant, payment.date " +
                "FROM payment " +
                "JOIN users ON payment.id_user = users.id " +
                "WHERE payment.date = ? " +
                "ORDER BY payment.id DESC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the current date as a parameter
            preparedStatement.setDate(1, currentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Payment payment = new Payment(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("montant"),
                        resultSet.getString("date")
                );
                paymentsList.add(payment);
            }
        }
        return paymentsList;
    }

    public synchronized List<Product> getAllProduct() throws SQLException {
        List<Product> productsList = new ArrayList<>();
        String query = "SELECT * FROM product";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("productName"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("type")
                );
                productsList.add(product);
            }
        }
        return productsList;
    }


    public synchronized List<Payment> getAllPaymentsForThisMonth() throws SQLException {
        List<Payment> paymentsList = new ArrayList<>();
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        // Set the start date of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // Get the first day of the current month
        Date startDate = new Date(calendar.getTimeInMillis());
        // Set the end date of the current month
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1); // Set to the last day of the month
        // Get the last day of the current month
        Date endDate = new Date(calendar.getTimeInMillis());

        // SQL query to select payments within the current month
        String query = "SELECT payment.id, users.username, payment.montant, payment.date " +
                "FROM payment " +
                "JOIN users ON payment.id_user = users.id " +
                "WHERE payment.date BETWEEN ? AND ? " +
                "ORDER BY payment.date DESC, payment.id DESC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set start and end date parameters
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Payment payment = new Payment(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("montant"),
                        resultSet.getString("date")
                );
                paymentsList.add(payment);
            }
        }
        return paymentsList;
    }


    public synchronized List<Purchase> getAllPurchases() throws SQLException {
        List<Purchase> purchasesList = new ArrayList<>();
        String query = "SELECT * FROM achats ORDER BY date DESC, id DESC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Purchase purchase = new Purchase(
                        resultSet.getInt("id"),
                        resultSet.getString("productName"),
                        resultSet.getString("type"),
                        resultSet.getString("montant"),
                        resultSet.getString("date")
                );
                purchasesList.add(purchase);
            }
        }
        return purchasesList;
    }

    public synchronized List<Purchase> getPurchasesForToday() throws SQLException {
        List<Purchase> purchasesList = new ArrayList<>();
        // Get the current date
        Date currentDate = new Date(System.currentTimeMillis());

        // SQL query to select purchases for today
        String query = "SELECT * FROM achats WHERE date = ? ORDER BY id DESC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the current date as a parameter
            preparedStatement.setDate(1, currentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Purchase purchase = new Purchase(
                        resultSet.getInt("id"),
                        resultSet.getString("productName"),
                        resultSet.getString("type"),
                        resultSet.getString("montant"),
                        resultSet.getString("date")
                );
                purchasesList.add(purchase);
            }
        }
        return purchasesList;
    }

    public synchronized List<Purchase> getPurchasesForThisMonth() throws SQLException {
        List<Purchase> purchasesList = new ArrayList<>();

        // Get the first day of the current month
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = new Date(calendar.getTimeInMillis());

        // Get the last day of the current month
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = new Date(calendar.getTimeInMillis());

        // SQL query to select purchases for this month
        String query = "SELECT * FROM achats WHERE date BETWEEN ? AND ? ORDER BY id DESC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set start and end date parameters
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Purchase purchase = new Purchase(
                        resultSet.getInt("id"),
                        resultSet.getString("productName"),
                        resultSet.getString("type"),
                        resultSet.getString("montant"),
                        resultSet.getString("date")
                );
                purchasesList.add(purchase);
            }
        }
        return purchasesList;
    }

    public synchronized List<Entrant> getAllEntrant() throws SQLException {
        List<Entrant> entrantList = new ArrayList<>();
        String query = "SELECT bilan.id_facture AS facteur_id, clients.nom AS client_nom, clients.prenom AS client_prenom, factures.montant AS montant_facture, " +
                "bilan.date_com AS date_facture " +
                "FROM bilan " +
                "JOIN clients ON bilan.id_client = clients.id " +
                "JOIN factures ON bilan.id_facture = factures.id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Entrant deliveryInfo = new Entrant(
                        resultSet.getInt("facteur_id"),
                        resultSet.getString("client_nom"),
                        resultSet.getString("client_prenom"),
                        resultSet.getString("montant_facture"),
                        resultSet.getString("date_facture")
                );
                entrantList.add(deliveryInfo);
            }
        }
        return entrantList;
    }


    public synchronized List<Entrant> getEntrantsForToday() throws SQLException {
        List<Entrant> entrantList = new ArrayList<>();
        // Get the current date
        Date currentDate = new Date(System.currentTimeMillis());

        // SQL query to select entrants for today
        String query = "SELECT bilan.id_facture AS facteur_id, clients.nom AS client_nom, clients.prenom AS client_prenom, factures.montant AS montant_facture, " +
                "bilan.date_com AS date_facture " +
                "FROM bilan " +
                "JOIN clients ON bilan.id_client = clients.id " +
                "JOIN factures ON bilan.id_facture = factures.id " +
                "WHERE DATE(bilan.date_com) = ?"; // Filter by the date of the transaction

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the current date as a parameter
            preparedStatement.setDate(1, currentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Entrant deliveryInfo = new Entrant(
                        resultSet.getInt("facteur_id"),
                        resultSet.getString("client_nom"),
                        resultSet.getString("client_prenom"),
                        resultSet.getString("montant_facture"),
                        resultSet.getString("date_facture")
                );
                entrantList.add(deliveryInfo);
            }
        }
        return entrantList;
    }


    public synchronized List<Entrant> getEntrantsForThisMonth() throws SQLException {
        List<Entrant> entrantList = new ArrayList<>();

        // Get the first day of the current month
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = new Date(calendar.getTimeInMillis());

        // Get the last day of the current month
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = new Date(calendar.getTimeInMillis());

        // SQL query to select entrants for this month
        String query = "SELECT bilan.id_facture AS facteur_id, clients.nom AS client_nom, clients.prenom AS client_prenom, factures.montant AS montant_facture, " +
                "bilan.date_com AS date_facture " +
                "FROM bilan " +
                "JOIN clients ON bilan.id_client = clients.id " +
                "JOIN factures ON bilan.id_facture = factures.id " +
                "WHERE DATE(bilan.date_com) BETWEEN ? AND ?"; // Filter by the date range

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set start and end date parameters
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Entrant deliveryInfo = new Entrant(
                        resultSet.getInt("facteur_id"),
                        resultSet.getString("client_nom"),
                        resultSet.getString("client_prenom"),
                        resultSet.getString("montant_facture"),
                        resultSet.getString("date_facture")
                );
                entrantList.add(deliveryInfo);
            }
        }
        return entrantList;
    }

    public synchronized boolean addPatient( String nom, String prenom, String sexe, String date, String tel, String email, String ville) throws SQLException {
        String query = "INSERT INTO `clients`(`nom`, `prenom`, `date_nais`, `sexe`, `telephone`, `email`, `ville`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, sexe);
            preparedStatement.setString(5, tel);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, ville);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if insertion was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized boolean editPatient(String id, String nom, String prenom, String sexe, String date, String tel, String email, String ville) throws SQLException {
        String query = "UPDATE `clients` SET `nom`= ?,`prenom`= ?,`date_nais`= ?,`sexe`= ?,`telephone`= ?,`email`= ?,`ville`= ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, sexe);
            preparedStatement.setString(5, tel);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, ville);
            preparedStatement.setString(8, id);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if insertion was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized List<Patient> getAllPatients() throws SQLException {
        List<Patient> patientList = new ArrayList<>();
        String query = "SELECT * FROM clients";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Patient patient = new Patient(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getDate("date_nais"),
                        resultSet.getString("sexe"),
                        resultSet.getString("telephone"),
                        resultSet.getString("email"),
                        resultSet.getString("ville")
                );
                patientList.add(patient);
            }
        }
        return patientList;
    }

    public synchronized List<Analyse> getAllAnalyses() throws SQLException {
        List<Analyse> analysesList = new ArrayList<>();
        String query = "SELECT * FROM analyses";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Analyse analyse = new Analyse(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("nom_court"),
                        resultSet.getFloat("prix")
                );

                analysesList.add(analyse);
            }
        }
        return analysesList;
    }

    public synchronized List<Bilan> getAllBilans() throws SQLException {
        List<Bilan> bilanList = new ArrayList<>();
        String query = "SELECT bilan.id, bilan.id_client, clients.nom, clients.prenom, factures.montant, analyses.nom_court, bilan.date_com FROM bilan, clients, factures, analyses, bilan_analyses WHERE bilan.id_client = clients.id AND bilan.id_facture = factures.id AND bilan.id = bilan_analyses.id_bilan AND analyses.id = bilan_analyses.id_analyse ORDER BY id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Bilan bilan = new Bilan(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_client"),
                        resultSet.getString("nom") + " " + resultSet.getString("prenom"),
                        resultSet.getDouble("montant"),
                        resultSet.getString("nom_court"),
                        resultSet.getDate("date_com")
                );

                bilanList.add(bilan);
            }
        }
        return bilanList;
    }

    public synchronized String getBilanByCode(String code) throws SQLException {
        String id = new String();
        String query = "SELECT `id`, `id_client`, `id_facture`, `id_resultas`, `date_com`, `code` FROM `bilan` WHERE `code` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id =  resultSet.getString("id");
            }
        }
        return id;
    }

    public synchronized Patient getPatient(String id) throws SQLException {
        Patient p = new Patient();
        String query = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Patient patient = new Patient(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getDate("date_nais"),
                        resultSet.getString("sexe"),
                        resultSet.getString("telephone"),
                        resultSet.getString("email"),
                        resultSet.getString("ville")
                );
                p = patient;
            }
        }
        return p;
    }

    public synchronized boolean addFacture(String id, Double montant) throws SQLException {
        String query = "INSERT INTO `factures`(`id`, `montant`) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,  id);
            preparedStatement.setDouble(2,  montant);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if insertion was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized boolean addTube( String id_bilan, String id_analyse) throws SQLException {
        String query = "INSERT INTO `bilan_analyses`(`id_bilan`, `id_analyse`) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id_bilan);
            preparedStatement.setString(2, id_analyse);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if insertion was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized boolean addBilan( String id, String factureId, String date, String code) throws SQLException {
        String query = "INSERT INTO `bilan`(`id_client`, `id_facture`, `date_com`, `code`) VALUES (?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, factureId);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, code);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if insertion was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized String getBilanResultatId(String id) throws SQLException {
        String query = "SELECT id_resultas FROM bilan WHERE id = ?";
        String id_resultat = new String();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id_resultat = resultSet.getString("id_resultas");
            }
            // Check if any rows were affected (1 row should be affected if deletion was successful)
            return id_resultat;
        }
    }

    public synchronized boolean removeResultat(String id) throws SQLException {
        String query = "DELETE FROM resultat WHERE resultat.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if deletion was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized boolean removeBilanAssociation(String id) throws SQLException {
        String query = "DELETE FROM bilan_analyses WHERE bilan_analyses.id_bilan = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if deletion was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized boolean removeBilan(String id) throws SQLException {
        String query = "DELETE bilan, factures FROM bilan JOIN factures ON factures.id = bilan.id_facture WHERE bilan.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if deletion was successful)
            return rowsAffected > 0;
        }
    }
    //this is the code of mohcine


    public synchronized List<Analyse> getAllAnalyses(String code) throws SQLException {
        List<Analyse> analyses = new ArrayList<>();
        String query ="SELECT b.code, a.id, a.nom, a.nom_court, r.resultats FROM bilan b JOIN bilan_analyses ba ON b.id = ba.id_bilan JOIN analyses a ON ba.id_analyse = a.id LEFT JOIN resultat r ON b.id_resultas = r.id WHERE b.code = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code); // Set the code parameter in the query
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Analyse analyse1 = new Analyse(
                        resultSet.getString("code"),
                        resultSet.getString("nom"),
                        resultSet.getString("nom_court"),
                        resultSet.getString("resultats")

                );
                analyses.add(analyse1);
            }



            System.out.println(analyses);
            return analyses;
        }


    }

    public synchronized List<Analyse> getAllAnalysesByBilan(String id) throws SQLException {
        List<Analyse> analyses = new ArrayList<>();
        String query = "SELECT b.id, a.id, a.nom, a.nom_court, r.resultats FROM bilan b " +
                "JOIN bilan_analyses ba ON b.id = ba.id_bilan " +
                "JOIN analyses a ON ba.id_analyse = a.id " +
                "LEFT JOIN resultat r ON b.id_resultas = r.id " +
                "WHERE b.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id); // Set the id parameter in the query
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Analyse analyse = new Analyse(
                        resultSet.getString("id"),  // Assuming the Analyse constructor takes an int for id
                        resultSet.getString("nom"),
                        resultSet.getString("nom_court"),
                        resultSet.getString("resultats")
                );
                analyses.add(analyse);
            }

            System.out.println(analyses);
            return analyses;
        }
    }
    public synchronized List<Analyse> getAnalysesWithDetails(String code) throws SQLException {
        List<Analyse> analyses = new ArrayList<>();
        String query = "SELECT a.nom, a.prix, r.resultats FROM bilan b " +
                "JOIN bilan_analyses ba ON b.id = ba.id_bilan " +
                "JOIN analyses a ON ba.id_analyse = a.id " +
                "LEFT JOIN resultat r ON b.id_resultas = r.id " +
                "WHERE b.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code); // Set the code parameter in the query
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Analyse analyse = new Analyse(
                        resultSet.getString("nom"),
                        resultSet.getFloat("prix"),
                        resultSet.getString("resultats")
                );
                analyses.add(analyse);
            }
        }
        return analyses;
    }


    public synchronized boolean addResult(String id, String resultat) throws SQLException {
        String query = "INSERT INTO resultat (id, resultats) VALUES (?, ?)"; // Use lowercase table name and column name
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, resultat);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if insertion was successful)
            return rowsAffected > 0;
        }
    }


    public synchronized boolean modifyBilanIdResultat(String id, String code) throws SQLException {
        String query = "UPDATE `bilan` SET `id_resultas` = ? WHERE code = ?"; // Use lowercase table name and column name
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, code);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if insertion was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized String getDateBilan(String code) throws SQLException {
        String data = new String();
        String query = "SELECT clients.nom, clients.prenom, bilan.date_com FROM clients, bilan WHERE bilan.id_client = clients.id AND bilan.code = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                data = resultSet.getString("nom") + " " + resultSet.getString("prenom") + ":" + resultSet.getString("date_com");
            }
        }
        return data;
    }

    public synchronized String getEtat(String code) throws SQLException {
        String data = new String();
        String query = "SELECT resultat.etat FROM bilan, resultat WHERE bilan.id_resultas = resultat.id AND bilan.code = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                data = resultSet.getString("etat");
            }
        }
        return data;
    }

    public synchronized String getRemarque(String code) throws SQLException {

        String remarque = "";
        String query = "SELECT resultat.remarque FROM bilan, resultat WHERE bilan.id_resultas = resultat.id AND bilan.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                remarque = resultSet.getString("remarque");
            }
        }
        return remarque;
    }


    public synchronized boolean editState(String code, String remarque, String state) throws SQLException {
        String query = "UPDATE `resultat` SET `remarque`= ?,`etat`= ? WHERE id = ?";
        Boolean etat;
        if(state.equals("true")) {
            etat = true;
        } else {
            etat = false;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, remarque);
            preparedStatement.setBoolean(2, etat);
            preparedStatement.setString(3, code);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if insertion was successful)
            return rowsAffected > 0;
        }
    }

    public synchronized String getIdResultat(String code) throws SQLException {
        String id = new String();
        String query = "SELECT bilan.id_resultas FROM bilan WHERE bilan.code = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getString("id_resultas");
            }
        }
        return id;
    }

    // Method to close the connection
    public synchronized void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}