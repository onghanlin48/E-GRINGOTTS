package ajax;

import data.Account;
import db.DBConnection;
import user.Transaction;
import user.TransactionHistory;

import javax.servlet.RequestDispatcher;
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
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@WebServlet("/history_f")
public class history_f extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        TransactionHistory history = new TransactionHistory();
        Account account = (Account) session.getAttribute("account");
        int id = account.getUserId();

        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String c = "%" +req.getParameter("c")+"%";
        String m ="%" + req.getParameter("m")+"%";
        int cu = Integer.parseInt(req.getParameter("cu"));

        try (Connection connection = DBConnection.getConnection()) {
            String sql;
            if(cu == 0){
                sql = "SELECT * FROM history h JOIN account a ON a.userId = h.receive JOIN currency c ON c.id = h.currency WHERE h.form = ? AND h.category LIKE ? AND h.user LIKE ? AND h.date BETWEEN ? AND ?";
            }else{
                sql = "SELECT * FROM history h JOIN account a ON a.userId = h.receive JOIN currency c ON c.id = h.currency WHERE h.form = ? AND h.category LIKE ? AND h.user LIKE ? AND h.currency = ? AND h.date BETWEEN ? AND ?";
            }
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                if(cu == 0){
                    statement.setInt(1,id);
                    statement.setString(2,c);
                    statement.setString(3,m);
                    statement.setString(4,from);
                    statement.setString(5,to);
                }else{
                    statement.setInt(1,id);
                    statement.setString(2,c);
                    statement.setString(3,m);
                    statement.setInt(4,cu);
                    statement.setString(5,from);
                    statement.setString(6,to);
                }

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String inv = resultSet.getString("invoice");
                    String toUserId = resultSet.getInt  ("userId") + "("+resultSet.getString("full_name")+")"+"("+resultSet.getString("phone")+")";
                    double amount = resultSet.getDouble("amount");
                    String currency = resultSet.getString("short");
                    String date = String.valueOf(resultSet.getDate("date"));
                    String category = resultSet.getString("category");
                    String reference = resultSet.getString("reference");
                    int status = resultSet.getInt("status");
                    String method = resultSet.getString("user");

                    Transaction transaction = new Transaction(inv,toUserId, amount, currency, date, category, reference, status,method);
                    history.addTransaction(transaction);   }

                statement.close();
                connection.close();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error connecting to the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<Transaction> userTransactions = history.getTransactionHistory();
        for (Transaction transaction : userTransactions) {
            out.println(transaction);
        }
    }


}