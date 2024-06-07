<%@ page import="com.Beans.Util.Program" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.db.*" %>
<%@ page import="com.Beans.Users.Client" %>
<%@ page import="com.Beans.Users.Seller" %>
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
    Seller seller = (Seller) session.getAttribute("seller");
    ArrayList<Program> programms = clientDB.getAllPrograms();
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <title>Add new Client</title>
</head>
<body>
<div class="container">
    <h1>Add Client</h1>
    <form method="POST" action='<%=request.getContextPath()%>/seller-servlet' name="frmAddClient">
        <input type="hidden" name="action" value="insert"/>

        <div class="form-group row">
            <%--@declare id="firstname"--%><label for="FirstName" class="col-sm-2 col-form-label">FirstName</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" name="FirstName" placeholder="Enter FirstName" required>
            </div>
        </div>

        <div class="form-group row">
            <%--@declare id="lastname"--%><label for="LastName" class="col-sm-2 col-form-label">LastName</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" name="LastName" placeholder="Enter LastName" required>
            </div>
        </div>

        <div class="form-group row">
            <%--@declare id="email"--%><label for="Email" class="col-sm-2 col-form-label">Email</label>
            <div class="col-sm-7">
                <input type="email" class="form-control" name="Email" placeholder="Enter Email" required>
            </div>
        </div>

        <div class="form-group row">
            <%--@declare id="username"--%><label for="Username" class="col-sm-2 col-form-label">Username</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" name="Username" placeholder="Enter Username" required>
            </div>
        </div>

        <div class="form-group row">
            <%--@declare id="Password"--%><label for="Password" class="col-sm-2 col-form-label">Password</label>
            <div class="col-sm-7">
                <input type="password" id="Password" class="form-control" name="Password" placeholder="Enter Password">
                <input type="checkbox" onclick="myFunction()">Show Password</input>
            </div>
        </div>
        <div class="form-group row">
            <%--@declare id="Password2"--%><label for="Password2" class="col-sm-2 col-form-label">Repeat
            Password</label>
            <div class="col-sm-7">
                <input type="password" class="form-control" name="Password2" id="Password2"
                       placeholder="Enter Password">
                <input type="checkbox" onclick="myFunction2()">Show Password</input>
            </div>
        </div>

        <div class="form-group row">
            <%--@declare id="birthday"--%><label for="Birthday" class="col-sm-2 col-form-label">Birthday</label>
            <div class="col-sm-7">
                <input type="date" class="form-control" name="Birthday" required>
            </div>
        </div>

        <div class="form-group row">
            <%--@declare id="vat"--%><label for="VAT" class="col-sm-2 col-form-label">VAT</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" name="VAT" placeholder="Enter VAT" required>
            </div>
        </div>

        <div class="form-group row">
            <label for="Program" class="col-sm-2 col-form-label">Program</label>
            <div class="col-sm-7">
                <select id="program" class="form-control" name="Program">
                    <% for (Program p : programms) { %>
                    <option value="<%= p.getProgramID()%>"><%= p.getProgramName()%>
                    </option>
                    <% } %>
                </select>
            </div>
        </div>

        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>
<script>
    function myFunction() {
        const x = document.getElementById("Password");
        if (x.type === "password") {
            x.type = "text";
        } else {
            x.type = "password";
        }
    }

    function myFunction2() {
        const x = document.getElementById("Password2");
        if (x.type === "password") {
            x.type = "text";
        } else {
            x.type = "password";
        }
    }

    Window.addEventListener('load', function () {
        document.frmAddClient.addEventListener('submit', function (e) {
            if (!passwordCheck()) {
                e.preventDefault();
            }
        });
    });

    function passwordCheck() {
        const x = document.getElementById("Password");
        const y = document.getElementById("Password2");
        if (x.value !== y.value && !x.value.isEmpty()) {
            alert("Passwords do not match");
            return false;
        } else {
            return true;
        }
    }
</script>
</body>
</html>
