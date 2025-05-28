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

@WebServlet("/currency_edit")
public class currency_edit extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        double value = Double.parseDouble(req.getParameter("value"));
        String s = req.getParameter("s");
        s = s.toUpperCase();
        String sql;
        String msg;
        sql = "UPDATE currency set currency='"+name+"',short='"+s+"',v="+value+" where id="+id;
        String update = "UPDATE proccessing SET value=? WHERE cid=? AND c_id =?";
        String select = "SELECT * FROM currency WHERE id !="+id;
        msg = name+" updated successfully";
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                int row = statement.executeUpdate();

                if(row > 0) {
                    try(PreparedStatement preparedStatement = connection.prepareStatement(select)) {
                        ResultSet resultSet = preparedStatement.executeQuery();
                        while(resultSet.next()) {
                            try(PreparedStatement preparedStatement1 = connection.prepareStatement(update)) {
                                preparedStatement1.setDouble(1, resultSet.getDouble("v")/value);
                                preparedStatement1.setInt(2, id);
                                preparedStatement1.setInt(3, resultSet.getInt("id"));
                                preparedStatement1.executeUpdate();

                                preparedStatement1.setDouble(1, value/resultSet.getDouble("v"));
                                preparedStatement1.setInt(2, resultSet.getInt("id"));
                                preparedStatement1.setInt(3, id);
                                preparedStatement1.executeUpdate();
                            }
                        }
                    }
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