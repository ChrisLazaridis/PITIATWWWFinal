<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.Beans.Users.Client" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.Beans.Util.Bill" %>
<%@ page import="com.Beans.Util.PhoneNumber" %>
<%@ page import="com.Beans.Util.Call" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.sql.Timestamp" %>

<%
    session = request.getSession(false);
    if (session == null || session.getAttribute("client") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = currentDateTime.format(formatter);
    // Get client from session
    Client client = (Client) session.getAttribute("client");
    ArrayList<PhoneNumber> phoneNumbers = client.getPhoneNumbers();
    ArrayList<Call> calls = new ArrayList<>();
    for (PhoneNumber phoneNumber : phoneNumbers) {
        ArrayList<Call> phoneNumberCalls = phoneNumber.getCalls();
        for (Call call : phoneNumberCalls) {
            // If the call was made before the current date and time, add it to the list
            if(call.getTimestamp().compareTo(new Timestamp(System.currentTimeMillis())) < 0) {
                calls.add(call);
            }
        }
    }
    // Get the current date and time (έχω calls και sms για όλο το '24 μέσα στη βάση)
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="customcss.css">
    <title>Call History</title>
</head>
<body>
<nav class="navbar navbar-expand-lg">
    <a class="navbar-brand" href="client.jsp">Client Hub</a>
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
                <a class="nav-link" href="thisMonth.jsp">This Month</a>
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
    <h2>Current Date and Time: <%= formattedDateTime %></h2>
    <table class="table table-striped">
        <thead class="thead-dark">
        <tr>
            <th scope="col">From</th>
            <th scope="col">To</th>
            <th scope="col">Date Made</th>
            <th scope="col">Duration</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Call call : calls) {
        %>
        <tr>
            <td><%= call.getPhoneNumber().getPhoneNumber() %></td>
            <td><%= call.getCallerPhoneNumber() %></td>
            <td><%= call.getTimestamp() %></td>
            <td><%= call.getDuration() %></td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
</div>

</body>
</html>
