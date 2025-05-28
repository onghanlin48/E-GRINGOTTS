<%@ page import="data.Account" %>
<%@ page import="data.Card" %>
<%@ page import="db.DBConnection" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="connection.jsp" />
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
    <jsp:include page="header_home.jsp" />
</header>

    <main class="container">
        <h2>Account Balance</h2>
        <div class="balance-section">
            <div class="saving-balance">
                <h1>Account Detail</h1>
                <p>Username : <%=account.getUsername()%></p>
                <p>Account Number : <%=account.getUserId()%></p>
                <p>Name : <%=account.getFull_name()%></p>
                <p>Phone : <%=account.getPhone()%></p>
                <p>Email : <%=account.getEmail()%></p>
                <p>Member : <%=c_name%></p>
            </div>
            <div class="saving-balance">
                <h1>Saving Account</h1>
                <%
                    try(Connection connection = DBConnection.getConnection()){
                        String select = "select * from amount a JOIN currency c ON a.cid = c.id where userid=?";
                        try(PreparedStatement ps = connection.prepareStatement(select)) {
                            ps.setInt(1,account.getUserId());
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                %>
                <p><%=rs.getDouble("amount")%> <%=rs.getString("currency")%></p>
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
            <div class="debit-balance">
                <h1>Credit Account</h1>
                <p>Card Number : <%=card.getCardNum()%></p>
                <p>MM/YY : <%=card.getExpiryDate()%></p>
                <p>CVV : <%=card.getCVV()%></p>
                <%
                    if(card.getCardNum() != null){
                %>
                <form method="post" action="set_pin">
                    <p>Enter Old PIN : <input type="text" id="old" name="old" pattern="\d{6}" title="Only 6 Digit Number" required></p>
                    <p>Enter New PIN : <input type="text" id="new" name="new" pattern="\d{6}" title="Only 6 Digit Number" onkeyup="update_pin()" required></p>
                    <p>Enter Confirm PIN : <input type="text" id="conform" name="conform" pattern="\d{6}" title="Only 6 Digit Number" onkeyup="update_pin()" required></p>
                    <p style="color: red" id="msg"></p>
                    <p><button class="back-button">Update PIN</button></p>
                </form>
                <%
                    }
                %>


            </div>
        </div>
        <div class="button-container">

            <button class="back-button" onclick="window.location = 'Home'">Back</button>
        </div>
        
    </main>
</body>
</html>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script>
    function update_pin(){
        var n = $("#new").val();
        var c = $("#conform").val();
        if(n === c){
           $("#msg").empty()
        }else{
            $("#msg").html("New PIN and Confirm PIN Must be Same!")
        }

    }
</script>