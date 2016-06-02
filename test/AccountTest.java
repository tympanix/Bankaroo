import dtu.dagprojekt.bankaroo.models.Account;
import dtu.dagprojekt.bankaroo.util.DB;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountTest {

    private static final int ID = 11;
    private static final String NAME = "My new account 2";
    private static final double BALANCE = 199;
    private static final int CUSTOMER = 110793;
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

    @Test
    public void B_newAccount() throws SQLException {
        Account account = new Account(ID, NAME, BALANCE, CUSTOMER, TYPE, CURRENCY);
        DB.insertAccount(account);
    }

    @Test
    public void C_findAccount() throws SQLException {
        DB.deleteAccount(ID);
    }
}
