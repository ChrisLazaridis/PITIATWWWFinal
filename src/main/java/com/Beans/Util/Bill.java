package com.Beans.Util;

import java.util.Date;

/**
 * Η κλάση Bill αντιπροσωπεύει τον λογαριασμό ενός πελάτη.
 */
public class Bill implements java.io.Serializable {
    /**
     * Ο κωδικός του λογαριασμού.
     */
    private int billID;
    /**
     * Ο χρόνος ομιλίας.
     */
    private int timeSpentTalking;
    /**
     * Το συνολικό κόστος.
     */
    private double totalCost;
    /**
     * Η ημερομηνία έκδοσης.
     */
    private Date dateIssued;
    /**
     * Το status.
     */
    private String status;
    /**
     * Ο συνολικός αριθμός SMS.
     */
    private int totalSMS;

    /**
     * Δημιουργεί ένα νέο αντικείμενο Bill.
     */
    public Bill() {
    }

    /**
     * Επιστρέφει τον κωδικό του λογαριασμού.
     *
     * @return ο κωδικός του λογαριασμού
     */
    public int getBillID() {
        return billID;
    }

    /**
     * Θέτει τον κωδικό του λογαριασμού.
     *
     * @param billID ο κωδικός του λογαριασμού
     */
    public void setBillID(int billID) {
        this.billID = billID;
    }

    /**
     * Επιστρέφει τον χρόνο ομιλίας.
     *
     * @return ο χρόνος ομιλίας
     */
    public int getTimeSpentTalking() {
        return timeSpentTalking;
    }

    /**
     * Θέτει τον χρόνο ομιλίας.
     *
     * @param timeSpentTalking ο χρόνος ομιλίας
     */
    public void setTimeSpentTalking(int timeSpentTalking) {
        this.timeSpentTalking = timeSpentTalking;
    }

    /**
     * Επιστρέφει το συνολικό κόστος.
     *
     * @return το συνολικό κόστος
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Θέτει το συνολικό κόστος.
     *
     * @param totalCost το συνολικό κόστος
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Επιστρέφει την ημερομηνία έκδοσης.
     *
     * @return η ημερομηνία έκδοσης
     */
    public Date getDateIssued() {
        return dateIssued;
    }

    /**
     * Θέτει την ημερομηνία έκδοσης.
     *
     * @param dateIssued η ημερομηνία έκδοσης
     */
    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    /**
     * Επιστρέφει το status.
     *
     * @return το status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Θέτει το status.
     *
     * @param status το status
     */
    public void setStatus(String status) {
        if (checkStatus(status)) {
            this.status = status;
        }
    }

    /**
     * Επιστρέφει το συνολικό αριθμό SMS.
     *
     * @return ο συνολικός αριθμός SMS
     */
    public int getTotalSMS() {
        return totalSMS;
    }

    /**
     * Θέτει το συνολικό αριθμό SMS.
     *
     * @param totalSMS ο συνολικός αριθμός SMS
     */
    public void setTotalSMS(int totalSMS) {
        this.totalSMS = totalSMS;
    }

    /**
     * Ελέγχει αν το status είναι έγκυρο.
     *
     * @param str το status
     * @return true αν το status είναι έγκυρο, αλλιώς false
     */
    public static boolean checkStatus(String str) {
        if (!str.equals("Paid") && !str.equals("Issued") && !str.equals("Expired")) {
            throw new IllegalArgumentException("Status must be either Paid or Pending");
        }
        return true;
    }

}
