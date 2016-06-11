package dtu.dagprojekt.bankaroo.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Transaction {

    public enum Field {
        TransactionID, TransactionTime, AccountIDFrom, AccountIDTo, AmountForeign, Currency
    }

    private Date transactionTime;
    private int id;

    @XmlElement(name="amount") private double amount;
    @XmlElement(name="currency") private String currency;
    @XmlElement(name="accountFrom") private int accountFrom;
    @XmlElement(name="accountTo") private int accountTo;
    @XmlElement(name="messageFrom", defaultValue = "") private String messageFrom;
    @XmlElement(name="messageTo", defaultValue = "") private String messageTo;

    @XmlElement(name="password", defaultValue = "") private String password;

    public Object[] getParams(){
        return new Object[]{
                this.amount,
                this.currency,
                this.accountFrom,
                this.accountTo,
                this.messageFrom,
                this.messageTo};
    }

    public Transaction() {
    }

    public Transaction(double amount, String currency, int accountFrom, int accountTo, String messageFrom, String messageTo) {
        this.amount = amount;
        this.currency = currency;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
    }

    public int getId() {
        return id;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public String getMessageTo() {
        return messageTo;
    }

    public void setMessageTo(String messageTo) {
        this.messageTo = messageTo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
