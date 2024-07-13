<%@ page import="com.Beans.Util.Program" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.db.*" %>
<%@ page import="com.Beans.Users.Client" %>
<%@ page import="com.Beans.Users.Seller" %>
<%@ page import="com.Beans.Users.Admin" %>
<%@ page import="com.Beans.Util.Bill" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    session = request.getSession(false);
    if (session == null || session.getAttribute("seller") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Seller seller = (Seller) session.getAttribute("seller");
    Client client = (Client) request.getAttribute("client");
    ArrayList<Bill> bills = client.getBills();
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="customcss.css">
    <title>Add new Client</title>
    <script>
        const bills = <%= new com.google.gson.Gson().toJson(bills) %>;

        function updateForm() {
            const billID = document.getElementById("billCombo").value;
            const bill = bills.find(b => b.billID == billID);
            if (bill) {
                document.getElementById("TimeSpentTalking").value = bill.timeSpentTalking;
                document.getElementById("SMSSent").value = bill.totalSMS;
                document.getElementById("totalCost").value = bill.totalCost;
                document.getElementById("status").value = bill.status;
            }
        }

        window.onload = function() {
            updateForm();
        };
    </script>
</head>
<body>
<nav class="navbar navbar-expand-lg">
    <a class="navbar-brand" href="seller.jsp">Seller Hub</a>
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
<div class="container">
    <h1>Edit Bills</h1>
    <form method="POST" action='<%=request.getContextPath()%>/seller-servlet' name="frmAddClient">
        <input type="hidden" name="action" value="editBill"/>
        <input type="hidden" name="clientID" value="<%=client.getClientId()%>"/>

        <!-- Select Bill -->
        <div class="form-group row">
            <label for="billCombo" class="col-sm-2 col-form-label">Select Bill</label>
            <div class="col-sm-7">
                <select class="form-control" id="billCombo" name="billID" onchange="updateForm()">
                    <option value="-1">Select a Bill</option>
                    <% for (Bill b : bills) { %>
                    <option value="<%= b.getBillID() %>"><%= b.getDateIssued() %></option>
                    <% } %>
                </select>
            </div>
        </div>
        <!-- Time Spent Talking-->
        <div class="form-group row">
            <label for="TimeSpentTalking" class="col-sm-2 col-form-label">Time Spent Talking</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" id="TimeSpentTalking" name="TimeSpentTalking" placeholder="Time Spent Talking">
            </div>
        </div>
        <!-- SMS Sent -->
        <div class="form-group row">
            <label for="SMSSent" class="col-sm-2 col-form-label">SMS Sent</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" id="SMSSent" name="SMSSent" placeholder="SMS Sent">
            </div>
        </div>
        <!-- total Cost -->
        <div class="form-group row">
            <label for="totalCost" class="col-sm-2 col-form-label">Total Cost</label>
            <div class="col-sm-7">
                <input type="number" step="0.01" class="form-control" id="totalCost" name="totalCost" placeholder="Total Cost">
            </div>
        </div>
        <!-- Status -->
        <div class="form-group row">
            <label for="status" class="col-sm-2 col-form-label">Status</label>
            <div class="col-sm-7">
                <select class="form-control" id="status" name="status">
                    <option value="Paid">Paid</option>
                    <option value="Issued">Issued</option>
                </select>
            </div>
        </div>

        <!-- Submit Button -->
        <div class="form-group row">
            <div class="col-sm-7 offset-sm-2">
                <button type="submit" class="btn btn-primary">Submit</button>
            </div>
        </div>
    </form>
</div>
</body>
</html>

