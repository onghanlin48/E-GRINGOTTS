package ajax;

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

@WebServlet("/checkemail")
public class checkemail extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM account WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();

                if(resultSet.next()) {
                    out.println("Email already exists!");
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error connecting to the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        out.close();

    }
}