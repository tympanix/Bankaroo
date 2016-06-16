import dtu.dagprojekt.bankaroo.models.Credentials;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by GustavMadslund on 16/06/2016.
 */
public class CredentialsTest {
    Credentials c;

    @Before
    public void setUp(){
        c = new Credentials();
    }

    @Test
    public void testID(){
        Credentials c = new Credentials();
        c.setId(1);
        assertEquals(c.getId(), 1);
    }

    @Test
    public void testPassword(){
        Credentials c = new Credentials();
        c.setPassword("test");
        assertEquals(c.getPassword(), "test");
    }


}
