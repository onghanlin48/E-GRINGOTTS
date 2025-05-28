<%@ page import="db.DBConnection" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="data.Account" %>
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
    <title>Spending Analysis    </title>
    <link rel="icon" href="icon/logo.png" type="image/x-icon">
    <link rel="stylesheet" href="CSS/nav.css">
    <link rel="stylesheet" href="CSS/analysis.css">
</head>
<body>
<header>
    <jsp:include page="header_home.jsp" />
</header>
    <div class="bigContainer">
        <div class="smallContainer">
            <div class="leftContainer">
                <div class="filter">
                    <h2>Spending Analysis</h2>
                </div>
                <form method="get" action="analysis">
                    <div class="filter">

                        <input type="text" id="monthSelect" name="y" value="<%=request.getParameter("y")%>">
                        <select name="m">
                            <option value="<%=request.getParameter("m")%>"><%=request.getParameter("m")%></option>
                            <%
                                for (int i = 1; i <= 12; i++) {
                                    if(i != Integer.parseInt(request.getParameter("m"))){

                            %>
                            <option value="<%=i%>"><%=i%></option>
                            <%
                                    }
                                }
                            %>
                        </select>
                        <select name="c" id="currency">
                            <%
                                try(Connection connection = DBConnection.getConnection()){
                                    String select = "select * from currency where id="+request.getParameter("c")+"";
                                    try(PreparedStatement ps = connection.prepareStatement(select)) {
                                        ResultSet rs = ps.executeQuery();
                                        if (rs.next()) {

                            %>
                            <option value="<%=rs.getString("id")%>"><%=rs.getString("currency")%> (<%=rs.getString("short")%>)</option>
                            <%

                                        }
                                        ps.close();
                                        connection.close();
                                    }
                                } catch (SQLException | ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            %>
                            <%
                                try(Connection connection = DBConnection.getConnection()){
                                    String select = "select * from currency";
                                    try(PreparedStatement ps = connection.prepareStatement(select)) {
                                        ResultSet rs = ps.executeQuery();
                                        while (rs.next()) {
                                            if(rs.getInt("id") != Integer.parseInt(request.getParameter("c"))){
                            %>
                            <option value="<%=rs.getString("id")%>"><%=rs.getString("currency")%> (<%=rs.getString("short")%>)</option>
                            <%
                                            }
                                        }
                                        ps.close();
                                        connection.close();
                                    }
                                } catch (SQLException | ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            %>
                        </select>
                        <select name="p" id="p">
                            <%HttpSession session1 = request.getSession();
                                Account account = (Account) session1.getAttribute("account");
                                if(request.getParameter("p").isEmpty()){
                            %>
                            <option value="">All</option>
                            <%


                            }else{


                                try(Connection connection = DBConnection.getConnection()){

                                    String select = "select * from history where user LIKE '%"+request.getParameter("p")+"%' AND form="+account.getUserId()+" GROUP BY user";
                                    try(PreparedStatement ps = connection.prepareStatement(select)) {
                                        ResultSet rs = ps.executeQuery();
                                        if (rs.next()) {

                            %>
                            <option value="<%=rs.getString("user")%>"><%=rs.getString("user")%></option>
                            <option value="">All</option>
                            <%

                                            }
                                            ps.close();
                                            connection.close();
                                        }
                                    } catch (SQLException | ClassNotFoundException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            %>
                            <%
                                try(Connection connection = DBConnection.getConnection()){
                                    String select = "select * from history where form="+account.getUserId() +" GROUP BY user";
                                    try(PreparedStatement ps = connection.prepareStatement(select)) {
                                        ResultSet rs = ps.executeQuery();
                                        while (rs.next()) {
                                            if(!(rs.getString("user").equals(request.getParameter("p")))){
                            %>
                            <option value="<%=rs.getString("user")%>"><%=rs.getString("user")%></option>
                            <%
                                            }
                                        }
                                        ps.close();
                                        connection.close();
                                    }
                                } catch (SQLException | ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            %>
                        </select>
                        <input type="submit" value="submit" style="width: auto">
                    </div>
                </form>

            </div>
            <div>
                <canvas id="myChart" width="350" height="350"></canvas>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <script>
        const ctx = document.getElementById('myChart');

        // Data for the chart
        const dataValues = [<%=request.getAttribute("total")%>];
        const totalValue = dataValues.reduce((a, b) => a + b, 0); // Calculate total

        // Define the custom plugin for center text
        const centerTextPlugin = {
            id: 'centerText',
            afterDatasetsDraw(chart) {
                const { ctx, chartArea: { width, height } } = chart;
                ctx.save();
                
                // Adjust font and style for the first text line
                ctx.font = '20px Mystery Quest';
                ctx.fillStyle = 'black';
                ctx.textAlign = 'center';
                ctx.textBaseline = 'middle';
                
                // Adjust the content and position of the first text line
                ctx.fillText('Total spending', width / 2, height / 2 + 50);
                
                // Adjust font and style for the second text line
                ctx.font = '20px Mystery Quest';
                
                // Adjust the content and position of the second text line
                ctx.fillText(`${totalValue.toFixed(2)}<%=request.getAttribute("s")%>`, width / 2, height / 2 + 70);
                
                ctx.restore();
            }
        };

        // Register the custom plugin
        Chart.register(centerTextPlugin);

        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: [<%=request.getAttribute("ca")%>],
                datasets: [{
                    data: dataValues,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.4)',
                        'rgba(54, 162, 235, 0.5)',
                        'rgba(255, 206, 86, 0.5)',
                        'rgba(75, 192, 192, 0.5)',
                        'rgba(153, 102, 255, 0.5)',
                        'rgba(255, 159, 64, 0.5)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const label = context.label || '';
                                const value = context.raw || 0;
                                const percentage = ((value / totalValue) * 100).toFixed(2); // Dynamic percentage calculation
                                return `${label}: ${value}<%=request.getAttribute("s")%> (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        });
    </script>
</body>
</html>
