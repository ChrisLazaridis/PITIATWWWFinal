package com.db;

import com.Beans.Users.Admin;
import com.Beans.Users.Seller;
import com.Beans.Util.Program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Η κλάση AdminDB περιέχει μεθόδους για την επικοινωνία με τη βάση δεδομένων για τους διαχειριστές.
 */
public class AdminDB {
    /**
     * Η σύνδεση με τη βάση δεδομένων.
     */
    private final Database db;

    /**
     * Δημιουργεί ένα νέο αντικείμενο AdminDB.
     */
    public AdminDB(){
        db = new Database("root", "jdbc:mysql://localhost/uservault", "12345678PITIATwww$");
    }

    /**
     * Αναζητά έναν διαχειριστή στη βάση δεδομένων.
     * @param username το όνομα χρήστη του διαχειριστή
     * @return ο διαχειριστής
     * @throws Exception εάν υπάρξει σφάλμα κατά την αναζήτηση του διαχειριστή στη βάση δεδομένων
     */
    public Admin searchAdmin(String username) throws Exception {
        db.refreshConnection();
        String query = "SELECT * FROM admins WHERE username = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Admin admin = new Admin();
                    admin.setUsername(resultSet.getString("username"));
                    admin.setFirstName(resultSet.getString("first_name"));
                    admin.setLastName(resultSet.getString("last_name"));
                    admin.setEmail(resultSet.getString("email"));
                    admin.setPassword(resultSet.getString("password_hash"));
                    admin.setAdminID(resultSet.getInt("admin_id"));
                    admin.setExistInDB(true);
                    SellerDB sellerDB = new SellerDB();
                    ArrayList<Seller> sellers = sellerDB.getAllSellersForAdmin(admin);
                    for (Seller seller : sellers) {
                        admin.addSeller(seller);
                    }
                    return admin;
                } else {
                    return null; // No admin found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error searching admin in the database.", e);
        }
    }

    /**
     * Προσθέτει ένα πρόγραμμα στη βάση δεδομένων.
     * @param program το πρόγραμμα
     * @throws Exception εάν υπάρξει σφάλμα κατά την προσθήκη του προγράμματος
     */
    public void addProgram(Program program) throws Exception {
        db.refreshConnection();
        // Check if program already exists in the database
        String checkQuery = "SELECT program_id FROM programs WHERE program_name = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {

            checkStatement.setString(1, program.getProgramName());

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        db.refreshConnection();
        String query = "INSERT INTO programs (program_name, fixed_cost, cost_per_minute, cost_per_sms, available_minutes, available_sms) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, program.getProgramName());
            statement.setDouble(2, program.getFixedCost());
            statement.setDouble(3, program.getCostPerMinute());
            statement.setDouble(4, program.getCostPerSMS());
            statement.setInt(5, program.getAvailableMinutes());
            statement.setInt(6, program.getAvailableSMS());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
        // Get the program ID
        db.refreshConnection();
        String getIDQuery = "SELECT program_id FROM programs WHERE program_name = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(getIDQuery)) {

            statement.setString(1, program.getProgramName());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    program.setProgramID(resultSet.getInt("program_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ανανεώνει τα στοιχεία ενός προγράμματος στη βάση δεδομένων.
     * @param program το πρόγραμμα
     * @throws Exception εάν υπάρξει σφάλμα κατά την ανανέωση των στοιχείων του προγράμματος
     */
    public void updateProgram(Program program) throws Exception {
        db.refreshConnection();
        String query = "UPDATE programs SET program_name = ?, fixed_cost = ?, cost_per_minute = ?, cost_per_sms = ?, available_minutes = ?, available_sms = ? WHERE program_id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, program.getProgramName());
            statement.setDouble(2, program.getFixedCost());
            statement.setDouble(3, program.getCostPerMinute());
            statement.setDouble(4, program.getCostPerSMS());
            statement.setInt(5, program.getAvailableMinutes());
            statement.setInt(6, program.getAvailableSMS());
            statement.setInt(7, program.getProgramID());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    /**
     * Διαγράφει ένα πρόγραμμα με βάση το ID του στη βάση
     * @param programID το ID του προγράμματος
     * @throws Exception εάν υπάρξει σφάλμα κατά τη διαγραφή του προγράμματος
     */
    public void deleteProgramByID(int programID) throws Exception {
        db.refreshConnection();
        // set the program_id of all phonenumbers with the program to 11
        String query = "UPDATE phonenumbers SET program_id = 11 WHERE program_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, programID);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        db.refreshConnection();
        query = "DELETE FROM programs WHERE program_id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, programID);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

}