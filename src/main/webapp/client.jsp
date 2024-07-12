<%--
  Created by IntelliJ IDEA.
  User: claza
  Date: 7/12/2024
  Time: 4:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.Beans.Users.Client" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.Beans.Util.Bill" %>

<%
    session = request.getSession(false);
    if (session == null || session.getAttribute("client") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Get client from session
    Client client = (Client) session.getAttribute("client");
    ArrayList<Bill> bills = client.getBills();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <title>Client Hub</title>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="#">Client Hub</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="callHistory.jsp">Call History</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="smsHistory.jsp">SMS History</a>
            </li>
            <li class="nav-item">
                <form class="form-inline" action="${pageContext.request.contextPath}/logout-servlet" method="get">
                    <button class="btn btn-danger" type="submit">Logout</button>
                </form>
            </li>
        </ul>
    </div>
</nav>

<header class="container-fluid mt-3 mb-3">
    <div class="row">
        <div class="col-md-6">
            <!-- Username -->
            <h1><%= client.getUsername() %></h1>
        </div>
    </div>
</header>

<div class="container">
    <table class="table table-striped">
        <thead class="thead-dark">
        <tr>
            <th scope="col">Total Call Minutes</th>
            <th scope="col">Cost</th>
            <th scope="col">Month</th>
            <th scope="col">SMS</th>
            <th scope="col">Disbursement</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Bill bill : bills) {
        %>
        <tr>
            <td><%= bill.getTimeSpentTalking() %></td>
            <td><%= bill.getTotalCost() %></td>
            <td><%= bill.getDateIssued() %></td>
            <td><%= bill.getTotalSMS() %></td>
            <td>
                <%
                    if (!"Paid".equals(bill.getStatus())) {
                %>
                <form action="${pageContext.request.contextPath}/client-servlet" method="post">
                    <input type="hidden" name="billID" value="<%= bill.getBillID() %>">
                    <button class="btn btn-primary" type="submit">Pay</button>
                </form>
                <%
                } else {
                %>
                <p class="lead blockquote">Paid</p>
                <%
                    }
                %>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>
