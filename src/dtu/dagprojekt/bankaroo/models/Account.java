package dtu.dagprojekt.bankaroo.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.ResultSet;
import java.sql.SQLException;

@XmlRootElement
public class Account {

    @XmlElement(name="id", required = false) private int id;
    @XmlElement(name="name", required = true) private String name;
    @XmlElement(name="balance", required = false) private double balance;
    @XmlElement(name="customer", required = true) private int customer;
    @XmlElement(name="type", required = true) private String accountType;
    @XmlElement(name="currency", required = true) private String currency;

    public Account(int id, String name, double balance, int customer, String accountType, String currency) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.customer = customer;
        this.accountType = accountType;
        this.currency = currency;
    }

    public Account(String name, int customer, String accountType, String currency) {
        this.id = -1;
        this.name = name;
        this.balance = 0;
        this.customer = customer;
        this.accountType = accountType;
        this.currency = currency;
    }

    public Account(ResultSet set) throws SQLException {
        this.id = set.getInt("AccountID");
        this.name = set.getString("AccountName");
        this.balance = set.getDouble("Balance");
        this.customer = set.getInt("CustomerID");
        this.accountType = set.getString("AccountTypeName");
        this.currency = set.getString("Currency");
    }

    public Account(){}

    public String getId() {
        if (id < 0){
            return "DEFAULT";
        } else {
            return String.valueOf(id);
        }
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public int getCustomer() {
        return customer;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getCurrency() {
        return currency;
    }
}