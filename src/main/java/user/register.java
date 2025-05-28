package user;

import function.JavaMail;
import function.function;
import data.*;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/regis")
public class register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = req.getParameter("username");
        String full_name = req.getParameter("full_name");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String dob = req.getParameter("date");

        dob = function.Dateformat(dob);

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setEmail(email);
        account.setPhone(phone);
        account.setAddress(address);
        account.setDOB(dob);
        account.setFull_name(full_name);

        session.setAttribute("account", account);

        String code_v = function.generateRandomNumber();
        account.setCode(code_v);

        resend send = new resend();
        send.setResend(3);
        send.setPage("verify");

        req.setAttribute("account", account);
        req.setAttribute("resend", send);


        try {
            JavaMail.sendmail(email,"Username:"+username+"\nCode Email Verify :"+code_v,"Email Verify");
            RequestDispatcher rd = req.getRequestDispatcher("verify.jsp");
            rd.forward(req, resp);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

}