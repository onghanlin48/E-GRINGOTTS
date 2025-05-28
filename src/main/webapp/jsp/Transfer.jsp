<%@ page import="db.DBConnection" %>
<%@ page import="java.sql.*" %>
<%@ page import="data.Account" %>
<%@ page import="ajax.check_currency" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="connection.jsp" />
<%
    Boolean checklogin = (Boolean) request.getAttribute("checklogin");
    if(!checklogin){
        String site = new String("login");
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", site);
    }
    HttpSession session1 = request.getSession();
    session1.removeAttribute("check_currency");
    check_currency check = new check_currency();
    session1.setAttribute("check_currency", check);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Transfer Page</title>

    <title>WinZard Bank Home</title>
    <link rel="icon" href="icon/logo.png" type="image/x-icon">
    <link rel="stylesheet" href="CSS/nav.css">
    <link rel="stylesheet" href="CSS/Transfer.css">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>
<body>
    <header>
        <jsp:include page="header_home.jsp" />
    </header>
    <br>
    <%
        Integer id = (Integer) request.getAttribute("user_id");
        String name = (String) request.getAttribute("full_name");
        String phone = (String) request.getAttribute("phone");
        check_currency check_c = new check_currency();
        check_c.setUser_id(id);
        check_c.setName(name);
        check_c.setPhone(phone);
        session1.setAttribute("check_currency", check_c);
    %>
    <main class="container">
        <button class="transfer-button" onclick="window.location.href = 'Search'">
            Back
        </button>
      <h1>Transfer</h1>
        <form method="post" action="transfer_c">
            <div>
                <p>
                    From You Account (Currency)
                </p>
                <div class="amount_reference">
                    <div class="amount" style="width:100%;">
                        <div style="display: flex">
                            <select class="inputText" id="currency" style="width: 90%">
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
                            <div style="display: flex; margin-left: 2px">
                                <button class="transfer-button" type="button" style="font-size:24px;padding: 3px 2px;height: 35px;width: 30px;" onclick="add()"><i class="material-icons">add</i></button>
                            </div>
                            <div style="display: flex; margin-left: 2px">
                                <button class="transfer-button" type="button" style="font-size:24px;padding: 3px 2px;height: 35px;width: 30px" onclick="remove()"><i class="material-icons">delete</i></button>
                            </div>


                        </div>

                        <div class="amountnote" id="currencynote">

                        </div>
                    </div>


                </div>

                <%
                    Account account = (Account) session1.getAttribute("account");
                    int user_id = account.getUserId();
                    try(Connection connection = DBConnection.getConnection()){
                        String select = "select * from currency c JOIN amount a ON a.cid = c.id WHERE a.userid=?";
                        try(PreparedStatement ps = connection.prepareStatement(select)) {
                            ps.setInt(1, user_id);
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                %>
                <div class="amount_reference" id="amount_reference_<%=rs.getInt("id")%>" style="display: none">

                </div>
                <%
                            }
                            ps.close();
                            connection.close();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                %>


                <div class="to">
                    To <%=id+" (" + name + ") " + "(" + phone +")"%>
                </div>
                <p>Receipt Currency</p>
                <div class="amount_reference">
                    <div class="amount">
                        <select class="inputText" id="to" name="to" onchange="change_to()">
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
                        <div class="amountnote">
                            <p id="received_note" style="color: red">

                            </p>
                        </div>
                    </div>
                </div>
                <div class="to">
                    Amount Transfer
                </div>
                <div class="reference">
                    <div class="recipientreference">
                        <input type="text" class="inputText" name="reference" required>
                        <span class="floating-label">Reference</span>
                    </div>
                    <div class="referencenote">
                        <p>
                            Appear on recipient's statement.
                        </p>
                    </div>
                </div>
                <br>
                <div class="note">
                    Note: Transfer to same category without processing fee
                    <%
                        try(Connection connection = DBConnection.getConnection()){
                            String select = "SELECT p.id, c1.currency AS cid_currency, c2.currency AS c_id_currency, p.value, p.fee FROM proccessing p " +
                                    "INNER JOIN currency c1 ON p.cid = c1.id " +
                                    "INNER JOIN currency c2 ON p.c_id = c2.id";
                            try(PreparedStatement ps = connection.prepareStatement(select)) {
                                ResultSet rs = ps.executeQuery();
                                while (rs.next()) {
                    %>
                    <br>1 <%=rs.getString("cid_currency")%> - <%=rs.getString("value")%> <%=rs.getString("c_id_currency")%> | Processing Fee : <%=rs.getString("fee")%>%
                    <%
                                }
                                ps.close();
                                connection.close();

                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    %>
                    <br>
                    <%
                        try(Connection connection = DBConnection.getConnection()){
                            String select = "SELECT c.name,c.discount FROM category c JOIN account a ON c.id = a.category WHERE a.userId=?";
                            try(PreparedStatement ps = connection.prepareStatement(select)) {
                                ps.setInt(1, user_id);
                                ResultSet rs = ps.executeQuery();
                                while (rs.next()) {
                    %>
                    <br>Your member is <%=rs.getString("name")%> have <%=rs.getInt("discount")%>% for Processing Fee
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
                <div class="button-container">
                    <button class="transfer-button">
                        Transfer
                    </button>
                </div>
            </div>
        </form>


    </main>   
</body>
</html>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script>
    function add() {
        var x = $("#currency").val();
        var z = "#amount_reference_" + x;
        var y = $(z).css("display");
        $("#currencynote").css("color","red");
        if (y === "none") {
            $.ajax({
                type: 'GET',
                url: 'add',
                data: {id: x,check: 1},
                success : function(result){
                    if(result === ""){
                        $("#currencynote").html("This currency no balance!");
                    }else{
                        $(z).css("display", "block");
                        $("#currencynote").css("color","green");
                        $(z).html(result);
                        $("#currencynote").html("Currency added successfully!");
                    }

                }
            })

        } else {
            $("#currencynote").html("This currency is already added!");
        }
    }
    function remove(){
        var x = $("#currency").val();
        var z = "#amount_reference_" + x;
        var y = $(z).css("display");
        $("#currencynote").css("color","red");
        if (y === "none") {
            $("#currencynote").html("This currency haven't add!");
        } else {
            $.ajax({
                type: 'GET',
                url: 'add',
                data: {id: x,check: 2},
                success : function(result){
                    $(z).css("display", "none");
                    $("#currencynote").css("color","green");
                    $(z).empty();
                    $("#currencynote").html("Currency remove successfully!");
                    let y = result.split(" ");
                    for(let i = 0; i < y.length - 1; i++){
                        if(y[i] !== " " && y[i] !== ""){
                            checkamount(y[i]);
                        }

                    }

                }
            })


        }
    }
    function checkamount(id){
        var amountnote = "#amountnote_"+id;
        var value_ = "#"+id;
        $(amountnote).empty();
        var to = $("#to").val();

        var value = $(value_).val();
        if(value == ""){
            value = 0;
        }
        $.ajax({
            type: 'GET',
            url: 'currency_c',
            data: {id: id,to: to,value: value},
            success : function(result){
                $(amountnote).html(result);
                received(to);
            }
        })
    }
    function received(id){

        $("#received_note").empty();
        $.ajax({
            type: 'GET',
            url: 'received',
            data: {id: id},
            success : function(result){
                $("#received_note").html(result);
            }
        })
    }
    function change_to(){
        var to = $("#to").val();
        $.ajax({
            type: 'GET',
            url: 'add',
            data: {id: to,check: 3},
            success : function(result){
                let y = result.split(" ");

                for(let i = 0; i < y.length; i++){
                    if(y[i] !== " " && y[i] !== ""){
                        console.log(y[i]);
                        checkamount(parseInt(y[i]));
                    }

                }

            }
        })
    }


</script>