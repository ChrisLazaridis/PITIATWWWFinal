<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.Beans.Users.Client" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.Beans.Util.PhoneNumber" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="com.Beans.Util.SMS" %>
<%@ page import="com.Beans.Util.Call" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="com.Beans.Util.Program" %>
<%@ page import="java.util.Date" %>
<%
    session = request.getSession(false);
    if (session == null || session.getAttribute("client") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    LocalDateTime currentDateTime = LocalDateTime.now();
    LocalDateTime startOfMonth = currentDateTime.withDayOfMonth(1).toLocalDate().atStartOfDay();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = currentDateTime.format(formatter);
    Timestamp currentTimestamp = Timestamp.valueOf(currentDateTime);
    Timestamp startOfMonthTimestamp = Timestamp.valueOf(startOfMonth);

    // Get client from session
    Client client = (Client) session.getAttribute("client");
    ArrayList<PhoneNumber> phoneNumbers = client.getPhoneNumbers();
    HashMap<PhoneNumber, List<Call>> calls = new HashMap<>();
    HashMap<PhoneNumber, List<SMS>> sms = new HashMap<>();
    HashMap<PhoneNumber, Program> programs = new HashMap<>();

    for (PhoneNumber phoneNumber : phoneNumbers) {
        ArrayList<Call> phoneNumberCalls = phoneNumber.getCalls();
        ArrayList<SMS> phoneNumberSMS = phoneNumber.getSMS();
        Program program = phoneNumber.getProgram(); // Assuming each PhoneNumber has a getProgram method
        programs.put(phoneNumber, program);
        List<Call> validCalls = new ArrayList<>();
        List<SMS> validSms = new ArrayList<>();

        for (Call call : phoneNumberCalls) {
            Timestamp callTimestamp;
            if (call.getTimestamp() instanceof Date) {
                callTimestamp = new Timestamp(((Date) call.getTimestamp()).getTime());
            } else {
                callTimestamp = (Timestamp) call.getTimestamp();
            }
            // Check if the call was made within the current month and before the current date and time
            if (callTimestamp.after(startOfMonthTimestamp) && callTimestamp.before(currentTimestamp)) {
                validCalls.add(call);
            }
        }

        for (SMS sms1 : phoneNumberSMS) {
            Timestamp smsTimestamp;
            if (sms1.getTimeStamp() instanceof Date) {
                smsTimestamp = new Timestamp(((Date) sms1.getTimeStamp()).getTime());
            } else {
                smsTimestamp = (Timestamp) sms1.getTimeStamp();
            }
            // Check if the SMS was made within the current month and before the current date and time
            if (smsTimestamp.after(startOfMonthTimestamp) && smsTimestamp.before(currentTimestamp)) {
                validSms.add(sms1);
            }
        }

        if (!validCalls.isEmpty()) {
            calls.put(phoneNumber, validCalls);
        }

        if (!validSms.isEmpty()) {
            sms.put(phoneNumber, validSms);
        }
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="customcss.css">
    <title>SMS History</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js" type="text/javascript"></script>
</head>
<body>
<nav class="navbar navbar-expand-lg">
    <a class="navbar-brand" href="client.jsp">Client Hub</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
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

    <!-- ComboBox for selecting phone number -->
    <div class="form-group">
        <label for="phoneNumberSelect">Select Phone Number:</label>
        <select class="form-control" id="phoneNumberSelect" onchange="updateDetails()">
            <option value="all">All</option>
            <% for (PhoneNumber phoneNumber : phoneNumbers) { %>
            <option value="<%= phoneNumber.getPhoneNumber() %>"><%= phoneNumber.getPhoneNumber() %></option>
            <% } %>
        </select>
    </div>

    <!-- Display call and SMS details -->
    <div id="details"></div>

    <!-- Canvas for charts -->
    <div class="row">
        <div class="col-md-6">
            <div class="chart-container">
                <canvas id="callsChart"></canvas>
            </div>
        </div>
        <div class="col-md-6">
            <div class="chart-container">
                <canvas id="smsChart"></canvas>
            </div>
        </div>
    </div>

</div>

<script type="text/javascript">
    let phoneData = {};
    <% for (PhoneNumber phoneNumber : phoneNumbers) { %>
    phoneData["<%= phoneNumber.getPhoneNumber() %>"] = {
        calls: <%= calls.get(phoneNumber) != null ? calls.get(phoneNumber).size() : 0 %>,
        sms: <%= sms.get(phoneNumber) != null ? sms.get(phoneNumber).size() : 0 %>,
        availableMinutes: <%= programs.get(phoneNumber).getAvailableMinutes() %>,
        availableSMS: <%= programs.get(phoneNumber).getAvailableSMS() %>
    };
    <% } %>

    function updateDetails() {
        let selectedNumber = document.getElementById("phoneNumberSelect").value;
        let detailsDiv = document.getElementById("details");
        detailsDiv.innerHTML = "";

        let totalCalls = 0;
        let totalSMS = 0;
        let totalAvailableMinutes = 0;
        let totalAvailableSMS = 0;

        if (selectedNumber === "all") {
            for (let number in phoneData) {
                totalCalls += phoneData[number].calls;
                totalSMS += phoneData[number].sms;
                totalAvailableMinutes += phoneData[number].availableMinutes;
                totalAvailableSMS += phoneData[number].availableSMS;
            }
        } else {
            totalCalls = phoneData[selectedNumber].calls;
            totalSMS = phoneData[selectedNumber].sms;
            totalAvailableMinutes = phoneData[selectedNumber].availableMinutes;
            totalAvailableSMS = phoneData[selectedNumber].availableSMS;
        }

        detailsDiv.innerHTML = "<h3>Number of Calls Made: " + totalCalls + "</h3>" +
            "<h3>Number of SMS Sent: " + totalSMS + "</h3>" +
            "<h3>Available Minutes: " + totalAvailableMinutes + "</h3>" +
            "<h3>Available SMS: " + totalAvailableSMS + "</h3>";

        // Update charts
        updateCallsChart(totalCalls, totalAvailableMinutes);
        updateSMSChart(totalSMS, totalAvailableSMS);
    }

    function updateCallsChart(calls, availableMinutes) {
        let ctx = document.getElementById('callsChart').getContext('2d');
        let chartData = {
            labels: ['Used Calls', 'Available Calls'],
            datasets: [{
                data: [calls, availableMinutes],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)'
                ],
                borderWidth: 1
            }]
        };

        if (window.callsChart && typeof window.callsChart.destroy === 'function') {
            window.callsChart.destroy();
        }

        window.callsChart = new Chart(ctx, {
            type: 'doughnut',
            data: chartData,
            options: {
                responsive: true,
                maintainAspectRatio: false, // Ensure the chart takes the space it needs
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'Calls Usage vs Available'
                },
                animation: {
                    animateScale: true,
                    animateRotate: true
                }
            }
        });
    }

    function updateSMSChart(sms, availableSMS) {
        let ctx = document.getElementById('smsChart').getContext('2d');
        let chartData = {
            labels: ['Used SMS', 'Available SMS'],
            datasets: [{
                data: [sms, availableSMS],
                backgroundColor: [
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)'
                ],
                borderWidth: 1
            }]
        };

        if (window.smsChart && typeof window.smsChart.destroy === 'function') {
            window.smsChart.destroy();
        }

        window.smsChart = new Chart(ctx, {
            type: 'doughnut',
            data: chartData,
            options: {
                responsive: true,
                maintainAspectRatio: false, // Ensure the chart takes the space it needs
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'SMS Usage vs Available'
                },
                animation: {
                    animateScale: true,
                    animateRotate: true
                }
            }
        });
    }

    document.addEventListener('DOMContentLoaded', function() {
        updateDetails();
    });
</script>

</body>
</html>
