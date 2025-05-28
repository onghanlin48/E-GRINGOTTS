package page;

import ajax.check_amount;
import ajax.check_currency;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/Home")
public class Home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        check_amount check = (check_amount) session.getAttribute("check_amount");
        check_currency c_c = (check_currency) session.getAttribute("check_currency");

        session.removeAttribute("check_amount");
        session.removeAttribute("check_currency");
        RequestDispatcher rd = req.getRequestDispatcher("Home.jsp");
        rd.forward(req, resp);
    }
}