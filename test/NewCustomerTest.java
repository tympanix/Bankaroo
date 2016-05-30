import dtu.dagprojekt.bankaroo.util.DB;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class NewCustomerTest {

    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
        DB.addNewCustomer(110793, "Henning", "password123");
    }
}
