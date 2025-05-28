package user;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Transaction {
    private String inv;
    private String toUserId;
    private double amount;
    private String currency;
    private String dateOfTrans;
    private String category;
    private String reference;
    private int status;
    private String method;

    public Transaction(String inv, String toUserId, double amount, String currency, String dateString, String category, String reference, int status,String method) throws ParseException {
        this.inv = inv;
        this.toUserId = toUserId;
        this.currency = currency;
        this.dateOfTrans = dateString;
        this.category = category;
        this.reference = reference;
        this.status = status;
        this.method = method;

        // Adjust the amount based on the status
        if (status == 2) {
            this.amount = -amount;
        } else {
            this.amount = amount;
        }
    }
    public String getInv() {
        return inv;
    }
    public void setInv(String inv) {
        this.inv = inv;
    }
    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDateOfTrans() {
        return dateOfTrans;
    }

    public void setDateOfTrans(String dateOfTrans) {
        this.dateOfTrans = dateOfTrans;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        String to;
        if (status == 2) {
            to = "To : ";
        } else {
           to = "Received From : ";
        }

        return "<div class=\"history\">\n" +
                "            <div>"+inv+"<br>"+dateOfTrans+"<br>"+to+toUserId+"<br>Reference : "+reference+"<br>Category : "+category+"<br>Payment Method : "+method+"</div>\n" +
                "            <div><p>"+amount+" "+currency+"</p></div>\n" +
                "        </div>";
    }
}


