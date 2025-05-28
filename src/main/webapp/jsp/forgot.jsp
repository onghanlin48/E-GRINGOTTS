<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=<device-width>, initial-scale=1.0">
  <title>WinZard Bank Forgot</title>
  <link rel="stylesheet" href="CSS/Signup.css">
  <link rel="icon" href="icon/logo.png" type="image/x-icon">
</head>
<body>
<jsp:include page="header.jsp" />

<main>
  <section class="container">
    <h1>Forgot Password</h1>
    <form action="forgot_verify" method="post" class="form" style="margin-top: -4px">
      <div class="input-box">
        <label for="username">Username</label>
        <input type="text" name="username" id="username" placeholder="Enter Your username" required>
      </div>
      <div class="input-box">
        <label for="email">Email</label>
        <input type="text" name="email" id="email"  placeholder="Enter Your email" pattern="[a-z0-9._%+\-]+@[a-z0-9.\-]+\.[a-z]{2,}$" required>
      </div>
      <button id="button">Reset</button>
      <br><br><a href="login" style="color: white">I reminder password</a>
    </form>
  </section>
</main>
</body>
</html>
