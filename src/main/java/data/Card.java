package data;

public class Card {
    private String cardNum;
    private String CVV;
    private String expiryDate;
    private String PIN;
    // Add more card information as needed

    public Card(String cardNum, String CVV, String expiryDate, String PIN) {
        this.cardNum = cardNum;
        this.CVV = CVV;
        this.expiryDate = expiryDate;
        this.PIN = PIN;
    }

    public Card() {

    }


    // Add getters and setters
    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String userId) {
        this.PIN = userId;
    }
}
