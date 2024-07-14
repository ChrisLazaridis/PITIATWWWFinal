package com.db;

import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Η κλάση Database χρησιμοποιείτε για την επικοινωνία με τη βάση δεδομένων MySQL
 */
public class Database {
    /**
     * Το όνομα χρήστη στο server της MySQL.
     */
    private String username;
    /**
     * Η διεύθυνση του server της MySQL.
     */
    private String url;
    /**
     * Ο κωδικός πρόσβασης στο server της MySQL.
     */
    private String password;
    /**
     * Η σύνδεση με τη βάση δεδομένων MySQL.
     */
    private Connection connection;

    /**
     * '
     * Αρχικοποιεί το αντικείμενο Database με τα στοιχεία της βάσης δεδομένων.
     *
     * @param username το όνομα χρήστη στο server της MySQL
     * @param url      η διεύθυνση του server της MySQL
     * @param password ο κωδικός πρόσβασης στο server της MySQL
     */
    public Database(String username, String url, String password) {
        this.username = username;
        this.url = url;
        this.password = password;
        this.connection = getConnection(username, url, password);
    }

    /**
     * Instantiates a new Database.
     */
    public Database() {
    }

    /**
     * Αρχικοποιεί Επιστρέφει μια σύνδεση με τη βάση δεδομένων MySQL.
     *
     * @param username το όνομα χρήστη
     * @param url      η διεύθυνση του server
     * @param password ο κωδικός πρόσβασης
     * @return μια σύνδεση με τη βάση δεδομένων MySQL
     */
    public static Connection getConnection(String username, String url, String password){
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Επιστρέφει τη σύνδεση με τη βάση δεδομένων.
     *
     * @return η σύνδεση με τη βάση δεδομένων
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Επιστρέφει το όνομα χρήστη στο server της MySQL.
     *
     * @return το όνομα χρήστη στο server της MySQL
     */
    public String getUsername() {
        return username;
    }

    /**
     * Θέτει το όνομα χρήστη στο server της MySQL.
     *
     * @param username το όνομα χρήστη στο server της MySQL
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Επιστρέφει τον κωδικό πρόσβασης στο server της MySQL.
     *
     * @return ο κωδικός πρόσβασης στο server της MySQL
     */
    public String getPassword() {
        return password;
    }

    /**
     * Θέτει τον κωδικό πρόσβασης στο server της MySQL.
     *
     * @param password ο κωδικός πρόσβασης στο server της MySQL
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Θέτει τη σύνδεση με τη βάση δεδομένων.
     *
     * @param connection η σύνδεση με τη βάση δεδομένων
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Κρυπτογραφεί τον κωδικό πρόσβασης.
     *
     * @param password ο κωδικός πρόσβασης
     * @return ο κρυπτογραφημένος κωδικός πρόσβασης
     */
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Ελέγχει αν ο κωδικός πρόσβασης είναι σωστός.
     *
     * @param password       ο κωδικός πρόσβασης
     * @param hashedPassword ο κρυπτογραφημένος κωδικός πρόσβασης
     * @return true εάν ο κωδικός πρόσβασης είναι σωστός, αλλιώς false
     */
    public boolean checkPassword(String password, String hashedPassword) {
        try {
            // Ensure the input password is in UTF-8 encoding
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
            String utf8Password = new String(passwordBytes, StandardCharsets.UTF_8);

            return BCrypt.checkpw(utf8Password, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Log the error and handle it appropriately
            System.err.println("Invalid salt revision in hashed password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ανανεώνει τη σύνδεση με τη βάση δεδομένων.
     *
     * @throws SQLException εάν υπάρξει σφάλμα στην επικοινωνία με τη βάση δεδομένων
     */
    public void refreshConnection() throws SQLException{
        this.connection = getConnection(username, url, password);
    }

}
