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

@WebServlet("/search_user")
public class search_user extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        Account account = (Account) session.getAttribute("account");
        int id = account.getUserId();
        boolean favourite = true;
        ArrayList<Integer> user_id = new ArrayList<>();

        String name = "%" + req.getParameter("name") +"%";
        String phone = "%" + req.getParameter("phone") +"%";

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT  a.userId,a.full_name, a.phone " +
                    "FROM favourite f " +
                    "JOIN account a ON f.favourite = a.userId " +
                    "WHERE f.user_id = ? AND a.full_name LIKE ? AND a.phone LIKE ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1,id);
                statement.setString(2,name);
                statement.setString(3,phone);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()) {
                    favourite = false;
                    user_id.add(resultSet.getInt("userId"));
                    out.println("<tr>\n" +
                            "                        <td>"+resultSet.getString("userId")+"</td>\n" +
                            "                        <td>"+resultSet.getString("full_name")+" (Favourite)</td>\n" +
                            "                        <td>"+resultSet.getString("phone")+"</td>\n" +
                            "                        <td>\n" +
                            "                            <button class='name1' onclick='select(\""+resultSet.getString("userId")+"\")'>\n" +
                            "                                Select\n" +
                            "                            </button>\n" +
                            "                        </td>\n" +
                            "                    </tr>");
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

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT userId,full_name,phone " +
                    "FROM account " +
                    "WHERE userId != '"+id+"' AND full_name like ? AND phone like ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1,name);
                statement.setString(2,phone);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()) {
                    boolean check = true;
                    if(user_id.isEmpty()){
                        check = true;
                    }else{
                        for (int i = 0; i < user_id.size(); i++) {
                            if(resultSet.getInt("userId")==user_id.get(i)) {
                                check = false;
                            }

                        }
                    }
                    if(check){
                        favourite = false;
                        out.println("<tr>\n" +
                                "                        <td>"+resultSet.getString("userId")+"</td>\n" +
                                "                        <td>"+resultSet.getString("full_name")+"</td>\n" +
                                "                        <td>"+resultSet.getString("phone")+"</td>\n" +
                                "                        <td>\n" +
                                "                            <button class='name1' onclick='add(\""+resultSet.getString("userId")+"\")'>\n" +
                                "                                Add\n" +
                                "                            </button>\n" +
                                "                            <button class='name1' onclick='select(\""+resultSet.getString("userId")+"\")'>\n" +
                                "                                Select\n" +
                                "                            </button>\n" +
                                "                        </td>\n" +
                                "                    </tr>");
                    }


                }
                if(favourite) {
                    out.println("<tr><td colspan=\"4\">No result</td></tr>");
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



    }


}