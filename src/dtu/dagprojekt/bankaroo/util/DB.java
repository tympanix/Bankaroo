package dtu.dagprojekt.bankaroo.util;

import dtu.dagprojekt.bankaroo.models.Customer;

import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class DB {

    static final String url = "jdbc:db2://192.86.32.54:5040/DALLASB";
    static final String driver = "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver";
    static final String user = "DTU24";
    static final String password = "FAGP2016";

    static private Connection con;

    public static Connection getConnection(){
        if (con != null){
            return con;
        } else {
            return newConnection();
        }
    }

    private static Connection newConnection(){
        try {
            // Load the driver
            Class.forName(driver);
            System.out.println("**** Loaded the JDBC driver");

            // Create the connection using the IBM Data Server Driver for JDBC and SQLJ
            con = DriverManager.getConnection(url, user, password);

            // Commit changes manually
            con.setAutoCommit(false);
            System.out.println("**** Created a JDBC connection to the data source");

        } catch (SQLException ex) {
            System.err.println("SQLException information");
            while (ex != null) {
                System.err.println("Error msg: " + ex.getMessage());
                System.err.println("SQLSTATE: " + ex.getSQLState());
                System.err.println("Error code: " + ex.getErrorCode());
                ex.printStackTrace();
                ex = ex.getNextException(); // For drivers that support chained exceptions
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return con;
    }

    public void close() throws SQLException {
        // Close the connection
        con.close();
    }


    public static StreamingOutput getEmployees() throws SQLException, IOException {
        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"Employee\"");
        return query.toJson();
    }

    public static StreamingOutput getAccounts() throws SQLException, IOException {
        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"Account\"");
        return query.toJson();
    }

    public static StreamingOutput getCustomers() throws SQLException, IOException {
        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"Customer\"");
        return query.toJson();
    }

    public static StreamingOutput getExchanges() throws SQLException, IOException {
        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"Exchange\"");
        return query.toJson();
    }

    public static StreamingOutput getHistory() throws SQLException, IOException {
        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"History\"");
        return query.toJson();
    }

    public static StreamingOutput getTransactions() throws SQLException, IOException {
        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"Transaction\"");
        return query.toJson();
    }

    public static StreamingOutput getAccountType() throws SQLException, IOException {
        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"AccountType\"");
        return query.toJson();
    }

    public static void addNewCustomer(int cpr, String name, String password) throws SQLException {
        String salt = Utils.newSalt();
        String hashPass = Utils.hashPassword(password, salt);

        Customer customer = new Customer(cpr, name, salt, hashPass, 1);
        insertCustomer(customer);
    }

    private static void insertCustomer(Customer c) throws SQLException {
        Statement statement = DB.getConnection().createStatement();
        statement.executeUpdate("INSERT INTO \"DTUGRP09\".\"Customer\" VALUES("+c.getCpr()+" ,'"+c.getName()+"', '"+c.getSalt()+"', '"+c.getPassword()+"', "+c.getAdvisor()+")");
        statement.close();
        DB.getConnection().commit();
    }

    public static Customer login(int cpr, String password) throws SQLException {
        Customer customer = getCustomerByCPR(cpr);
        String hashpass = Utils.hashPassword(password, customer.getSalt());
        if (hashpass.equals(customer.getPassword())){
            return customer;
        } else {
            throw new SQLException("Incorrect username/password");
        }
    }

    public static Customer getCustomerByCPR(int cpr) throws SQLException {
        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"Customer\" WHERE \"CustomerID\" = "+cpr+"");
        ResultSet res = query.getResultSet();
        if (!res.next()) throw new SQLException("No user");
        return new Customer(res);
    }
}
