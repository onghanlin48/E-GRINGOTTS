package admin;

import db.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Year;
import java.time.YearMonth;
import java.util.Random;

@WebServlet("/apply_card")
public class apply_card extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        YearMonth currentYearMonth = YearMonth.now();

        int id = Integer.parseInt(req.getParameter("id"));
        String creditCardNumber = generateCreditCardNumber();
        int cvv  = generateCVV();
        int currentYear = (Year.now().getValue() % 100)+5;
        int currentMonth = currentYearMonth.getMonthValue();
        String date =currentMonth + "" + currentYear;

        String sql;
        String msg ;
        sql = "Insert into card (number,mmyy,cvv,id,try) values("+creditCardNumber+","+date+","+cvv+","+id+",0)";
        msg = "User ID " + id + " Card is Apply.";
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                int row = statement.executeUpdate();

                if(row > 0) {
                    out.println(msg);
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error connecting to the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static int calculateLuhnChecksum(int[] digits) {
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = digits.length - 1; i >= 0; i--) {
            int digit = digits[i];
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }
        return sum;
    }
    public static String generateCreditCardNumber() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        // Generate the first digit (major industry identifier)
        int majorIndustryIdentifier = 4; // For Visa cards
        builder.append(majorIndustryIdentifier);

        // Generate the next 15 digits (account identifier and checksum)
        for (int i = 0; i < 15; i++) {
            builder.append(random.nextInt(10)); // Random digits
        }

        // Generate the final digit (checksum using Luhn algorithm)
        int[] cardNumberDigits = new int[16];
        for (int i = 0; i < 16; i++) {
            cardNumberDigits[i] = Character.getNumericValue(builder.charAt(i));
        }
        int checksum = calculateLuhnChecksum(cardNumberDigits);
        builder.append((10 - (checksum % 10)) % 10);

        return builder.toString();
    }
    public static int generateCVV() {
        Random random = new Random();
        // Generate a random integer between 100 and 999 (inclusive)
        return random.nextInt(900) + 100;
    }
}