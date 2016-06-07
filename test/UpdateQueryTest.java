import dtu.dagprojekt.bankaroo.models.Account;
import dtu.dagprojekt.bankaroo.models.User;
import dtu.dagprojekt.bankaroo.util.Procedure;
import dtu.dagprojekt.bankaroo.util.Schema;
import dtu.dagprojekt.bankaroo.util.UpdateQuery;
import org.junit.Test;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public class UpdateQueryTest {

    @Test
    public void test1() throws SQLException {

        LinkedHashMap<Enum, Object> p = new LinkedHashMap<Enum, Object>();
        p.put(User.Field.Address, "Strandparken 111");
        p.put(User.Field.Phone, 11223344);

        UpdateQuery q = new UpdateQuery().update(Schema.User).set(p).where(User.Field.UserID).equal(1107931111);


        System.out.println(q.getQuery());
        q.execute().expect(1);
    }

    @Test
    public void test2(){
        //"UPDATE \"DTUGRP09\".\"User\" SET \"Salt\" = '"+salt+"', \"Password\" = '"+hashedPassword+"' WHERE \"UserID\" = '"+cpr+"';"
        UpdateQuery q = new UpdateQuery().update(Schema.User)
                .set(User.Field.Salt, "salt").set(User.Field.Password, "pass")
                .where(User.Field.UserID).equal(123);

        System.out.println(q.getQuery());
    }

    @Test
    public void test3(){
        // statement.execute("CALL TRANSACTION("+amount+", '"+currency+"', "+accountFrom+",
        // "+accountTo+", '"+messageFrom+"', '"+messageTo+"')");
        UpdateQuery q = new UpdateQuery()
                .call(Procedure.Transaction)
                .params(200, "DKK", 1,2,"Hej?","Hej!");

        System.out.println(q.getQuery());
    }

    @Test
    public void test4(){
        //Query query = new Query("SELECT * FROM \"DTUGRP09\".\"HistoryView\"
        // WHERE \"AccountID\" = "+accountId+"");
        UpdateQuery q = new UpdateQuery()
                .select().all().from(Schema.HistoryView)
                .where(Account.Field.AccountID).equal("1");

        System.out.println(q.getQuery());
    }

    @Test
    public void test5(){
        Account a = new Account("My account", 1107931111, "Savings", "DKK");

        UpdateQuery q = new UpdateQuery()
                .insert(Schema.Account)
                .values(a.getId(), a.getName(), a.getBalance(), a.getCustomer(), a.getAccountType(), a.getCurrency());

        System.out.println(q);
    }





}
