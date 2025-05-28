<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="admin_check.jsp" />
<%
    Boolean checklogin = (Boolean) request.getAttribute("checklogin");
if(!checklogin){
    String site = new String("login");
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", site);
}
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WinZard Bank Home</title>
        <link rel="icon" href="icon/logo.png" type="image/x-icon">
    <link rel="stylesheet" href="CSS/nav.css">
    <link rel="stylesheet" href="CSS/home.css">
</head>
<body>
    <header>
        <jsp:include page="header_admin.jsp" />
    </header>

    <div class="option">
        <button><a href="admin_user">User</a></button>
        <button><a href="add_amount">Add Amount</a></button>
        <button><a href="member">Edit member</a></button>
        <button><a href="admin_currency">Edit Currency</a></button>
        <button><a href="fee">Edit Processing Fee</a></button>
        <button><a href="msg">Edit MSG</a></button>
        <button><a href="admin_fee.jsp">Total Fee</a></button>
    </div>
    
</body>
</html>

