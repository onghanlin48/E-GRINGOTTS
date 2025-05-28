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

@WebServlet("/amount_c")
public class amount_c extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));
        int value = Integer.parseInt(req.getParameter("value"));
        String sql = "Select * from amount a JOIN currency c ON a.cid = c.id where a.userid = " + id + " and a.cid = " + value;

        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    out.println(rs.getDouble("amount") + " " + rs.getString("short"));
                }
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