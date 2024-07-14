package com.Servlets;

import com.Beans.Users.Admin;
import com.Beans.Users.Client;
import com.Beans.Users.Seller;
import com.db.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
/**
 * Η κλάση LoginServlet εξυπηρετεί την είσοδο των χρηστών στο σύστημα.
 */
@WebServlet(name = "loginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet {
    /**
     * Η σύνδεση με τη βάση δεδομένων.
     */
    private Database db;
    /**
     * Αρχικοποιεί τον Servlet.
     */
    @Override
    public void init() {
        db = new Database();
    }
    /**
     * Δημιουργεί ένα νέο αντικείμενο LoginServlet.
     */
    public LoginServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        HttpSession session = request.getSession(true);

        try {
            switch (role) {
                case "Client":
                    ClientLogin(request, response, username, password, session);
                    break;
                case "Seller":
                    SellerLogin(request, response, username, password, session);
                    break;
                case "Admin":
                    AdminLogin(request, response, username, password, session);
                    break;
                default:
                    response.sendRedirect("login.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred during login. Please try again.");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
    }
    /**
     * Εκτελεί την είσοδο του πελάτη.
     *
     * @param request  το αίτημα προς το Servlet
     * @param response η απάντηση του Servlet
     * @param username το όνομα χρήστη του πελάτη
     * @param password ο κωδικός πρόσβασης του πελάτη
     * @param session  η τρέχουσα συνεδρία
     */
    private void ClientLogin(HttpServletRequest request, HttpServletResponse response, String username, String password, HttpSession session) throws Exception {
        ClientDB clientDB = new ClientDB();
        Client client = clientDB.searchClient(username);
        if (client != null && db.checkPassword(password, client.getPassword())) {
            session.setAttribute("client", client);
            response.sendRedirect("client.jsp");
        } else {
            request.setAttribute("errorMessage", "Invalid username or password.");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
    }
    /**
     * Εκτελεί την είσοδο του πωλητή.
     *
     * @param request  το αίτημα προς το Servlet
     * @param response η απάντηση του Servlet
     * @param username το όνομα χρήστη του πωλητή
     * @param password ο κωδικός πρόσβασης του πωλητή
     * @param session  η τρέχουσα συνεδρία
     */
    private void SellerLogin(HttpServletRequest request, HttpServletResponse response, String username, String password, HttpSession session) throws Exception {
        SellerDB sellerDB = new SellerDB();
        Seller seller = sellerDB.searchSeller(username);
        if (seller != null && db.checkPassword(password, seller.getPassword().trim())) {
            session.setAttribute("seller", seller);
            response.sendRedirect("seller.jsp");
        } else {
            request.setAttribute("errorMessage", "Invalid username or password.");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
    }
    /**
     * Εκτελεί την είσοδο του διαχειριστή.
     *
     * @param request  το αίτημα προς το Servlet
     * @param response η απάντηση του Servlet
     * @param username το όνομα χρήστη του διαχειριστή
     * @param password ο κωδικός πρόσβασης του διαχειριστή
     * @param session  η τρέχουσα συνεδρία
     */
    private void AdminLogin(HttpServletRequest request, HttpServletResponse response, String username, String password, HttpSession session) throws Exception {
        AdminDB adminDB = new AdminDB();
        Admin admin = adminDB.searchAdmin(username);
        if (admin != null && db.checkPassword(password, admin.getPassword())) {
            session.setAttribute("admin", admin);
            response.sendRedirect("admin.jsp");
        } else {
            request.setAttribute("errorMessage", "Invalid username or password.");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
    }
}
