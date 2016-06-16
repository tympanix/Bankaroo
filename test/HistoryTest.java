import dtu.dagprojekt.bankaroo.models.History;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by GustavMadslund on 16/06/2016.
 */
public class HistoryTest {

    @Test
    public void testHistory() throws Exception {
        assertEquals(History.Field.TransactionID.name(), "TransactionID");
        assertEquals(History.Field.Balance.name(), "Balance");
        assertEquals(History.Field.AccountID.name(), "AccountID");
        assertEquals(History.Field.AmountLocal.name(), "AmountLocal");
        assertEquals(History.Field.TransactionType.name(), "TransactionType");
        assertEquals(History.Field.Message.name(), "Message");
    }
}
