package user;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/reset_password")
public class reset_password extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String code = req.getParameter("code");
        resend send = (resend) session.getAttribute("send");
        Account account = (Account) session.getAttribute("account");
        int attempt = send.getResend();
        System.out.println(attempt);
        String code_c = account.getCode();
        if(attempt != 0){
            if(code.equals(code_c)){
                RequestDispatcher rd = req.getRequestDispatcher("reset_password.jsp");
                rd.forward(req, resp);

            }else{
                attempt = attempt - 1;
                send.setResend(attempt);
                req.setAttribute("account", account);
                req.setAttribute("resend", send);
                RequestDispatcher rd = req.getRequestDispatcher("verify.jsp");
                rd.forward(req, resp);
            }
        }else{
            req.setAttribute("account", account);
            req.setAttribute("resend", send);
            RequestDispatcher rd = req.getRequestDispatcher("verify.jsp");
            rd.forward(req, resp);
        }
    }

}