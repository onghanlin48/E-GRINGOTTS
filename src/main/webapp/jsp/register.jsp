
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=<device-width>, initial-scale=1.0">
    <title>WinZard Bank Register</title>
    <link rel="stylesheet" href="CSS/Signup.css">
    <link rel="icon" href="icon/logo.png" type="image/x-icon">

</head>
<body>
<jsp:include page="header.jsp" />

<main>
    <section class="container">
        <h1>Register a account now !</h1>
        <form action="regis" method="post" class="form" style="margin-top: -4px">
            <div class="column">
                <div class="input-box">
                    <label>UserName</label>
                    <input type="text" name="username" id="username" onkeyup="checkusername()" placeholder="Enter a username" required>
                    <div id="check" style="color: red">
                    </div>
                </div>
                <div class="input-box">
                    <label>Email Address</label>
                    <input type="email" name="email" id="email" onkeyup="checkemail()" placeholder="Enter your email address" pattern="[a-z0-9._%+\-]+@[a-z0-9.\-]+\.[a-z]{2,}$" required>
                    <div id="checkemail" style="color: red">
                    </div>
                </div>
            </div>
            <div class="column">
                <div class="input-box">
                    <label>Full Name</label>
                    <input type="text" name="full_name" placeholder="Enter your Full name" required>
                </div>
                <div class="input-box">
                    <label>Address</label>
                    <input type="text" name="address" placeholder="Enter your address" required>
                </div>
            </div>

            <div class="column">
                <div class="input-box">
                    <label>Phone Number</label>
                    <input type="tel" name="phone" id="phone" onkeyup="checkphone()" placeholder="Enter your phone number (01xxxxxxxx)" pattern="01\d{8,12}" title="Please enter a valid phone number starting with '01' and containing 10 to 14 digits" required>
                    <div id="checkphone" style="color: red">

                    </div>

                </div>
                <div class="input-box">
                    <Label>Birth Date</Label>
                    <input type="date" id="dateInput" name="date" placeholder="Enter your birth date" required>
                </div>
            </div>
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
            <br>
            <br>
            <a href="login" style="color: white">I already have an account</a>

        </form>
    </section>
</main>
</body>
</html>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script>
    var button = "<button id=\"button\">Submit</button>";

    function checkusername(){

        var username = $("#username").val();

        $("#check").empty();

        $.ajax({
            type: 'GET',
            url: 'checkusername',
            data: { username:  username  },
            success : function(result){
                $('#check').html(result);
                if(!(username == "")){
                    if(result == ""){
                        $("#button").html(button);
                    }else{
                        $("#button").empty();
                    }
                }else{
                    $("#button").empty();
                }
                check();
            }
        })

    }
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
    function checkphone(){

        var phone = $("#phone").val();

        $("#checkphone").empty();

        $.ajax({
            type: 'GET',
            url: 'checkphone',
            data: { phone:  phone  },
            success : function(result){
                $('#checkphone').html(result);
                if(!(phone == "")){
                    if(result == ""){
                        $("#button").html(button);
                    }else{
                        $("#button").empty();
                    }
                }else{
                    $("#button").empty();
                }
                check();

            }
        })

    }
    function checkemail(){

        var email = $("#email").val();

        $("#checkemail").empty();

        $.ajax({
            type: 'GET',
            url: 'checkemail',
            data: { email:  email  },
            success : function(result){
                $('#checkemail').html(result);
                if(!(phone == "")){
                    if(result == ""){
                        $("#button").html(button);
                    }else{
                        $("#button").empty();
                    }
                }else{
                    $("#button").empty();
                }
                check();

            }
        })


    }
    function  check(){
        var email = document.getElementById("checkemail").textContent;
        var username = document.getElementById("check").textContent;
        var phone = document.getElementById("phone").textContent;
        var password = document.getElementById("checkpassword").textContent;
        if(email !== "" || username !== "" || phone !== "" || password !== ""){
            document.getElementById("button").innerHTML = "";
        }else{
            document.getElementById("button").innerHTML = button;
        }

    }

</script>