package com.Servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout-servlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    /**
     * Δημιουργεί ένα νέο αντικείμενο LogoutServlet.
     */
    public LogoutServlet() {
        super();
    }
    /**
     * Εκτελεί τη μέθοδο doGet του Servlet.
     *
     * @param request  το αίτημα προς το Servlet
     * @param response η απάντηση του Servlet
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Get existing session if exists

        if (session != null) {
            // Clear all attributes and invalidate session
            session.invalidate();
        }

        // Redirect to login page or any other appropriate page
        response.sendRedirect("login.jsp");
    }
}
