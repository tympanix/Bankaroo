import dtu.dagprojekt.bankaroo.models.User;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by GustavMadslund on 16/06/2016.
 */
public class UserTest {

    User u1;
    User u2;
    private static final int CPR = 1107931111;
    private static final String NAME = "Bjoern";
    private static final int ZIP = 4000;
    private static final String ADDRESS = "Strandparken 10";
    private static final int PHONE = 28513125;
    private static final String EMAIL = "bjoern@email.com";
    private static final String PASSWORD = "password";


    @Before
    public void setUp(){
        u1 = new User();
        u2 = new User(CPR, NAME, ZIP, ADDRESS, PHONE, EMAIL, PASSWORD);

    }

    @Test
    public void testPermissionsFromDB() throws SQLException {
        LinkedList<String> permissions = u2.getPermissionsFromDB();
        assertTrue(permissions.isEmpty());
    }

    @Test
    public void testPlainPassword(){
        assertEquals(u2.getPlainPassword(), PASSWORD);
    }

    @Test
    public void testToString(){
        assertEquals(u2.toString(), "Customer: Bjoern (1107931111)");
    }

    @Test
    public void testUpdatedFields(){
        LinkedHashMap<Enum, Object> params = u2.getUpdatedFields();
        System.out.println(params);
        LinkedList<Object> testParams = new LinkedList<Object>();
        testParams.add("UserName="+NAME);
        testParams.add("PostalCode="+ZIP);
        testParams.add("Address="+ADDRESS);
        testParams.add("Phone="+PHONE);
        testParams.add("Email="+EMAIL);
        for (Object o : params.entrySet()) {
            assertEquals(o.toString(), testParams.pop().toString());
        }
    }




}
