package ajax;

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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/received")
public class received extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        check_currency check = (check_currency) session.getAttribute("check_currency");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        int id = Integer.parseInt(req.getParameter("id"));

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM currency WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1,id);
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()) {
                    out.println("Total Transfer :" + check.getTotal() + " " + resultSet.getString("short"));
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