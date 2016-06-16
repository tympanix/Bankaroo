import dtu.dagprojekt.bankaroo.database.DB;
import dtu.dagprojekt.bankaroo.models.Account;
import org.junit.Before;
import org.junit.Test;


import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class AccountTest {

    private static final int ID = 11;
    private static final String NAME = "My new account 3";
    private static final double BALANCE = 199;
    private static final int USER = 110793;
    private static final String TYPE = "Savings";
    private static final String CURRENCY = "DKK";

    @Test
    public void A_init() {
        try {
            DB.deleteAccount(ID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  /*  @Test
    public void B_newAccount() throws SQLException {
        Account a = new Account(0, NAME, BALANCE, USER, TYPE, CURRENCY);
        DB.insertAccount(a);
    }

    @Test
    public void C_findAccount() throws SQLException {
        DB.deleteAccount(ID);
    }*/

    @Test
    public void testCurrency(){
        Account a = new Account(0, NAME, BALANCE, USER, TYPE, CURRENCY);
        a.setCurrency("USD");
        assertEquals(a.getCurrency(), "USD");
    }

    @Test
    public void testID(){
        Account a = new Account(0, NAME, BALANCE, USER, TYPE, CURRENCY);
        a.setId(2);
        assertEquals(a.getId(), "2");
    }

    @Test
    public void testName(){
        Account a = new Account(0, NAME, BALANCE, USER, TYPE, CURRENCY);
        a.setName("a");
        assertEquals(a.getName(), "a");
    }

    @Test
    public void testBalance(){
        Account a = new Account(0, NAME, BALANCE, USER, TYPE, CURRENCY);
        a.setBalance(200);
        assertTrue(a.getBalance() == 200);
    }

    @Test
    public void testCustomer(){
        Account a = new Account(0, NAME, BALANCE, USER, TYPE, CURRENCY);
        a.setCustomer(1);
        assertEquals(a.getCustomer(), 1);
    }

    @Test
    public void testAccountType(){
        Account a = new Account(0, NAME, BALANCE, USER, TYPE, CURRENCY);
        a.setAccountType("test");
        assertEquals(a.getAccountType(), "test");
    }


}
