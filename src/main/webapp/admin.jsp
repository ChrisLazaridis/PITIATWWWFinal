<%--
  Created by IntelliJ IDEA.
  User: claza
  Date: 7/12/2024
  Time: 6:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.Beans.Users.Seller" %>
<%@ page import="com.Beans.Users.Client" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.Beans.Util.PhoneNumber" %>
<%@ page import="com.db.SellerDB" %>
<%@ page import="com.Beans.Util.Bill" %>
<%@ page import="com.Beans.Users.Admin" %>
<%

    session = request.getSession(false);
    if (session == null || session.getAttribute("admin") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Get seller from session
    Admin admin = (Admin) session.getAttribute("admin");
    ArrayList<Seller> sellers = admin.getSellersBelow();
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
            <h1><%= admin.getUsername() %>
            </h1>
            <!-- Logout button -->
            <form action="${pageContext.request.contextPath}/logout-servlet" method="get">
                <button class="btn btn-danger" type="submit">Logout</button>
            </form>
        </div>
        <div class="col-md-6 d-flex flex-column align-items-end">
            <!-- Buttons -->
            <button type="button" class="btn btn-dark mb-2"
                    onclick="window.location.href='admin-servlet?action=insertProgram';">
                Add Program
            </button>
            <button type="button" class="btn btn-dark"
                    onclick="window.location.href='admin-servlet?action=editProgram';">
                Edit Programs
            </button>
            <button type="button" class="btn btn-dark"
                    onclick="window.location.href='admin-servlet?action=insertSeller';">
                Add Seller
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
            <th scope="col">Action</th>
        </tr>
        </thead>
        <tbody>
        <% for (Seller s : sellers) { %>
        <tr>
            <th scope="row"><%= s.getFirstName() %>
            </th>
            <td><%= s.getLastName() %>
            </td>
            <td><%= s.getEmail() %>
            </td>
            <td><%= s.getBirthday() %>
            </td>
            <td>
            <button type="button" class="btn btn-success"
                    onclick="window.location.href='admin-servlet?action=editSeller&sellerUn=<%= s.getUsername()%>';">
                Update
            </button>

            <button type="button" class="btn btn-warning"
                    onclick="confirmDeleteSeller('<%= s.getUsername() %>');">
                Delete
            </button>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>

</div>
<script type="text/javascript">
    function confirmDeleteSeller(sellerUn) {
        if (confirm("Are you sure you want to delete this seller? This action is permanent.")) {
            window.location.href = 'admin-servlet?action=deleteSeller&sellerUn=' + sellerUn;
        }
    }
</script>
</body>
</html>
