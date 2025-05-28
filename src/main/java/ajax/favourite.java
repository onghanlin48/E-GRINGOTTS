package ajax;

import data.Account;
import db.DBConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/favourite")
public class favourite extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        Account account = (Account) session.getAttribute("account");
        int id = account.getUserId();

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT  a.userId,a.full_name, a.phone " +
                    "FROM favourite f " +
                    "JOIN account a ON f.favourite = a.userId " +
                    "WHERE f.user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1,id);
                ResultSet resultSet = statement.executeQuery();
                boolean favourite = true;
                while(resultSet.next()) {
                    favourite = false;
                    out.println("<tr>\n" +
                            "                        <td>"+resultSet.getString("userId")+"</td>\n" +
                            "                        <td>"+resultSet.getString("full_name")+"</td>\n" +
                            "                        <td>"+resultSet.getString("phone")+"</td>\n" +
                            "                        <td>\n" +
                            "                            <button class='name1' onclick='delete_f(\""+resultSet.getString("userId")+"\")'>\n" +
                            "                                Delete\n" +
                            "                            </button>\n" +
                            "                            <button class='name1' onclick='select(\""+resultSet.getString("userId")+"\")'>\n" +
                            "                                Select\n" +
                            "                            </button>\n" +
                            "                        </td>\n" +
                            "                    </tr>");
                }
                if(favourite) {
                    out.println("<tr><td colspan=\"4\">You don't have Favourite</td></tr>");
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