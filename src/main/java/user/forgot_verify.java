package user;

import function.JavaMail;
import function.function;
import data.Account;
import data.resend;
import db.DBConnection;

import javax.mail.MessagingException;
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

@WebServlet("/forgot_verify")
public class forgot_verify extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM account WHERE username = ? AND email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, email);
                ResultSet resultSet = statement.executeQuery();

                if(resultSet.next()) {
                    String code_v = function.generateRandomNumber();
                    Account account = new Account();
                    account.setCode(code_v);
                    account.setEmail(email);
                    account.setUsername(username);

                    resend send = new resend();
                    send.setResend(3);
                    send.setPage("reset_password");
                    req.setAttribute("resend", send);
                    req.setAttribute("account", account);
                    session.setAttribute("account", account);
                    JavaMail.sendmail(email,"Username:"+username+"\nCode Verify :"+code_v,"Reset Password Verify");
                    RequestDispatcher rd = req.getRequestDispatcher("verify.jsp");
                    rd.forward(req, resp);
                }else{
                    req.setAttribute("des", "You username and email no match or no exists!");
                    req.setAttribute("page", "login");
                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                    rd.forward(req, resp);
                }
                connection.close();
            } catch (MessagingException e) {
                req.setAttribute("des", "System Error!Please contact admin.");
                req.setAttribute("page", "login");
                RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                rd.forward(req, resp);
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error connecting to the database: " + e.getMessage());
            req.setAttribute("des", "System Error!Please contact admin.");
            req.setAttribute("page", "login");
            RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
            rd.forward(req, resp);
        } catch (ClassNotFoundException e) {
            req.setAttribute("des", "System Error!Please contact admin.");
            req.setAttribute("page", "login");
            RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
            rd.forward(req, resp);
            throw new RuntimeException(e);
        }

        out.close();

    }


}