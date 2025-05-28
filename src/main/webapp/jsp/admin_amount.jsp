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
    <title>Add Amount</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Mystery+Quest&display=swap" rel="stylesheet">
    <link rel="icon" href="icon/logo.png" type="image/x-icon">
    <link rel="stylesheet" href="CSS/BeforeTransfer.css">
    <link rel="stylesheet" href="CSS/nav.css">
</head>
<body onload="load_s();">
    <header class="header">
        <jsp:include page="header_admin.jsp" />
    </header>
    <main class="container" style="max-width: 1200px">
        <input type="button" class="Proceed-button" style="margin-right: 100%" onclick="window.location.href = 'admin'" value="Back">
        <h1>Add Amount</h1>
        <div>
            <div class="favourite-list" style="max-height: 500px;width: 100%">
                <table>
                    <thead>
                    <tr>
                        <th>Account Number</th>
                        <th>Full Name</th>
                        <th>Phone Number</th>
                        <th>Balance</th>
                        <th>Currency</th>
                        <th>Add</th>
                    </tr>
                    </thead>
                    <tbody id="list">
                    </tbody>
                </table>

            </div>
        </div>
    </main>
</body>
</html>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script>
    function load_s(){
        $.ajax({
            type: 'GET',
            url: 'amount_list',
            data: {id:'1'},
            success : function(result){
                $("#list").html(result);
            }
        })
    }
    function change_c(id){
        var name = "#currency_"+id;
        var b = "#balance_"+id;
        var value = $(name).val();
        $.ajax({
            type: 'GET',
            url: 'amount_c',
            data: {id:id,value:value},
            success : function(result){
                $(b).html(result);
            }
        })
    }
    function add(id){
        var name = "#currency_"+id;
        var a = "#amount_"+id;
        var value = $(name).val();
        var amount = $(a).val();
        $.ajax({
            type: 'GET',
            url: 'amount_a',
            data: {id:id,value:value,amount:amount,check:1},
            success : function(result){
                alert(result);
                load_s();
            }
        })
    }
    function deduct(id){
        var name = "#currency_"+id;
        var a = "#amount_"+id;
        var value = $(name).val();
        var amount = $(a).val();
        $.ajax({
            type: 'GET',
            url: 'amount_a',
            data: {id:id,value:value,amount:amount,check:2},
            success : function(result){
                alert(result);
                load_s();
            }
        })
    }

</script>