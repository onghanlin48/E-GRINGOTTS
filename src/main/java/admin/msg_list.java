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

@WebServlet("/msg_list")
public class msg_list extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM image";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String path = resultSet.getString("path");

                    out.println("<tr>\n" +
                            "                        <td><img src=\"msg//"+path+"\" style=\"width: 50%\"></td>\n" +
                            "                        <td><button class=\"name1\" type=\"button\" onclick=\"dele("+id+")\">Delete</button></td>\n" +
                            "                    </tr>");

                }
                out.println(" <tr>\n" +
                        "                            <td><input type=\"file\" id=\"file\"  name=\"file\" accept=\"image/*\" ></td>\n" +
                        "                            <td><button class=\"name1\" type=\"submit\">Submit</button></td>\n" +
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