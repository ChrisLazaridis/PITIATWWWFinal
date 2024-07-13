<%@ page import="com.Beans.Util.Program" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.db.*" %>
<%@ page import="com.Beans.Users.Client" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    ClientDB clientDB;
    try {
        clientDB = new ClientDB();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    session = request.getSession(false);
    if (session == null || session.getAttribute("seller") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    ArrayList<Program> programms = null;
    try {
        programms = clientDB.getAllPrograms();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Show All Programs</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="customcss.css">
</head>
<body>
<nav class="navbar navbar-expand-lg">
    <a class="navbar-brand" href="#">Seller Hub</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="seller-servlet?action=insert&sellerUn=<%=seller.getUsername()%>">Add Client!</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="showAllPrograms.jsp?&sellerUn=<%=seller.getUsername()%>">See all available programs</a>
            </li>
            <li class="nav-item">
                <form class="form-inline" action="${pageContext.request.contextPath}/logout-servlet" method="get">
                    <button class="btn btn-danger" type="submit">Logout</button>
                </form>
            </li>
        </ul>
    </div>
</nav>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-10">
            <table class="table table-striped">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">Program ID</th>
                    <th scope="col">Program Name</th>
                    <th scope="col">Fixed Cost</th>
                    <th scope="col">Cost per Minute</th>
                    <th scope="col">Cost per SMS</th>
                    <th scope="col">Available Minutes</th>
                    <th scope="col">Available SMS</th>
                </tr>
                </thead>
                <tbody>
                <% for (Program p : programms) { %>
                <tr>
                    <th scope="row"><%= p.getProgramID() %></th>
                    <td><%= p.getProgramName() %></td>
                    <td><%= String.format("%.2f €", p.getFixedCost()) %></td>
                    <td><%= String.format("%.2f €", p.getCostPerMinute()) %></td>
                    <td><%= String.format("%.2f €", p.getCostPerSMS()) %></td>
                    <td><%= p.getAvailableMinutes() %></td>
                    <td><%= p.getAvailableSMS() %></td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <button type="button" class="btn btn-success" onclick="window.location.href='seller.jsp';">Back to clients list</button>
        </div>
    </div>
</div>
</body>
</html>
