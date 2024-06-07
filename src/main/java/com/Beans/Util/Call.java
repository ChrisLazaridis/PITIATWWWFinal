package com.Beans.Util;
import java.util.Date;

/**
 * Η κλάση Call αντιπροσωπεύει μία κλήση.
 */
public class Call implements java.io.Serializable{
    /**
     * Ο αριθμός τηλεφώνου.
     */
    private PhoneNumber phoneNumber;
    /**
     * Η χρονική σήμανση.
     */
    private Date timestamp;
    /**
     * Η διάρκεια.
     */
    private int duration;

    /**
     * Δημιουργεί ένα νέο αντικείμενο Call.
     */
    public Call() {}

    /**
     * Επιστρέφει τον αριθμό τηλεφώνου.
     *
     * @return ο αριθμός τηλεφώνου
     */
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Θέτει τον αριθμό τηλεφώνου.
     *
     * @param phoneNumber ο αριθμός τηλεφώνου
     */
    public void setPhoneNumber(Object phoneNumber) {
        if(phoneNumber instanceof PhoneNumber){
            this.phoneNumber = (PhoneNumber) phoneNumber;
        }
        else{
            throw new IllegalArgumentException("Not a valid phone number object");
        }
    }

    /**
     * Επιστρέφει τη χρονική σήμανση.
     *
     * @return η χρονική σήμανση
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Θέτει τη χρονική σήμανση.
     *
     * @param timestamp η χρονική σήμανση
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Επιστρέφει τη διάρκεια.
     *
     * @return η διάρκεια
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Θέτει τη διάρκεια.
     *
     * @param duration η διάρκεια
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

}
