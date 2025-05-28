<%@ page import="db.DBConnection" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.time.YearMonth" %>
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
    <title>WinZard Bank Home</title>
        <link rel="icon" href="icon/logo.png" type="image/x-icon">
    <link rel="stylesheet" href="CSS/nav.css">
    <link rel="stylesheet" href="CSS/home.css">
</head>
<body>
    <header>
        <jsp:include page="header_home.jsp" />
    </header>
     
    <div class="container">
        <div class="slider-wrapper">
            <div class="slider">
                <%
                    int x = 0;
                    try(Connection connection = DBConnection.getConnection()){
                        String select = "select * from image ORDER BY id desc";
                        try(PreparedStatement ps = connection.prepareStatement(select)) {
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                x++;
                %>
                <img id="slide1" src="msg///<%=rs.getString("path")%>">
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
            <div class="slider-nav">
                <%
                    for (int i = 0; i < x; i++) {


                %>
                <a href="#slide<%=i%>"></a>
                <%
                    }
                %>

            </div>
        </div>

    </div>
<%
    YearMonth currentYearMonth = YearMonth.now();
    int year = currentYearMonth.getYear();
    int month = currentYearMonth.getMonthValue();
%>
    <div class="option">
        <button><a href="Search">Quick Transfer</a></button>
        <button><a href="convert_c">Convert Currency</a></button>
        <button><a href="History">Transaction history</a></button>
        <button><a href="analysis?y=<%=year%>&m=<%=month%>&c=1&p=">Spending analysis</a></button>
        <button><a href="Balance">Account balance</a></button>
    </div>
    
</body>
</html>

