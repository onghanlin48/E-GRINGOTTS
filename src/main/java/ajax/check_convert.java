package ajax;

import db.DBConnection;

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
import java.text.DecimalFormat;
import java.util.ArrayList;

@WebServlet("/check_convert")
public class check_convert extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        check_amount check = (check_amount) session.getAttribute("check_amount");
        int from = Integer.parseInt(req.getParameter("from"));
        int get = Integer.parseInt(req.getParameter("get"));
        double amount = Double.parseDouble(req.getParameter("amount"));
        ArrayList<String> currency = new ArrayList<>();
        currency.add(0,"0");
        check.setT(amount);

        Conversion<String,Double> booth=new Conversion<>();
        try (Connection connection = DBConnection.getConnection()) {


            String sql = "SELECT * FROM currency";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    currency.add(resultSet.getInt("id"),resultSet.getString("currency"));
                    booth.addCurrency(resultSet.getString("currency"));
                }

                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DBConnection.getConnection()) {


            String sql = "SELECT * FROM proccessing";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    booth.addToCurrency(currency.get(resultSet.getInt("cid")),currency.get(resultSet.getInt("c_id")),resultSet.getDouble("value"),resultSet.getDouble("fee")/100);
                    if(from == resultSet.getInt("cid") && get == resultSet.getInt("c_id")){
                        check.setValue("1 " + currency.get(resultSet.getInt("cid")) + " = " + resultSet.getDouble("value") + " " + currency.get(resultSet.getInt("c_id")));
                        check.setRate(check.getValue());
                    }
                }

                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        double converting = booth.DFSrate(currency.get(from), currency.get(get), amount);
        out.println(df.format(converting));
        double processing= booth.DFSprocessingfee(currency.get(from), currency.get(get), amount);

        check.setGet(currency.get(get));
        check.setConverting(converting);
        check.setFee(processing);
        check.setFrom(from);
        check.setTo(get);
        session.setAttribute("check_amount", check);

    }


}
