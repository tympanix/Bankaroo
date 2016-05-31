import dtu.dagprojekt.bankaroo.models.Customer;
import dtu.dagprojekt.bankaroo.param.Credentials;
import dtu.dagprojekt.bankaroo.util.DB;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerTest {

    private static final int CPR = 110793;
    private static final String NAME = "Bjørn";
    private static final String PASSWORD = "password123";
    private static final int ADVISOR = 1;

    @Test
    public static void A_init(){
        try {
            DB.deleteCustomer(CPR);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public static void B_createCustomer() throws SQLException {
        Customer customer = new Customer(CPR, NAME, PASSWORD, ADVISOR);
        DB.insertCustomer(customer);
    }

    @Test
    public static void C_findCustomer() throws SQLException {
        Customer customer = DB.getCustomerByCPR(CPR);
        assertEquals(customer.getCpr(), CPR);
        assertEquals(customer.getName(), NAME);
    }

    @Test
    public static void D_loginCustomer() throws SQLException {
        Credentials credentials = new Credentials(CPR, PASSWORD);
        Customer customer = DB.login(credentials);
        assertEquals(customer.getCpr(), CPR);
        assertEquals(customer.getName(), NAME);
    }

    @Test(expected=SQLException.class)
    public static void E_cantLogin() throws SQLException {
        Credentials credentials = new Credentials(CPR, "wrong_password");
        Customer customer = DB.login(credentials);
        assertNull(customer);
    }

    @Test
    public static void E_removeCustomer() throws SQLException {
        DB.deleteCustomer(CPR);
    }

    @Test(expected=SQLException.class)
    public static void F_cannotFindCustomer() throws SQLException {
        DB.getCustomerByCPR(CPR);
    }
}
