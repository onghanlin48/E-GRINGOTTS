package ajax;

import data.Account;
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
import java.sql.SQLException;

@WebServlet("/delete_f")
public class delete_f extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "DELETE FROM favourite WHERE favourite = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1,id);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    out.println("Delete Successful");
                } else {
                    out.println("Delete Failed");
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
