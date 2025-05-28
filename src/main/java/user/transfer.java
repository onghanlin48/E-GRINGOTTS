package user;

import data.Account;
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

@WebServlet("/transfer")
public class transfer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

       if(req.getParameter("id").isEmpty()) {
           req.setAttribute("des", "Please Select An Account!");
           req.setAttribute("page", "Search");
           RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
           rd.forward(req, resp);
       }else{
           int id = Integer.parseInt(req.getParameter("id"));

           try (Connection connection = DBConnection.getConnection()) {
               String sql = "SELECT  userId,full_name,phone,status " +
                       "FROM account " +
                       "WHERE userId = ?";
               try (PreparedStatement statement = connection.prepareStatement(sql)) {
                   statement.setInt(1,id);
                   ResultSet resultSet = statement.executeQuery();

                   if (resultSet.next()) {
                       if(resultSet.getInt("status") == 1){
                           req.setAttribute("des", "This account number haven't been approved yet！");
                           req.setAttribute("page", "Search");
                           RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                           rd.forward(req, resp);
                       }else if(resultSet.getInt("status") == 2){
                           req.setAttribute("user_id", resultSet.getInt("userId"));
                           req.setAttribute("full_name", resultSet.getString("full_name"));
                           req.setAttribute("phone",   resultSet.getString("phone"));
                           HttpSession session = req.getSession();
                           session.setAttribute("user_id", resultSet.getInt("userId"));
                           RequestDispatcher rd = req.getRequestDispatcher("Transfer.jsp");
                           rd.forward(req, resp);
                       }else if(resultSet.getInt("status") == 3){
                           req.setAttribute("des", "This account number have been blocked！");
                           req.setAttribute("page", "Search");
                           RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                           rd.forward(req, resp);
                       }else{
                           req.setAttribute("des", "This account number have been blocked！");
                           req.setAttribute("page", "Search");
                           RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
                           rd.forward(req, resp);
                       }
                   }
                   statement.close();
                   connection.close();
               }
           } catch (SQLException e) {
               req.setAttribute("des", "Please Contact Admin！");
               req.setAttribute("page", "Search");
               RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
               rd.forward(req, resp);
               e.printStackTrace();
           } catch (ClassNotFoundException e) {
               req.setAttribute("des", "Please Contact Admin！");
               req.setAttribute("page", "Search");
               RequestDispatcher rd = req.getRequestDispatcher("Failed.jsp");
               rd.forward(req, resp);
               throw new RuntimeException(e);
           }
       }




    }


}