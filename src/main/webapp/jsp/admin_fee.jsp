<%@ page import="data.Account" %>
<%@ page import="data.Card" %>
<%@ page import="db.DBConnection" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
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
<%
    HttpSession session1 = request.getSession();
    Card card = (Card) session1.getAttribute("card");
    Account account = (Account) session1.getAttribute("account");
    String c_name = "";
    try(Connection connection = DBConnection.getConnection()){
        String select = "select * from category where id =?";
        try(PreparedStatement ps = connection.prepareStatement(select)) {
            ps.setInt(1,account.getCategory());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c_name = rs.getString("name");
            }
            ps.close();
            connection.close();
        }
    } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    }

%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Balance</title>
    <link rel="icon" href="icon/logo.png" type="image/x-icon">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Mystery+Quest&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="CSS/Balance.css">
    <link rel="stylesheet" href="CSS/nav.css">
</head>
<body>
<header>
    <jsp:include page="header_admin.jsp" />
</header>

    <main class="container">
        <h2>Total Fee</h2>
        <div class="balance-section">
            <div class="saving-balance">
                <h1>Total Fee</h1>
                <%
                    try(Connection connection = DBConnection.getConnection()){
                        String select = "select * from admin a JOIN currency c ON a.currency = c.id";
                        try(PreparedStatement ps = connection.prepareStatement(select)) {

                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                %>
                <p><%=rs.getDouble("balance")%> <%=rs.getString("short")%></p>
                <%
                            }
                            ps.close();
                            connection.close();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                %>
            </div>

        </div>
        <div class="button-container">
            <button class="back-button" onclick="window.location = 'admin'">Back</button>
        </div>
        
    </main>
</body>
</html>