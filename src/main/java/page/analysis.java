package page;

import data.Account;
import db.DBConnection;
import user.Transaction;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;

@WebServlet("/analysis")
public class analysis extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        int year = Integer.parseInt(req.getParameter("y"));
        int month = Integer.parseInt(req.getParameter("m"));
        int c = Integer.parseInt(req.getParameter("c"));
        String p ="%"+req.getParameter("p")+"%";
        Account account = (Account) session.getAttribute("account");
        int id = account.getUserId();
        StringBuilder total = new StringBuilder();
        StringBuilder ca = new StringBuilder();
        String short_c = "";

        try (Connection connection = DBConnection.getConnection()) {
            String sql;

            sql = "SELECT SUM(h.amount) AS amount,h.category,h.currency,c.short FROM history h JOIN currency c ON c.id = h.currency WHERE h.form=? AND h.category != \"Change Currency\" AND YEAR(h.date) = ? AND MONTH(h.date) = ? AND h.status = 2 AND h.currency = ? AND h.  user LIKE ? GROUP BY h.category";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {


                    statement.setInt(1,id);
                    statement.setInt(2,year);
                    statement.setInt(3,month);
                    statement.setInt(4,c);
                    statement.setString(5,p);

                ResultSet resultSet = statement.executeQuery();
                int x = 0;
                while (resultSet.next()) {
                    if(x != 0){
                        total.append(',');
                        ca.append(',');
                    }
                    x++;
                     total.append(df.format(resultSet.getDouble("amount")));
                     ca.append("'").append(resultSet.getString("category")).append("'");
                     short_c = resultSet.getString("short");
                }

                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        req.setAttribute("total",String.valueOf(total));
        req.setAttribute("ca",String.valueOf(ca));
        req.setAttribute("s",short_c);
        RequestDispatcher rd = req.getRequestDispatcher("spendingAnalysis.jsp");
        rd.forward(req, resp);
    }


}