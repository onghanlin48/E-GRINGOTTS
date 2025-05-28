package admin;

import ajax.check_currency;
import db.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static function.function.generateInvoiceNumber;

@WebServlet("/amount_a")
public class amount_a extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));
        int check = Integer.parseInt(req.getParameter("check"));
        double amount = Double.parseDouble(req.getParameter("amount"));
        int value = Integer.parseInt(req.getParameter("value"));
        String sql;
        String msg;
        if(check==1) {
            sql = "UPDATE amount set amount = amount + "+amount+" where userid="+id+" AND cid ="+value;
            msg = "User ID " + id + " amount is add";
        } else {
            sql = "UPDATE amount set amount = amount - "+amount+" where userid="+id+" AND cid ="+value;
            msg = "User ID " + id + " amount is deduct";
        }

        LocalDate currentDate = LocalDate.now();
        String invoiceNumber = generateInvoiceNumber();
        String insertSql = "INSERT INTO history (invoice, form, receive, amount, currency, date, category, reference, status, user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                int row = statement.executeUpdate();

                if(row > 0) {
                    try(PreparedStatement insertStatement = connection.prepareStatement(insertSql)){
                        insertStatement.setString(1, invoiceNumber);
                        insertStatement.setInt(2, id);
                        insertStatement.setInt(3, 1);
                        insertStatement.setDouble(4, amount);
                        insertStatement.setInt(5, value);
                        insertStatement.setDate(6, Date.valueOf(currentDate));
                        insertStatement.setString(7, "Admin");
                        insertStatement.setString(8, "Admin");
                        insertStatement.setInt(9, check);
                        insertStatement.setString(10, "Admin");

                        insertStatement.executeUpdate();
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