import dtu.dagprojekt.bankaroo.util.DB;
import org.junit.Test;

import java.sql.SQLException;

public class TransactionTest {

    @Test
    public void testTransaction() throws SQLException {
        DB.transaction(200, "DKK", 1, 2, "My transaction", "Here you go");
    }
}
