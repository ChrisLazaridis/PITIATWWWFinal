<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f8f8;
            color: #333;
        }
        .container {
            margin: 50px auto;
            max-width: 600px;
            padding: 20px;
            border: 1px solid #ddd;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #d9534f;
        }
        .error-details {
            margin-top: 20px;
        }
        pre {
            background-color: #f7f7f7;
            padding: 10px;
            border: 1px solid #ddd;
            overflow: auto;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Oops! Something went wrong.</h1>
    <p>We're sorry, but an error has occurred. Please try again later.</p>

    <div class="error-details">
        <h2>Error Details:</h2>
        <p>Status Code: <%= request.getAttribute("jakarta.servlet.error.status_code") %></p>
        <p>Error Message: <%= request.getAttribute("jakarta.servlet.error.message") %></p>
        <p>Exception Type: <%= request.getAttribute("jakarta.servlet.error.exception_type") %></p>

        <%
            Throwable throwable = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
            if (throwable != null) {
                out.println("<h3>Stack Trace:</h3>");
                out.println("<pre>");
                throwable.printStackTrace(new java.io.PrintWriter(out));
                out.println("</pre>");
            }
        %>
    </div>
</div>
</body>
</html>
