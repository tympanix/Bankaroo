import dtu.dagprojekt.bankaroo.models.Account;
import dtu.dagprojekt.bankaroo.models.User;
import dtu.dagprojekt.bankaroo.util.*;
import org.junit.Test;

import java.sql.SQLException;
import java.util.LinkedHashMap;

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
        User u1 = new User(1212141212, "Test Person", 2800, "Testtrack", 23232323, "test@testmail.test", "test");

        Query q1 = new Query()
                .insert(Schema.User)
                .values(u1.getCpr(), u1.getName(), u1.getZip(), u1.getAddress(), u1.getPhone(), u1.getEmail(), u1.getSalt(), u1.getPlainPassword()).execute();
        String salt = Utils.newSalt();
        String hashPassword = Utils.hashPassword("password", salt);


        assertFalse(u1.getSalt().equals(salt));
        assertFalse(u1.getHashPassword().equals(hashPassword));

        Query q = new Query().update(Schema.User)
                .set(User.Field.Salt, salt)
                .set(User.Field.Password, hashPassword)
                .where(User.Field.UserID)
                .equal(1212141212);

        q.execute().close();

        Query q2 = new Query()
                .select()
                .all()
                .from(Schema.User)
                .where(User.Field.UserID)
                .equal("1212141212")
                .execute();

        q2.resultSet().next();
        User u = new User(q2);
        assertTrue(u.getSalt().equals(salt));
        assertTrue(u.getHashPassword().equals(hashPassword));
        DB.deleteUser(u1.getCpr());
        q1.close();
        q2.close();
    }

    @Test
    public void testCallTransactionQuery(){
        // statement.execute("CALL TRANSACTION("+amount+", '"+currency+"', "+accountFrom+",
        // "+accountTo+", '"+messageFrom+"', '"+messageTo+"')");
        String query = "CALL Transaction('200', 'DKK', '1', '2', 'Hej?', 'Hej!')";
        Query q = new Query()
                .call(Procedure.Transaction)
                .params(200, "DKK", 1,2,"Hej?","Hej!");
        assertEquals(query, q.toString().trim());
    }

    @Test
    public void testSelectWhere(){
        //Query query = new Query("SELECT * FROM \"DTUGRP09\".\"HistoryView\"
        // WHERE \"AccountID\" = "+accountId+"");
        String query = "SELECT * FROM \"DTUGRP09\".\"HistoryView\" WHERE \"AccountID\" = '1'";
        Query q = new Query()
                .select().all().from(Schema.HistoryView)
                .where(Account.Field.AccountID).equal("1");

        assertEquals(query, q.toString().trim());
    }

    @Test
    public void insertAccount() throws SQLException {
        User u1 = new User(1212131212, "Test Person", 2800, "Testtrack", 23232323, "test@testmail.test", "test");

        Query q1 = new Query()
                .insert(Schema.User)
                .values(u1.getCpr(), u1.getName(), u1.getZip(), u1.getAddress(), u1.getPhone(), u1.getEmail(), u1.getSalt(), u1.getPlainPassword()).execute();

        Account a = new Account("My account", 1212131212, "Savings", "DKK");


        String query = "INSERT INTO \"DTUGRP09\".\"Account\" VALUES(DEFAULT, 'My account', '0.0', '1212131212', 'Savings', 'DKK') ";

        Query q = new Query()
                .insert(Schema.Account)
                .values(a.getId(), a.getName(), a.getBalance(), a.getCustomer(), a.getAccountType(), a.getCurrency());

        assertEquals(query, q.getQuery());
        q.execute().close();

        Query q2 = new Query()
                .select()
                .all()
                .from(Schema.Account)
                .where(Account.Field.AccountName)
                .equal("My account")
                .execute();

        q2.resultSet().next();
        Account a2 = new Account(q2.resultSet());
        assertEquals(a, a2);

        Query q3 = new Query()
                .delete()
                .from(Schema.Account)
                .where(Account.Field.AccountName)
                .equal("My account")
                .execute();

        DB.deleteUser(u1.getCpr());

        q2.execute();
        q2.resultSet().next();
        assertTrue(q2.resultSet().isClosed());


        q.close();
        q1.close();
        q2.close();

    }

    @Test
    public void testUpperLikeUpper() throws SQLException {

        User u1 = new User(1212121234, "Test Person", 2800, "Testtrack", 23232323, "test@testmail.test", "test");

        Query q1 = new Query()
                .insert(Schema.User)
                .values(u1.getCpr(), u1.getName(), u1.getZip(), u1.getAddress(), u1.getPhone(), u1.getEmail(), u1.getSalt(), u1.getPlainPassword()).execute();

        String query = "SELECT * FROM \"DTUGRP09\".\"User\" WHERE UPPER(\"UserName\") LIKE UPPER('%Tes%')";

        Query q2 = new Query()
                .select()
                .all()
                .from(Schema.User)
                .where(User.Field.UserID)
                .equal("1212121234").execute();

        q2.resultSet().next();
        User u2 = new User(q2);


        Query q3 = new Query()
                .select()
                .all()
                .from(Schema.User)
                .where()
                .upperLike(User.Field.UserName, "Tes").execute();
        q3.resultSet().next();
        User u3 = new User(q3);
        assertEquals(query, q3.toString().trim());
        assertEquals(u2,u3);
        DB.deleteUser(u1.getCpr());

        q2.execute();
        q2.resultSet().next();
        assertTrue(q2.resultSet().isClosed());
        q1.close();
        q2.close();
        q3.close();
    }
}
