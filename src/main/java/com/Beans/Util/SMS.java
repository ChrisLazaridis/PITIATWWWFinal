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

    /**
     * Επιστρέφει την χρονική σήμανση.
     *
     * @return η χρονική σήμανση
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * Θέτει την χρονική σήμανση.
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

