import dtu.dagprojekt.bankaroo.database.DB;
import dtu.dagprojekt.bankaroo.database.Query;
import dtu.dagprojekt.bankaroo.models.Transaction;
import dtu.dagprojekt.bankaroo.models.User;
import dtu.dagprojekt.bankaroo.util.Utils;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Created by GustavMadslund on 16/06/2016.
 */
public class DBTest {

    @Test
    public void testExchanges() throws IOException, SQLException {
        String query = "SELECT * FROM \"DTUGRP09\".\"Exchange\"";
        Query q = DB.getExchanges();
        assertEquals(query, q.toString().trim());

    }

    @Test
    public void testAccountType() throws IOException, SQLException {
        String query = "SELECT * FROM \"DTUGRP09\".\"AccountType\"";
        Query q = DB.getAccountType();
        assertEquals(query, q.toString().trim());
    }

    @Test
    public void testAccounts() throws IOException, SQLException {
        String query = "SELECT * FROM \"DTUGRP09\".\"Account\"";
        Query q = DB.getAccounts();
        assertEquals(query, q.toString().trim());
    }

    @Test
    public void testAccountByUser() throws SQLException {
        String query = "SELECT * FROM \"DTUGRP09\".\"Account\" WHERE \"UserID\" = ?";
        Query q = DB.getAccountsByUser(1234567890);
        assertEquals(query, q.toString().trim());
    }

    @Test
    public void testSearchUser() throws IOException, SQLException {
        String query = "SELECT * FROM \"DTUGRP09\".\"User\" WHERE UPPER(\"UserName\") LIKE UPPER(?) OR CAST(\"UserID\" AS VARCHAR(10)) LIKE ?";
        Query q = DB.searchUser("test");
        assertEquals(query, q.toString().trim());
    }

    @Test
    public void testPermissions() throws SQLException {
        User u = new User(1, "Test", 2800, "Testvej", 12345678, "test@testmail.com", "test");
        String query = "SELECT * FROM \"DTUGRP09\".\"UserRoles\" WHERE \"UserID\" = ?";
        Query q = DB.getPermissions(u);
        assertEquals(query, q.toString().trim());
    }

    @Test
    public void testHistory() throws IOException, SQLException {
        String query = "SELECT * FROM \"DTUGRP09\".\"HistoryView\" WHERE \"AccountID\" = ?ORDER BY \"TransactionTime\" DESC";
        Query q = DB.getHistory(1);
        assertEquals(query, q.toString().trim());
    }

    @Test
    public void testTransaction() throws SQLException {
        Transaction t = new Transaction(1, "DKK", 1, 2, "Test", "Test");
        String query = "CALL \"DTUGRP09\".Transaction(?, ?, ?, ?, ?, ?)";
        Query q = DB.transaction(t);
        assertEquals(query, q.toString().trim());
    }

    @Test
    public void testTransaction2() throws SQLException {
        String query = "CALL \"DTUGRP09\".Transaction(?, ?, ?, ?, ?, ?)";
        Query q = DB.transaction(1, "DKK", 1, 2, "Test", "Test");
        assertEquals(query, q.toString().trim());
    }

}
