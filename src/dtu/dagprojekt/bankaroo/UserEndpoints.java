package dtu.dagprojekt.bankaroo;

import dtu.dagprojekt.bankaroo.database.DB;
import dtu.dagprojekt.bankaroo.models.Account;
import dtu.dagprojekt.bankaroo.models.Credentials;
import dtu.dagprojekt.bankaroo.models.Transaction;
import dtu.dagprojekt.bankaroo.security.AuthContext;
import dtu.dagprojekt.bankaroo.security.Secured;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@ApplicationPath("api")
@Path("/user")
public class UserEndpoints extends Application {

    @GET
    @Secured
    @Path("/accounts")
    public Response getAccounts(@Context AuthContext s) {
        try {
            return Response.ok(DB.getAccountsByUser(s.getId()).toJson(), MediaType.APPLICATION_JSON).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Secured
    @Path("/user")
    public Response getUser(@Context AuthContext s) {
        try {
            return Response.ok(DB.getUserByCPR(s.getId()).toJson(), MediaType.APPLICATION_JSON).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Secured
    @Path("/close/account")
    public Response closeAccount(@Context AuthContext s,
                                 @QueryParam("account") int closeId,
                                 @QueryParam("transfer") int transferId) {
        try {
            Account accountClose = DB.getAccountById(closeId);
            if (accountClose.getCustomer() != s.getId()) throw new SQLException("Insufficient permission");
            DB.closeAccount(closeId, transferId);
            return Response.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).build();
        }
    }


    @GET
    @Secured
    @Path("/history")
    public Response getHistory(@Context AuthContext s, @DefaultValue("-1") @QueryParam("account") int accountId) {
        try {
            return Response.ok(DB.getHistory(accountId).toJson(), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
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
