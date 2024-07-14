package com.Beans.Users;

import com.Beans.Util.PhoneNumber;
import com.Beans.Util.Bill;
import java.util.ArrayList;

/**
 * Η κλάση Client αντιπροσωπεύει έναν πελάτη του συστήματος.
 * Κληρονομεί την κλάση User.
 */
public class Client extends User implements java.io.Serializable{
    /**
     * Ο κωδικός του πελάτη.
     */
    private int clientId;
    /**
     * Οι αριθμοί τηλεφώνου του πελάτη.
     */
    private final ArrayList<PhoneNumber> phoneNumbers = new ArrayList<>();
    /**
     * Οι λογαριασμοί του πελάτη.
     */
    private String VAT;
    /**
     * Οι λογαριασμοί του πελάτη.
     */
    private ArrayList<Bill> bills = new ArrayList<>();

    /**
     * Δημιουργεί ένα νέο αντικείμενο Client.
     */
    public Client() {
        super();
    }

    /**
     * Επιστρέφει τον κωδικό του πελάτη.
     *
     * @return ο κωδικός του πελάτη
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets client id.
     *
     * @param clientId the client id
     */
    public void setClientID(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Επιστρέφει τους αριθμούς τηλεφώνου του πελάτη.
     *
     * @return οι αριθμοί τηλεφώνου του πελάτη
     */
    public ArrayList<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    /**
     * Προσθέτει έναν αριθμό τηλεφώνου στον πελάτη.
     *
     * @param phoneNumber ο αριθμός τηλεφώνου
     */
    public void addPhoneNumber(PhoneNumber phoneNumber) {
        phoneNumbers.add(phoneNumber);
    }

    /**
     * Επιστρέφει τους λογαριασμούς του πελάτη.
     *
     * @return οι λογαριασμοί του πελάτη
     */
    public ArrayList<Bill> getBills() {
        return bills;
    }

    /**
     * Θέτει τους λογαριασμούς του πελάτη.
     *
     * @param bills οι λογαριασμοί του πελάτη
     */
    public void setBills(ArrayList<Bill> bills) {
        this.bills = bills;
    }

    /**
     * Προσθέτει ένα λογαριασμό στον πελάτη.
     *
     * @param bill ο λογαριασμός
     */
    public void addBill(Bill bill) {
        bills.add(bill);
    }

    /**
     * Επιστρέφει το ΑΦΜ του πελάτη.
     *
     * @return το ΑΦΜ του πελάτη
     */
    public String getVAT() {
        return VAT;
    }

    /**
     * Θέτει το ΑΦΜ του πελάτη.
     *
     * @param VAT το ΑΦΜ του πελάτη
     */
    public void setVAT(String VAT) {
        this.VAT = VAT;
    }

    /**
     * Αφαιρεί έναν αριθμό τηλεφώνου από τον πελάτη.
     *
     * @param phoneNumber ο αριθμός τηλεφώνου
     */
    public void removePhoneNumber(String phoneNumber) {
        for (PhoneNumber p : phoneNumbers) {
            if (p.getPhoneNumber().equals(phoneNumber)) {
                phoneNumbers.remove(p);
                break;
            }
        }
    }


}
