<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="db.DBConnection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="data.Account" %>
<jsp:include page="connection.jsp" />
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
    <title>Transfer History</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Mystery+Quest&display=swap" rel="stylesheet">
    <link rel="icon" href="icon/logo.png" type="image/x-icon">
    <link rel="stylesheet" href="CSS/nav.css">
    <link rel="stylesheet" href="CSS/history.css">
    
</head>
<body onload="apply()">
<header class="header">
    <jsp:include page="header_home.jsp" />
</header>
<%
    HttpSession session1 = request.getSession();
    Account account = (Account) session1.getAttribute("account");
    int id = account.getUserId();
    LocalDate currentDate = LocalDate.now();

    LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);

    LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    String formattedFirstDay = firstDayOfMonth.format(formatter);
    String formattedLastDay = lastDayOfMonth.format(formatter);
%>
<div class="bigContainer">
    <div class="container">
        <h1>Transaction history</h1>
        <div class="filter">
            <div><h2>From</h2></div>
            <div><h2>To</h2></div>
            <div><h2 class="status">Category</h2></div>
            <div><h2 style="margin-left: -60px">Payment Method</h2></div>
            <div><h2 style="margin-left: -60px">Currency</h2></div>
        </div>
    </div>
    <div class="search">
        <label>
            <input type="date" placeholder="Start From" id="from" name="from" value="<%=formattedFirstDay%>">
        </label>
        <label>
            <input type="date" placeholder="End To" id="to" name="to" value="<%=formattedLastDay%>">
        </label>
        <select name="category" id="category">
            <option value="">All</option>
            <%
                try(Connection connection = DBConnection.getConnection()){
                    String select = "select * from history where form=? GROUP BY category";
                    try(PreparedStatement ps = connection.prepareStatement(select)) {
                        ps.setInt(1,id);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
            %>
            <option value="<%=rs.getString("category")%>"><%=rs.getString("category")%></option>
            <%
                        }
                        ps.close();
                        connection.close();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            %>

        </select>
        <select name="method" id="method">
            <option value="">All</option>
            <%
                try(Connection connection = DBConnection.getConnection()){
                    String select = "select * from history where form=? GROUP BY user";
                    try(PreparedStatement ps = connection.prepareStatement(select)) {
                        ps.setInt(1,id);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
            %>
            <option value="<%=rs.getString("user")%>"><%=rs.getString("user")%></option>
            <%
                        }
                        ps.close();
                        connection.close();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            %>
        </select>
        <select name="currency" id="currency">
            <option value="0">All</option>
            <%
                try(Connection connection = DBConnection.getConnection()){
                    String select = "select * from currency";
                    try(PreparedStatement ps = connection.prepareStatement(select)) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
            %>
            <option value="<%=rs.getString("id")%>"><%=rs.getString("currency")%> (<%=rs.getString("short")%>)</option>
            <%
                        }
                        ps.close();
                        connection.close();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            %>
        </select>
        <br><br>
        <button onclick="apply()" style="margin-left: -4px">Apply</button>

        
    </div>
    <div class="butir">
        <div class="butir1">
        <div><p>Details</p></div>
        <div><p>Amount</p></div>
    </div>
        
    </div>
    <div id="result">

    </div>
</div>
</body>
</html>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script>
    function apply(){
        $("#result").empty();
        var from = $("#from").val();
        var to = $("#to").val();
        var c = $("#category").val();
        var m = $("#method").val();
        var cu = $("#currency").val();
        $.ajax({
            type: 'GET',
            url: 'history_f',
            data: {from:from,to:to,c:c,m:m,cu:cu},
            success : function(result){
                $("#result").html(result);
            }
        })
    }
</script>