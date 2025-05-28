package user;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import ajax.check_currency;
import data.Account;
import data.resend;
import db.DBConnection;
import function.JavaMail;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;

import static function.function.generateInvoiceNumber;

@WebServlet("/c_transfer")
public class c_transfer extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException,FileNotFoundException, MalformedURLException {
        HttpSession session = req.getSession();
        String code = req.getParameter("code");
        resend send = (resend) session.getAttribute("send");
        Account account = (Account) session.getAttribute("account");
        int attempt = send.getResend();
        System.out.println(attempt);
        String code_c = account.getCode();
        if(attempt != 0){
            if(code.equals(code_c)){
                String invoiceNumber = generateInvoiceNumber();
                check_currency check = (check_currency) session.getAttribute("check_currency");
                LocalDate currentDate = LocalDate.now();
                ArrayList<String> amount = check.getAmount();
                StringBuilder amount_send = new StringBuilder();
                StringBuilder fee_send = new StringBuilder();
                double total = Double.parseDouble(df.format(check.getTotal()));
                String s = check.getC_s();
                int userid = account.getUserId();
                int to_id = check.getUser_id();
                int to_c = check.getTo();
                String reference = check.getReference();
                try (Connection connection = DBConnection.getConnection()) {
                    String selectSql = "SELECT amount FROM amount WHERE userid = ? AND cid = ?";
                    String updateSql = "UPDATE amount SET amount = ? WHERE userid = ? AND cid = ?";
                    String insertSql = "INSERT INTO history (invoice, form, receive, amount, currency, date, category, reference, status, user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                        selectStatement.setInt(1, to_id);
                        selectStatement.setInt(2, to_c);
                        ResultSet resultSet = selectStatement.executeQuery();

                        if (resultSet.next()) {
                            double Amount = resultSet.getDouble("amount");
                            Amount += total;

                            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                                updateStatement.setDouble(1, Amount);
                                updateStatement.setInt(2, to_id);
                                updateStatement.setInt(3, to_c);
                                updateStatement.executeUpdate();
                            }
                            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                                insertStatement.setString(1, invoiceNumber);
                                insertStatement.setInt(2, to_id);
                                insertStatement.setInt(3, userid);
                                insertStatement.setDouble(4, total);
                                insertStatement.setInt(5, to_c);
                                insertStatement.setDate(6, Date.valueOf(currentDate));
                                insertStatement.setString(7, "Quick Transfer");
                                insertStatement.setString(8, reference);
                                insertStatement.setInt(9, 1);
                                insertStatement.setString(10, "Online");

                                insertStatement.executeUpdate();
                            }

                        }
                        resultSet.close();
                        selectStatement.close();
                        connection.close();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    check.clear();
                    session.removeAttribute("check_currency");
                    req.setAttribute("des", "Please contact admin!");
                    req.setAttribute("page", "Home");
                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                    rd.forward(req, resp);
                }
                for (String z : amount) {
                    int x = Integer.parseInt(z.split(" ")[0]);
                    double y = Double.parseDouble(z.split(" ")[1]);
                    double fee = Double.parseDouble(z.split(" ")[2]);
                    double left = Double.parseDouble(z.split(" ")[3]);
                    String c_s = z.split(" ")[4];
                    double t = Double.parseDouble(z.split(" ")[5]);

                    amount_send.append(y).append(c_s).append("(").append(t).append(s).append(") ");
                    fee_send.append(fee).append(c_s).append(" ");

                    try (Connection connection = DBConnection.getConnection()) {
                        String selectSql = "SELECT balance FROM admin WHERE currency = ?";
                        String updateSql = "UPDATE admin SET balance = ? WHERE currency = ?";
                        String update_user = "UPDATE amount SET amount = ? WHERE cid = ? AND userid = ?";
                        String insertSql = "INSERT INTO history (invoice, form, receive, amount, currency, date, category, reference, status, user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                        try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                            selectStatement.setInt(1, x);
                            ResultSet resultSet = selectStatement.executeQuery();

                            if (resultSet.next()) {
                                double adminAmount = resultSet.getDouble("balance");
                                adminAmount += fee;

                                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                                    updateStatement.setDouble(1, adminAmount);
                                    updateStatement.setInt(2, x);
                                    updateStatement.executeUpdate();

                                }
                                try (PreparedStatement updateuser = connection.prepareStatement(update_user)) {
                                    updateuser.setDouble(1, left);
                                    updateuser.setInt(2, x);
                                    updateuser.setInt(3, userid);
                                    updateuser.executeUpdate();
                                }
                                try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                                    if(x != to_c){
                                        insertStatement.setString(1, invoiceNumber);
                                        insertStatement.setInt(2, userid);
                                        insertStatement.setInt(3, userid);
                                        insertStatement.setDouble(4, y);
                                        insertStatement.setInt(5, x);
                                        insertStatement.setDate(6, Date.valueOf(currentDate));
                                        insertStatement.setString(7, "Convert Currency");
                                        insertStatement.setString(8, "Convert Currency");
                                        insertStatement.setInt(9, 2);
                                        insertStatement.setString(10, "Online");

                                        insertStatement.executeUpdate();

                                        insertStatement.setString(1, invoiceNumber);
                                        insertStatement.setInt(2, userid);
                                        insertStatement.setInt(3, 1);
                                        insertStatement.setDouble(4, fee);
                                        insertStatement.setInt(5, x);
                                        insertStatement.setDate(6, Date.valueOf(currentDate));
                                        insertStatement.setString(7, "Fee");
                                        insertStatement.setString(8, "Processing Fee");
                                        insertStatement.setInt(9, 2);
                                        insertStatement.setString(10, "Online");

                                        insertStatement.executeUpdate();

                                        insertStatement.setString(1, invoiceNumber);
                                        insertStatement.setInt(2, userid);
                                        insertStatement.setInt(3, userid);
                                        insertStatement.setDouble(4, t);
                                        insertStatement.setInt(5, to_c);
                                        insertStatement.setDate(6, Date.valueOf(currentDate));
                                        insertStatement.setString(7, "Convert Currency");
                                        insertStatement.setString(8, "Convert Currency");
                                        insertStatement.setInt(9, 1);
                                        insertStatement.setString(10, "Online");

                                        insertStatement.executeUpdate();
                                    }

                                }

                            }
                            resultSet.close();
                            selectStatement.close();
                            connection.close();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        check.clear();
                        session.removeAttribute("check_currency");
                        req.setAttribute("des", "Please contact admin!");
                        req.setAttribute("page", "Home");
                        RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                        rd.forward(req, resp);
                    }




                }
                try (Connection connection = DBConnection.getConnection()) {
                    String insertSql = "INSERT INTO history (invoice, form, receive, amount, currency, date, category, reference, status, user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                        insertStatement.setString(1, invoiceNumber);
                        insertStatement.setInt(2, userid);
                        insertStatement.setInt(3, to_id);
                        insertStatement.setDouble(4, total);
                        insertStatement.setInt(5, to_c);
                        insertStatement.setDate(6, Date.valueOf(currentDate));
                        insertStatement.setString(7, "Quick Transfer");
                        insertStatement.setString(8, reference);
                        insertStatement.setInt(9, 2);
                        insertStatement.setString(10, "Online");

                        insertStatement.executeUpdate();
                        connection.close();
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    check.clear();
                    session.removeAttribute("check_currency");
                    req.setAttribute("des", "Please contact admin!");
                    req.setAttribute("page", "Home");
                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                    rd.forward(req, resp);
                }

                String filePath = getServletContext().getRealPath("/invoice/" + invoiceNumber + ".pdf");
                File invoiceDir = new File("invoice");
                if (!invoiceDir.exists()) {
                    invoiceDir.mkdirs(); // Create directory if it doesn't exist
                }

                try {

                    function.function.generatePdf(filePath,invoiceNumber,getServletContext().getRealPath("/Image/logo.png"),getServletContext().getRealPath("/Image/Navbar.png"), account.getFull_name() ,check.getName(), String.valueOf(currentDate),String.valueOf(account.getUserId()), account.getPhone(), String.valueOf(check.getUser_id()), check.getPhone(), String.valueOf(amount_send),String.valueOf(fee_send), total + " " + s);

                    // Set up the response headers to serve the file as a download
                    File pdfFile = new File(filePath);
                    try {
                        JavaMail.sendmail(account.getEmail(), "You have make a transfer!","Invoice Transfer",filePath);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                    check.clear();
                    session.removeAttribute("check_currency");
                    req.setAttribute("des", "Transfer Successfully!");
                    req.setAttribute("page", "Home");
                    RequestDispatcher rd = req.getRequestDispatcher("Success.jsp");
                    rd.forward(req, resp);
                } catch (Exception e) {
                    throw new ServletException("Exception while creating PDF", e);
                }




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