package com.db;

import com.Beans.Users.Admin;
import com.Beans.Users.Seller;
import com.Beans.Util.PhoneNumber;
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
     * Προσθέτει έναν διαχειριστή στη βάση δεδομένων.
     * @param admin ο διαχειριστής
     * @throws Exception εάν υπάρξει σφάλμα κατά την προσθήκη του διαχειριστή
     */
    public void addAdmin(Admin admin) throws Exception {
        db.refreshConnection();
        if (admin.existInDB()) {
            throw new Exception("Admin already exists in the database.");
        }
        String query = "INSERT INTO admins (username, first_name, last_name, email, birthday,  password_hash) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setAdminStatements(admin, statement);
            statement.executeUpdate();
            admin.setExistInDB(true);
            admin.setAdminID(this.searchAdmin(admin.getUsername()).getAdminID());

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error adding admin to the database.", e);
        }
    }

    /**
     * Θέτει τις τιμές του διαχειριστή στο statement.
     * @param admin ο διαχειριστής
     * @param statement το statement
     * @throws SQLException εάν υπάρξει σφάλμα κατά την θέση των τιμών
     */
    private void setAdminStatements(Admin admin, PreparedStatement statement) throws SQLException {
        statement.setString(1, admin.getUsername());
        statement.setString(2, admin.getFirstName());
        statement.setString(3, admin.getLastName());
        statement.setString(4, admin.getEmail());
        statement.setString(5, db.hashPassword(admin.getPassword()));
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
     * Ανανεώνει τα στοιχεία ενός διαχειριστή στη βάση δεδομένων.
     * @param admin ο διαχειριστής
     * @throws Exception εάν υπάρξει σφάλμα κατά την ανανέωση των στοιχείων του διαχειριστή
     */
    public void updateAdmin(Admin admin) throws Exception {
        db.refreshConnection();
        if (admin.existInDB()) {
            String query = "SELECT * FROM admins WHERE admin_id = ?";
            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, admin.getAdminID());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new Exception("Admin does not exist in the database.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Error verifying admin existence in the database.", e);
            }
            db.refreshConnection();
            query = "UPDATE admins SET username = ?, first_name = ?, last_name = ?, email = ?, password_hash = ? WHERE admin_id = ?";

            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                setAdminStatements(admin, statement);
                statement.setInt(6, admin.getAdminID());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new Exception("No admin found with the given ID.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Error updating admin in the database.", e);
            }
        } else {
            throw new Exception("Admin does not exist in the database.");
        }
    }

    /**
     * Διαγράφει έναν διαχειριστή από τη βάση δεδομένων.
     * @param admin ο διαχειριστής
     * @throws Exception εάν υπάρξει σφάλμα κατά την διαγραφή του διαχειριστή
     */
    public void deleteAdmin(Admin admin) throws Exception {
        db.refreshConnection();
        if (admin.existInDB()) {
            // delete the admin seller relationship
            String query = "DELETE FROM admin_seller WHERE admin_id = ?";
            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, admin.getAdminID());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Error deleting admin seller relationship from the database.", e);
            }
            db.refreshConnection();
            query = "DELETE FROM admins WHERE admin_id = ?";

            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, admin.getAdminID());

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted == 0) {
                    throw new Exception("No admin found with the given ID.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Error deleting admin from the database.", e);
            }
        } else {
            throw new Exception("Admin does not exist in the database.");
        }
    }
    /**
     * Αναζητά έναν διαχειριστή στη βάση δεδομένων.
     * @param adminID ο κωδικός του διαχειριστή
     * @return ο διαχειριστής
     * @throws Exception εάν υπάρξει σφάλμα κατά την αναζήτηση του διαχειριστή στη βάση δεδομένων
     */
    public Admin searchAdmin(int adminID) throws Exception {
        db.refreshConnection();
        String query = "SELECT * FROM admins WHERE admin_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, adminID);
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Επιστρέφει όλους τους διαχειριστές από τη βάση δεδομένων.
     * @return οι διαχειριστές
     * @throws Exception εάν υπάρξει σφάλμα κατά την ανάκτηση των διαχειριστών από τη βάση δεδομένων
     */
    public ArrayList<Admin> getAllAdmins() throws Exception{
        db.refreshConnection();
        String query = "SELECT * FROM admins";
        ArrayList<Admin> admins = new ArrayList<>();
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Admin admin = new Admin();
                    admin.setUsername(resultSet.getString("username"));
                    admin.setFirstName(resultSet.getString("first_name"));
                    admin.setLastName(resultSet.getString("last_name"));
                    admin.setEmail(resultSet.getString("email"));
                    admin.setPassword(resultSet.getString("password_hash"));
                    admin.setAdminID(resultSet.getInt("admin_id"));
                    admin.setExistInDB(true);
                    admins.add(admin);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
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

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    /**
     * Διαγράφει ένα πρόγραμμα από τη βάση δεδομένων.
     * @param program το πρόγραμμα
     * @return οι αριθμοί τηλεφώνου που ανήκαν στο πρόγραμμα (θα πάρουν το default πρόγραμμα)
     * @throws Exception εάν υπάρξει σφάλμα κατά την διαγραφή του προγράμματος
     */
    public ArrayList<PhoneNumber> deleteProgram(Program program) throws Exception {
        db.refreshConnection();
        // set the program_id of all phonenumbers with the program to 11
        String query = "UPDATE phonenumbers SET program_id = 11 WHERE program_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, program.getProgramID());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        ArrayList<PhoneNumber> phoneNumbers = getPhoneNumbersForProgram(program);
        db.refreshConnection();
        query = "DELETE FROM programs WHERE program_id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, program.getProgramID());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted != 0) {
                throw new Exception("Error deleting program from the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return phoneNumbers;
    }

    /**
     * Αναζητά ένα πρόγραμμα στη βάση δεδομένων.
     * @param program το πρόγραμμα
     * @return το πρόγραμμα
     * @throws Exception εάν υπάρξει σφάλμα κατά την αναζήτηση του προγράμματος στη βάση δεδομένων
     */
    public ArrayList<PhoneNumber> getPhoneNumbersForProgram(Program program) throws Exception {
        db.refreshConnection();
        ArrayList<PhoneNumber> phoneNumbers = new ArrayList<>();
        String query = "SELECT * FROM phonenumbers WHERE program_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, program.getProgramID());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    phoneNumber.setPhoneNumber(resultSet.getString("phone_number"));
                    phoneNumber.setProgram(program);
                    phoneNumbers.add(phoneNumber);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return phoneNumbers;
    }

    /**
     * Ανανεώνει το πρόγραμμα ενός αριθμού τηλεφώνου.
     * @param phoneNumber ο αριθμός τηλεφώνου
     * @param program το πρόγραμμα
     * @throws SQLException εάν υπάρξει σφάλμα κατά την ανανέωση του προγράμματος του αριθμού τηλεφώνου
     */
    public void updateProgramForPhoneNumber(PhoneNumber phoneNumber,Program program) throws SQLException{
        db.refreshConnection();
        phoneNumber.setProgram(program);
        String query = "UPDATE phonenumbers SET program_id = ? WHERE phone_number = ?";
        try(Connection connection = db.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1,program.getProgramID());
            statement.setString(2,phoneNumber.getPhoneNumber());
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }

    }
    /**
     * Διαγράφει ένα πρόγραμμα με βάση το ID του στη βάση
     * @param programID το ID του προγράμματος
     * @throws Exception εάν υπάρξει σφάλμα κατά την διαγραφή του προγράμματος
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