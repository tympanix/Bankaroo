package dtu.dagprojekt.bankaroo.models;

import dtu.dagprojekt.bankaroo.util.Utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.ResultSet;
import java.sql.SQLException;

@XmlRootElement
public class Customer {

    @XmlElement(name="cpr") private long cpr;
    @XmlElement(name="name") private String name;
    @XmlElement(name="salt", required = false) private String salt;
    @XmlElement(name="advisor") private int advisor;
    @XmlElement(name="password") private String plainPassword;
    private String hashPassword;

    public Customer(ResultSet resultSet) throws SQLException {
        this.cpr = resultSet.getInt("CustomerID");
        this.name = resultSet.getString("CustomerName");
        this.salt = resultSet.getString("Salt");
        this.hashPassword = resultSet.getString("Password");
        this.advisor = resultSet.getInt("EmployeeID");
    }

    public Customer(int cpr, String name, String plainPassword, int advisor){
        String salt = Utils.newSalt();
        String hashedPassword = Utils.hashPassword(plainPassword, salt);

        this.cpr = cpr;
        this.name = name;
        this.salt = salt;
        this.hashPassword = hashedPassword;
        this.advisor = advisor;
    }

    public long getCpr() {
        return cpr;
    }

    public String getName() {
        return name;
    }

    public String getSalt() {
        return salt;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public int getAdvisor() {
        return advisor;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    @Override
    public String toString() {
        return "Customer: " + this.name + " (" + this.cpr + ")";
    }
}
