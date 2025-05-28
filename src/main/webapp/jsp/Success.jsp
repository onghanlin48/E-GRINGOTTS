<!DOCTYPE html>
<html lang="en" xmlns:jsp="http://java.sun.com/JSP/Page">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Success</title>
    <link rel="stylesheet" href="CSS/success.css">
    <link rel="stylesheet" href="CSS/nav.css">
    <link rel="icon" href="icon/logo.png" type="image/x-icon">
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container">
        <div class="popup">
    <img src="Image/tick1-removebg-preview.png" >
        <h2>Successful !</h2>
        <p>
            <%
                String des = (String) request.getAttribute("des");
                String page1 = (String) request.getAttribute("page");
            %>
            <%=des%>
        </p>
            <form action="<%=page1%>">
                <button type="submit">Next</button>
            </form>

        </div>
        
    </div>
</body>
</html>