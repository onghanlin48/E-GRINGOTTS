<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="db.DBConnection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="ajax.check_amount" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="connection.jsp" />
<%
    Boolean checklogin = (Boolean) request.getAttribute("checklogin");
    if(!checklogin){
        String site = new String("login");
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", site);
    }
    check_amount check = new check_amount();
    session.setAttribute("check_amount",check);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Convert Currency</title>
    <link rel="icon" href="icon/logo.png" type="image/x-icon">
    <link rel="stylesheet" href="CSS/nav.css">
    <link rel="stylesheet" href="CSS/currency.css">
</head>
<body onload="check()">
<header>
    <jsp:include page="header_home.jsp" />
</header>
    <form method="post" action="check_c_c">
        <div class="container">
            <div class="popup" style="height: auto;">
                <div>
                    <h1>Currency Converter</h1>
                    <div class="currency_Container">

                        <div>
                            <p class="Yhv" id="yhv">You have</p>
                            <input type="number" placeholder="Enter amount" name="amount_from" id="amount_from" step="0.01" min="0.01" onkeyup="check()" required>
                        </div>
                        <div>
                            <p>Currency</p>
                            <select class="inputText" id="from" name="from" style="width: 90%" onchange="check()">
                                <%
                                    try(Connection connection = DBConnection.getConnection()){
                                        String select = "select * from currency";
                                        try(PreparedStatement ps = connection.prepareStatement(select)) {
                                            ResultSet rs = ps.executeQuery();
                                            while (rs.next()) {
                                %>
                                <option value="<%=rs.getInt("id")%>"><%=rs.getString("currency")%></option>
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
                        </div>
                    </div>
                    <div class="currency_Container">

                        <div>
                            <p class="Yhv">You get</p>
                            <input type="text" name="amount_get" id="amount_get" placeholder="Amount" step="0.01" min="0.01" required disabled>
                        </div>
                        <div>
                            <p>Currency</p>
                            <select class="inputText" id="get" name="amount_get" style="width: 90%" onchange="check()">
                                <%
                                    try(Connection connection = DBConnection.getConnection()){
                                        String select = "select * from currency";
                                        try(PreparedStatement ps = connection.prepareStatement(select)) {
                                            ResultSet rs = ps.executeQuery();
                                            while (rs.next()) {
                                %>
                                <option value="<%=rs.getInt("id")%>"><%=rs.getString("currency")%></option>
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
                        </div>
                    </div>
                    <p class="rate" id="rate" style="color: red"></p>
                    <button>Convert</button>

                </div>

            </div>

        </div>
    </form>

    
</body>
</html>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script>
    function check(){
        $("#yhv").empty();
        $("#rate").empty();
        var from = $("#from").val();
        var get = $("#get").val();
        var amount = $("#amount_from").val();
        check_amount(from);
        if(from === get){
            $("#amount_get").val(amount);
            $("#rate").html("The currency cannot be same!");
        }else{

            if(amount === ""){
                $("#rate").html("Please fill in amount");
                $("#amount_get").val(amount);
            }else{
                $.ajax({
                    type: 'GET',
                    url: 'check_convert',
                    data: {from:from,get:get,amount:amount},
                    success : function(result){
                        $("#amount_get").val(result);
                        msg();
                    }
                })
            }

        }
    }
    function msg(){
        $.ajax({
            type: 'GET',
            url: 'c_amount',
            data: {id:'1'},
            success : function(result){
                $("#rate").html(result);
            }
        })
    }
    function check_amount(id){

        $.ajax({
            type: 'GET',
            url: 'amount',
            data: {id:id},
            success : function(result){
                $("#yhv").html(result);
            }
        })
    }

</script>