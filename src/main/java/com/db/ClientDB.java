package com.db;

import com.Beans.Users.Client;
import com.Beans.Users.Seller;
import com.Beans.Util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση ClientDB χρησιμοποιείτε για την αλληλεπίδραση με τη βάση για ότι αφορά τις λειτουργίες
 * σχετικές με τους πελάτες.
 */
public class ClientDB {
    /**
     * Το αντικείμενο Database.
     */
    private final Database db;

    /**
     * Δημιουργεί ένα νέο αντικείμενο ClientDB.
     * Αρχικοποιεί ένα αντικείμενο Database.
     */
    public ClientDB() {
        db = new Database("root", "jdbc:mysql://localhost/uservault", "12345678PITIATwww$");
    }

    /**
     * Προσθέτει έναν νέο πελάτη στη βάση δεδομένων.
     * Προσθέτει τα τηλέφωνα, συνδεδεμένα με τον πελάτη, στη βάση δεδομένων.
     * Δημιουργείτε μια σχέση μεταξύ του πωλητή και του πελάτη.
     * Εάν το username ή το email υπάρχει ήδη στη βάση δεδομένων, επιστρέφει σφάλμα.
     *
     * @param client ο πελάτης
     * @param seller ο πωλητής
     * @throws Exception εάν υπάρξει σφάλμα κατά την προσθήκη του πελάτη
     */
    public void addClient(Client client, Seller seller) throws Exception {
        db.refreshConnection();
        if (client.existInDB()) {
            throw new Exception("Client already exists in the database.");
        }

        // Check if username already exists in the database
        String checkQuery = "SELECT client_id FROM clients WHERE username = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {

            checkStatement.setString(1, client.getUsername());

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    throw new Exception("Username already exists in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error checking for existing username in the database.", e);
        }

        db.refreshConnection();

        // Check if email already exists in the database
        checkQuery = "SELECT client_id FROM clients WHERE email = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {

