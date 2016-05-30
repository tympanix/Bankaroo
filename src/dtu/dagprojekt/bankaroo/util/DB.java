package dtu.dagprojekt.bankaroo.util;

import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.UUID;

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

    public static void addNewCustomer(int cpr, String name, String password) throws SQLException, NoSuchAlgorithmException {

        byte[] pass = password.getBytes(StandardCharsets.UTF_8);
        byte[] salt = Utils.newSalt();
        byte[] saltpass = Utils.concat(pass, salt);

        String strhash = Utils.bytesToHex(salt);
        String hashpass = Utils.bytesToHex(Utils.sha256(saltpass));

        Statement statement = DB.getConnection().createStatement();

        int updated = statement.executeUpdate("INSERT INTO \"DTUGRP09\".\"Customer\" VALUES("+cpr+" ,'" + name + "', '" + strhash + "', '" + hashpass + "', '1')");

        statement.close();
        DB.getConnection().commit();
    }

}
