package ajax;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

@WebServlet("/c_amount")
public class c_amount extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        check_amount check = (check_amount) session.getAttribute("check_amount");
        out.println(check.getValue() + "<br>");

        double amount = check.getAmount();
        double fee = check.getFee();
        double dis = check.getDis();
        String s = check.getS();
        double t = check.getT();
        double total;
        if(dis <= 0){
            total = Double.parseDouble(df.format(amount - fee - t));
            if(total <= 0){
                out.println("You don't have enough balance");
            }else{
                out.println("Processing Fee : "+ fee+" "+s);
            }
        }else{
            double a = Double.parseDouble(df.format(fee - (fee * dis)));
            total = Double.parseDouble(df.format(amount - a - t));
            System.out.println(t);
            if(total <= 0){
                out.println("You don't have enough balance");
            }else{
                out.println("Processing Fee : "+ fee+" "+s+" - "+" " + (dis*100) + "% = "+a +" " + s);
            }
        }
        System.out.println(total);
    }


}