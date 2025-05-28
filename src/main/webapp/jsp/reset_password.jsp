
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=<device-width>, initial-scale=1.0">
    <title>WinZard Bank Reset</title>
    <link rel="stylesheet" href="CSS/Signup.css">
    <link rel="icon" href="icon/logo.png" type="image/x-icon">

</head>
<body>
<jsp:include page="header.jsp" />

<main>
    <section class="container">
        <h1>Reset Password</h1>
        <form action="change_password" method="post" class="form" style="margin-top: -4px">
            <div class="column">
                <div class="input-box">
                    <label>Password</label>
                    <input type="password" name="password" onkeyup="checkpassword()" id="password" placeholder="Enter password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" title="Must contain at least one  number and one uppercase and lowercase letter, and at least 8 or more characters" required>
                </div>
                <div class="input-box">
                    <Label>Confirm password</Label>
                    <input type="password" onkeyup="checkpassword()" id="c_password" placeholder="Confirm password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" title="Must contain at least one  number and one uppercase and lowercase letter, and at least 8 or more characters" required>
                </div>
            </div>
            <div id="checkpassword" style="color: red">

            </div>
            <div id="button">

            </div>
        </form>
    </section>
</main>
</body>
</html>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script>
    var button = "<button id=\"button\">Submit</button>";

    function checkpassword(){
        var password = $("#password").val();
        var c_password = $("#c_password").val();

        if(password != c_password){
            $("#button").empty();
            $("#checkpassword").html("Password and Confirm password must be same!");
        }else{
            $("#button").html(button);
            $("#checkpassword").empty();
        }
        check();


    }

    function  check(){

        var password = document.getElementById("checkpassword").textContent;
        if(password !== ""){
            document.getElementById("button").innerHTML = "";
        }else{
            document.getElementById("button").innerHTML = button;
        }

    }

</script>