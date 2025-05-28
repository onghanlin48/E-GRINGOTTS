package admin;

import db.DBConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/user_list")
public class user_list extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM account a JOIN category c ON c.id = a.category WHERE a.userId != 1";
            String ca = "SELECT * FROM category WHERE id != ? AND id != 4";
            String card = "SELECT * FROM card WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("userId");
                    String name = resultSet.getString("full_name");
                    String phone = resultSet.getString("phone");
                    int status = resultSet.getInt("status");
                    int category = resultSet.getInt("category");
                    String name_category = resultSet.getString("name");

                    out.println("<tr>\n" +
                            "                        <td>"+id+"</td>\n" +
                            "                        <td>"+name+"</td>\n" +
                            "                        <td>"+phone+"</td>\n" +
                            "                        <td>\n" );

                            if(status == 1) {
                                out.println("<button class='name1' onclick='approved("+id+",1)'>\n" +
                                        "                                Approve\n" +
                                        "                            </button>\n" +
                                        "                            <button class='name1' onclick='approved("+id+",2)'>\n" +
                                        "                                Block\n" +
                                        "                            </button>\n");
                            }else if(status == 2) {
                                out.println(" <button class='name1' onclick='approved("+id+",2)'>\n" +
                                        "                                Block\n" +
                                        "                            </button>");
                            }else{
                                out.println(" <button class='name1' onclick='approved("+id+",1)'>\n" +
                                        "                                Unblock\n" +
                                        "                            </button>");
                            }

                            out.println("                        </td>\n" +
                            "                        <td>\n" +
                            "                            <select class=\"did-floating-input\" id=\"c_"+id+"\" >\n");

                            out.println("<option value=\""+category+"\">"+name_category+"</option>\n");

                            try(PreparedStatement statement1 = connection.prepareStatement(ca) ){
                                statement1.setInt(1, category);

                                ResultSet resultSet1 = statement1.executeQuery();
                                while(resultSet1.next()) {
                                    out.println("<option value=\""+resultSet1.getInt("id")+"\">"+resultSet1.getString("name")+"</option>\n");
                                }
                            }

                            out.println("                            </select>\n" +
                            "                            <button class='name1' onclick='category(\""+id+"\")'>\n" +
                            "                                Submit\n" +
                            "                            </button>\n" +
                            "                        </td>\n" +
                            "                        <td>\n");
                            try(PreparedStatement statement2 = connection.prepareStatement(card) ){
                                statement2.setInt(1, id);

                                ResultSet resultSet2 = statement2.executeQuery();
                                if(resultSet2.next()) {
                                    out.println("                            <button class='name1' onclick='reset("+id+")'>\n" +
                                            "                                Reset\n" +
                                            "                            </button>\n");
                                    out.println("                            <button class='name1' onclick='dele("+id+")'>\n" +
                                            "                                Delete\n" +
                                            "                            </button>\n");
                                }else{
                                    out.println("                            <button class='name1' onclick='apply("+id+")'>\n" +
                                            "                                Apply\n" +
                                            "                            </button>\n");
                                }
                            }
                           out.println("                        </td>\n" +
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
    }


}