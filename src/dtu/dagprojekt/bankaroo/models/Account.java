package dtu.dagprojekt.bankaroo.models;

import javax.xml.bind.annotation.*;
import java.sql.ResultSet;
import java.sql.SQLException;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Account {

    @XmlElement(name="id", required = false) private int id;
    @XmlElement(name="name", required = true) private String name;
    @XmlElement(name="balance", required = false) private double balance;
    @XmlElement(name="customer", required = true) private long customer;
    @XmlElement(name="type", required = true) private String accountType;
    @XmlElement(name="currency", required = true) private String currency;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        if (Double.compare(account.getBalance(), getBalance()) != 0) return false;
        if (getCustomer() != account.getCustomer()) return false;
        if (getName() != null ? !getName().equals(account.getName()) : account.getName() != null) return false;
        if (getAccountType() != null ? !getAccountType().equals(account.getAccountType()) : account.getAccountType() != null)
            return false;
        return getCurrency() != null ? getCurrency().equals(account.getCurrency()) : account.getCurrency() == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getName() != null ? getName().hashCode() : 0;
        temp = Double.doubleToLongBits(getBalance());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (getCustomer() ^ (getCustomer() >>> 32));
        result = 31 * result + (getAccountType() != null ? getAccountType().hashCode() : 0);
        result = 31 * result + (getCurrency() != null ? getCurrency().hashCode() : 0);
        return result;
    }

    public enum Field {
        AccountID, AccountName, Balance, UserID, AccountTypeName, Currency
    }

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
        this.id = set.getInt(Field.AccountID.toString());
        this.name = set.getString(Field.AccountName.toString());
        this.balance = set.getDouble(Field.Balance.toString());
        this.customer = set.getInt(Field.UserID.toString());
        this.accountType = set.getString(Field.AccountTypeName.toString());
        this.currency = set.getString(Field.Currency.toString());
    }

    public Account(){}

    public String getId() {
        if (id <= 0){
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

    public long getCustomer() {
        return customer;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCustomer(long customer) {
        this.customer = customer;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
