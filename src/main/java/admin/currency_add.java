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

@WebServlet("/currency_add")
public class currency_add extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String name = req.getParameter("name");
        double value = Double.parseDouble(req.getParameter("value"));
        String s = req.getParameter("s");
        s = s.toUpperCase();
        String sql;
        String msg;
        sql = "INSERT INTO currency (currency,short,v)VALUES(?,?,?)";
        String insert_pro = "INSERT INTO proccessing (cid,c_id,value,fee)VALUES(?,?,?,?)";
        String admin = "INSERT INTO admin (currency,balance) VALUES(?,?)";
        String user = "INSERT INTO amount (cid,amount,userid) VALUES(?,?,?)";
        String s_user = "SELECT userId FROM account WHERE userId != 1";
        String select = "SELECT * FROM currency WHERE id !=?";
        msg = name+" added successfully";
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, name);
                statement.setString(2, s);
                statement.setDouble(3, value);

                int row = statement.executeUpdate();

                if(row > 0) {
                    ResultSet rs = statement.getGeneratedKeys();
                    rs.next();
                    int id = rs.getInt(1);
                    try(PreparedStatement ps = connection.prepareStatement(insert_pro)) {
                        try(PreparedStatement ps1 = connection.prepareStatement(select)) {
                            ps1.setInt(1, id);
                            ResultSet rs1 = ps1.executeQuery();
                            while(rs1.next()) {
                                ps.setInt(1, id);
                                ps.setInt(2, rs1.getInt("id"));
                                ps.setDouble(3, rs1.getDouble("v")/value);
                                ps.setInt(4, 1);
                                ps.executeUpdate();

                                ps.setInt(1, rs1.getInt("id"));
                                ps.setInt(2, id);
                                ps.setDouble(3, value/rs1.getDouble("v"));
                                ps.setInt(4, 1);
                                ps.executeUpdate();
                            }
                            ps1.close();
                            ps.close();
                        }


                    }
                    try(PreparedStatement ps = connection.prepareStatement(user)) {
                        try(PreparedStatement ps1 = connection.prepareStatement(s_user)) {
                            ResultSet rs1 = ps1.executeQuery();
                            while(rs1.next()) {
                                ps.setInt(1, id);
                                ps.setInt(2,0);
                                ps.setDouble(3, rs1.getDouble("userId"));
                                ps.executeUpdate();
                            }
                            ps1.close();
                            ps.close();
                        }
                    }
                    try(PreparedStatement ps = connection.prepareStatement(admin)) {
                        ps.setInt(1, id);
                        ps.setDouble(2, 0);
                        ps.executeUpdate();

                        out.println(msg);
                    }
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