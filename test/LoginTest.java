import dtu.dagprojekt.bankaroo.models.Customer;
import dtu.dagprojekt.bankaroo.util.DB;
import org.junit.Test;

import java.sql.SQLException;

public class LoginTest {

    @Test
    public void test1() throws SQLException {
        Customer customer = DB.login(110793, "password123");
        System.out.println(customer);
    }
}
