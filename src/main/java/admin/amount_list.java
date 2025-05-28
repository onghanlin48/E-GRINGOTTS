package admin;

import db.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/amount_list")
public class amount_list extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM account WHERE userId != 1";
            String amount = "SELECT * FROM amount a JOIN currency c ON a.cid = c.id WHERE a.userid = ? AND cid = 1";
            String currecny = "SELECT * FROM currency";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("userId");
                    String name = resultSet.getString("full_name");
                    String phone = resultSet.getString("phone");

                    out.println("<tr>\n" +
                            "                        <td>"+id+"</td>\n" +
                            "                        <td>"+name+"</td>\n" +
                            "                        <td>"+phone+"</td>\n" +
                            "                        <td id=\"balance_"+id+"\">\n");
                    try(PreparedStatement statement_amount = connection.prepareStatement(amount)) {
                        statement_amount.setInt(1, id);

                        ResultSet rs = statement_amount.executeQuery();
                        if(rs.next()) {
                            out.println(rs.getDouble("amount") + " " + rs.getString("short"));
                        }
                    }
                    out.println(" </td> <td><select onchange=\"change_c("+id+")\" id=\"currency_"+id+"\">");
                    try(PreparedStatement statement_currency = connection.prepareStatement(currecny)) {

                        ResultSet rs_currency = statement_currency.executeQuery();
                        while(rs_currency.next()) {
                            out.println("<option value=\""+rs_currency.getInt("id")+"\">"+rs_currency.getString("currency")+"</option>");
                        }
                    }
                    out.println("</select></td><td>");
                    out.println("<input type=\"number\" id=\"amount_"+id+"\" step=\"0.01\" min=\"0.01\">");
                    out.println("<button class=\"name1\" onclick=\"add("+id+")\">ADD</button>");
                    out.println("<button class=\"name1\" onclick=\"deduct("+id+")\">DEDUCT</button>");
                    out.println("</td></tr>\n");
                }

                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error connecting to the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}