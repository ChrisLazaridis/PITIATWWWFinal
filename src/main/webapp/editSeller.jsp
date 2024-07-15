
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.Beans.Users.Seller" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.Beans.Users.Admin" %>
<%

    session = request.getSession(false);
    if (session == null || session.getAttribute("admin") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Get seller from session
    Admin admin = (Admin) session.getAttribute("admin");
    Seller sellerToEdit = (Seller) request.getAttribute("seller");
    if (sellerToEdit == null) {
        response.sendRedirect("admin.jsp");
        return;
    }

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <style type="text/css">
        .btn-square {
            width: 40px;
            height: 40px;
            padding: 0;
            text-align: center;
            vertical-align: middle;
            line-height: 40px;
            border-radius: 50%;
        }

        .btn-square .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
        }
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="customcss.css">
    <title>Edit Client</title>
</head>
<body>
<nav class="navbar navbar-expand-lg">
    <a class="navbar-brand" href="#">Admin Hub</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="admin-servlet?action=insertProgram">Add Program</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="admin-servlet?action=editProgram">Edit Programs</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="admin-servlet?action=insertSeller">Add Seller</a>
            </li>
            <li class="nav-item">
                <form class="form-inline" action="${pageContext.request.contextPath}/logout-servlet" method="get">
                    <button class="btn btn-danger" type="submit">Logout</button>
                </form>
            </li>
        </ul>
    </div>
</nav>

<div class="container mt-4">
    <h2>Edit Seller Information</h2>

    <form method="POST" action='<%=request.getContextPath()%>/admin-servlet' name="frmEditClient">
        <input type="hidden" name="action" value="editSeller"/>
        <input type="hidden" name="SellerID" value="<%= sellerToEdit.getSellerID() %>"/>

        <!-- First Name -->
        <div class="form-group row">
            <label for="FirstName" class="col-sm-2 col-form-label">First Name</label>
            <div class="col-sm-7">
                <input type="text" id="FirstName" class="form-control" name="FirstName" placeholder="Enter First Name"
                       value="<%= sellerToEdit.getFirstName() %>" required>
            </div>
        </div>

        <!-- Last Name -->
        <div class="form-group row">
            <label for="LastName" class="col-sm-2 col-form-label">Last Name</label>
            <div class="col-sm-7">
                <input type="text" id="LastName" class="form-control" name="LastName" placeholder="Enter Last Name"
                       value="<%= sellerToEdit.getLastName() %>" required>
            </div>
        </div>

        <!-- Email -->
        <div class="form-group row">
            <label for="Email" class="col-sm-2 col-form-label">Email</label>
            <div class="col-sm-7">
                <input type="email" id="Email" class="form-control" name="Email" placeholder="Enter Email"
                       value="<%= sellerToEdit.getEmail() %>" required>
            </div>
        </div>

        <!-- Username -->
        <div class="form-group row">
            <label for="Username" class="col-sm-2 col-form-label">Username</label>
            <div class="col-sm-7">
                <input type="text" id="Username" class="form-control" name="Username" placeholder="Enter Username"
                       value="<%= sellerToEdit.getUsername() %>" required>
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
                <input type="password" class="form-control" name="Password2" id="Password2"
                       placeholder="Repeat Password">
            </div>
            <div class="col-sm-3">
                <button class="btn btn-outline-secondary" type="button" onclick="myFunction2()">Show</button>
            </div>
        </div>

        <!-- Birthday -->
        <div class="form-group row">
            <label for="Birthday" class="col-sm-2 col-form-label">Birthday</label>
            <div class="col-sm-7">
                <input type="date" id="Birthday" class="form-control" name="Birthday"
                       value="<%= new SimpleDateFormat("yyyy-MM-dd").format(sellerToEdit.getBirthday()) %>" required>
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
<script type="text/javascript">
    window.addEventListener('load', function () {
        document.frmEditClient.addEventListener('submit', function (e) {
            if (!passwordCheck()) {
                e.preventDefault();
            }
        });
        updateProgramInfo();
    });

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
<script type="text/javascript" src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
<script type="text/javascript" src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
