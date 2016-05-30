package dtu.dagprojekt.bankaroo;

import dtu.dagprojekt.bankaroo.util.DB;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationPath("api")
@Path("/")
public class HelloWorld extends Application {

//    @Resource(name = "jdbc/exampleDS")
//    DataSource ds1;

    @GET
    @Path("/employees")
    public Response getEmployees() throws IOException, SQLException {
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
}
