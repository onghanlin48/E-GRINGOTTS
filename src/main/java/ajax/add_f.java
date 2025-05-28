package ajax;

import data.Account;
import db.DBConnection;

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
import java.sql.SQLException;

@WebServlet("/add_f")
public class add_f extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));
        Account account = (Account) session.getAttribute("account");
        int id_f = account.getUserId();

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO favourite(user_id,favourite) VALUES(?,?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1,id_f);
                statement.setInt(2,id);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    out.println("Add to Favourite Successfully");
                } else {
                    out.println("Add to Favourite Failed");
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
