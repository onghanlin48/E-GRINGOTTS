package user;

import data.Account;
import data.pin;
import data.resend;
import db.DBConnection;
import function.function;
import function.JavaMail;

import javax.mail.MessagingException;
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
import java.util.Objects;

@WebServlet("/set_pin")
public class set_pin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String old = req.getParameter("old");
        String new_pin = req.getParameter("new");
        String old_pin = req.getParameter("conform");

        Account account = (Account) session.getAttribute("account");

        if(!Objects.equals(old_pin, new_pin)) {
            req.setAttribute("des", "New PIN and Confirm PIN Must be Same!");
            req.setAttribute("page", "Home");
            RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
            rd.forward(req, resp);
        }else{
            pin p = new pin();
            p.setNew_pin(new_pin);
            p.setOld(old);
            session.setAttribute("pin", p);
            String code_v = function.generateRandomNumber();
            account.setCode(code_v);
            resend send = new resend();
            send.setResend(3);
            send.setPage("change_pin");
            req.setAttribute("resend", send);
            req.setAttribute("account", account);
            session.setAttribute("account", account);

            try {
                JavaMail.sendmail(account.getEmail(),"Code Confirm : "+code_v,"Change credit card Pin");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            RequestDispatcher rd = req.getRequestDispatcher("verify.jsp");
            rd.forward(req, resp);


        }
    }


}