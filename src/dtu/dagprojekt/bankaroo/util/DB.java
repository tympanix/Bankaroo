package dtu.dagprojekt.bankaroo.util;

import dtu.dagprojekt.bankaroo.models.Account;
import dtu.dagprojekt.bankaroo.models.User;
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

    public static UpdateQuery getExchanges() throws SQLException, IOException {
        return new UpdateQuery()
                .select().all().from(Schema.Exchange)
                .execute();
    }

    public static UpdateQuery getAccountType() throws SQLException, IOException {
        return new UpdateQuery()
                .select().all().from(Schema.AccountType)
                .execute();
    }

    public static UpdateQuery getAccounts() throws SQLException, IOException {
        return new UpdateQuery()
                .select().all().from(Schema.Account)
                .execute();
    }

    public static UpdateQuery getUser(String name) throws SQLException, IOException {
        return new UpdateQuery()
                .select().all().from(Schema.User)
                .where().upperLike(User.Field.UserName, name)
                .execute();
    }

    public static UpdateQuery getUser(long id) throws SQLException, IOException {
        return new UpdateQuery()
                .select().all().from(Schema.UserView)
                .where(User.Field.UserID).equal(id)
                .execute();
    }

    public static UpdateQuery getAccounts(long id) throws SQLException {
        return new UpdateQuery()
                .select().all().from(Schema.Account)
                .where(User.Field.UserID).equal(id)
                .execute();
    }

    public static UpdateQuery insertUser(User c) throws SQLException {
        return new UpdateQuery()
                .insert(Schema.User)
                .values(c.getCpr(), c.getName(), c.getZip(), c.getAddress(), c.getPhone(), c.getEmail(), c.getSalt(), c.getHashPassword())
                .execute().expect(1).close();
    }

    public static UpdateQuery insertAccount(Account a) throws SQLException {
        return new UpdateQuery()
                .insert(Schema.Account)
                .values(a.getId(), a.getName(), a.getBalance(), a.getCustomer(), a.getAccountType(), a.getCurrency())
                .execute().expect(1).close();
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
        new UpdateQuery()
                .delete().from(Schema.User)
                .where(User.Field.UserID).equal(cpr)
                .execute().expect(1).close();
    }

    public static void deleteUser(User c) throws SQLException {
        deleteUser(c.getCpr());
    }

    public static void deleteAccount(int id) throws SQLException {
        new UpdateQuery()
                .delete().from(Schema.Account)
                .where(Account.Field.AccountID).equal(id)
                .execute().expect(1).close();
    }

    public static void deleteAccount(Account a) throws SQLException {
        int id = Integer.parseInt(a.getId());
        deleteAccount(id);
    }

    public static User getUserByCPR(long cpr) throws SQLException {
        UpdateQuery q = new UpdateQuery()
                .select().all().from(Schema.User)
                .where(User.Field.UserID).equal(cpr)
                .execute();

        ResultSet result = q.resultSet();
        if (!result.next()) throw new SQLException("No user");
        User user = new User(result);
        q.close();
        return user;
    }

    public static UpdateQuery getHistory(int accountId) throws SQLException, IOException {
        return new UpdateQuery()
                .select().all().from(Schema.HistoryView)
                .where(Account.Field.AccountID).equal(accountId)
                .execute();
    }

    public static UpdateQuery transaction(double amount, String currency, int accountFrom, int accountTo, String messageFrom, String messageTo) throws SQLException {
        return new UpdateQuery()
                .call(Procedure.Transaction)
                .params(amount, currency, accountFrom, accountTo, messageFrom, messageTo)
                .execute().expect(1).close();

    }

    public static UpdateQuery changePassword(long cpr, String password) throws SQLException {
        String salt = Utils.newSalt();
        String hashedPassword = Utils.hashPassword(password, salt);

        return new UpdateQuery()
                .update(Schema.User)
                .set(User.Field.Salt, salt)
                .set(User.Field.Password, hashedPassword)
                .where(User.Field.UserID).equal(cpr)
                .execute().expect(1).close();
    }

    public static UpdateQuery updateUser(long id, User user) throws SQLException {
        return new UpdateQuery()
                .update(Schema.User)
                .set(user.getUpdatedFields())
                .where(User.Field.UserID).equal(id)
                .execute().expect(1).close();
    }

}
