package com.Beans.Users;

import java.util.Date;

/**
 * Η κλάση User αντιπροσωπεύει έναν χρήστη του συστήματος.
 * Εφαρμόζει τη διεπαφή Serializable.
 */
public class User implements java.io.Serializable {
    /**
     * Το username του χρήστη.
     */
    private String username;
    /**
     * Ο κωδικός πρόσβασης του χρήστη.
     */
    private String password;
    /**
     * Το μικρό όνομα του χρήστη.
     */
    private String firstName;
    /**
     * Το επώνυμο του χρήστη.
     */
    private String lastName;
    /**
     * Η ημερομηνία γέννησης του χρήστη.
     */
    private Date birthday;
    /**
     * Η διεύθυνση email του χρήστη.
     */
    private String email;
    /**
     * Αν ο χρήστης υπάρχει στη βάση δεδομένων.
     */
    private boolean existInDB = false;
    /**
     * Ο αριθμός των χρηστών.
     */
    private static int userCounter = 0;

    /**
     * Δημιουργεί ένα νέο αντικείμενο User.
     */
    public User() {
        userCounter++;
    }

    // getters and setters

    /**
     * Επιστρέφει το όνομα χρήστη.
     *
     * @return το όνομα χρήστη
     */
    public String getUsername() {
        return username;
    }

    /**
     * Θέτει το όνομα χρήστη.
     *
     * @param username το όνομα χρήστη
     */
    public void setUsername(String username) {
        if (isUsernameValid(username)) {
            this.username = username;
        }
    }

    /**
     * Επιστρέφει τον κωδικό πρόσβασης.
     *
     * @return ο κωδικός πρόσβασης
     */
    public String getPassword() {
        return password;
    }

    /**
     * Θέτει τον κωδικό πρόσβασης.
     *
     * @param password ο κωδικός πρόσβασης
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Επιστρέφει το μικρό όνομα.
     *
     * @return το μικρό όνομα
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Θέτει το μικρό όνομα.
     *
     * @param firstName το μικρό όνομα
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Επιστρέφει το επώνυμο.
     *
     * @return το επώνυμο
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Θέτει το επώνυμο.
     *
     * @param lastName το επώνυμο
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Επιστρέφει την ημερομηνία γέννησης.
     *
     * @return η ημερομηνία γέννησης
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * Θέτει την ημερομηνία γέννησης.
     *
     * @param birthday η ημερομηνία γέννησης
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * Επιστρέφει τη διεύθυνση email.
     *
     * @return η διεύθυνση email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Θέτει τη διεύθυνση email.
     *
     * @param email η διεύθυνση email
     */
    public void setEmail(String email) {
        if (isEmailValid(email)) {
            this.email = email;
        }
    }

    /**
     * Επιστρέφει τον αριθμό των χρηστών.
     *
     * @return ο αριθμός των χρηστών
     */
    public static int getUserCounter() {
        return userCounter;
    }

    /**
     * Θέτει τον αριθμό των χρηστών.
     *
     * @param userCounter ο αριθμός των χρηστών
     */
    public static void setUserCounter(int userCounter) {
        User.userCounter = userCounter;
    }

    /**
     * Επιστρέφει αν ο χρήστης υπάρχει στη βάση δεδομένων.
     *
     * @return true αν ο χρήστης υπάρχει στη βάση δεδομένων, αλλιώς false
     */
    public boolean existInDB() {
        return existInDB;
    }

    /**
     * Θέτει αν ο χρήστης υπάρχει στη βάση δεδομένων.
     *
     * @param existInDB true αν ο χρήστης υπάρχει στη βάση δεδομένων, αλλιώς false
     */
    public void setExistInDB(boolean existInDB) {
        this.existInDB = existInDB;
    }

    /**
     * Ελέγχει το όνομα χρήστη.
     *
     * @param username το όνομα χρήστη
     * @return true αν το όνομα χρήστη είναι επιτρεπτό, αλλιώς πετάει IllegalArgumentException
     */
    public static boolean isUsernameValid(String username) {
        if (username.length() < 6 || username.length() > 24) {
            throw new IllegalArgumentException("Username must be between 6 and 24 characters long");
        }
        return true;
    }

    /**
     * Ελέγχει τη διεύθυνση email.
     *
     * @param email η διεύθυνση email
     * @return true αν η διεύθυνση email είναι επιτρεπτή
     * @throws IllegalArgumentException Εάν η διεύθυνση email δεν είναι έγκυρη
     */
    public static boolean isEmailValid(String email) {
        if (!email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-zA-Z]+")) {
            throw new IllegalArgumentException("Email address is not valid");
        }
        return true;
    }
}
