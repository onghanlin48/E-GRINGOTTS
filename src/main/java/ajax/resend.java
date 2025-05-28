package ajax;

import function.JavaMail;
import function.function;
import data.Account;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/resend")
public class resend extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String check = req.getParameter("check");
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();
        Account account = (Account) session.getAttribute("account");
        data.resend send = (data.resend) session.getAttribute("send");
        String code_v = function.generateRandomNumber();
        account.setCode(code_v);

        send.setResend(3);

        String email = account.getEmail();
        String username = account.getUsername();

        try {
            String des;
            String em;

            if(check.equals("verify")) {
                des="Resend Email Verify";
                em = "Username:"+username+"\nCode Verify :"+code_v;
            } else if (check.equals("reset_password")) {
                des="Resend verify Password";
                em = "Username:"+username+"\nCode Verify :"+code_v;
            } else if (check.equals("c_transfer")) {
                check_currency check_ = (check_currency) session.getAttribute("check_currency");
                des="Resend Confirm Transfer To "+ check_.getName();
                em = "Received :"+check_.getUser_id() +"," + check_.getName() + "," + check_.getPhone()+"\nFrom Account :\n"+check_.getMsg()+"\n Total Transfer : "+check_.getTotal() + " "+check_.getC_s() +"\n\nCode Confirm :"+code_v;
            } else if (check.equals("s_convert")) {
                check_amount c_a = (check_amount) session.getAttribute("check_amount");
                des="Resend Confirm Convert";
                em = c_a.getValue() + code_v;
            } else if (check.equals("change_pin")) {
                des="Resend Change credit card Pin";
                em = "Code Confirm :"+code_v;
            } else{
                des="Wrong send !!";
                em ="Wrong send !!";
            }

            System.out.println("Send....");
            JavaMail.sendmail(email,em,des);
            out.println("Attempt left : 3");
            session.setAttribute("send", send);
            session.setAttribute("account", account);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        out.close();

    }

}