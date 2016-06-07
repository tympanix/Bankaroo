package dtu.dagprojekt.bankaroo;

import dtu.dagprojekt.bankaroo.models.Account;
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
        return Response.ok(DB.getAccounts(s.getId()), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Secured
    @Path("/history")
    public Response getHistory(@Context AuthContext s, @DefaultValue("-1") @QueryParam("account") int accountId) throws IOException, SQLException {
        return Response.ok(DB.getHistory(accountId), MediaType.APPLICATION_JSON).build();
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

}
