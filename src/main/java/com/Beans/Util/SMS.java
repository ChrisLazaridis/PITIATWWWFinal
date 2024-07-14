package com.Beans.Util;

import java.util.Date;

/**
 * Η κλάση SMS αντιπροσωπεύει ένα μήνυμα SMS.
 */
public class SMS {
    /**
     * Ο αριθμός τηλεφώνου.
     */
    private  PhoneNumber phoneNumber;
    /**
     * Ο αριθμός προς τον οποίο αποστέλλεται το μήνυμα.
     */
    private final String receiverPhoneNumber;
    /**
     * Η χρονική σήμανση.
     */
    private Date timeStamp;
    /**
     * Το μήνυμα.
     */
    private String message ;

    /**
     * Δημιουργεί ένα νέο αντικείμενο SMS.
     */
    public SMS() {
        this.receiverPhoneNumber = "693" + String.format("%07d", (int) (Math.random() * 10000000));
    }

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
    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    /**
     * Επιστρέφει τη χρονική σήμανση.
     *
     * @return η χρονική σήμανση
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * Θέτει τη χρονική σήμανση.
     *
     * @param timeStamp η χρονική σήμανση
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Επιστρέφει το μήνυμα.
     *
     * @return το μήνυμα
     */
    public String getMessage() {
        return message;
    }

    /**
     * Θέτει το μήνυμα.
     *
     * @param message το μήνυμα
     */
    public void setMessage(String message) {
        this.message = message;
    }






}

