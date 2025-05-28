package ajax;


import data.Account;
import data.resend;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/set")
public class set extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("text/html");
        int code = Integer.parseInt(req.getParameter("code"));
        String page = req.getParameter("page");

        PrintWriter out = resp.getWriter();
        Account account = (Account) session.getAttribute("account");
        data.resend send = new resend();

        System.out.println(code);
        send.setResend(code);
        send.setPage(page);
        req.setAttribute("account", account);
        req.setAttribute("send", send);

        session.setAttribute("account", account);
        session.setAttribute("send", send);
        out.close();

    }

}