package com.Beans.Users;

import java.util.ArrayList;

/**
 * Η κλάση Seller αντιπροσωπεύει έναν πωλητή του συστήματος.
 * Κληρονομεί την κλάση User.
 */
public class Seller extends User {
    /**
     * Ο κωδικός του πωλητή.
     */
    private int sellerID;
    /**
     * Οι πελάτες του πωλητή.
     */
    private ArrayList<Client> clientsBelow = new ArrayList<Client>();

    /**
     * Δημιουργεί ένα νέο αντικείμενο Seller.
     */
    public Seller() {
        super();
    }

    /**
     * Επιστρέφει τον κωδικό του πωλητή.
     *
     * @return ο κωδικός του πωλητή
     */
    public int getSellerID() {
        return sellerID;
    }

    /**
     * Θέτει τον κωδικό του πωλητή.
     *
     * @param seller_id ο κωδικός του πωλητή
     */
    public void setSellerID(int seller_id) {
        this.sellerID = seller_id;
    }

    /**
     * Επιστρέφει τους πελάτες του πωλητή.
     *
     * @return οι πελάτες του πωλητή
     */
    public ArrayList<Client> getClientsBelow() {
        return clientsBelow;
    }

    /**
     * Θέτει τους πελάτες του πωλητή.
     *
     * @param clientsBelow οι πελάτες του πωλητή
     */
    public void setClientsBelow(ArrayList<Client> clientsBelow) {
        this.clientsBelow = clientsBelow;
    }

    /**
     * Προσθέτει έναν πελάτη στον πωλητή.
     *
     * @param client ο πελάτης
     */
    public void addClient(Client client) {
        clientsBelow.add(client);
    }

    /**
     * Αφαιρεί έναν πελάτη από τον πωλητή.
     *
     * @param client ο πελάτης
     */
    public void removeClient(Client client) {
        clientsBelow.remove(client);
    }

    /**
     * Αφαιρεί έναν πελάτη από τον πωλητή.
     *
     * @param Username το όνομα χρήστη του πελάτη
     */
    public void removeClient(String Username){
        for (Client client : clientsBelow) {
            if (client.getUsername().equals(Username)) {
                clientsBelow.remove(client);
                break;
            }
        }
    }

    /**
     * Αφαιρεί όλους τους πελάτες από τον πωλητή.
     */
    public void removeAllClients(){
        clientsBelow.clear();
    }

}

