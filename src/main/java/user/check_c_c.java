package user;

import function.JavaMail;
import ajax.check_amount;
import data.Account;
import data.resend;
import function.function;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;

@WebServlet("/check_c_c")
public class check_c_c extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        check_amount check = (check_amount) session.getAttribute("check_amount");

        double amount = check.getAmount();
        double fee = check.getFee();
        double dis = check.getDis();
        String s = check.getS();
        double t = check.getT();
        double converting = Double.parseDouble(df.format(check.getConverting()));
        check.setConverting(converting);
        converting = check.getConverting();
        String get = check.getGet();
        double total;
        String value = "";
        boolean flag = true;
        int from = check.getFrom();
        int to = check.getTo();

        if(from == to){
            flag = false;
            req.setAttribute("des", "Cannot Convert Same Currency!");
            req.setAttribute("page", "Home");
            RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
            rd.forward(req, resp);
        }else{
            if(dis <= 0){
                total = Double.parseDouble(df.format(amount - fee - t));
                check.setAmount(total);
                if(total <= 0){
                    flag = false;
                    req.setAttribute("des", "You don't have enough balance");
                    req.setAttribute("page", "Home");
                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                    rd.forward(req, resp);
                }else{
                    value = t + " " + s + " = " + converting + " " + get + " \n" + "Processing Fee : "+ fee+" "+s + " \nCode Confirm : " ;
                    check.setValue(value);
                }
            }else{
                double a = Double.parseDouble(df.format(fee - (fee * dis)));
                total = Double.parseDouble(df.format(amount - a - t));
                check.setFee(a);
                check.setAmount(total);
                System.out.println(t);
                if(total <= 0){
                    flag = false;
                    req.setAttribute("des", "You don't have enough balance");
                    req.setAttribute("page", "Home");
                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                    rd.forward(req, resp);
                }else{
                    value = t + " " + s + " = " + converting + " " + get + " \n" + "Processing Fee : "+ fee+" "+s+" - "+" " + (dis*100) + "% = "+a +" " + s + " \nCode Confirm : " ;
                    check.setValue(value);
                }
            }
        }


        if(flag){
            Account account = (Account) session.getAttribute("account");

            String code_v = function.generateRandomNumber();
            account.setCode(code_v);

            resend send = new resend();
            send.setResend(3);
            send.setPage("s_convert");

            req.setAttribute("account", account);
            req.setAttribute("resend", send);
            session.setAttribute("check_amount", check);
            try {
                JavaMail.sendmail(account.getEmail(), value +code_v,"Confirm Convert");
                RequestDispatcher rd = req.getRequestDispatcher("verify.jsp");
                rd.forward(req, resp);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }


    }


}