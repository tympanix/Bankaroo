import dtu.dagprojekt.bankaroo.models.Employee;
import dtu.dagprojekt.bankaroo.util.DB;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeeTest {

    private static final int ID = 99;
    private static final String NAME = "Donald";
    private static final String PASSWORD = "don123";

    @Test
    public void A_init() throws SQLException {

    }

    @Test
    public void B_newEmployee() throws SQLException {
        Employee e = new Employee(ID, NAME, PASSWORD);

        assertEquals(e.getId(), ID);
        assertEquals(e.getName(), NAME);
        assertNotEquals(e.getHashPassword(), PASSWORD);
        assertEquals(e.getHashPassword().length(), 64);
        assertEquals(e.getSalt().length(), 64);

        DB.insertEmployee(e);
    }

    @Test
    public void C_deleteEmployee() throws SQLException {
        DB.deleteEmployee(ID);
    }
}
