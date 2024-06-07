<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.Beans.Users.Seller" %>
<%@ page import="com.Beans.Users.Client" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.Beans.Util.PhoneNumber" %>
<%@ page import="com.db.ClientDB" %>
<%@ page import="com.Beans.Util.Bill" %>
<%

    session = request.getSession(false);
    if (session == null || session.getAttribute("seller") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Get seller from session
    Seller seller = (Seller) session.getAttribute("seller");
    ArrayList<Client> clients = seller.getClientsBelow();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <title>Show All Sellers</title>

</head>
<body>
<header class="container-fluid mt-3 mb-3">
    <div class="row">
        <div class="col-md-6">
            <!-- Username -->
            <h1><%= seller.getUsername() %>
            </h1>
            <!-- Logout button -->
            <form action="${pageContext.request.contextPath}/logout-servlet" method="get">
                <button class="btn btn-danger" type="submit">Logout</button>
            </form>
        </div>
        <div class="col-md-6 d-flex flex-column align-items-end">
            <!-- Buttons -->
            <button type="button" class="btn btn-dark mb-2"
                    onclick="window.location.href='seller-servlet?action=insert&sellerUn=<%=seller.getUsername()%>';">
                Add Client!
            </button>
            <button type="button" class="btn btn-dark"
                    onclick="window.location.href='showAllPrograms.jsp?&sellerUn=<%=seller.getUsername()%>';">
                See all available programs
            </button>
        </div>
    </div>
</header>

<div class="container">
    <table class="table table-striped">

        <thead class="thead-dark">
        <tr>
            <th scope="col">Name</th>
            <th scope="col">Surname</th>
            <th scope="col">Email</th>
            <th scope="col">Birthday</th>
            <th scope="col">Phone Numbers</th>
            <th scope="col">debts</th>
            <th scope="col">Action</th>
        </tr>
        </thead>
        <tbody>
        <% for (Client c : clients) { %>
        <tr>
            <th scope="row"><%= c.getFirstName() %></th>
            <td><%= c.getLastName() %></td>
            <td><%= c.getEmail() %></td>
            <td><%= c.getBirthday() %></td>
            <td>
                <select class="form-control">
                    <% for (PhoneNumber phoneNumber : c.getPhoneNumbers()) { %>
                    <option value="<%= phoneNumber.getPhoneNumber() %>"><%= phoneNumber.getPhoneNumber() %></option>
                    <% } %>
                </select>
            </td>
            <%
                double totalDebt = 0.0;
                for (Bill bill : c.getBills()) {
                    if (!bill.getStatus().equalsIgnoreCase("Paid")) {
                        totalDebt += bill.getTotalCost();
                    }
                }
                String debtClass = "";
                if (totalDebt > 0 && totalDebt < 100) {
                    debtClass = "bg-warning";
                } else if (totalDebt >= 100) {
                    debtClass = "bg-danger text-white";
                } else {
                    debtClass = "bg-success text-white";
                }
                String formattedDebt = String.format("%.2f €", totalDebt);
            %>
            <td class="<%= debtClass %>">
                <%= formattedDebt %>
            </td>
            <td>
                <button type="button" class="btn btn-success"
                        onclick="window.location.href='seller-servlet?action=edit&sellerUn=<%=seller.getUsername()%>&clientUn=<%= c.getUsername()%>';">
                    Update
                </button>
                <button type="button" class="btn btn-warning"
                        onclick="confirmDeleteClient('<%=seller.getUsername()%>', '<%= c.getUsername() %>');">
                    Delete
                </button>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>

</div>
<script type="text/javascript">
    function confirmDeleteClient(sellerUn, clientUn) {
        if (confirm("Are you sure you want to delete this client? This action is permanent.")) {
            window.location.href = 'seller-servlet?action=delete&sellerUn=' + sellerUn + '&clientUn=' + clientUn;
        }
    }
</script>
</body>
</html>