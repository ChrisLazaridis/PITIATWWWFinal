package com.Beans.Util;

/**
 * Η κλάση Program αντιπροσωπεύει ένα πρόγραμμα τηλεφωνίας.
 */
public class Program {
    /**
     * Ο κωδικός του προγράμματος.
     */
    private int programID;
    /**
     * Το όνομα του προγράμματος.
     */
    private String programName;
    /**
     * Το κόστος του προγράμματος.
     */
    private Double fixedCost;
    /**
     * Το κόστος ανά λεπτό.
     */
    private Double costPerMinute;
    /**
     * Το κόστος ανά SMS.
     */
    private Double costPerSMS;
    /**
     * Τα διαθέσιμα λεπτά.
     */
    private int availableMinutes;
    /**
     * Τα διαθέσιμα SMS.
     */
    private int availableSMS;

    /**
     * Δημιουργεί ένα νέο αντικείμενο Program.
     */
    public Program() {
    }

    /**
     * Επιστρέφει τον κωδικό του προγράμματος.
     *
     * @return ο κωδικός του προγράμματος
     */
    public int getProgramID() {
        return programID;
    }

    /**
     * Θέτει τον κωδικό του προγράμματος.
     *
     * @param programID ο κωδικός του προγράμματος
     */
    public void setProgramID(int programID) {
        this.programID = programID;
    }

    /**
     * Επιστρέφει το όνομα του προγράμματος.
     *
     * @return το όνομα του προγράμματος
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Θέτει το όνομα του προγράμματος.
     *
     * @param programName το όνομα του προγράμματος
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * Επιστρέφει το κόστος του προγράμματος.
     *
     * @return το κόστος του προγράμματος
     */
    public Double getFixedCost() {
        return fixedCost;
    }

    /**
     * Θέτει το κόστος του προγράμματος.
     *
     * @param fixedCost το κόστος του προγράμματος
     */
    public void setFixedCost(Double fixedCost) {
        this.fixedCost = fixedCost;

    }

    /**
     * Επιστρέφει το κόστος ανά λεπτό.
     *
     * @return το κόστος ανά λεπτό
     */
    public Double getCostPerMinute() {
        return costPerMinute;
    }

    /**
     * Θέτει το κόστος ανά λεπτό.
     *
     * @param costPerMinute το κόστος ανά λεπτό
     */
    public void setCostPerMinute(Double costPerMinute) {
        this.costPerMinute = costPerMinute;

    }

    /**
     * Επιστρέφει το κόστος ανά SMS.
     *
     * @return το κόστος ανά SMS
     */
    public Double getCostPerSMS() {
        return costPerSMS;
    }

    /**
     * Θέτει το κόστος ανά SMS.
     *
     * @param costPerSMS το κόστος ανά SMS
     */
    public void setCostPerSMS(Double costPerSMS) {
        this.costPerSMS = costPerSMS;

    }

    /**
     * Επιστρέφει τα διαθέσιμα λεπτά.
     *
     * @return τα διαθέσιμα λεπτά
     */
    public int getAvailableMinutes() {
        return availableMinutes;
    }

    /**
     * Θέτει τα διαθέσιμα λεπτά.
     *
     * @param availableMinutes τα διαθέσιμα λεπτά
     */
    public void setAvailableMinutes(int availableMinutes) {
        this.availableMinutes = availableMinutes;

    }

    /**
     * Επιστρέφει τα διαθέσιμα SMS.
     *
     * @return τα διαθέσιμα SMS
     */
    public int getAvailableSMS() {
        return availableSMS;
    }

    /**
     * Θέτει τα διαθέσιμα SMS.
     *
     * @param availableSMS τα διαθέσιμα SMS
     */
    public void setAvailableSMS(int availableSMS) {
        this.availableSMS = availableSMS;

    }

}
