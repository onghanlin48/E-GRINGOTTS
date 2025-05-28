
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WinZard_Bank</title>
    <link rel="icon" href="icon/logo.png" type="image/x-icon">
    <link rel="stylesheet" href="CSS/login2.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Mystery+Quest&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="header.jsp" />
<main>
    <section class="form">
        <form method="post" action="home">
            <h1>Welcome back</h1>
            <input type="text" name="username" placeholder="Username" required>
            <input type="password" name="password" placeholder="Password" required>
            <a href="forgot" style="width: 100%">forgot password ?</a>
            <input class="submit" type="submit" value="Login">
            <br><a href="register">I don't have an account</a>
        </form>

    </section>
</main>

</body>
</html>