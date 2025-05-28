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

@WebServlet("/fee_list")
public class fee_list extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT \n" +
                    "    c.currency AS f_currency,\n" +
                    "    cu.currency AS t_currency,\n" +
                    "    p.value AS val,\n" +
                    "    p.fee AS fee,\n" +
                    "    p.id AS id \n" +
                    "FROM \n" +
                    "    proccessing p \n" +
                    "JOIN \n" +
                    "    currency c ON p.cid = c.id \n" +
                    "JOIN \n" +
                    "    currency cu ON p.c_id = cu.id;\n";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String from = resultSet.getString("f_currency");
                    String to = resultSet.getString("t_currency");
                    String value = resultSet.getString("val");
                    int fee = resultSet.getInt("fee");

                    out.println(" <tr>\n" +
                            "                        <td>"+from+"</td>\n" +
                            "                        <td>"+to+"</td>\n" +
                            "                        <td>"+value+"</td>\n" +
                            "                        <td><input type=\"number\" id=\"fee_"+id+"\" value=\""+fee+"\"></td>\n" +
                            "                        <td><button class=\"name1\" onclick=\"edit("+id+")\">Edit</button></td>\n" +
                            "                    </tr>");

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