<%@ page import="com.Beans.Util.Program" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.db.*" %>
<%@ page import="com.Beans.Users.Admin" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    session = request.getSession(false);
    if (session == null || session.getAttribute("admin") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Admin admin = (Admin) session.getAttribute("admin");
    ClientDB clientDB = new ClientDB();
    ArrayList<Program> programs = null;
    try {
        programs = clientDB.getAllPrograms();
        // remove the program with program_id = 11
        programs.removeIf(program -> program.getProgramID() == 11);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="customcss.css">
    <title>Edit Programs</title>
    <script>
        const programs = <%= new com.google.gson.Gson().toJson(programs) %>;

        function updateForm() {
            const programID = document.getElementById("programCombo").value;
            const program = programs.find(p => p.programID == programID);
            if (program) {
                document.getElementById("ProgramName").value = program.programName;
                document.getElementById("FixedCost").value  = program.fixedCost;
                document.getElementById("CostPerMinute").value  = program.costPerMinute;
                document.getElementById("CostPerSMS").value  = program.costPerSMS;
                document.getElementById("AvailableMinutes").value  = program.availableMinutes;
                document.getElementById("AvailableSMS").value  = program.availableSMS;
            }
        }
        function updateDeleteButtonUrl() {
            const programID = document.getElementById("programCombo").value;
            const url = `<%=request.getContextPath()%>/admin-servlet?action=deleteProgram&programID=` + programID;
            if (confirm("Are you sure you want to delete this program? This action is permanent.")) {
                window.location.href = url;
            }
        }

        window.onload = function() {
            updateForm();
        };
    </script>
</head>
<body>
<nav class="navbar navbar-expand-lg">
    <a class="navbar-brand" href="admin.jsp">Admin Hub</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
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
<div class="container">
    <h1>Edit Program</h1>
    <form method="POST" action='<%=request.getContextPath()%>/admin-servlet' name="frmAddClient">
        <input type="hidden" name="action" value="editProgram"/>

        <!-- Program Combo Box -->
        <div class="form-group row">
            <label for="programCombo" class="col-sm-2 col-form-label">Select Program</label>
            <div class="col-sm-7">
                <select id="programCombo" name="programCombo" class="form-control" onchange="updateForm()">
                    <% for (Program program : programs) { %>
                    <option value="<%= program.getProgramID() %>"><%= program.getProgramName() %></option>
                    <% } %>
                </select>
            </div>
            <button type="button" id="deleteButton" class="btn btn-primary btn-square ml-2" onclick="updateDeleteButtonUrl();">-</button>
        </div>

        <!-- Program Name -->
        <div class="form-group row">
            <label for="ProgramName" class="col-sm-2 col-form-label">Program Name</label>
            <div class="col-sm-7">
                <input type="text" id="ProgramName" class="form-control" name="ProgramName" placeholder="Enter Program Name" required>
            </div>
        </div>
        <!-- Fixed Cost -->
        <div class="form-group row">
            <label for="FixedCost" class="col-sm-2 col-form-label">Fixed Cost</label>
            <div class="col-sm-7">
                <input type="number" step="0.01" id="FixedCost" class="form-control" name="FixedCost" placeholder="Enter Fixed Cost" required>
            </div>
        </div>
        <!-- Cost Per Minute -->
        <div class="form-group row">
            <label for="CostPerMinute" class="col-sm-2 col-form-label">Cost Per Minute</label>
            <div class="col-sm-7">
                <input type="number" step="0.01" id="CostPerMinute" class="form-control" name="CostPerMinute" placeholder="Enter Cost Per Minute" required>
            </div>
        </div>
        <!-- Cost Per SMS -->
        <div class="form-group row">
            <label for="CostPerSMS" class="col-sm-2 col-form-label">Cost Per SMS</label>
            <div class="col-sm-7">
                <input type="number" step="0.01" id="CostPerSMS" class="form-control" name="CostPerSMS" placeholder="Enter Cost Per SMS" required>
            </div>
        </div>
        <!-- Available Minutes -->
        <div class="form-group row">
            <label for="AvailableMinutes" class="col-sm-2 col-form-label">Available Minutes</label>
            <div class="col-sm-7">
                <input type="number" id="AvailableMinutes" class="form-control" name="AvailableMinutes" placeholder="Enter Available Minutes" required>
            </div>
        </div>
        <!-- Available SMS -->
        <div class="form-group row">
            <label for="AvailableSMS" class="col-sm-2 col-form-label">Available SMS</label>
            <div class="col-sm-7">
                <input type="number" id="AvailableSMS" class="form-control" name="AvailableSMS" placeholder="Enter Available SMS" required>
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

