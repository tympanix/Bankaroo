import dtu.dagprojekt.bankaroo.models.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransactionTest {

    Transaction t;

    @Before
    public void setUp(){
        t = new Transaction();
    }

    @Test
    public void testID(){
        t.setId(1);
        assertEquals(t.getId(), 1);
    }

    @Test
    public void testCurrency(){
        t.setCurrency("DKK");
        assertEquals(t.getCurrency(), "DKK");
    }

    @Test
    public void testAccountFrom(){
        t.setAccountFrom(2);
        assertEquals(t.getAccountFrom(), 2);
    }

    @Test
    public void testAccountTo(){
        t.setAccountTo(3);
        assertEquals(t.getAccountTo(), 3);
    }

    @Test
    public void testMessageFrom(){
        t.setMessageFrom("TestFrom");
        assertEquals(t.getMessageFrom(), "TestFrom");
    }

    @Test
    public void testMessageTo(){
        t.setMessageTo("TestTo");
        assertEquals(t.getMessageTo(), "TestTo");
    }

    @Test
    public void testPassword(){
        t.setPassword("testPassword");
        assertEquals(t.getPassword(), "testPassword");
    }

    @Test
    public void testAmount(){
        t.setAmount(200);
        assertTrue(t.getAmount() == 200);
    }

    @Test
    public void testTransactionTime(){
        Date d = new Date();
        t.setTransactionTime(d);
        assertEquals(t.getTransactionTime(), d);
    }
}
