<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.Beans.Users.Client" %>
<%@ page import="com.Beans.Users.Seller" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.Beans.Util.Program" %>
<%@ page import="com.Beans.Util.PhoneNumber" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.db.ClientDB" %>
<%

    session = request.getSession(false);
    if (session == null || session.getAttribute("seller") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Get seller from session
    Seller seller = (Seller) session.getAttribute("seller");
    Client clientToEdit = (Client) request.getAttribute("client");
    if (clientToEdit == null) {
        response.sendRedirect("seller.jsp");
        return;
    }
    ClientDB clientDB;
    try {
        clientDB = new ClientDB();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    clientDB.getClientId(clientToEdit);
    ArrayList<Program> programs = clientDB.getAllPrograms();
    ArrayList<PhoneNumber> phoneNumbers = clientToEdit.getPhoneNumbers();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <style>
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
    <title>Edit Client</title>
</head>
<body>
<header>
    <h1>Edit Client</h1>
    <!-- Logout button -->
    <form action="logout-servlet" method="get">
        <button type="submit">Logout</button>
    </form>
</header>

<div class="container mt-4">
    <h2>Edit Client Information</h2>

    <form method="POST" action='<%=request.getContextPath()%>/seller-servlet' name="frmEditClient">
        <input type="hidden" name="action" value="edit"/>
        <input type="hidden" name="ClientID" value="<%= clientToEdit.getClientId() %>"/>

        <!-- First Name -->
        <div class="form-group row">
            <label for="FirstName" class="col-sm-2 col-form-label">First Name</label>
            <div class="col-sm-7">
                <input type="text" id="FirstName" class="form-control" name="FirstName" placeholder="Enter First Name" value="<%= clientToEdit.getFirstName() %>" required>
            </div>
        </div>

        <!-- Last Name -->
        <div class="form-group row">
            <label for="LastName" class="col-sm-2 col-form-label">Last Name</label>
            <div class="col-sm-7">
                <input type="text" id="LastName" class="form-control" name="LastName" placeholder="Enter Last Name" value="<%= clientToEdit.getLastName() %>" required>
            </div>
        </div>

        <!-- Email -->
        <div class="form-group row">
            <label for="Email" class="col-sm-2 col-form-label">Email</label>
            <div class="col-sm-7">
                <input type="email" id="Email" class="form-control" name="Email" placeholder="Enter Email" value="<%= clientToEdit.getEmail() %>" required>
            </div>
        </div>

        <!-- Username -->
        <div class="form-group row">
            <label for="Username" class="col-sm-2 col-form-label">Username</label>
            <div class="col-sm-7">
                <input type="text" id="Username" class="form-control" name="Username" placeholder="Enter Username" value="<%= clientToEdit.getUsername() %>" required>
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
                <input type="date" id="Birthday" class="form-control" name="Birthday" value="<%= new SimpleDateFormat("yyyy-MM-dd").format(clientToEdit.getBirthday()) %>" required>
            </div>
        </div>

        <!-- VAT -->
        <div class="form-group row">
            <label for="VAT" class="col-sm-2 col-form-label">VAT</label>
            <div class="col-sm-7">
                <input type="text" id="VAT" class="form-control" name="VAT" placeholder="Enter VAT" value="<%= clientToEdit.getVAT() %>" required>
            </div>
        </div>

        <!-- Phone Number -->
        <div class="form-group row">
            <label for="PhoneNumber" class="col-sm-2 col-form-label">Phone Number</label>
            <div class="col-sm-7">
                <select id="phoneNumber" class="form-control" name="PhoneNumber" onchange="updateProgramInfo()">
                    <% for (PhoneNumber p : clientToEdit.getPhoneNumbers()) { %>
                    <option value="<%= p.getPhoneNumber() %>" data-program="<%= p.getProgram().getProgramName() %>"><%= p.getPhoneNumber() %></option>
                    <% } %>
                </select>
            </div>
            <div class="col-sm-3">
                <button type="button" class="btn btn-primary btn-square ml-2" onclick="window.location.href='seller-servlet?action=addPhone&clientUn=<%=clientToEdit.getUsername()%>';">+</button>
                <button type="button" id="deleteButton" class="btn btn-primary btn-square ml-2" onclick="updateDeleteButtonUrl();">-</button>
            </div>
        </div>

        <!-- Phone Number Program -->
        <div class="form-group row mt-2">
            <div class="col-sm-2"></div>
            <div class="col-sm-7">
                <div id="PhoneNumberProgram" class="alert alert-info">
                    <% if (!phoneNumbers.isEmpty()) { %>
                    <%= phoneNumbers.get(0).getProgram().getProgramName() %>
                    <% } %>
                </div>
            </div>
        </div>

        <!-- Program -->
        <div class="form-group row">
            <label for="Program" class="col-sm-2 col-form-label">Program</label>
            <div class="col-sm-7">
                <select id="program" class="form-control" name="Program">
                    <% for (Program p : programs) { %>
                    <option value="<%= p.getProgramID()%>"><%= p.getProgramName()%></option>
                    <% } %>
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
<script>
    Window.addEventListener('load', function () {
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

    function updateProgramInfo() {
        const select = document.getElementById("phoneNumber");
        const selectedOption = select.options[select.selectedIndex];
        document.getElementById("PhoneNumberProgram").innerText = selectedOption.getAttribute("data-program");
    }

    function updateDeleteButtonUrl() {
        const select = document.getElementById('phoneNumber');
        const selectedValue = select.options[select.selectedIndex].value;
        const clientUsername = '<%= clientToEdit.getUsername() %>';
        const url = 'seller-servlet?action=deletePhone&clientUn=' + clientUsername + '&phoneNumber=' + selectedValue;

        if (confirm("Are you sure you want to delete this phone number? This action is permanent.")) {
            window.location.href = url;
        }
    }

    // Call the function once to set the initial URL
    updateDeleteButtonUrl();
</script>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
