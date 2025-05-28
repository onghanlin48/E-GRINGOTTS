<%@ page import="db.DBConnection" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Debit Card</title>
    <link rel="icon" href="icon/logo.png" type="image/x-icon">
    <link rel="stylesheet" href="CSS/nav.css">
    <link rel="stylesheet" href="CSS/debit.css">
</head>
<body>
<form action="debit" method="post">
    <div class="bigContainer" style="margin-top: 10px">
        <div class="smallContainer" style="height: 90%">

            <div class="container">
                <div class="another">
                    <label>Amount</label><br>
                    <select name="currency">
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
                    <input type="number" placeholder="Amount" name="amount" step="0.01" min="0.01" required><br>
                </div>

                <label>Full Name</label><br>
                <input type="text" placeholder="Name on card" name="name" required><br>
                <label>Card Number</label><br>
                <input type="text" placeholder="0000000000000000" name="cardNum" required><br>
                <label>PIN</label><br>
                <input type="text" placeholder="6 digit number" name="pin" required><br>
            </div>

            <div class="small2">
                <div>
                    <label for="Month">Month</label><br>
                    <select id="month" name="month">
                        <option value="1">January</option>
                        <option value="2">February</option>
                        <option value="3">March</option>
                        <option value="4">April</option>
                        <option value="5">May</option>
                        <option value="6">June</option>
                        <option value="7">July</option>
                        <option value="8">August</option>
                        <option value="9">September</option>
                        <option value="10">October</option>
                        <option value="11">November</option>
                        <option value="12">December</option>
                    </select>
                </div>
                <div>
                    <label>Year</label><br>
                    <input type="text" placeholder="Enter year" name="year" required>
                </div>
                <div>
                    <label for="cvv">CVV</label><br>
                    <input type="text"placeholder="000" id="cvv" name="cvv" required>
                </div>
                <div></div>
            </div>
            <div class="additional">
                <select id="otherOption" name="category">
                    <option value="Food">Food</option>
                    <option value="Shopping">Shopping</option>
                    <option value="Entertainment">Entertainment</option>
                    <option value="Other">Other</option>
                </select>
                <input type="text" placeholder="Specify other option" id="otherInput" style="display: none;">
            </div>
            <button class="btn">Submit Payment</button>

        </div>
    </div>
</form>


</body>
</html>