<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Deleting Cache
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Main Menu</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="customcss.css">
    <!-- Custom CSS -->
    <style>
        body {
            padding-top: 5rem;
        }
    </style>
</head>
<body>

<div class="container">
        <h1 class="display-4">Welcome</h1>
        <p class="lead">This is the final assignment for the course of "Programming in the Internet and the WorldWide Web 2024 at the University of Piraeus</p>
        <p class="lead">from Students</p>
        <list class="block-list"">
            <li>Alexandris Lampros: p22007</li>
            <li>Lazaridis Christos-Lazaros: p22083</li>
            <li>Osma Feti: p22126</li>
            <li>Kalogeropoulos Athanasios: p22223</li>
        </list>
        <hr class="my-4">
        <ul class="block-list">
            <li><a href="login.jsp" class="btn btn-primary btn-lg">Login</a></li>
        </ul>
</div>
<!-- Bootstrap JS and jQuery (optional, if you need Bootstrap JS functionality) -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
        integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>
</body>
</html>
