package dtu.dagprojekt.bankaroo.models;

import dtu.dagprojekt.bankaroo.util.Utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.ResultSet;
import java.sql.SQLException;

@XmlRootElement
public class Employee {

    @XmlElement(name="id") private int id;
    @XmlElement(name="name") private String name;
    @XmlElement(name="salt") private String salt;
    @XmlElement(name="password") private String plainPassword;
    private String hashPassword;

    public Employee(ResultSet set) throws SQLException {
        this.id = set.getInt("EmployeeID");
        this.name = set.getString("EmployeeName");
        this.salt = set.getString("Salt");
        this.hashPassword = set.getString("Password");
    }

    public Employee(int id, String name, String password) {
        String salt = Utils.newSalt();
        String hashedPassword = Utils.hashPassword(password, salt);

        this.id = id;
        this.name = name;
        this.salt = salt;
        this.hashPassword = hashedPassword;
    }

    public int getId() {
        return id;
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
}
