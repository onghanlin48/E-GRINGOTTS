package ajax;

import data.Account;
import db.DBConnection;

import javax.crypto.spec.PSource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.text.DecimalFormat;

@WebServlet("/currency_c")
public class currency_c extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        check_currency check = (check_currency) session.getAttribute("check_currency");
        ArrayList<String> check_amount = check.getAmount();
        Account account = (Account) session.getAttribute("account");
        int category = account.getCategory();
        int userId = account.getUserId();
        int id = Integer.parseInt(req.getParameter("id"));
        double value = Double.parseDouble(req.getParameter("value"));
        int to = Integer.parseInt(req.getParameter("to"));
        String s = "";


        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM currency WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1,to);
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()) {
                    s = resultSet.getString("short");
                }
                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error connecting to the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        if(check_amount == null || check_amount.isEmpty()){
            check_amount = new ArrayList<>();
            check_amount.add(id+" "+value);
        }else {
            boolean c = true;
            for(int i=0; i<check_amount.size(); i++){
                int x = Integer.parseInt(check_amount.get(i).split(" ")[0]);
                if(x == id){
                    c = false;
                    if(value == 0){
                        check_amount.remove(i);
                    }else{
                        check_amount.set(i,id+" "+value);
                    }

                }
            }
            if(c){
                check_amount.add(id+" "+value);
            }
        }
        check.setAmount(check_amount);
        double total_to = 0;


        for (int i = 0; i < check_amount.size(); i++) {
            int x = Integer.parseInt(check_amount.get(i).split(" ")[0]);
            double amount = Double.parseDouble(check_amount.get(i).split(" ")[1]);

            if (x == to) {
                try (Connection connection = DBConnection.getConnection()) {
                    String sql = "SELECT * FROM amount a JOIN currency c ON a.cid = c.id WHERE a.userid = ? AND a.cid=?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, userId);
                        statement.setInt(2, x);
                        ResultSet resultSet = statement.executeQuery();
                        if (resultSet.next()) {

                            double balance = resultSet.getDouble("amount");
                            String short_name = resultSet.getString("short");

                            if (balance < 0) {

                                out.println("You don't have balance for this currency");
                            } else {
                                if (amount > balance) {
                                    if(id == x) {
                                        out.println("You don't have enough balance for this currency");
                                    }
                                } else {

                                    double a = Double.parseDouble(df.format(balance - amount));

                                        if (amount < 0) {
                                            if (id == x) {
                                                out.println("You don't have enough balance for this currency");
                                            }
                                        } else {
                                            total_to = amount + total_to;
                                            if(id == x){
                                                out.println("Balance : " + balance + " " + short_name + " || " + "Left : " + a + " || No Transfer Fee");
                                            }
                                        }



                                }


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
            } else {
                try (Connection connection = DBConnection.getConnection()) {
                    String sql = "SELECT * FROM amount a JOIN proccessing p ON a.cid = p.cid JOIN currency c ON a.cid = c.id JOIN category ca ON ca.id = " + category + " WHERE a.userid = ? AND p.c_id = ? AND a.cid=?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, userId);
                        statement.setInt(2, to);
                        statement.setInt(3, x);
                        ResultSet resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            double transfer = resultSet.getDouble("value");
                            double pre = resultSet.getDouble("fee");
                            double balance = resultSet.getDouble("amount");
                            String short_name = resultSet.getString("short");
                            int discount = resultSet.getInt("discount");


                            if (balance < 0) {
                                if(id == x) {
                                    out.println("You don't have enough balance for this currency");
                                }
                            } else {
                                if (amount > balance) {
                                    if(id == x) {
                                        out.println("You don't have enough balance for this currency");
                                    }
                                } else {
                                    df.setRoundingMode(RoundingMode.UP);
                                    transfer = Double.parseDouble(df.format(amount * transfer));


                                    pre = Double.parseDouble(df.format(value * (pre / 100)));
                                    amount = Double.parseDouble(df.format(balance - amount));
                                    if (discount > 0) {

                                        double fee = Double.parseDouble(df.format(pre - (pre * ((double) discount / 100))));
                                        amount = Double.parseDouble(df.format(amount - pre));

                                            if(amount < 0){
                                                if(id == x){
                                                    out.println("You don't have enough balance for this currency");
                                                }
                                            }else{
                                                    total_to = transfer + total_to;
                                                if(id == x){
                                                    out.println("Balance : " + balance + " " + short_name + " || " + "Transfer fee : " + pre + " " + short_name + " - " + discount + "% = ");
                                                    out.println(fee + " " + short_name + " || Transfer : " + transfer +" "+ s + " || Left : " + amount);
                                                }

                                            }


                                    } else {
                                        amount = Double.parseDouble(df.format(amount - pre));

                                            if (amount < 0) {
                                                if (id == x) {
                                                    out.println("You don't have enough balance for this currency");
                                                }
                                            } else {
                                                total_to = transfer + total_to;

                                                if(id == x){
                                                    out.println("Balance : " + balance + " " + short_name + " || " + "Translate fee : " + pre + " " + short_name + " || Transfer : " + transfer +" "+ s +" || Left : " + amount );
                                                }
                                            }



                                    }


                                }
                            }
                            statement.close();
                            connection.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }


            }


        }
        System.out.println(total_to);
        check.setTotal(Double.parseDouble(df.format(total_to)));
        session.setAttribute("check_currency", check);
    }
}