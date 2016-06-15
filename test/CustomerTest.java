import dtu.dagprojekt.bankaroo.models.User;
import dtu.dagprojekt.bankaroo.models.Credentials;
import dtu.dagprojekt.bankaroo.database.DB;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.xml.bind.ValidationException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerTest {

    private static final int CPR = 1107931111;
    private static final String NAME = "Bjoern";
    private static final int ZIP = 4000;
    private static final String ADDRESS = "Strandparken 10";
    private static final int PHONE = 28513125;
    private static final String EMAIL = "bjoern@email.com";
    private static final String PASSWORD = "password";

    @Test
    public void A_init(){
        try {
            DB.deleteUser(CPR);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void B_createCustomer() throws SQLException {
        User user = new User(CPR, NAME, ZIP, ADDRESS, PHONE, EMAIL, PASSWORD);
        DB.insertUser(user);
    }

    @Test
    public void C_findCustomer() throws SQLException {
        User user = new User(DB.getUserByCPR(CPR));
        assertEquals(user.getCpr(), CPR);
        assertEquals(user.getName(), NAME);
    }

    @Test
    public void D_loginCustomer() throws SQLException, ValidationException {
        Credentials credentials = new Credentials(CPR, PASSWORD);
        User user = DB.login(credentials);
        assertEquals(user.getCpr(), CPR);
        assertEquals(user.getName(), NAME);
    }

    @Test(expected=SQLException.class)
    public void E_cantLogin() throws SQLException, ValidationException {
        Credentials credentials = new Credentials(CPR, "wrong_password");
        User user = DB.login(credentials);
        assertNull(user);
    }

    @Test
    public void E_removeCustomer() throws SQLException {
        DB.deleteUser(CPR);
    }

    @Test(expected=SQLException.class)
    public void F_cannotFindCustomer() throws SQLException {
        DB.getUserByCPR(CPR);
    }
}
