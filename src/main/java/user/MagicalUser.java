package user;
import data.Account;
import data.Card;

import java.util.*;
class MagicalUser<T> {
    private T userType;
    private Account account;
    private Card card;
    private List<Transaction> transactions;

    public MagicalUser(T userType, Account account, Card card) {
        this.userType = userType;
        this.account = account;
        this.card = card;
        this.transactions = new ArrayList<>();
    }

    // Add getters and setters
    public T getUserType() {
        return userType;
    }

    public void setUserType(T userType) {
        this.userType = userType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
