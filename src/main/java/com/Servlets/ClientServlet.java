package com.Servlets;

import com.Beans.Util.Bill;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.Serial;

import com.Beans.Users.*;
import com.db.*;
/**
 * Η κλάση ClientServlet εξυπηρετεί τις λειτουργίες του πελάτη.
 */
@WebServlet(name = "clientServlet", value = "/client-servlet")
public class ClientServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    public ClientServlet() {
        super();
    }

    @Override
    public void init() {
    }

    /**
     * Η μέθοδος που καλείται όταν ο χρήστης κάνει Post request στον servlet.
     * Υλοποιεί κυρίως λειτουργίες συνδεδεμένες με τη βάση
     *
     * @param request  an {@link HttpServletRequest} αντικείμενο που περιέχει το request που έστειλε ο client
     * @param response an {@link HttpServletResponse} αντικείμενο που περιέχει το response που θα σταλεί στον client
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ClientDB clientDB;

        try {
            clientDB = new ClientDB();
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("client") == null) {
                response.sendRedirect("index.jsp");
                return;
            }
            Client client = (Client) session.getAttribute("client");
            int billID = Integer.parseInt(request.getParameter("billID"));
            Bill bill = clientDB.searchBillByID(billID);
            if (bill != null && "Issued".equals(bill.getStatus())) {
                bill.setStatus("Paid");
                clientDB.updateBill(bill);

                // Refresh the client object in session to reflect the updated bill status
                Client updatedClient = clientDB.searchClient(client.getUsername());
                session.setAttribute("client", updatedClient);
            }
            response.sendRedirect("client.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

}
