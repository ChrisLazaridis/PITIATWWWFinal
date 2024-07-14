package com.db;
import com.Beans.Users.Client;
import com.Beans.Users.Seller;
import com.Beans.Users.Admin;
import java.sql.*;
import java.util.ArrayList;

/**
 * Η κλάση SellerDB χρησιμοποιείτε για την επικοινωνία με τη βάση δεδομένων για λειτουργίες
 * που έχουν να κάνουν με τους πωλητές.
 */
public class SellerDB {
    /**
     * Η σύνδεση με τη βάση δεδομένων.
     */
    private final Database db;

    /**
     * Αρχικοποιεί το αντικείμενο SellerDB.
     */
    public SellerDB(){
        db = new Database("root", "jdbc:mysql://localhost/uservault", "12345678PITIATwww$");
    }

    /**
     * Προσθέτει έναν νέο πωλητή στη βάση δεδομένων.
     *
     * @param seller ο πωλητής που θα προστεθεί
     * @throws Exception εάν υπάρξει σφάλμα κατά την προσθήκη του πωλητή
     */
    public void addSeller(Seller seller) throws Exception {
        db.refreshConnection();
        if (seller.existInDB()) {
            throw new Exception("Seller already exists in the database.");
        }

        String checkQuery = "SELECT seller_id FROM sellers WHERE username = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {

            checkStatement.setString(1, seller.getUsername());

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
        String query = "INSERT INTO sellers (username, first_name, last_name, email, birthday,  password_hash) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setSellerStatements(seller, statement);

            statement.executeUpdate();
            seller.setExistInDB(true);
            seller.setSellerID(this.searchSeller(seller.getUsername()).getSellerID());

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error adding seller to the database.", e);
        }
    }

    /**
     * Θέτει τις τιμές του seller στο statement.
     * @param seller ο πωλητής
     * @param statement το statement
     * @throws SQLException εάν υπάρξει σφάλμα κατά την εισαγωγή των τιμών
     */
    private void setSellerStatements(Seller seller, PreparedStatement statement) throws SQLException {
        statement.setString(1, seller.getUsername());
        statement.setString(2, seller.getFirstName());
        statement.setString(3, seller.getLastName());
        statement.setString(4, seller.getEmail());
        statement.setDate(5, new java.sql.Date(seller.getBirthday().getTime()));
        statement.setString(6, db.hashPassword(seller.getPassword()));
    }

    /**
     * Αναζητά έναν πωλητή με βάση το username.
     *
     * @param username το username του πωλητή
     * @return ο πωλητής που αναζητήθηκε
     * @throws Exception εάν υπάρξει σφάλμα κατά την αναζήτηση του πωλητή
     */
    public Seller searchSeller(String username) throws Exception {
        db.refreshConnection();
        String query = "SELECT * FROM sellers WHERE username = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Seller seller = new Seller();
                    seller.setUsername(resultSet.getString("username"));
                    seller.setFirstName(resultSet.getString("first_name"));
                    seller.setLastName(resultSet.getString("last_name"));
                    seller.setEmail(resultSet.getString("email"));
                    seller.setBirthday(resultSet.getDate("birthday"));
                    seller.setPassword(resultSet.getString("password_hash"));
                    int sellerId = resultSet.getInt("seller_id");
                    seller.setSellerID(sellerId);
                    seller.setExistInDB(true);
                    ClientDB clientDB = new ClientDB();
                    ArrayList<Client> clients = clientDB.getAllClientsForSeller(seller);
                    for (Client client : clients) {
                        seller.addClient(client);
                    }
                    return seller;
                } else {
                    return null; // No seller found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error searching seller in the database.", e);
        }
    }

    /**
     * Ενημερώνει τα στοιχεία ενός πωλητή.
     *
     * @param seller ο πωλητής που θα ενημερωθεί
     * @throws Exception εάν υπάρξει σφάλμα κατά την ενημέρωση του πωλητή
     */
    public void updateSeller(Seller seller) throws Exception {
        db.refreshConnection();
        if (seller.existInDB()) {
            // Check if the seller exists
            String query = "SELECT * FROM sellers WHERE seller_id = ?";
            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, seller.getSellerID());
                System.out.println(seller.getSellerID());

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
            query = "UPDATE sellers SET username = ?, first_name = ?, last_name = ?, email = ?, birthday = ?,  password_hash = ? WHERE seller_id = ?";

            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                setSellerStatements(seller, statement);
                statement.setInt(7, seller.getSellerID());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new Exception("No seller found with the given ID and username.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Error updating seller in the database.", e);
            }
        } else {
            throw new Exception("Seller does not exist in the database.");
        }
    }

    /**
     * Διαγράφει έναν πωλητή από τη βάση δεδομένων.
     *
     * @param seller ο πωλητής που θα διαγραφεί
     * @throws Exception εάν υπάρξει σφάλμα κατά τη διαγραφή του πωλητή
     */
    public void deleteSeller(Seller seller) throws Exception {
        db.refreshConnection();
        if (seller.existInDB()) {
            // delete any relationships with clients and admin
            String query = "DELETE FROM client_seller WHERE seller_id = ?";
            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, seller.getSellerID());

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Error deleting client-seller relationships from the database.", e);
            }
            db.refreshConnection();
            query = "DELETE FROM admin_seller WHERE seller_id = ?";
            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, seller.getSellerID());

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Error deleting admin-seller relationships from the database.", e);
            }
            db.refreshConnection();
            query = "DELETE FROM sellers WHERE seller_id = ?";

            try (Connection connection = db.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, seller.getSellerID());

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted == 0) {
                    throw new Exception("No seller found with the given ID.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Error deleting seller from the database.", e);
            }
        } else {
            throw new Exception("Seller does not exist in the database.");
        }
    }

    /**
     * Προσθέτει μια σχέση μεταξύ διαχειριστή και πελάτη.
     *
     * @param seller ο πωλητής
     * @param admin  ο διαχειριστής
     * @throws Exception εάν υπάρξει σφάλμα κατά την προσθήκη της σχέσης
     */
    public void addSellerAdminRelationship(Seller seller, Admin admin) throws Exception {
        db.refreshConnection();
        if (!admin.existInDB()) {
            throw new Exception("Seller does not exist in the database.");
        }

        // Check if the admin exists
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
            throw new Exception("Error verifying seller existence in the database.", e);
        }
        if(!seller.existInDB()){
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
        query = "DELETE FROM admin_seller WHERE seller_id = ? ";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, seller.getSellerID());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error deleting existing admin-seller relationship from the database.", e);
        }
        db.refreshConnection();
        // Add new admin-seller relationship
        query = "INSERT INTO admin_seller (admin_id, seller_id) VALUES (?, ?)";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, admin.getAdminID());
            statement.setInt(2, seller.getSellerID());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error adding admin-seller relationship to the database.", e);
        }
    }

    /**
     * Αναζητά έναν πωλητή με βάση το ID.
     *
     * @param sellerID το ID του πωλητή
     * @return ο πωλητής που αναζητήθηκε
     * @throws Exception εάν υπάρξει σφάλμα κατά την αναζήτηση του πωλητή
     */
    public Seller searchSeller(int sellerID) throws Exception {
        db.refreshConnection();
        String query = "SELECT * FROM sellers WHERE seller_id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, sellerID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Seller seller = new Seller();
                    seller.setUsername(resultSet.getString("username"));
                    seller.setFirstName(resultSet.getString("first_name"));
                    seller.setLastName(resultSet.getString("last_name"));
                    seller.setEmail(resultSet.getString("email"));
                    seller.setBirthday(resultSet.getDate("birthday"));
                    seller.setPassword(resultSet.getString("password_hash"));
                    seller.setSellerID(resultSet.getInt("seller_id"));
                    seller.setExistInDB(true);
                    ClientDB clientDB = new ClientDB();
                    ArrayList<Client> clients = clientDB.getAllClientsForSeller(seller);
                    for (Client client : clients) {
                        seller.addClient(client);
                    }
                    return seller;
                } else {
                    return null; // No seller found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error searching seller in the database.", e);
        }
    }

    /**
     * Επιστρέφει όλους τους πωλητές που ανήκουν σε έναν διαχειριστή.
     *
     * @param admin ο διαχειριστής
     * @return ο πωλητής που αναζητήθηκε
     * @throws Exception εάν υπάρξει σφάλμα κατά την αναζήτηση του πωλητή
     */
    public ArrayList<Seller> getAllSellersForAdmin(Admin admin) throws Exception{
        db.refreshConnection();
        if(!admin.existInDB()){
            throw new Exception("Admin does not exist in the database.");
        }
        ArrayList<Seller> sellers = new ArrayList<>();
        // check if admin exists
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
        query = "SELECT * FROM admin_seller WHERE admin_id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, admin.getAdminID());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Seller seller = this.searchSeller(resultSet.getInt("seller_id"));
                    sellers.add(seller);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sellers;
    }

}
