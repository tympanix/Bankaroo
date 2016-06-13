package dtu.dagprojekt.bankaroo;

import dtu.dagprojekt.bankaroo.models.Account;
import dtu.dagprojekt.bankaroo.models.Transaction;
import dtu.dagprojekt.bankaroo.models.User;
import dtu.dagprojekt.bankaroo.param.Credentials;
import dtu.dagprojekt.bankaroo.util.AuthContext;
import dtu.dagprojekt.bankaroo.util.DB;
import dtu.dagprojekt.bankaroo.util.Secured;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationPath("api")
@Path("/user")
public class UserEndpoints {

    @GET
    @Secured
    @Path("/accounts")
    public Response getAccounts(@Context AuthContext s) throws IOException, SQLException {
        return Response.ok(DB.getAccountsByUser(s.getId()).toJson(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Secured
    @Path("/user")
    public Response getUser(@Context AuthContext s) throws IOException, SQLException {
        return Response.ok(DB.getUserByCPR(s.getId()).toJson(), MediaType.APPLICATION_JSON).build();
    }


    @GET
    @Secured
    @Path("/history")
    public Response getHistory(@Context AuthContext s, @DefaultValue("-1") @QueryParam("account") int accountId) throws IOException, SQLException {
        return Response.ok(DB.getHistory(accountId).toJson(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Secured
    @Path("/new/account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newAccount(@Context AuthContext s, Account account) {
        account.setCustomer(s.getId());
        account.setBalance(0);

        try {
            System.out.println("Insert Account:");
            DB.insertAccount(account);
            return Response.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @POST
    @Secured
    @Path("/transaction")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newTransaction(@Context AuthContext s, Transaction transaction) {
        try {
            Credentials credentials = new Credentials(s.getId(), transaction.getPassword());
            DB.login(credentials);
            Account accountFrom = DB.getAccountById(transaction.getAccountFrom());
            if (accountFrom.getCustomer() != s.getId()) throw new SQLException("Insufficient permission");
            DB.transaction(transaction);
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).build();
        }
    }


}
