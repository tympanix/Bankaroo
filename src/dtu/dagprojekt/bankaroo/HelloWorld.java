package dtu.dagprojekt.bankaroo;

import com.sun.xml.internal.ws.client.ResponseContext;
import dtu.dagprojekt.bankaroo.models.Account;
import dtu.dagprojekt.bankaroo.models.Customer;
import dtu.dagprojekt.bankaroo.param.Credentials;
import dtu.dagprojekt.bankaroo.util.DB;
import dtu.dagprojekt.bankaroo.util.Secured;
import dtu.dagprojekt.bankaroo.util.Token;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationPath("api")
@Path("/")
public class HelloWorld extends Application {

    @Context
    ResponseContext context;

//    @Resource(name = "jdbc/exampleDS")
//    DataSource ds1;

    @GET
    @Secured
    @Path("/employees")
    public Response getEmployees(@Context SecurityContext s) throws IOException, SQLException {
        String id = s.getUserPrincipal().getName();
        System.out.println("ID: " + id);
        return Response.ok(DB.getEmployees(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/customers")
    public Response getCustomers() throws IOException, SQLException {
        return Response.ok(DB.getCustomers(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/accounts")
    public Response getAccounts() throws IOException, SQLException {
        return Response.ok(DB.getAccounts(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/accounttypes")
    public Response getAccountTypes() throws IOException, SQLException {
        return Response.ok(DB.getAccountType(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/history")
    public Response getHistory() throws IOException, SQLException {
        return Response.ok(DB.getHistory(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/exchange")
    public Response getExchange() throws IOException, SQLException {
        return Response.ok(DB.getExchanges(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/transactions")
    public Response getTransactions() throws IOException, SQLException {
        return Response.ok(DB.getTransactions(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Credentials credentials) {
        try {
            Customer customer = DB.login(credentials);
            return Token.tokenResponse(customer);
        } catch (Exception e){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
