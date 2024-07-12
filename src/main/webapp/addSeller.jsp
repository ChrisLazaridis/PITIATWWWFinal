<%--
  Created by IntelliJ IDEA.
  User: claza
  Date: 7/12/2024
  Time: 7:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.Beans.Util.Program" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.db.*" %>
<%@ page import="com.Beans.Users.Client" %>
<%@ page import="com.Beans.Users.Seller" %>
<%@ page import="com.Beans.Users.Admin" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    session = request.getSession(false);
    if (session == null || session.getAttribute("admin") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Admin admin = (Admin) session.getAttribute("admin");
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
    <form method="POST" action='<%=request.getContextPath()%>/admin-servlet' name="frmAddClient">
        <input type="hidden" name="action" value="insert"/>

        <!-- First Name -->
        <div class="form-group row">
            <label for="FirstName" class="col-sm-2 col-form-label">First Name</label>
            <div class="col-sm-7">
                <input type="text" id="FirstName" class="form-control" name="FirstName" placeholder="Enter First Name" required>
            </div>
        </div>

        <!-- Last Name -->
        <div class="form-group row">
            <label for="LastName" class="col-sm-2 col-form-label">Last Name</label>
            <div class="col-sm-7">
                <input type="text" id="LastName" class="form-control" name="LastName" placeholder="Enter Last Name" required>
            </div>
        </div>

        <!-- Email -->
        <div class="form-group row">
            <label for="Email" class="col-sm-2 col-form-label">Email</label>
            <div class="col-sm-7">
                <input type="email" id="Email" class="form-control" name="Email" placeholder="Enter Email" required>
            </div>
        </div>

        <!-- Username -->
        <div class="form-group row">
            <label for="Username" class="col-sm-2 col-form-label">Username</label>
            <div class="col-sm-7">
                <input type="text" id="Username" class="form-control" name="Username" placeholder="Enter Username" required>
            </div>
        </div>

        <!-- Password -->
        <div class="form-group row">
            <label for="Password" class="col-sm-2 col-form-label">Password</label>
            <div class="col-sm-7">
                <input type="password" class="form-control" name="Password" id="Password" placeholder="Enter Password">
            </div>
            <div class="col-sm-3">
                <button class="btn btn-outline-secondary" type="button" onclick="myFunction()">Show</button>
            </div>
        </div>

        <!-- Repeat Password -->
        <div class="form-group row">
            <label for="Password2" class="col-sm-2 col-form-label">Repeat Password</label>
            <div class="col-sm-7">
                <input type="password" class="form-control" name="Password2" id="Password2" placeholder="Repeat Password">
            </div>
            <div class="col-sm-3">
                <button class="btn btn-outline-secondary" type="button" onclick="myFunction2()">Show</button>
            </div>
        </div>

        <!-- Birthday -->
        <div class="form-group row">
            <label for="Birthday" class="col-sm-2 col-form-label">Birthday</label>
            <div class="col-sm-7">
                <input type="date" id="Birthday" class="form-control" name="Birthday" required>
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

