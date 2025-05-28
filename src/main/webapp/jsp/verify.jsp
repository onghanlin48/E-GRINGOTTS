<%@ page import="data.resend" %>
<%@ page import="data.Account" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=<device-width>, initial-scale=1.0">
    <title>WinZard Bank Verify</title>
    <link rel="stylesheet" href="CSS/Signup.css">
    <link rel="icon" href="icon/logo.png" type="image/x-icon">
</head>
<body onload="set()">
<jsp:include page="header.jsp" />

<main>
    <section class="container">
        <h1>Verify</h1>
        <%resend send = (resend) request.getAttribute("resend");
            String action = send.getPage();
        %>
        <form action="<%=action%>" method="post" class="form" style="margin-top: -4px">
                <div class="input-box">
                    <%
                        Account account = (Account) request.getAttribute("account");
                        String email = account.getEmail();
                    %>
                    <label>Verify Code ( <%= email %> )</label>
                    <input type="text" name="code" id="code" pattern="[0-9]{6}" title="6 digits number"  placeholder="Enter Verify code" required>
                    <br><div id="check" style="color: red">
                        <%

                            int left = send.getResend();
                            String Msg;
                            send.setResend(left);

                            if(left == 0){
                                Msg = "Attempt Finished! Please Resend!";
                            }else{
                                Msg = "Attempt left : " + left;
                            }
                        %>
                        <%= Msg%>
                    </div>
                </div>


            <button id="button">Verify</button>
            <br>
            <br>
            Don't have received?<a href="#" onclick="resend()" style="color: white">Resend</a>
            <br>
            <br>
            <div style="color: green" id="resend"></div>

        </form>
    </section>
</main>
</body>
</html>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script>
    function resend(){
        $('#check').empty();
        $('#resend').html("Waiting....")
        $.ajax({
            type: 'GET',
            url: 'resend',
            data: { check: "<%=action%>"},
            success : function(result){

                $('#check').html(result);
                $('#resend').html("Verify code resend successfully!")

            }
        })

    }
    function set(){
        $.ajax({
            type: 'GET',
            url: 'set',
            data: { code: "<%=left%>",page: "<%=action%>"},
            success : function(result){

            }
        })

    }

</script>