            checkStatement.setString(1, client.getEmail());

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    throw new Exception("Email already exists in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error checking for existing email in the database.", e);
        }

        db.refreshConnection();

        // Insert new client into the database
        String insertQuery = "INSERT INTO clients (username, first_name, last_name, email, birthday, VAT, password_hash) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = db.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            setClientStatements(client, insertStatement);

            insertStatement.executeUpdate();
            client.setExistInDB(true);
            client.setClientID(this.searchClient(client.getUsername()).getClientId());

            // Collect phone numbers to be added after the iteration
            List<PhoneNumber> phoneNumbersToAdd = new ArrayList<>(client.getPhoneNumbers());

            // Add phone numbers after collecting them
            for (PhoneNumber phoneNumber : phoneNumbersToAdd) {
                addPhoneNumber(phoneNumber, client);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error adding client to the database.", e);
        }

        addClientSellerRelationship(client, seller);
    }

    /**
     * Ελέγχει αν το τηλέφωνο υπάρχει ήδη στη βάση δεδομένων.
     *
     * @param PhoneNumber το τηλέφωνο
     * @return true αν το τηλέφωνο υπάρχει, αλλιώς false
     * @throws SQLException εάν υπάρξει σφάλμα κατά τον έλεγχο του τηλεφώνου
     */
    public boolean checkIfPhoneNumberExists(String PhoneNumber) throws SQLException {
        db.refreshConnection();
        String query = "SELECT * FROM phonenumbers WHERE phone_number = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, PhoneNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Βοηθητική συνάρτηση για την εισαγωγή των στοιχείων του πελάτη στη βάση
     *
     * @param client    ο πελάτης
     * @param statement το statement εισαγωγής ή ανανέωσης του πελάτη στη βάση
     * @throws SQLException εάν υπάρξει σφάλμα κατά την επικοινωνία με τη βάση
     */
    private void setClientStatements(Client client, PreparedStatement statement) throws SQLException {
        statement.setString(1, client.getUsername());
        statement.setString(2, client.getFirstName());
        statement.setString(3, client.getLastName());
        statement.setString(4, client.getEmail());
        statement.setDate(5, new java.sql.Date(client.getBirthday().getTime()));
        statement.setString(6, client.getVAT());
        statement.setString(7, db.hashPassword(client.getPassword()));
    }

    /**
     * Προσθέτει σε ένα αντικείμενο πελάτη, το ID του μέσα στη βάση.
     *
     * @param client ο πελάτης
     * @throws SQLException εάν υπάρξει σφάλμα κατά την επικοινωνία με τη βάση
     */
    public void getClientId(Client client) throws SQLException {
        db.refreshConnection();
        String query = "SELECT client_id FROM clients WHERE username = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, client.getUsername());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    client.setClientID(resultSet.getInt("client_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ανανεώνει τα στοιχεία του πελάτη, όταν δε χρειάζεται ανανέωση το συνθηματικό (password)
     *
     * @param client ο πελάτης
     * @throws Exception εάν υπάρξει σφάλμα κατά την ανανέωση του πελάτη
     */
    public void updateClientNoPw(Client client) throws Exception {
        db.refreshConnection();
        String query = "UPDATE clients SET username = ?, first_name = ?, last_name = ?, email = ?, birthday = ?, VAT = ? WHERE client_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, client.getUsername());
            statement.setString(2, client.getFirstName());
            statement.setString(3, client.getLastName());
            statement.setString(4, client.getEmail());
            statement.setDate(5, new java.sql.Date(client.getBirthday().getTime()));
            statement.setString(6, client.getVAT());
            statement.setInt(7, client.getClientId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error updating client in the database.", e);
        }
        List<PhoneNumber> phoneNumbersToAdd = new ArrayList<>(client.getPhoneNumbers());

        // Add phone numbers after collecting them
        for (PhoneNumber phoneNumber : phoneNumbersToAdd) {
            if (checkIfPhoneNumberExists(phoneNumber.getPhoneNumber())) {
                changeProgramForPhoneNumber(phoneNumber, phoneNumber.getProgram());
            } else {
                addPhoneNumber(phoneNumber, client);
            }
        }
        // if after that there exist phone numbers in the database that are not in the client's list, delete them
        db.refreshConnection();
        query = "SELECT * FROM phonenumbers WHERE client_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, client.getClientId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    boolean found = false;
                    for (PhoneNumber phoneNumber : client.getPhoneNumbers()) {
                        if (phoneNumber.getPhoneNumber().equals(resultSet.getString("phone_number"))) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        PhoneNumber phoneNumber = new PhoneNumber();
                        phoneNumber.setPhoneNumber(resultSet.getString("phone_number"));
                        deletePhoneNumber(phoneNumber);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error deleting phone numbers from the database.", e);
        }

    }

    /**
     * Ψάχνει για έναν πελάτη στη βάση, με γνώμονα το username.
     *
     * @param username το username του πελάτη
     * @return τον πελάτη, εάν υπάρχει, αλλιώς null
     * @throws Exception εάν υπάρξει σφάλμα κατά την αναζήτηση του πελάτη
     */
    public Client searchClient(String username) throws Exception {
        db.refreshConnection();
        String query = "SELECT * FROM clients WHERE username = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Client client = new Client();
                    client.setUsername(resultSet.getString("username"));
                    client.setFirstName(resultSet.getString("first_name"));
                    client.setLastName(resultSet.getString("last_name"));
                    client.setEmail(resultSet.getString("email"));
                    client.setBirthday(resultSet.getDate("birthday"));
                    client.setPassword(resultSet.getString("password_hash"));
                    client.setClientID(resultSet.getInt("client_id"));
                    client.setVAT(resultSet.getString("VAT"));
                    client.setExistInDB(true);
                    getPhoneNumbers(client);
                    getClientBills(client);
                    return client;
                } else {
                    return null; // No seller found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error searching client in the database.", e);
        }
    }

    /**
     * Ανανεώνει τα στοιχεία του πελάτη στη βάση, όταν απαιτείτε αλλαγή του συνθηματικού (password)
     *
     * @param client ο πελάτης
     * @throws Exception εάν υπάρξει σφάλμα κατά την ανανέωση του πελάτη
     */
    public void updateClient(Client client) throws Exception {
        db.refreshConnection();
        if (client.existInDB()) {
            // Check if the client exists
            String query = "SELECT * FROM clients WHERE client_id = ?";
            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, client.getClientId());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new Exception("Client does not exist in the database.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Error verifying client existence in the database.", e);
            }
            db.refreshConnection();
            query = "UPDATE clients SET username = ?, first_name = ?, last_name = ?, email = ?, birthday = ?, VAT = ?, password_hash = ? WHERE client_id = ?";

            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, client.getUsername());
                statement.setString(2, client.getFirstName());
                statement.setString(3, client.getLastName());
                statement.setString(4, client.getEmail());
                statement.setDate(5, new java.sql.Date(client.getBirthday().getTime()));
                statement.setString(6, client.getVAT());
                statement.setString(7, db.hashPassword(db.hashPassword(client.getPassword())));

                statement.setInt(8, client.getClientId());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new Exception("No client found with the given ID and username.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("SomethingWentWrong", e);
            }
        } else {
            throw new Exception("Client does not exist in the database.");
        }
        List<PhoneNumber> phoneNumbersToAdd = new ArrayList<>(client.getPhoneNumbers());

        // Add phone numbers after collecting them
        for (PhoneNumber phoneNumber : phoneNumbersToAdd) {
            if (checkIfPhoneNumberExists(phoneNumber.getPhoneNumber())) {
                changeProgramForPhoneNumber(phoneNumber, phoneNumber.getProgram());
            } else {
                addPhoneNumber(phoneNumber, client);
            }
        }

    }

    /**
     * Διαγράφει το πελάτη από τη βάση
     *
     * @param client ο πελάτης
     * @throws Exception εάν υπάρξει σφάλμα κατά τη διαγραφή του πελάτη
     */
    public void deleteClient(Client client) throws Exception {
        db.refreshConnection();
        if (client.existInDB()) {
            ArrayList<PhoneNumber> phoneNumbers = client.getPhoneNumbers();
            if (!phoneNumbers.isEmpty()) {
                for (PhoneNumber phoneNumber : phoneNumbers) {
                    deletePhoneNumber(phoneNumber);
                }
            }
            db.refreshConnection();
            ArrayList<Bill> bills = client.getBills();
            if (!bills.isEmpty()) {
                for (Bill bill : bills) {
                    deleteBill(bill);
                }
            }
            // delete the seller relationship
            db.refreshConnection();
            String query = "DELETE FROM client_seller WHERE client_id = ?";
            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, client.getClientId());

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Error deleting client-seller relationship from the database.", e);
            }
            db.refreshConnection();
            query = "DELETE FROM clients WHERE client_id = ?";

            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, client.getClientId());

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted == 0) {
                    throw new Exception("No client found with the given ID.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Error deleting client from the database.", e);
            }
        } else {
            throw new Exception("Client does not exist in the database.");
        }
    }

    /**
     * Προσθέτει έναν αριθμό τηλεφώνου στη βάση
     * Εάν ο αριθμός υπάρχει ήδη, ανανεώνει τον πελάτη που τον κατέχει
     *
     * @param phoneNumber ο αριθμός τηλεφώνου
     * @param client      ο πελάτης
     * @throws Exception εάν υπάρξει σφάλμα κατά την προσθήκη του αριθμού τηλεφώνου
     */
    public void addPhoneNumber(PhoneNumber phoneNumber, Client client) throws Exception {
        db.refreshConnection();
        if (!client.existInDB()) {
            throw new Exception("Client does not exist in the database.");
        }

        // Check if the client exists
        String query = "SELECT * FROM clients WHERE client_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, client.getClientId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new Exception("Client does not exist in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error verifying client existence in the database.", e);
        }
        db.refreshConnection();
        query = "SELECT * FROM phonenumbers WHERE phone_number = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, phoneNumber.getPhoneNumber());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    db.refreshConnection();
                    query = "UPDATE phonenumbers SET client_id = ?, program_id = ? WHERE phone_number = ?";
                    try (Connection connection2 = db.getConnection();
                         PreparedStatement statement2 = connection2.prepareStatement(query)) {

                        statement2.setInt(1, client.getClientId());
                        statement2.setInt(2, phoneNumber.getProgram().getProgramID());
                        statement2.setString(3, phoneNumber.getPhoneNumber());

                        statement2.executeUpdate();
                        return;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new Exception("Error updating phone number in the database.", e);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error verifying phone number existence in the database.", e);
        }
        db.refreshConnection();
        query = "INSERT INTO phonenumbers (client_id, phone_number, program_id) VALUES (?, ?, ?)";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, client.getClientId());
            statement.setString(2, phoneNumber.getPhoneNumber());
            statement.setInt(3, phoneNumber.getProgram().getProgramID());

            statement.executeUpdate();
            client.addPhoneNumber(phoneNumber);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error adding phone number to the database.", e);
        }
    }

    /**
     * Προσθέτει στο πελάτη τους αριθμούς τηλεφώνου του
     *
     * @param client ο πελάτης
     * @throws Exception εάν υπάρξει σφάλμα κατά την προσθήκη των αριθμών τηλεφώνου
     */
    public void getPhoneNumbers(Client client) throws Exception {
        db.refreshConnection();
        if (!client.existInDB()) {
            throw new Exception("Client does not exist in the database.");
        }

        // Check if the client exists
        String query = "SELECT * FROM clients WHERE client_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, client.getClientId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new Exception("Client does not exist in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error verifying client existence in the database.", e);
        }
        db.refreshConnection();
        query = "SELECT * FROM phonenumbers WHERE client_id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, client.getClientId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    phoneNumber.setPhoneNumber(resultSet.getString("phone_number"));
                    phoneNumber.setProgram(searchProgramByID(resultSet.getInt("program_id")));
                    retrieveCalls(phoneNumber);
                    retrieveSMS(phoneNumber);
                    client.addPhoneNumber(phoneNumber);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error retrieving phone numbers from the database.", e);
        }
    }

    /**
     * Διαγράφει κάθε κλήση και SMS στη βάση για κάποιο αριθμό τηλεφώνου
     *
     * @param phoneNumber ο αριθμός τηλεφώνου
     * @throws Exception εάν υπάρξει σφάλμα κατά τη διαγραφή των κλήσεων και SMS
     */
    private void deleteAllCallsAndSMS(PhoneNumber phoneNumber) throws Exception {
        db.refreshConnection();
        String query = "DELETE FROM calls WHERE phone_number = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, phoneNumber.getPhoneNumber());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error deleting call from the database.", e);
        }
        db.refreshConnection();
        query = "DELETE FROM sms WHERE phone_number = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, phoneNumber.getPhoneNumber());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error deleting sms from the database.", e);
        }
    }

    /**
     * Διαγράφει έναν αριθμό τηλεφώνου από τη βάση
     *
     * @param phoneNumber ο αριθμός τηλεφώνου
     * @throws Exception εάν υπάρξει σφάλμα κατά τη διαγραφή του αριθμού τηλεφώνου
     */
    public void deletePhoneNumber(PhoneNumber phoneNumber) throws Exception {
        db.refreshConnection();
        deleteAllCallsAndSMS(phoneNumber);
        db.refreshConnection();
        String query = "DELETE FROM phonenumbers WHERE phone_number = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, phoneNumber.getPhoneNumber());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new Exception("No phone number found with the given number.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error deleting phone number from the database.", e);
        }
    }

    /**
     * Ανακτά τους λογαριασμούς του πελάτη από τη βάση
     *
     * @param client ο πελάτης
     * @throws Exception εάν υπάρξει σφάλμα κατά την ανάκτηση των λογαριασμών
     */
    public void getClientBills(Client client) throws Exception {
        db.refreshConnection();
        if (!client.existInDB()) {
            throw new Exception("Client does not exist in the database.");
        }

        // Check if the client exists
        String query = "SELECT * FROM clients WHERE client_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, client.getClientId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new Exception("Client does not exist in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error verifying client existence in the database.", e);
        }
        db.refreshConnection();
        query = "SELECT * FROM bills WHERE client_id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, client.getClientId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Bill bill = new Bill();
                    bill.setBillID(resultSet.getInt("bill_id"));
                    bill.setTimeSpentTalking(resultSet.getInt("time_spent_talking"));
                    bill.setTotalSMS(resultSet.getInt("total_sms"));
                    bill.setTotalCost(resultSet.getDouble("total_cost"));
                    bill.setDateIssued(resultSet.getDate("month_issued"));
                    bill.setStatus(resultSet.getString("status"));
                    client.addBill(bill);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error retrieving bills from the database.", e);
        }
    }

    /**
     * Ανανεώνει τον λογαριασμό του πελάτη
     *
     * @param bill ο λογαριασμός
     * @throws Exception εάν υπάρξει σφάλμα κατά την ανανέωση του λογαριασμού
     */
    public void updateBill(Bill bill) throws Exception {
        db.refreshConnection();
        String query = "UPDATE bills SET time_spent_talking = ?, total_sms = ?, total_cost = ?, month_issued = ?, status = ? WHERE bill_id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bill.getTimeSpentTalking());
            statement.setInt(2, bill.getTotalSMS());
            statement.setDouble(3, bill.getTotalCost());
            statement.setDate(4, new java.sql.Date(bill.getDateIssued().getTime()));
            statement.setString(5, bill.getStatus());
            statement.setInt(6, bill.getBillID());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new Exception("No bill found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error updating bill in the database.", e);
        }
    }

    /**
     * Διαγράφει έναν λογαριασμό από τη βάση
     *
     * @param bill ο λογαριασμός
     * @throws Exception εάν υπάρξει σφάλμα κατά τη διαγραφή του λογαριασμού
     */
    public void deleteBill(Bill bill) throws Exception {
        db.refreshConnection();
        String query = "DELETE FROM bills WHERE bill_id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bill.getBillID());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new Exception("No bill found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error deleting bill from the database.", e);
        }
    }

    /**
     * Προσθέτει τη σχέση πελάτη-πωλητή στη βάση
     *
     * @param client ο πελάτης
     * @param seller ο πωλητής
     * @throws Exception εάν υπάρξει σφάλμα κατά την προσθήκη της σχέσης
     */
    public void addClientSellerRelationship(Client client, Seller seller) throws Exception {
        db.refreshConnection();
        if (!client.existInDB()) {
            throw new Exception("Client does not exist in the database.");
        }

        // Check if the client exists
        String query = "SELECT * FROM clients WHERE client_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, client.getClientId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new Exception("Client does not exist in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error verifying client existence in the database.", e);
        }
        if (!seller.existInDB()) {
            throw new Exception("Seller does not exist in the database.");
        }
        db.refreshConnection();
        // Check if seller exists
        query = "SELECT * FROM sellers WHERE seller_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, seller.getSellerID());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new Exception("Seller does not exist in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error verifying seller existence in the database.", e);
        }
        db.refreshConnection();
        // Delete existing relationship if it exists
        query = "DELETE FROM client_seller WHERE client_id = ? ";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, client.getClientId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error deleting existing client-seller relationship from the database.", e);
        }
        db.refreshConnection();
        // Add new client-seller relationship
        query = "INSERT INTO client_seller (client_id, seller_id) VALUES (?, ?)";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, client.getClientId());
            statement.setInt(2, seller.getSellerID());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error adding client-seller relationship to the database.", e);
        }
    }

    /**
     * Ανακτά τις κλήσεις ενός αριθμού τηλεφώνου από τη βάση
     *
     * @param phoneNumber ο αριθμός τηλεφώνου
     * @throws Exception εάν υπάρξει σφάλμα κατά την ανάκτηση των κλήσεων
     */
    public void retrieveCalls(PhoneNumber phoneNumber) throws Exception {
        db.refreshConnection();
        String query = "SELECT * FROM calls WHERE phone_number = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, phoneNumber.getPhoneNumber());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Call call = new Call();
                    call.setPhoneNumber(phoneNumber);
                    call.setDuration(resultSet.getInt("duration"));
                    call.setTimestamp(resultSet.getDate("timestamp"));
                    phoneNumber.addCall(call);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    /**
     * Ανακτά τα SMS ενός αριθμού τηλεφώνου από τη βάση
     *
     * @param phoneNumber ο αριθμός τηλεφώνου
     * @throws Exception εάν υπάρξει σφάλμα κατά την ανάκτηση των SMS
     */
    public void retrieveSMS(PhoneNumber phoneNumber) throws Exception {
        db.refreshConnection();
        String query = "SELECT * FROM sms WHERE phone_number = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, phoneNumber.getPhoneNumber());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    SMS sms = new SMS();
                    sms.setPhoneNumber(phoneNumber);
                    sms.setMessage(resultSet.getString("message"));
                    sms.setTimeStamp(resultSet.getDate("timestamp"));
                    phoneNumber.addSMS(sms);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    /**
     * Αλλάζει το πρόγραμμα ενός αριθμού τηλεφώνου
     *
     * @param phoneNumber ο αριθμός τηλεφώνου
     * @param program     το πρόγραμμα
     * @throws SQLException εάν υπάρξει σφάλμα κατά την αλλαγή του προγράμματος
     */
    public void changeProgramForPhoneNumber(PhoneNumber phoneNumber, Program program) throws SQLException {
        db.refreshConnection();
        phoneNumber.setProgram(program);
        String query = "UPDATE phonenumbers SET program_id = ? WHERE phone_number = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, program.getProgramID());
            statement.setString(2, phoneNumber.getPhoneNumber());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Αναζητά ένα πρόγραμμα με βάση το ID του
     *
     * @param id το ID του προγράμματος
     * @return το πρόγραμμα
     * @throws Exception εάν υπάρξει σφάλμα κατά την αναζήτηση του προγράμματος
     */
    public Program searchProgramByID(int id) throws Exception {
        db.refreshConnection();
        String query = "SELECT * FROM programs WHERE program_id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Program program = new Program();
                    program.setProgramName(resultSet.getString("program_name"));
                    program.setFixedCost(resultSet.getDouble("fixed_cost"));
                    program.setCostPerMinute(resultSet.getDouble("cost_per_minute"));
                    program.setCostPerSMS(resultSet.getDouble("cost_per_sms"));
                    program.setAvailableMinutes(resultSet.getInt("available_minutes"));
                    program.setAvailableSMS(resultSet.getInt("available_sms"));
                    program.setProgramID(resultSet.getInt("program_id"));
                    return program;
                } else {
                    return null; // No program found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error searching program by ID in the database.", e);
        }
    }

    /**
     * Επιστρέφει όλα τα προγράμματα από τη βάση
     *
     * @return τα προγράμματα
     * @throws Exception εάν υπάρξει σφάλμα κατά την ανάκτηση των προγραμμάτων
     */
    public ArrayList<Program> getAllPrograms() throws Exception {
        db.refreshConnection();
        ArrayList<Program> programs = new ArrayList<>();
        String query = "SELECT * FROM programs";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Program program = new Program();
                    program.setProgramName(resultSet.getString("program_name"));
                    program.setFixedCost(resultSet.getDouble("fixed_cost"));
                    program.setCostPerMinute(resultSet.getDouble("cost_per_minute"));
                    program.setCostPerSMS(resultSet.getDouble("cost_per_sms"));
                    program.setAvailableMinutes(resultSet.getInt("available_minutes"));
                    program.setAvailableSMS(resultSet.getInt("available_sms"));
                    program.setProgramID(resultSet.getInt("program_id"));
                    programs.add(program);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return programs;
    }

    /**
     * Επιστρέφει όλους τους πελάτες σχετίζονται με έναν πωλητή
     *
     * @param seller ο πωλητής
     * @return οι πελάτες
     * @throws Exception εάν υπάρξει σφάλμα κατά την ανάκτηση των πελατών
     */
    public ArrayList<Client> getAllClientsForSeller(Seller seller) throws Exception {
        if (!seller.existInDB()) {
            throw new Exception("Seller does not exist in the database.");
        }
        db.refreshConnection();
        ArrayList<Client> clients = new ArrayList<>();
        // check if the seller exists
        String query = "SELECT * FROM sellers WHERE seller_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, seller.getSellerID());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new Exception("Seller does not exist in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error verifying seller existence in the database.", e);
        }
        db.refreshConnection();
        query = "SELECT * FROM clients WHERE client_id IN (SELECT client_id FROM client_seller WHERE seller_id = ?)";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, seller.getSellerID());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Client client = new Client();
                    client.setUsername(resultSet.getString("username"));
                    client.setFirstName(resultSet.getString("first_name"));
                    client.setLastName(resultSet.getString("last_name"));
                    client.setEmail(resultSet.getString("email"));
                    client.setBirthday(resultSet.getDate("birthday"));
                    client.setPassword(resultSet.getString("password_hash"));
                    client.setClientID(resultSet.getInt("client_id"));
                    client.setVAT(resultSet.getString("VAT"));
                    client.setExistInDB(true);
                    // get all phonenumbers associated with the current client
                    getPhoneNumbers(client);
                    getClientBills(client);
                    clients.add(client);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    /**
     * Αναζήτηση λογαριασμού με βάση το ID του
     *
     * @param bill_ID το ID του λογαριασμού
     * @return τον λογαριασμό
     */
    public Bill searchBillByID(int bill_ID) throws Exception {
        db.refreshConnection();
        String query = "SELECT * FROM bills WHERE bill_id = ?";
        Bill bill = new Bill();
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bill_ID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    bill.setBillID(resultSet.getInt("bill_id"));
                    bill.setTimeSpentTalking(resultSet.getInt("time_spent_talking"));
                    bill.setTotalSMS(resultSet.getInt("total_sms"));
                    bill.setTotalCost(resultSet.getDouble("total_cost"));
                    bill.setDateIssued(resultSet.getDate("month_issued"));
                    bill.setStatus(resultSet.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bill;
    }
}

