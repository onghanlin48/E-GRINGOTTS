<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="CSS/success.css">
    <link rel="stylesheet" href="CSS/nav.css">
    <link rel="icon" href="Image/website_logo-removebg-preview.png" >
</head>
<body>
<jsp:include page="header.jsp" />
    <div class="container">
        <div class="popup">
    <img src="Image/close-removebg-preview.png" >
        <h2>Failed !</h2>
        <p>
            <%String des = (String) request.getAttribute("des");
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