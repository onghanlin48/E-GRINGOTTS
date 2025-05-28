package data;
import java.util.*;

// Class to hold account metadata
public class Account {

    private int userId;
    private String username;
    private String password;
    private String DOB;
    private String address;
    private String phone;
    private String email;
    private String code;
    private String full_name;
    private int category;
    // Add more metadata as needed

    public Account(){}



    public Account(int userId, String username,String full_name,String password, String DOB, String address, String phone, String email, int category) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.DOB = DOB;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.full_name = full_name;
        this.category = category;
    }

    // Add getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
public String getPhone() {
        return phone;
}
public void setPhone(String phone) {
        this.phone = phone;
}

public String getEmail() {
        return email;
}
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getFull_name() {
        return full_name;
    }
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    public int getCategory() {
        return category;
    }
    public void setCategory(int category) {
        this.category = category;
    }
    public void clear(){
        userId = 0;
        username = null;
        password = null;
        DOB = null;
        address = null;
        phone = null;
        email = null;
        code = null;
        full_name = null;
        category = -1;
    }
}

