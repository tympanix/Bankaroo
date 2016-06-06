package dtu.dagprojekt.bankaroo.util;

import dtu.dagprojekt.bankaroo.models.Account;
import dtu.dagprojekt.bankaroo.models.User;
import dtu.dagprojekt.bankaroo.models.Employee;
import dtu.dagprojekt.bankaroo.param.Credentials;

import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.sql.*;

public class DB {

    static final String URL = "jdbc:db2://192.86.32.54:5040/DALLASB";
    static final String DRIVER = "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver";
    static final String USER = "DTU24";
    static final String PASSWORD = "FAGP2016";

    public static final String TABLE = "DTUGRP09";

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
            // Load the DRIVER
            Class.forName(DRIVER);
            System.out.println("**** Loaded the JDBC driver");

            // Create the connection using the IBM Data Server Driver for JDBC and SQLJ
            con = DriverManager.getConnection(URL, USER, PASSWORD);

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

    public static StreamingOutput getUser(String name) throws SQLException, IOException {
        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"User\" WHERE UPPER(\"UserName\") LIKE UPPER('%"+name+"%')");
        return query.toJson();
    }

    public static StreamingOutput getUser(long id) throws SQLException, IOException {
        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"UserView\" WHERE \"UserID\" = "+id+"");
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

    public static StreamingOutput getAccounts(long id) throws SQLException {
        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"Account\" WHERE \"UserID\" = "+id+"");
        return query.toJson();
    }

    public static void insertUser(User c) throws SQLException {
        Statement statement = DB.getConnection().createStatement();
        statement.executeUpdate("INSERT INTO \"DTUGRP09\".\"User\" VALUES("+c.getCpr()+" ,'"+c.getName()+"', '"+c.getZip()+"', '"+c.getAddress()+"', '"+c.getPhone()+"', '"+c.getEmail()+"', '"+c.getSalt()+"', '"+c.getHashPassword()+"')");
        statement.close();
        DB.getConnection().commit();
    }

    public static void insertAccount(Account a) throws SQLException {
        Statement statement = DB.getConnection().createStatement();
        statement.executeUpdate("INSERT INTO \"DTUGRP09\".\"Account\" VALUES("+a.getId()+" ,'"+a.getName()+"', '"+a.getBalance()+"', '"+a.getCustomer()+"', '"+a.getAccountType()+"', '"+a.getCurrency()+"')");
        statement.close();
        DB.getConnection().commit();
    }

    public static void insertEmployee(Employee e) throws SQLException {
        Statement statement = DB.getConnection().createStatement();
        statement.executeUpdate("INSERT INTO \"DTUGRP09\".\"Employee\" VALUES("+e.getId()+" ,'"+e.getName()+"', '"+e.getSalt()+"', '"+e.getHashPassword()+"')");
        statement.close();
        DB.getConnection().commit();
    }

    public static User login(Credentials c) throws SQLException {
        User user = getUserByCPR(c.getId());
        String hashPass = Utils.hashPassword(c.getPassword(), user.getSalt());
        if (hashPass.equals(user.getHashPassword())){
            return user;
        } else {
            throw new SQLException("Incorrect username/password");
        }
    }

    public static void deleteUser(long cpr) throws SQLException {
        Statement statement = DB.getConnection().createStatement();
        statement.executeUpdate("DELETE FROM \"DTUGRP09\".\"User\" WHERE \"UserID\" = '"+cpr+"'");
        statement.close();
        DB.getConnection().commit();
    }

    public static void deleteUser(User c) throws SQLException {
        deleteUser(c.getCpr());
    }

    public static void deleteEmployee(long id) throws SQLException {
        Statement statement = DB.getConnection().createStatement();
        statement.executeUpdate("DELETE FROM \"DTUGRP09\".\"Employee\" WHERE \"EmployeeID\" = '"+id+"'");
        statement.close();
        DB.getConnection().commit();
    }

    public static void deleteEmployee(Employee e) throws SQLException {
        deleteEmployee(e.getId());
    }

    public static void deleteAccount(int id) throws SQLException {
        Statement statement = DB.getConnection().createStatement();
        int updated = statement.executeUpdate("DELETE FROM \"DTUGRP09\".\"Account\" WHERE \"AccountID\" = '"+id+"'");
        statement.close();
        DB.getConnection().commit();
        if (updated == 0) throw new SQLException("No account found");
    }

    public static void deleteAccount(Account a) throws SQLException {
        int id = Integer.parseInt(a.getId());
        deleteAccount(id);
    }

    public static User getUserByCPR(long cpr) throws SQLException {
        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"User\" WHERE \"UserID\" = "+cpr+"");
        ResultSet res = query.getResultSet();
        if (!res.next()) throw new SQLException("No user");
        return new User(res);
    }

    public static StreamingOutput getHistory(int accountId) throws SQLException, IOException {
        StreamingOutput output;

        UpdateQuery q = new UpdateQuery();
        q.select(Account.Field.All);
        q.from(Schema.HistoryView);
        q.where(Account.Field.AccountID).equals(accountId);
        output = q.execute();
        q.expect(1);




        Query query = new Query("SELECT * FROM \"DTUGRP09\".\"HistoryView\" WHERE \"AccountID\" = "+accountId+"");
        return query.toJson();
        // Lav new execute
    }

    public static UpdateQuery transaction(double amount, String currency, int accountFrom, int accountTo, String messageFrom, String messageTo) throws SQLException {
        UpdateQuery q = new UpdateQuery();
        q.call(Procedure.Transaction);
        q.params(amount,currency,accountFrom,accountTo,messageFrom,messageTo);
        q.execute();
        q.expect(1);
    }

    public static UpdateQuery changePassword(long cpr, String password) throws SQLException {
        String salt = Utils.newSalt();
        String hashedPassword = Utils.hashPassword(password, salt);

        UpdateQuery q = new UpdateQuery();
        q.update(Schema.User)
                .set(User.Field.Salt, salt)
                .set(User.Field.Password, hashedPassword)
                .where(User.Field.UserID).equal(cpr);
        q.execute();
        q.expect(1);
    }

    public static UpdateQuery updateUser(User user) throws SQLException {
        UpdateQuery q = new UpdateQuery();
        q.update(Schema.User);
        q.set(user.getUpdatedFields());
        q.where(User.Field.UserID).equal(user.getCpr());
        q.execute();
        q.expect(1);
    }

}
