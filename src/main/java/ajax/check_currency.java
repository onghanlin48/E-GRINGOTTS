package ajax;


import java.util.ArrayList;

public class check_currency {
    private String currency;
    private ArrayList<String> amount;
    private double total;
    private int user_id;
    private String name;
    private String phone;
    private String reference;
    private int to;
    private String msg;
    private String c_s;

    public check_currency() {}

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public ArrayList<String> getAmount() {
        return amount;
    }
    public void setAmount(ArrayList<String> amount) {
        this.amount = amount;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public int getTo() {
        return to;
    }
    public void setTo(int to) {
        this.to = to;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getC_s() {
        return c_s;
    }
    public void setC_s(String c_s) {
        this.c_s = c_s;
    }
    public void clear() {
        this.amount = null;
        this.currency = null;
        this.total = 0;
        this.user_id = 0;
        this.name = null;
        this.phone = null;
        this.reference = null;
        this.to = 0;
        this.msg = null;
        this.c_s = null;
    }
}
