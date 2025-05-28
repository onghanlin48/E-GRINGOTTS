package ajax;

import data.Account;
import db.DBConnection;

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

@WebServlet("/amount")
public class amount extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        Account account = (Account) session.getAttribute("account");
        check_amount check = (check_amount) session.getAttribute("check_amount");
        int id = account.getUserId();
        int c = Integer.parseInt(req.getParameter("id"));

        try (Connection connection = DBConnection.getConnection()) {


            String sql = "SELECT * FROM amount a JOIN currency c ON c.id = a.cid JOIN account ac ON ac.userId = a.userid JOIN category ca ON ca.id = ac.category WHERE a.userid=? AND a.cid=?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1,id);
                statement.setInt(2,c);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    if(resultSet.getDouble("amount") <= 0){
                        out.println("You don't have enough balance");
                    }else{

                        out.println("You have "+resultSet.getDouble("amount") + resultSet.getString("short"));
                    }
                    check.setAmount(resultSet.getDouble("amount"));
                    check.setDis(resultSet.getDouble("discount")/100);
                    check.setS(resultSet.getString("short"));
                    session.setAttribute("check", check);
                }

                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


}