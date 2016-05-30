package dtu.dagprojekt.bankaroo.models;

import dtu.dagprojekt.bankaroo.util.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer {
    private int cpr;
    private String name;
    private String salt;
    private String password;
    private int advisor;

    public Customer(ResultSet resultSet) throws SQLException {
        this.cpr = resultSet.getInt("CustomerID");
        this.name = resultSet.getString("CustomerName");
        this.salt = resultSet.getString("Salt");
        this.password = resultSet.getString("Password");
        this.advisor = resultSet.getInt("EmployeeID");
    }

    public Customer(int cpr, String name, String salt, String password, int advisor){
        this.cpr = cpr;
        this.name = name;
        this.salt = salt;
        this.password = password;
        this.advisor = advisor;
    }

    public int getCpr() {
        return cpr;
    }

    public String getName() {
        return name;
    }

    public String getSalt() {
        return salt;
    }

    public String getPassword() {
        return password;
    }

    public int getAdvisor() {
        return advisor;
    }

    @Override
    public String toString() {
        return "Customer: " + this.name + " (" + this.cpr + ")";
    }
}
