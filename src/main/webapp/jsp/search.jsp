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
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Before Transfer</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Mystery+Quest&display=swap" rel="stylesheet">
    <link rel="icon" href="icon/logo.png" type="image/x-icon">
    <link rel="stylesheet" href="CSS/BeforeTransfer.css">
    <link rel="stylesheet" href="CSS/nav.css">
</head>
<body>
    <header class="header">
        <jsp:include page="header_home.jsp" />
    </header>
    <main class="container">
        <input type="button" class="Proceed-button" style="margin-right: 100%" onclick="window.location.href = 'Home'" value="Back">
        <h1>Transfer</h1>
        <div>
            <div class="to-container">
                <div class="to">To</div>
                <div class="toaccountnumber" id="id"> account number</div>
            </div>
            <div class="searchby">
                Search by
            </div>
            <div class="search-container">
                <div class="did-floating-label-content input-group">
                    <div class="input-group-text">+</div>
                    <input class="did-floating-input" type="text" id="phone" placeholder=" ">
                    <label class="did-floating-label">Phone Number</label>
                </div>
                <div class="did-floating-label-content">
                    <input type="text" class="did-floating-input" id="name" placeholder=" ">
                    <label class="did-floating-label">Name</label>
                </div>
            </div>
            <div class="favourite-search">
                <button class="favourite-button" onclick="favourite()">Favourite</button>
                <div style="color: white" id="msg">Search name</div>
                <button class="search-button" onclick="search()">Search</button>
            </div>
            <div class="favourite-list">
                <table>
                    <thead>
                    <tr>
                        <th>Account Number</th>
                        <th>Full Name</th>
                        <th>Phone Number</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody id="list">


                    </tbody>
                </table>

            </div>
            <form method="post" action="transfer">
                <input type="hidden" name="id" id="id_to">
                <button class="Proceed-button submit">Proceed</button>
            </form>
        </div>
    </main>
</body>
</html>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script>
    function select(id){
        msg(1);
        $.ajax({
            type: 'GET',
            url: 'select',
            data: {id: id},
            success : function(result){
                $("#id").empty();
                $("#id").html(result);
                if(result === ""){
                    msg(4);
                }else{
                    $("#id_to").val(id);
                    msg(5);
                }

            }
        })
    }
    function add(id){
        msg(1);
        $.ajax({
            type: 'GET',
            url: 'add_f',
            data: {id: id},
            success : function(result){
                    search();

                $("#msg").empty();
                $("#msg").html(result);

            }
        })
    }
    function search(){
        var name = $("#name").val();
        var phone = $("#phone").val();
        $("#msg").empty();
        $("#list").empty();
        msg(1);
        if(name === "" && phone === ""){
            $("#msg").html("Please fill in name and phone");
        }else {
            $.ajax({
                type: 'GET',
                url: 'search_user',
                data: { name: name,phone: phone},
                success : function(result){
                    $("#list").html(result);
                    msg(3);
                }
            })
        }

    }
    function delete_f(id){
        msg(1);
        $.ajax({
            type: 'GET',
            url: 'delete_f',
            data: { id: id},
            success : function(result){
                favourite();
                $("#msg").empty();
                $("#msg").html(result);


            }
        })
    }
    function favourite(){
        $("#list").empty();
        msg(1);
        $.ajax({
            type: 'GET',
            url: 'favourite',
            data: { code: 1},
            success : function(result){
                $("#list").html(result);
                msg(2);
            }
        })
    }
    function msg(id){
        $("#msg").empty();
        if(id == 1){
            $("#msg").html("Waiting....");
        }else if(id == 2){
            $("#msg").html("Favourite List");
        }else if(id == 3){
            $("#msg").html("Search List");
        }else if(id == 4){
            $("#msg").html("Select Failed");
        }else if(id == 5){
            $("#msg").html("Select Successfully");
        }

    }
</script>

