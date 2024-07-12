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

@WebServlet(name = "adminServlet", value = "/admin-servlet")
public class AdminServlet extends HttpServlet{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String INSERT_SELLER = "/addSeller.jsp";
    private static final String EDIT_SELLER = "/editSeller.jsp";
    private static final String INSERT_PROGRAM = "/addProgram.jsp";
    private static final String EDIT_PROGRAM = "/editProgram.jsp";
    protected Database db;
    /**
     * Ο constructor της κλάσης.
     */
    public AdminServlet() {
        super();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        SellerDB sellerDB = new SellerDB();
        AdminDB adminDB = new AdminDB();
        String sellerName = "";
        String action = request.getParameter("action");
        switch (action) {
            case "insertProgram":
                forward = INSERT_PROGRAM;
                break;
            case "editProgram":
                forward = EDIT_PROGRAM;
                break;
            case "insertSeller":
                forward = INSERT_SELLER;
                break;
            case "editSeller":
                forward = EDIT_SELLER;
                break;
            case "deleteSeller":
                sellerName = request.getParameter("sellerUn");
                System.out.println(sellerName);
                try {
                    Seller seller = sellerDB.searchSeller(sellerName);
                    sellerDB.deleteSeller(seller);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                forward = "/admin.jsp";
                break;
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        String adminName = admin.getUsername();
        try {
            Admin newAdmin = adminDB.searchAdmin(adminName);
            request.getSession().setAttribute("admin", newAdmin);
            // send to the forward view
            view.forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

}
