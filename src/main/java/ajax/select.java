package ajax;

import data.Account;
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

@WebServlet("/select")
public class select extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT  userId,full_name, phone FROM account WHERE userId = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1,id);
                ResultSet resultSet = statement.executeQuery();

                if(resultSet.next()) {
                    out.println(resultSet.getInt("userId")+" ( "+resultSet.getString("full_name")+" ) ("+resultSet.getString("phone")+")");
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