import dtu.dagprojekt.bankaroo.util.Schema;
import dtu.dagprojekt.bankaroo.util.UpdateQuery;
import org.junit.Test;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public class UpdateQueryTest {

    @Test
    public void test1() throws SQLException {

        LinkedHashMap<String, Object> p = new LinkedHashMap<String, Object>();
        p.put("Address", "Strandparken 111");

        UpdateQuery q = new UpdateQuery().update(Schema.User).set(p).where("\"UserID\" = 1107931111");

        System.out.println(q.getQuery());
        q.execute().expect(1);
    }
}
