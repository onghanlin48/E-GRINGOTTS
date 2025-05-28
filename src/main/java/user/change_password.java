package user;

import function.function;
import data.Account;
import data.resend;
import db.DBConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static function.function.generateSalt;
import static function.function.hashPassword;

@WebServlet("/change_password")
public class change_password extends HttpServlet {
    private static final long serialVersionUID = 1L;



    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String password = req.getParameter("password");
        Account account = (Account) session.getAttribute("account");
        String username = account.getUsername();
        resend send = (resend) session.getAttribute("resend");
        byte[] salt = generateSalt();

        byte[] hashedPassword;
        try {
            hashedPassword = hashPassword(password.toCharArray(), salt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        try(Connection connection = DBConnection.getConnection()){
            String sql = "update account set salt=?,password=? where username=?";
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setBytes(1,salt);
                ps.setBytes(2, hashedPassword);
                ps.setString(3, username);
                int row =  ps.executeUpdate();

                if(row>0){
                    req.setAttribute("des", "Change Password Successfully!Please Try Login!");
                    req.setAttribute("page", "login");

                    session.removeAttribute("account");
                    session.removeAttribute("resend");
                    RequestDispatcher rd = req.getRequestDispatcher("Success.jsp");
                    rd.forward(req, resp);
                }else{
                    req.setAttribute("des", "Change Password failed!<br>Please contact admin!");
                    req.setAttribute("page", "login");
                    session.removeAttribute("account");
                    session.removeAttribute("resend");
                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                    rd.forward(req, resp);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error connecting to the database: " + e.getMessage());
            req.setAttribute("des", "Change Password failed!<br>Please contact admin!");
            req.setAttribute("page", "login");
            session.removeAttribute("account");
            session.removeAttribute("resend");
            RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
            rd.forward(req, resp);

        } catch (ClassNotFoundException e) {
            req.setAttribute("des", "Change Password failed!<br>Please contact admin!");
            req.setAttribute("page", "login");
            session.removeAttribute("account");
            session.removeAttribute("resend");
            RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
            rd.forward(req, resp);
            throw new RuntimeException(e);

        }
    }


}
