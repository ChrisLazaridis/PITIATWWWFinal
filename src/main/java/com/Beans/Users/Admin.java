package com.Beans.Users;

import java.util.ArrayList;

/**
 * Η κλάση Admin αντιπροσωπεύει έναν διαχειριστή του συστήματος.
 * Κληρονομεί την κλάση User.
 */
public class Admin extends User implements java.io.Serializable{
    /**
     * Ο κωδικός του διαχειριστή.
     */
    private int adminID;
    /**
     * Οι πωλητές του διαχειριστή.
     */
    private ArrayList<Seller> sellersBelow = new ArrayList<>();

    /**
     * Δημιουργεί ένα νέο αντικείμενο Admin.
     */
    public Admin() {
        super();
    }

    /**
     * Επιστρέφει τον κωδικό του διαχειριστή.
     *
     * @return ο κωδικός του διαχειριστή
     */
    public int getAdminID() {
        return adminID;
    }

    /**
     * Θέτει τον κωδικό του διαχειριστή.
     *
     * @param adminID ο κωδικός του διαχειριστή
     */
    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    /**
     * Επιστρέφει τους πωλητές του διαχειριστή.
     *
     * @return οι πωλητές του διαχειριστή
     */
    public ArrayList<Seller> getSellersBelow() {
        return sellersBelow;
    }

    /**
     * Προσθέτει έναν πωλητή στον διαχειριστή.
     *
     * @param seller ο πωλητής
     */
    public void addSeller(Seller seller) {
        sellersBelow.add(seller);
    }

}
