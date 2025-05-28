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
import java.sql.SQLException;

@WebServlet("/member_add")
public class member_add extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String name = req.getParameter("name");
        int promo = Integer.parseInt(req.getParameter("promo"));
        String sql;
        String msg;
        sql = "INSERT INTO category (name,discount)VALUES(?,?)";
        msg = "New Category Added Successfully";
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setInt(2, promo);

                int row = statement.executeUpdate();

                if(row > 0) {
                    out.println(msg);
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