package dtu.dagprojekt.bankaroo.util;

import dtu.dagprojekt.bankaroo.models.Account;
import dtu.dagprojekt.bankaroo.models.Transaction;
import dtu.dagprojekt.bankaroo.models.User;
import dtu.dagprojekt.bankaroo.models.UserRoles;
import dtu.dagprojekt.bankaroo.param.Credentials;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.sql.*;

public class DB {

    static final String URL = "jdbc:db2://192.86.32.54:5040/DALLASB";
    static final String DRIVER = "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver";
    static final String USER = "DTU24";
    static final String PASSWORD = "FAGP2016";

    public static final String TABLE = "DTUGRP09";

    static private Connection con;

    public static Connection getConnection() throws SQLException {
        if (con == null){
            return newConnection();
        } else if (con.isClosed())  {
            return newConnection();
        } else {
            return con;
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

    public static Query getExchanges() throws SQLException, IOException {
        return new Query()
                .select().all().from(Schema.Exchange)
                .execute();
    }

    public static Query getAccountType() throws SQLException, IOException {
        return new Query()
                .select().all().from(Schema.AccountType)
                .execute();
    }

    public static Query getAccounts() throws SQLException, IOException {
        return new Query()
                .select().all().from(Schema.Account)
                .execute();
    }

    public static Query getAccountsByUser(long userId) throws SQLException {
        return new Query()
                .select().all().from(Schema.Account)
                .where(User.Field.UserID).equal(userId)
                .execute();
    }

    public static Account getAccountById(int accountId) throws SQLException {
        Query q = new Query()
                .select().all().from(Schema.Account)
                .where(Account.Field.AccountID).equal(accountId)
                .execute();

        ResultSet result = q.resultSet();
        if (!result.next()) throw new SQLException("No account");
        Account account = new Account(result);
        q.close();
        return account;
    }

    public static Query searchUser(String search) throws SQLException, IOException {
        return new Query()
                .select().all().from(Schema.User)
                .where().upperLike(User.Field.UserName, search)
                .or().cast(User.Field.UserID, JDBCType.VARCHAR, 10).startsLike(search)
                .execute();
    }

    public static Query getUser(long id) throws SQLException, IOException {
        return new Query()
                .select().all().from(Schema.UserView)
                .where(User.Field.UserID).equal(id)
                .execute();
    }

    public static Query insertUser(User c) throws SQLException {
        return new Query()
                .insert(Schema.User)
                .values(c.getCpr(), c.getName(), c.getZip(), c.getAddress(), c.getPhone(), c.getEmail(), c.getSalt(), c.getHashPassword())
                .execute().expect(1).close();
    }

    public static Query insertAccount(Account a) throws SQLException {
        return new Query()
                .insert(Schema.Account)
                .values(a.getId(), a.getName(), a.getBalance(), a.getCustomer(), a.getAccountType(), a.getCurrency())
                .execute().expect(1).close();
    }

    public static User login(Credentials c) throws SQLException, ValidationException {
        c.validate();
        User user = new User(getUserByCPR(c.getId()));
        String hashPass = Utils.hashPassword(c.getPassword(), user.getSalt());

        if (hashPass.equals(user.getHashPassword())){
            return user;
        } else {
            throw new SQLException("Incorrect username/password");
        }
    }

    public static Query getPermissions(User user) throws SQLException {
        return new Query()
                .select().all().from(Schema.UserRoles)
                .where(UserRoles.Field.UserID).equal(user.getCpr())
                .execute();
    }

    public static void deleteUser(long cpr) throws SQLException {
        new Query()
                .delete().from(Schema.User)
                .where(User.Field.UserID).equal(cpr)
                .execute().expect(1).close();
    }

    public static void deleteUser(User c) throws SQLException {
        deleteUser(c.getCpr());
    }

    public static void deleteAccount(int id) throws SQLException {
        new Query()
                .delete().from(Schema.Account)
                .where(Account.Field.AccountID).equal(id)
                .execute().expect(1).close();
    }

    public static void deleteAccount(Account a) throws SQLException {
        int id = Integer.parseInt(a.getId());
        deleteAccount(id);
    }

    public static Query getUserByCPR(long cpr) throws SQLException {
        return new Query()
                .select().all().from(Schema.UserView)
                .where(User.Field.UserID).equal(cpr)
                .execute();
    }

    public static Query getHistory(int accountId) throws SQLException, IOException {
        return new Query()
                .select().all().from(Schema.HistoryView)
                .where(Account.Field.AccountID).equal(accountId)
                .orderBy(Transaction.Field.TransactionTime, Query.DESC)
                .execute();
    }

    public static Query transaction(double amount, String currency, int accountFrom, int accountTo, String messageFrom, String messageTo) throws SQLException {
        return new Query()
                .call(Procedure.Transaction)
                .params(amount, currency, accountFrom, accountTo, messageFrom, messageTo)
                .execute().close();

    }

    public static Query transaction(Transaction transaction) throws SQLException {
        return new Query()
                .call(Procedure.Transaction)
                .params(transaction.getParams())
                .execute().close();
    }

    public static Query changePassword(long cpr, String password) throws SQLException {
        String salt = Utils.newSalt();
        String hashedPassword = Utils.hashPassword(password, salt);

        return new Query()
                .update(Schema.User)
                .set(User.Field.Salt, salt)
                .set(User.Field.Password, hashedPassword)
                .where(User.Field.UserID).equal(cpr)
                .execute().expect(1).close();
    }

    public static Query updateUser(long id, User user) throws SQLException {
        return new Query()
                .update(Schema.User)
                .set(user.getUpdatedFields())
                .where(User.Field.UserID).equal(id)
                .execute().expect(1).close();
    }

}
