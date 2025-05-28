package user;

import data.Account;
import data.resend;
import db.DBConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static function.function.generateSalt;
import static function.function.hashPassword;

@WebServlet("/verify")
public class checkverify extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String code = req.getParameter("code");
        resend send = (resend) session.getAttribute("send");
        Account account = (Account) session.getAttribute("account");
        int attempt = send.getResend();
        System.out.println(attempt);
        String code_c = account.getCode();
        if(attempt != 0){
            if(code.equals(code_c)){
                String password = account.getPassword();
                String email = account.getEmail();
                String phone = account.getPhone();
                String address = account.getAddress();
                String username = account.getUsername();
                String dob = account.getDOB();
                String full_name = account.getFull_name();
                byte[] salt = generateSalt();
                byte[] hashedPassword;
                try {
                    hashedPassword = hashPassword(password.toCharArray(), salt);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                }

                try (Connection connection = DBConnection.getConnection()) {
                    String insert = "INSERT INTO account (username, full_name,salt,password, email, phone,dob,address, status,category) VALUES (?,?,?,?,?,?,?,?,?,?)";
                    try (PreparedStatement statement = connection.prepareStatement(insert)) {
                        statement.setString(1, username);
                        statement.setString(2, full_name);
                        statement.setBytes(3,salt);
                        statement.setBytes(4, hashedPassword);
                        statement.setString(5, email);
                        statement.setString(6, phone);
                        statement.setString(7, dob);
                        statement.setString(8, address);
                        statement.setInt(9, 1);
                        statement.setInt(10, 1);
                        int rowsAffected = statement.executeUpdate();

                        // Checking if the insertion was successful
                        if (rowsAffected > 0) {
                            statement.close();
                            connection.close();
                            try (Connection conn = DBConnection.getConnection()) {
                                String select = "SELECT * FROM account WHERE username = ?";
                                try (PreparedStatement ps = conn.prepareStatement(select)) {
                                    ps.setString(1, username);
                                    ResultSet rs = ps.executeQuery();
                                    if (rs.next()) {
                                        int id = rs.getInt("userId");
                                        ps.close();
                                        conn.close();
                                        try(Connection con = DBConnection.getConnection()) {
                                            String currency = "Insert INTO amount (cid,amount,userid) VALUES (?,?,?)";
                                            String select_1 = "SELECT * FROM currency";
                                            try (PreparedStatement ps2 = con.prepareStatement(currency)) {
                                                try(PreparedStatement ps3 = con.prepareStatement(select_1)) {
                                                    ResultSet rs2 = ps3.executeQuery();
                                                    while(rs2.next()){
                                                        ps2.setInt(1, rs2.getInt("id"));
                                                        ps2.setInt(2, 0);
                                                        ps2.setInt(3, id);
                                                        ps2.executeUpdate();
                                                    }
                                                }


                                                System.out.println("Insertion successful!");
                                                req.setAttribute("des", "Registered successfully!<br>Please waiting admin approved!");
                                                req.setAttribute("page", "login");
                                                account.clear();
                                                send.clear();
                                                RequestDispatcher rd = req.getRequestDispatcher("Success.jsp");
                                                rd.forward(req, resp);
                                            }
                                            catch (SQLException e) {
                                                req.setAttribute("des", "Registration failed!<br>Please contact admin!");
                                                req.setAttribute("page", "login");
                                                account.clear();
                                                send.clear();
                                                RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                                                rd.forward(req, resp);
                                                e.printStackTrace();
                                            }
                                        }
                                    }else{
                                        req.setAttribute("des", "Registration failed!<br>Please contact admin!");
                                        req.setAttribute("page", "login");
                                        account.clear();
                                        send.clear();
                                        RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                                        rd.forward(req, resp);
                                    }
                                }
                            }

                        } else {
                            if (statement != null) statement.close();
                            if (connection != null) connection.close();
                            System.out.println("Insertion failed!");
                            req.setAttribute("des", "Registration failed!<br>Please contact admin!");
                            req.setAttribute("page", "login");
                            account.clear();
                            send.clear();
                            RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                            rd.forward(req, resp);
                        }
                        try {
                            if (statement != null) statement.close();
                            if (connection != null) connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error connecting to the database: " + e.getMessage());
                    req.setAttribute("des", "Registration failed!<br>Please contact admin!");
                    req.setAttribute("page", "login");
                    account.clear();
                    send.clear();
                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                    rd.forward(req, resp);

                } catch (ClassNotFoundException e) {
                    req.setAttribute("des", "Registration failed!<br>Please contact admin!");
                    req.setAttribute("page", "login");
                    account.clear();
                    send.clear();
                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                    rd.forward(req, resp);
                    throw new RuntimeException(e);

                }



            }else{
                attempt = attempt - 1;
                send.setResend(attempt);
                session.setAttribute("send",send);
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