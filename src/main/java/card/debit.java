package card;


import db.DBConnection;
import function.function;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static function.function.*;

@WebServlet("/debit")
public class debit extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       int currency = Integer.parseInt(req.getParameter("currency"));
       Double amount = Double.parseDouble(req.getParameter("amount"));
       String name = req.getParameter("name");
       String cardNum = req.getParameter("cardNum");
       int month = Integer.parseInt(req.getParameter("month"));
       int year = Integer.parseInt(req.getParameter("year"));
       String cvv= req.getParameter("cvv");
       String category = req.getParameter("category");
       String pin = req.getParameter("pin");

       String date = month+""+year;
        System.out.println(cardNum);


        String select = "SELECT * FROM card c JOIN account a ON a.userId = c.id WHERE c.number=? AND c.mmyy=? AND c.cvv=?";

        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(select)) {
                statement.setString(1, cardNum);
                statement.setString(2, date);
                statement.setString(3, cvv);

                ResultSet rs = statement.executeQuery();

                if(rs.next()) {
                    String f_name = rs.getString("full_name");

                    if(f_name.equals(name)) {
                        byte[] salt = null;
                        byte[] storedHash = null;
                        if(rs.getBytes("PIN") == null) {

                            req.setAttribute("des", "Card not active");
                            req.setAttribute("page", "DebitCard");
                            RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                            rd.forward(req, resp);
                        }else if(verifyPassword(pin.toCharArray(), rs.getBytes("salt"), rs.getBytes("PIN"))){
                            if(rs.getInt("try") >= 3){
                                req.setAttribute("des", "Card block");
                                req.setAttribute("page", "DebitCard");
                                RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                                rd.forward(req, resp);
                            }else{
                                LocalDate currentDate = LocalDate.now();


                                DateTimeFormatter mon = DateTimeFormatter.ofPattern("MM");
                                DateTimeFormatter y = DateTimeFormatter.ofPattern("yy");

                                int currentMonth = Integer.parseInt(currentDate.format(mon));
                                int currentYear = Integer.parseInt(currentDate.format(y));
                                if(currentYear > year){
                                    req.setAttribute("des", "Card expired");
                                    req.setAttribute("page", "DebitCard");
                                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                                    rd.forward(req, resp);
                                } else if (currentYear <= year) {
                                    boolean c = false;
                                    if(currentYear == year){
                                        if(currentMonth > month){
                                            c=true;
                                        }
                                    }
                                    if(c){
                                        req.setAttribute("des", "Card expired");
                                        req.setAttribute("page", "DebitCard");
                                        RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                                        rd.forward(req, resp);
                                    }else{
                                        if(rs.getInt("status") == 2){
                                            String check_amount = "SELECT * FROM amount WHERE userid="+rs.getInt("id")+" AND cid="+currency;
                                            try(PreparedStatement stmt = connection.prepareStatement(check_amount)){
                                                ResultSet rs2 = stmt.executeQuery();
                                                if(rs2.next()){
                                                    if(amount > rs2.getDouble("amount")){
                                                        req.setAttribute("des", "You not enough money");
                                                        req.setAttribute("page", "DebitCard");
                                                        RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                                                        rd.forward(req, resp);
                                                    }else{
                                                        String invoiceNumber = generateInvoiceNumber();
                                                        String update = "UPDATE amount SET amount=amount-"+amount+" WHERE userid="+rs.getInt("id")+" AND cid="+currency;
                                                        String um = "UPDATE amount SET amount=amount+"+amount+" WHERE userid=22 AND cid="+currency;
                                                        String card ="UPDATE card SET try=0 WHERE id = "+rs.getInt("id");
                                                        String insertSql = "INSERT INTO history (invoice, form, receive, amount, currency, date, category, reference, status, user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                                                        try(PreparedStatement statement1 = connection.prepareStatement(update)){
                                                            statement1.executeUpdate();
                                                            try(PreparedStatement statement2 = connection.prepareStatement(um)){
                                                                statement2.executeUpdate();
                                                            }
                                                            try(PreparedStatement statement2 = connection.prepareStatement(card)){
                                                                statement2.executeUpdate();
                                                            }
                                                            try(PreparedStatement insertStatement = connection.prepareStatement(insertSql)){
                                                                insertStatement.setString(1, invoiceNumber);
                                                                insertStatement.setInt(2, rs.getInt("id"));
                                                                insertStatement.setInt(3, 22);
                                                                insertStatement.setDouble(4, amount);
                                                                insertStatement.setInt(5, currency);
                                                                insertStatement.setDate(6, Date.valueOf(currentDate));
                                                                insertStatement.setString(7, category);
                                                                insertStatement.setString(8, category);
                                                                insertStatement.setInt(9, 2);
                                                                insertStatement.setString(10, "Debit Card");
                                                                insertStatement.executeUpdate();

                                                                insertStatement.setString(1, invoiceNumber);
                                                                insertStatement.setInt(2, 22);
                                                                insertStatement.setInt(3, rs.getInt("id"));
                                                                insertStatement.setDouble(4, amount);
                                                                insertStatement.setInt(5, currency);
                                                                insertStatement.setDate(6, Date.valueOf(currentDate));
                                                                insertStatement.setString(7, category);
                                                                insertStatement.setString(8, category);
                                                                insertStatement.setInt(9, 1);
                                                                insertStatement.setString(10, "Debit Card");
                                                                insertStatement.executeUpdate();

                                                                req.setAttribute("des", "Complete Payment");
                                                                req.setAttribute("page", "DebitCard");
                                                                RequestDispatcher rd = req.getRequestDispatcher("Success.jsp");
                                                                rd.forward(req, resp);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }else{
                                            req.setAttribute("des", "You account is block");
                                            req.setAttribute("page", "DebitCard");
                                            RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                                            rd.forward(req, resp);
                                        }

                                    }
                                }
                            }
                        }else{
                            if(rs.getInt("try") >= 3){
                                req.setAttribute("des", "Card block");
                                req.setAttribute("page", "DebitCard");
                                RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                                rd.forward(req, resp);
                            }else{
                                String update = "UPDATE card SET try=try+1 WHERE number=?";
                                try(PreparedStatement stmt = connection.prepareStatement(update)) {
                                    stmt.setString(1, cardNum);
                                    stmt.executeUpdate();
                                }
                                req.setAttribute("des", "PIN ERROR");
                                req.setAttribute("page", "DebitCard");
                                RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                                rd.forward(req, resp);
                            }

                        }
                    }else{
                        req.setAttribute("des", "Card not found");
                        req.setAttribute("page", "DebitCard");
                        RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                        rd.forward(req, resp);
                    }

                }else{
                    req.setAttribute("des", "Card not found");
                    req.setAttribute("page", "DebitCard");
                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                    rd.forward(req, resp);
                }

                connection.close();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}