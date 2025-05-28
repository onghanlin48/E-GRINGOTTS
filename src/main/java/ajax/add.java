package ajax;


import data.Account;
import db.DBConnection;

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
import java.util.ArrayList;

@WebServlet("/add")
public class add extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("text/html");
        Account account = (Account) session.getAttribute("account");
        int userId = account.getUserId();
        String id = req.getParameter("id");
        int check = Integer.parseInt(req.getParameter("check"));
        check_currency check_c = (check_currency) session.getAttribute("check_currency");

        PrintWriter out = resp.getWriter();

        if(check == 1){

            try (Connection connection = DBConnection.getConnection())  {
                String sql = "SELECT * FROM amount a JOIN currency c ON a.cid = c.id WHERE a.cid = ? AND a.userid = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1,id);
                    statement.setInt(2,userId);
                    ResultSet resultSet = statement.executeQuery();


                    if(resultSet.next()) {
                        if(resultSet.getDouble("amount") > 0.00) {
                            out.println(" <div class=\"amount\">\n" +
                                    "                       <p>"+resultSet.getString("currency")+"</p> <div class=\"amountmoney\">\n" +
                                    "                            <input type=\"number\" class=\"inputText\" id=\""+resultSet.getInt("cid")+"\" name=\""+resultSet.getInt("cid")+"\" onkeyup=\"checkamount('"+resultSet.getInt("cid")+"');\" step=\"0.01\" min=\"0.01\" placeholder=\"0.00\" required>\n" +
                                    "                            <span class=\"floating-label\">Amount</span>\n" +
                                    "                            <div class=\"amountnote_"+resultSet.getInt("cid")+"\">\n" +
                                    "                                <p style=\"color: red\" id=\"amountnote_"+resultSet.getInt("cid")+"\">\n" +
                                    "                                    Balance : "+resultSet.getDouble("amount")+" "+resultSet.getString("short")+"\n" +
                                    "                                </p>\n" +
                                    "                            </div>\n" +
                                    "                        </div>\n" +
                                    "                    </div>");
                            String check_currency = check_c.getCurrency();
                            if(check_currency == null){
                                check_currency = String.valueOf(resultSet.getInt("cid"));

                            }else{
                                check_currency = resultSet.getInt("cid") + " " + check_c.getCurrency();

                            }
                            check_c.setCurrency(check_currency);
                            System.out.println(check_currency);

                        }
                    }

                    statement.close();
                    connection.close();
                    out.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.println("Error connecting to the database: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            session.setAttribute("check_currency", check_c);
            session.setAttribute("account", account);
        }else if(check == 2){
            String currency = check_c.getCurrency();
            String[] remove = currency.split(" ");
            currency = "";
            for (int i = 0; i < remove.length; i++) {
                if(!remove[i].equals(id)) {
                    currency = remove[i] + " " + currency;
                }
            }
           check_c.setCurrency(currency);
            ArrayList<String> amount  = check_c.getAmount();
            if(amount == null || amount.isEmpty()){

            }else{
                for (int i = 0; i < amount.size(); i++) {
                    String x = amount.get(i).split(" ")[0];
                    if(id.equals(x)){
                        amount.remove(i);
                    }
                }
                check_c.setAmount(amount);
                out.println(currency);
            }
            session.setAttribute("check_currency", check_c);
            session.setAttribute("account", account);

        } else if (check == 3) {

            String currency = check_c.getCurrency();
            out.println(currency);
        }
        session.setAttribute("check_currency", check_c);
        session.setAttribute("account", account);


    }

}