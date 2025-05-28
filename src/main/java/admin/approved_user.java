package admin;

import db.DBConnection;

import javax.servlet.RequestDispatcher;
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

@WebServlet("/approved_user")
public class approved_user extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));
        int check = Integer.parseInt(req.getParameter("check"));
        String sql;
        String msg;
        if(check==1) {
            sql = "UPDATE account set status=2 where userId="+id;
            msg = "User ID " + id + " is approved";
        } else {
            sql = "UPDATE account set status=3 where userId="+id;
            msg = "User ID " + id + " is Blocked";
        }
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

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