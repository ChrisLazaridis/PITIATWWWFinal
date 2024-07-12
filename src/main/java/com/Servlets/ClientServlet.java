package com.Servlets;

import com.Beans.Util.Bill;
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

@WebServlet(name = "clientServlet", value = "/client-servlet")
public class ClientServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private Database db;

    public ClientServlet() {
        super();
    }

    @Override
    public void init() {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
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
