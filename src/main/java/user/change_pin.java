package user;

import data.Account;
import data.pin;
import data.resend;
import db.DBConnection;
import function.function;

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

import static function.function.*;

@WebServlet("/change_pin")
public class change_pin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String code = req.getParameter("code");
        resend send = (resend) session.getAttribute("send");
        Account account = (Account) session.getAttribute("account");
        int id = account.getUserId();
        int attempt = send.getResend();
        System.out.println(attempt);
        String code_c = account.getCode();
        if(attempt != 0){
            if(code.equals(code_c)){
                String old = pin.getOld();
                String new_pin = pin.getNew_pin();

                try (Connection connection = DBConnection.getConnection()) {
                    String sql = "SELECT * FROM card WHERE id = ?";
                    String update = "UPDATE card SET salt=?,PIN = ? WHERE id = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1,id);
                        ResultSet resultSet = statement.executeQuery();
                        if(resultSet.next()) {
                            if(resultSet.getBytes("PIN") == null) {
                                byte[] salt = generateSalt();

                                byte[] hashedPassword;
                                try {
                                    hashedPassword = hashPassword(new_pin.toCharArray(), salt);
                                } catch (NoSuchAlgorithmException e) {
                                    throw new RuntimeException(e);
                                } catch (InvalidKeySpecException e) {
                                    throw new RuntimeException(e);
                                }
                                try (PreparedStatement ps = connection.prepareStatement(update)) {
                                    ps.setBytes(1,salt);
                                    ps.setBytes(2,hashedPassword);
                                    ps.setInt(3,id);
                                    int row =  ps.executeUpdate();
                                    if(row > 0) {
                                        req.setAttribute("des", "Change PIN Successfully!!");
                                        req.setAttribute("page", "Home");
                                        RequestDispatcher rd = req.getRequestDispatcher("Success.jsp");
                                        rd.forward(req, resp);
                                    }
                                }

                            }else{
                                    if(verifyPassword(old.toCharArray(), resultSet.getBytes("salt"), resultSet.getBytes("PIN"))) {
                                        byte[] salt = generateSalt();

                                        byte[] hashedPassword;
                                        try {
                                            hashedPassword = hashPassword(new_pin.toCharArray(), salt);
                                        } catch (NoSuchAlgorithmException e) {
                                            throw new RuntimeException(e);
                                        } catch (InvalidKeySpecException e) {
                                            throw new RuntimeException(e);
                                        }
                                        try (PreparedStatement ps = connection.prepareStatement(update)) {
                                            ps.setBytes(1,salt);
                                            ps.setBytes(2,hashedPassword);
                                            ps.setInt(3,id);
                                        int row =  ps.executeUpdate();
                                        if(row > 0) {
                                            req.setAttribute("des", "Change PIN Successfully!!");
                                            req.setAttribute("page", "Home");
                                            RequestDispatcher rd = req.getRequestDispatcher("Success.jsp");
                                            rd.forward(req, resp);
                                        }
                                    }
                                }else{
                                    req.setAttribute("des", "You old PIN incorrect!");
                                    req.setAttribute("page", "Home");
                                    RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                                    rd.forward(req, resp);
                                }
                            }
                        }
                        statement.close();
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

            }else{
                attempt = attempt - 1;
                send.setResend(attempt);
                session.setAttribute("send", send);
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
