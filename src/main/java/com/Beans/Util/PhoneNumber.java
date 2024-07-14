package com.Beans.Util;

import java.util.ArrayList;

/**
 * Η κλάση PhoneNumber αντιπροσωπεύει έναν αριθμό τηλεφώνου.
 */
public class PhoneNumber {
    /**
     * Ο αριθμός τηλεφώνου.
     */
    private String phoneNumber;
    /**
     * Το πρόγραμμα.
     */
    private Program program;
    /**
     * Τα μηνύματα.
     */
    private ArrayList<SMS> sms = new ArrayList<>();
    /**
     * Οι κλήσεις.
     */
    private final ArrayList<Call> calls = new ArrayList<>();

    /**
     * Δημιουργεί ένα νέο αντικείμενο PhoneNumber.
     */
    public PhoneNumber() {
    }

    /**
     * Επιστρέφει τον αριθμό τηλεφώνου.
     *
     * @return ο αριθμός τηλεφώνου
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Θέτει τον αριθμό τηλεφώνου.
     *
     * @param phoneNumber ο αριθμός τηλεφώνου
     */
    public void setPhoneNumber(String phoneNumber) {
        if (checkPhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        }
    }

    /**
     * Επιστρέφει το πρόγραμμα.
     *
     * @return το πρόγραμμα
     */
    public Program getProgram() {
        return program;
    }

    /**
     * Θέτει το πρόγραμμα.
     *
     * @param program το πρόγραμμα
     */
    public void setProgram(Object program) {
        if (program instanceof Program) {
            this.program = (Program) program;
        } else {
            throw new IllegalArgumentException("Not a valid program object");
        }
    }

    /**
     * Επιστρέφει τα μηνύματα.
     *
     * @return τα μηνύματα
     */
    public ArrayList<SMS> getSMS() {
        return sms;
    }

    /**
     * Θέτει τα μηνύματα.
     *
     * @param sms τα μηνύματα
     */
    public void setSms(ArrayList<SMS> sms) {
        this.sms = sms;
    }

    /**
     * Προσθέτει ένα μήνυμα.
     *
     * @param sms το μήνυμα
     */
    public void addSMS(SMS sms) {
        this.sms.add(sms);
    }

    /**
     * Επιστρέφει τις κλήσεις.
     *
     * @return οι κλήσεις
     */
    public ArrayList<Call> getCalls() {
        return calls;
    }

    /**
     * Προσθέτει μία κλήση.
     *
     * @param call η κλήση
     */
    public void addCall(Call call) {
        this.calls.add(call);
    }

    /**
     * Ελέγχει αν ο αριθμός τηλεφώνου είναι έγκυρος.
     *
     * @param phoneNumber ο αριθμός τηλεφώνου
     * @return true αν ο αριθμός τηλεφώνου είναι έγκυρος, αλλιώς false
     * @throws IllegalArgumentException αν ο αριθμός τηλεφώνου δεν είναι έγκυρος
     */


    public static boolean checkPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        if (phoneNumber.matches("\\d{10}")) {
            return true;
        } else {
            throw new IllegalArgumentException("Phone number must be 10 digits long");// throws an exception in any other case
        }
    }
}
