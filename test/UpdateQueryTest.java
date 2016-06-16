import dtu.dagprojekt.bankaroo.database.DB;
import dtu.dagprojekt.bankaroo.database.Procedure;
import dtu.dagprojekt.bankaroo.database.Query;
import dtu.dagprojekt.bankaroo.database.Schema;
import dtu.dagprojekt.bankaroo.models.Account;
import dtu.dagprojekt.bankaroo.models.User;
import dtu.dagprojekt.bankaroo.util.*;
import org.junit.Test;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class UpdateQueryTest {



    @Test
    public void test1() throws SQLException {

        LinkedHashMap<Enum, Object> p = new LinkedHashMap<Enum, Object>();
        p.put(User.Field.Address, "Strandparken 111");
        p.put(User.Field.Phone, 11223344);

        Query q = new Query()
                .update(Schema.User)
                .set(p)
                .where(User.Field.UserID)
                .equal(1234567890);


        System.out.println(q.getQuery());
        q.execute().expect(1);
    }

    @Test
    public void testUpdate() throws SQLException {
        //"UPDATE \"DTUGRP09\".\"User\" SET \"Salt\" = '"+salt+"', \"Password\" = '"+hashedPassword+"' WHERE \"UserID\" = '"+cpr+"';"

        String salt = Utils.newSalt();
        String hashPassword = Utils.hashPassword("password", salt);
        int cpr = 1212141212;

        String query = "UPDATE \"DTUGRP09\".\"User\" SET \"Salt\" = ?, \"Password\" = ? WHERE \"UserID\" = ?";

        Query q = new Query().update(Schema.User)
                .set(User.Field.Salt, salt)
                .set(User.Field.Password, hashPassword)
                .where(User.Field.UserID)
                .equal(1212141212);

        assertEquals(query, q.toString().trim());
        LinkedList<Object> testParams = new LinkedList<Object>();
        testParams.add(salt);
        testParams.add(hashPassword);
        testParams.add(cpr);
        LinkedList<Object> params = q.getSqlParams();
        for (Object p : testParams) {
            assertEquals(p.toString(), params.pop().toString());
        }
        assertTrue(params.isEmpty());


    }

    @Test
    public void testCallTransactionQuery(){
        // statement.execute("CALL TRANSACTION("+amount+", '"+currency+"', "+accountFrom+",
        // "+accountTo+", '"+messageFrom+"', '"+messageTo+"')");
        String query = "CALL \"DTUGRP09\".Transaction(?, ?, ?, ?, ?, ?)";
        Query q = new Query()
                .call(Procedure.Transaction)
                .params(200, "DKK", 1,2,"Hej?","Hej!");
        assertEquals(query, q.toString().trim());
        LinkedList<Object> testParams = new LinkedList<Object>();
        testParams.add(200);
        testParams.add("DKK");
        testParams.add(1);
        testParams.add(2);
        testParams.add("Hej?");
        testParams.add("Hej!");
        LinkedList<Object> params = q.getSqlParams();
        for (Object p : testParams) {
            assertEquals(p.toString(), params.pop().toString());
        }
        assertTrue(params.isEmpty());

    }

    @Test
    public void testSelectWhere(){
        //Query query = new Query("SELECT * FROM \"DTUGRP09\".\"HistoryView\"
        // WHERE \"AccountID\" = "+accountId+"");
        String query = "SELECT * FROM \"DTUGRP09\".\"HistoryView\" WHERE \"AccountID\" = ?";
        Query q = new Query()
                .select().all().from(Schema.HistoryView)
                .where(Account.Field.AccountID).equal("1");

        assertEquals(query, q.toString().trim());
        LinkedList<Object> testParams = new LinkedList<Object>();
        testParams.add(1);
        LinkedList<Object> params = q.getSqlParams();
        for (Object p : testParams) {
            assertEquals(p.toString(), params.pop().toString());
        }
        assertTrue(params.isEmpty());
    }

    @Test
    public void testInsert() throws SQLException {

        Account a = new Account("My account", 1212131212, "Savings", "DKK");

        String query = "INSERT INTO \"DTUGRP09\".\"Account\" VALUES(DEFAULT, ?, ?, ?, ?, ?) ";

        Query q = new Query()
                .insert(Schema.Account)
                .values(a.getId(), a.getName(), a.getBalance(), a.getCustomer(), a.getAccountType(), a.getCurrency());

        LinkedList<Object> testParams = new LinkedList<Object>();
        testParams.add("My account");
        testParams.add(0.0);
        testParams.add(1212131212);
        testParams.add("Savings");
        testParams.add("DKK");
        LinkedList<Object> params = q.getSqlParams();
        for (Object p : testParams) {
            assertEquals(p.toString(), params.pop().toString());
        }
        assertTrue(params.isEmpty());

        assertEquals(query, q.getQuery());

    }

    @Test
    public void testUpperLikeUpper() throws SQLException {

        String query = "SELECT * FROM \"DTUGRP09\".\"User\" WHERE UPPER(\"UserName\") LIKE UPPER(?)";



        Query q = new Query()
                .select()
                .all()
                .from(Schema.User)
                .where()
                .upperLike(User.Field.UserName, "test").execute();

        assertEquals(query, q.toString().trim());
        LinkedList<Object> testParams = new LinkedList<Object>();
        testParams.add("%test%");
        LinkedList<Object> params = q.getSqlParams();
        for (Object p : testParams) {
            assertEquals(p.toString(), params.pop().toString());
        }
        assertTrue(params.isEmpty());

    }

    @Test
    public void testOrderby() throws SQLException {

        String query = "SELECT * FROM \"DTUGRP09\".\"User\" WHERE \"UserName\" = ?ORDER BY \"UserName\"";

        Query q = new Query()
                .select()
                .all()
                .from(Schema.User)
                .where(User.Field.UserName)
                .equal("Test Person").orderBy(User.Field.UserName, "");

        assertEquals(query, q.toString().trim());
        LinkedList<Object> testParams = new LinkedList<Object>();
        testParams.add("Test Person");
        LinkedList<Object> params = q.getSqlParams();
        for (Object p : testParams) {
            assertEquals(p.toString(), params.pop().toString());
        }
        assertTrue(params.isEmpty());

    }
}
