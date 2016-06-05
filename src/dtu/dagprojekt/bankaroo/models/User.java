package dtu.dagprojekt.bankaroo.models;

import dtu.dagprojekt.bankaroo.util.Utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.ResultSet;
import java.sql.SQLException;

@XmlRootElement
public class User {

    @XmlElement(name="cpr") private long cpr;
    @XmlElement(name="name") private String name;
    @XmlElement(name="zip") private int zip;
    @XmlElement(name="address") private String address;
    @XmlElement(name="phone") private int phone;
    @XmlElement(name="email") private String email;
    @XmlElement(name="salt", required = false) private String salt;
    @XmlElement(name="password") private String plainPassword;
    private String hashPassword;

    public User(ResultSet resultSet) throws SQLException {
        this.cpr = resultSet.getInt("UserID");
        this.name = resultSet.getString("UserName");
        this.zip = resultSet.getInt("PostalCode");
        this.address = resultSet.getString("Address");
        this.phone = resultSet.getInt("Phone");
        this.email = resultSet.getString("Email");
        this.salt = resultSet.getString("Salt");
        this.hashPassword = resultSet.getString("Password");
    }

    public User(long cpr, String name, int zip, String address, int phone, String email, String plainPassword) {
        String salt = Utils.newSalt();
        String hashedPassword = Utils.hashPassword(plainPassword, salt);

        this.cpr = cpr;
        this.name = name;
        this.zip = zip;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.salt = salt;
        this.plainPassword = plainPassword;
        this.hashPassword = hashedPassword;
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

    public String getPlainPassword() {
        return plainPassword;
    }

    public int getZip() {
        return zip;
    }

    public String getAddress() {
        return address;
    }

    public int getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Customer: " + this.name + " (" + this.cpr + ")";
    }
}
