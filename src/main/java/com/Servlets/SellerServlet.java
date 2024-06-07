package com.Servlets;

import com.Beans.Util.PhoneNumber;
import com.Beans.Util.Program;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.Serial;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.Beans.Users.*;
import com.db.*;

/**
 * Η κλάση SellerServlet χρησιμοποιείτε για την υλοποίηση των μεθόδων των πωλητών.
 * Κληρονομεί την κλάση HttpServlet.
 */
@WebServlet(name = "sellerServlet", value = "/seller-servlet")
public class SellerServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Η σελίδα για την εισαγωγή πελάτη.
     */
    private static final String INSERT = "/addClient.jsp";
    /**
     * Η σελίδα για την επεξεργασία πελάτη.
     */
    private static final String EDIT = "/editClient.jsp";
    /**
     * Η σελίδα για την προβολή της λίστας πελατών.
     */
    private static final String ClientList = "/seller.jsp";
    /**
     * Η σελίδα για την προσθήκη τηλεφώνου.
     */
    private static final String AddPhone = "/editClient.jsp";
    /**
     * Η σελίδα για την διαγραφή τηλεφώνου.
     */
    private static final String deletePhone = "/seller.jsp";
    /**
     * Λίστα με τους αριθμούς τηλεφώνου του πελάτη.
     */
    private ArrayList<PhoneNumber> phoneNumbersOfClient = new ArrayList<PhoneNumber>();
    /**
     * Η σύνδεση με τη βάση δεδομένων.
     */

    private Database db;

    /**
     * Αρχικοποιεί τον SellerServlet.
     */
    public SellerServlet() {
        super();
        db = new Database();
    }

    @Override
    public void init() {}
    /**
     * Εκτελεί τη μέθοδο doGet.
     * Ελέγχει την ενέργεια που ζητήθηκε και καλεί την αντίστοιχη μέθοδο.
     * Χρησιμοποιείτε κυρίως για την περιήγηση στις διάφορες σελίδες του συστήματος.
     * @param request το αίτημα προς τον servlet
     * @param response η απάντηση του servlet
     * @throws IOException εάν υπάρξει σφάλμα εισόδου/εξόδου
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String forward = "";
        String action = request.getParameter("action");

        try {
            ClientDB clientDB = new ClientDB();
            SellerDB sellerDB = new SellerDB();
            HttpSession session = request.getSession(false);
            phoneNumbersOfClient.clear();

            if (session == null || session.getAttribute("seller") == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            Seller seller = (Seller) session.getAttribute("seller");

            if (action.equalsIgnoreCase("delete")) {
                String clientUn = request.getParameter("clientUn");
                Client client = clientDB.searchClient(clientUn);
                if (client != null) {
                    clientDB.deleteClient(client);
                    seller.removeClient(clientUn);
                }
                forward = ClientList;
                request.setAttribute("seller", seller);
            } else if (action.equalsIgnoreCase("edit")) {
                forward = EDIT;
                String clientUn = request.getParameter("clientUn");
                Client client = clientDB.searchClient(clientUn);
                request.setAttribute("client", client);
            } else if (action.equalsIgnoreCase("insert")) {
                forward = INSERT;
            } else if (action.equalsIgnoreCase("addPhone")) {
                forward = AddPhone;
                String clientUn = request.getParameter("clientUn");
                Client client = clientDB.searchClient(clientUn);
                generateRandomPhoneNumber(client, clientDB.searchProgramByID(11));
                phoneNumbersOfClient.add(client.getPhoneNumbers().getLast());
                phoneNumbersOfClient = client.getPhoneNumbers();
                request.setAttribute("client", client);
            } else if (action.equalsIgnoreCase("deletePhone")){
                forward = deletePhone;
                String clientUn = request.getParameter("clientUn");
                String phoneNumber = request.getParameter("phoneNumber");
                Client client = clientDB.searchClient(clientUn);
                ArrayList<PhoneNumber> phoneNumbers = client.getPhoneNumbers();
                for (PhoneNumber p : phoneNumbers) {
                    if (p.getPhoneNumber().equals(phoneNumber)) {
                        client.removePhoneNumber(phoneNumber);
                        clientDB.deletePhoneNumber(p);
                        break;
                    }
                }
                phoneNumbersOfClient = client.getPhoneNumbers();
                request.setAttribute("client", client);
            }
            else {
                forward = ClientList;
            }
            seller.removeAllClients();
            ArrayList<Client> clients = clientDB.getAllClientsForSeller(seller);
            for (Client client : clients) {
                seller.addClient(client);
            }
            request.setAttribute("seller", seller);
            RequestDispatcher view = request.getRequestDispatcher(forward);
            view.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

        /**
     * Εκτελεί τη μέθοδο doPost.
     * Ελέγχει την ενέργεια που ζητήθηκε και καλεί την αντίστοιχη μέθοδο.
     * Χρησιμοποιείτε για την εισαγωγή, ενημέρωση και διαγραφή πελάτη από το σύστημα.
     * @param request το αίτημα προς τον servlet
     * @param response η απάντηση του servlet
     * @throws IOException εάν υπάρξει σφάλμα εισόδου/εξόδου
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        Client client = new Client();
        ClientDB clientDB;

        try {
            clientDB = new ClientDB();
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("seller") == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            boolean updatesPw = false;
            Seller seller = (Seller) session.getAttribute("seller");

            client.setFirstName(request.getParameter("FirstName"));
            client.setLastName(request.getParameter("LastName"));
            client.setEmail(request.getParameter("Email"));
            client.setUsername(request.getParameter("Username"));
            if(request.getParameter("Password") != null && !request.getParameter("Password").isEmpty()){
                client.setPassword(request.getParameter("Password"));
                updatesPw = true;
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            client.setVAT(request.getParameter("VAT"));

            try {
                Date birthday = formatter.parse(request.getParameter("Birthday"));
                client.setBirthday(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            client.setExistInDB(false);
            Program program = clientDB.searchProgramByID(Integer.parseInt(request.getParameter("Program")));
            if(program == null){
                response.sendRedirect("error.jsp");
                return;
            }

            if (action.equalsIgnoreCase("edit")) {
                String clientIdParam = request.getParameter("ClientID");
                if (clientIdParam != null && !clientIdParam.isEmpty()) {
                    client.setClientID(Integer.parseInt(clientIdParam));
                    client.setExistInDB(true);
                    clientDB.getPhoneNumbers(client);
                    for (PhoneNumber p : phoneNumbersOfClient) {
                        boolean found = false;
                        for (PhoneNumber p2 : client.getPhoneNumbers()) {
                            if (p.getPhoneNumber().equals(p2.getPhoneNumber())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            client.addPhoneNumber(p);
                        }
                    }
                }else{
                    throw new Exception("Client ID not found");
                }
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setPhoneNumber(request.getParameter("PhoneNumber"));
                phoneNumber.setProgram(program);
                client.removePhoneNumber(phoneNumber.getPhoneNumber());
                client.addPhoneNumber(phoneNumber);
                for (PhoneNumber p : client.getPhoneNumbers()) {
                    System.out.println(p.getPhoneNumber());
                }
                client.setExistInDB(true);
                if(updatesPw){
                    clientDB.updateClient(client);
                } else {
                    clientDB.updateClientNoPw(client);
                }
            } else if (action.equalsIgnoreCase("insert")) {
                generateRandomPhoneNumber(client, program);
                clientDB.addClient(client, seller);
                seller.addClient(client);
            }
            // Update the seller in the session to reflect the changes
            seller.removeAllClients();
            phoneNumbersOfClient.clear();
            ArrayList<Client> clients = clientDB.getAllClientsForSeller(seller);
            for (Client c : clients) {
                seller.addClient(c);
            }
            request.setAttribute("seller", seller);
            RequestDispatcher view = request.getRequestDispatcher("seller.jsp"); // change to appropriate success view
            view.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    /**
     * Δημιουργεί έναν τυχαίο αριθμό τηλεφώνου για την εισαγωγή νέου πελάτη.
     *
     * @param client  ο πελάτης
     * @param program το πρόγραμμα
     * @throws Exception εάν υπάρξει σφάλμα κατά τη δημιουργία του τηλεφώνου
     */
    protected void generateRandomPhoneNumber(Client client, Program program) throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber();
        ClientDB clientDB = new ClientDB();

        // Loop until a unique phone number is found
        do {
            String randomPhoneNumber = "693" + String.format("%07d", (int) (Math.random() * 10000000));
            phoneNumber.setPhoneNumber(randomPhoneNumber);
        } while (clientDB.checkIfPhoneNumberExists(phoneNumber.getPhoneNumber()));

        phoneNumber.setProgram(program);
        client.addPhoneNumber(phoneNumber);
    }
}
