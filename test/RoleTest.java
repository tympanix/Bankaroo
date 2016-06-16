
import dtu.dagprojekt.bankaroo.models.Role;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by GustavMadslund on 16/06/2016.
 */
public class RoleTest {

    @Test
    public void testRole(){
        assertEquals(Role.Employee.name(), "Employee");
    }
}
