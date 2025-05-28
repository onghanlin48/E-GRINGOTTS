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

@WebServlet("/member_list")
public class member_list extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM category WHERE id != 4";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String promo = resultSet.getString("discount");

                    out.println("<tr>\n" +
                            "                        <td>"+id+"</td>\n" +
                            "                        <td><input type=\"text\" id=\"name_"+id+"\" value=\""+name+"\"></td>\n" +
                            "                        <td><input type=\"number\" id=\"promo_"+id+"\" value=\""+promo+"\" ></td>\n" +
                            "                        <td><button class=\"name1\" onclick=\"edit("+id+")\">Edit</button><button class=\"name1\" onclick=\"dele("+id+")\">Delete</button></td>\n" +
                            "                    </tr>");

                }
                out.println("<tr>\n" +
                        "                        <td>\n" +
                        "                            ADD NEW\n" +
                        "                        </td>\n" +
                        "                        <td><input type=\"text\" id=\"name_\" placeholder=\"Add Name\"></td>\n" +
                        "                        <td><input type=\"number\" id=\"promo\" placeholder=\"Add Promo\" ></td>\n" +
                        "                        <td><button class=\"name1\" onclick=\"add()\">ADD</button></td>\n" +
                        "                    </tr>");

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