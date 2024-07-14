package com.Servlets;

import com.Beans.Util.Program;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.Serial;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.Beans.Users.*;
import com.db.*;

/**
 * Η κλάση AdminServlet εξυπηρετεί τις λειτουργίες του διαχειριστή.
 */
@WebServlet(name = "adminServlet", value = "/admin-servlet")
public class AdminServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Η σελίδα για την εισαγωγή νέου πωλητή.
     */
    private static final String INSERT_SELLER = "/addSeller.jsp";
    /**
     * Η σελίδα για την επεξεργασία πωλητή.
     */
    private static final String EDIT_SELLER = "/editSeller.jsp";
    /**
     * Η σελίδα για την εισαγωγή νέου προγράμματος.
     */
    private static final String INSERT_PROGRAM = "/addProgram.jsp";
    /**
     * Η σελίδα για την επεξεργασία προγράμματος.
     */
    private static final String EDIT_PROGRAM = "/editProgram.jsp";
    protected Database db;

    /**
     * Ο constructor της κλάσης.
     */
    public AdminServlet() {
        super();
    }

    /**
     * Η μέθοδος που καλείται όταν ο χρήστης κάνει GET request στον servlet.
     * Υλοποιεί κυρίως τη μετάβαση ανάμεσα στα jsp της εφαρμογής.
     * @param request an {@link HttpServletRequest} αντικείμενο που περιέχει το request που έστειλε ο client
     *
     * @param response an {@link HttpServletResponse} αντικείμενο που περιέχει το response που θα σταλεί στον client
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String forward = "";
        SellerDB sellerDB = new SellerDB();
        AdminDB adminDB = new AdminDB();
        String sellerName;
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
                sellerName = request.getParameter("sellerUn");
                try {
                    Seller seller = sellerDB.searchSeller(sellerName);
                    request.setAttribute("seller", seller);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
            case "deleteProgram":
                int programID = Integer.parseInt(request.getParameter("programID"));
                System.out.println(programID);
                try {
                    adminDB.deleteProgramByID(programID);
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
    /**
     * Η μέθοδος που καλείται όταν ο χρήστης κάνει Post request στον servlet.
     * Υλοποιεί κυρίως λειτουργίες συνδεδεμένες με τη βάση
     * @param request an {@link HttpServletRequest} αντικείμενο που περιέχει το request που έστειλε ο client
     *
     * @param response an {@link HttpServletResponse} αντικείμενο που περιέχει το response που θα σταλεί στον client
     *
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String forward = "admin.jsp";
        SellerDB sellerDB = new SellerDB();
        AdminDB adminDB = new AdminDB();
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        switch (action) {
            case "insertSeller":
                Seller seller = new Seller();
                seller.setUsername(request.getParameter("Username"));
                seller.setPassword(request.getParameter("Password"));
                seller.setFirstName(request.getParameter("FirstName"));
                seller.setLastName(request.getParameter("LastName"));
                seller.setEmail(request.getParameter("Email"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date birthday = formatter.parse(request.getParameter("Birthday"));
                    seller.setBirthday(birthday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    sellerDB.addSeller(seller);
                    sellerDB.addSellerAdminRelationship(seller, (Admin) session.getAttribute("admin"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "editSeller":
                Seller seller1 = new Seller();
                seller1.setSellerID(Integer.parseInt(request.getParameter("SellerID")));
                seller1.setExistInDB(true);
                seller1.setUsername(request.getParameter("Username"));
                seller1.setPassword(request.getParameter("Password"));
                seller1.setFirstName(request.getParameter("FirstName"));
                seller1.setLastName(request.getParameter("LastName"));
                seller1.setEmail(request.getParameter("Email"));
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date birthday = formatter1.parse(request.getParameter("Birthday"));
                    seller1.setBirthday(birthday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    sellerDB.updateSeller(seller1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "insertProgram":
                Program program = new Program();
                program.setProgramName(request.getParameter("ProgramName"));
                program.setFixedCost(Double.parseDouble(request.getParameter("FixedCost")));
                program.setAvailableMinutes(Integer.parseInt(request.getParameter("AvailableMinutes")));
                program.setAvailableSMS(Integer.parseInt(request.getParameter("AvailableSMS")));
                program.setCostPerMinute(Double.parseDouble(request.getParameter("CostPerMinute")));
                program.setCostPerSMS(Double.parseDouble(request.getParameter("CostPerSMS")));
                try {
                    adminDB.addProgram(program);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "editProgram":
                Program program1 = new Program();
                program1.setProgramID(Integer.parseInt(request.getParameter("programCombo")));
                program1.setProgramName(request.getParameter("ProgramName"));
                program1.setFixedCost(Double.parseDouble(request.getParameter("FixedCost")));
                program1.setAvailableMinutes(Integer.parseInt(request.getParameter("AvailableMinutes")));
                program1.setAvailableSMS(Integer.parseInt(request.getParameter("AvailableSMS")));
                program1.setCostPerMinute(Double.parseDouble(request.getParameter("CostPerMinute")));
                program1.setCostPerSMS(Double.parseDouble(request.getParameter("CostPerSMS")));
                try {
                    adminDB.updateProgram(program1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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

}
