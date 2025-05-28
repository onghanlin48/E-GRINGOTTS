package user;

import ajax.check_currency;
import function.JavaMail;
import function.function;
import data.Account;
import data.resend;
import db.DBConnection;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

@WebServlet("/transfer_c")
public class transfer_c extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        check_currency check = (check_currency) session.getAttribute("check_currency");
        ArrayList<String> check_amount = check.getAmount();
        ArrayList<String> new_amount = new ArrayList<>();
        Account account = (Account) session.getAttribute("account");
        int category = account.getCategory();
        int userId = account.getUserId();
        int to = Integer.parseInt(req.getParameter("to"));
        check.setTo(to);
        String reference = req.getParameter("reference");
        check.setAmount(check_amount);
        check.setReference(reference);
        StringBuilder out = new StringBuilder();
        StringBuilder send = new StringBuilder();
        out.append("You don't have enough balance for this currency");
        boolean check_result = false;
        double total_to = 0;
        String s = "";

        if(check_amount == null || check_amount.isEmpty()) {
            String result = String.valueOf(out);
            check.clear();
            req.setAttribute("des", "You dont have enough balance!");
            req.setAttribute("page", "Search");
            RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
            rd.forward(req, resp);
        }else{
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
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

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
                                    check_result = true;
                                    out.append(" (").append(resultSet.getString("currency")).append(")");
                                } else {
                                    if (amount > balance) {
                                        check_result = true;
                                        out.append(" (").append(resultSet.getString("currency")).append(")");
                                    } else {

                                        double a = Double.parseDouble(df.format(balance - amount));

                                        if (amount < 0) {
                                            check_result = true;
                                            out.append(" (").append(resultSet.getString("currency")).append(")<br>");
                                        } else {
                                            total_to = amount + total_to;
                                            new_amount.add(i,check_amount.get(i) +" 0" +" "+a +" "+short_name+" "+amount );
                                            send.append("Balance : ").append(balance).append(" ").append(short_name).append(" || ").append("Left : ").append(a).append(" || No Transfer Fee\n");
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
                                    check_result = true;
                                    out.append(" (").append(resultSet.getString("currency")).append(")");
                                } else {
                                    if (amount > balance) {
                                        check_result = true;
                                        out.append(" (").append(resultSet.getString("currency")).append(")");
                                    } else {
                                        df.setRoundingMode(RoundingMode.UP);
                                        transfer = Double.parseDouble(df.format(amount * transfer));


                                        pre = Double.parseDouble(df.format(amount * (pre / 100)));
                                        amount = Double.parseDouble(df.format(balance - amount));
                                        if (discount > 0) {

                                            double fee = Double.parseDouble(df.format(pre - (pre * ((double) discount / 100))));
                                            amount = Double.parseDouble(df.format(amount - pre));

                                            if(amount < 0){
                                                check_result = true;
                                                out.append(" (").append(resultSet.getString("currency")).append(")");
                                            }else{
                                                total_to = transfer + total_to;
                                                new_amount.add(i,check_amount.get(i) +" " +fee+" "+amount +" "+short_name+" "+transfer);
                                                send.append("Balance : ").append(balance).append(" ").append(short_name).append(" || ").append("Transfer fee : ").append(pre).append(" ").append(short_name).append(" - ").append(discount).append("% = ");
                                                send.append(fee).append(" ").append(short_name).append(" || Transfer : ").append(transfer).append(" ").append(s).append(" || Left : ").append(amount).append("\n");
                                            }


                                        } else {
                                            amount = Double.parseDouble(df.format(amount - pre));

                                            if (amount < 0) {
                                                check_result = true;
                                                out.append(" (").append(resultSet.getString("currency")).append(")");
                                            } else {
                                                total_to = transfer + total_to;
                                                new_amount.add(i,check_amount.get(i) +" " +pre+" "+amount +" "+short_name+" "+transfer);
                                                send.append("Balance : ").append(balance).append(" ").append(short_name).append(" || ").append("Translate fee : ").append(pre).append(" ").append(short_name).append(" || Transfer : ").append(transfer).append(" ").append(s).append(" || Left : ").append(amount).append("\n");
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
            if(check_result){
                String result = String.valueOf(out);
                check.clear();
                req.setAttribute("des", result);
                req.setAttribute("page", "Search");
                RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                rd.forward(req, resp);
            }else{
                check.setAmount(new_amount);
                check.setMsg(String.valueOf(send));
                String code_v = function.generateRandomNumber();

                account.setCode(code_v);

                resend r_send = new resend();
                r_send.setResend(3);
                r_send.setPage("c_transfer");
                req.setAttribute("resend", r_send);
                req.setAttribute("account", account);
                check.setTotal(Double.parseDouble(df.format(total_to)));
                check.setC_s(s);
                System.out.println(check.getAmount());
                session.setAttribute("check_currency", check);
                try {
                    JavaMail.sendmail(account.getEmail(), "Received :"+check.getUser_id() +"," + check.getName() + "," + check.getPhone()+"\nFrom Account :\n"+check.getMsg()+"\n Total Transfer : "+check.getTotal() + " "+s +"\n\nCode Confirm :"+code_v,"Confirm Transfer To "+ check.getName());
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                RequestDispatcher rd = req.getRequestDispatcher("verify.jsp");
                rd.forward(req, resp);
            }
        }


    }
}