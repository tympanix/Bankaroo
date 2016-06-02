package dtu.dagprojekt.bankaroo;

import dtu.dagprojekt.bankaroo.util.DB;
import dtu.dagprojekt.bankaroo.util.Secured;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationPath("api")
@Path("/admin")
public class EmployeeEndpoints {

    @GET
    @Secured
    @Path("/customers")
    public Response getCustomers(@DefaultValue("") @QueryParam("name") String name) throws IOException, SQLException {
        return Response.ok(DB.getCustomers(name), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Secured
    @Path("/accounts")
    public Response getAccounts(@QueryParam("id") int id) throws IOException, SQLException {
        return Response.ok(DB.getAccounts(id), MediaType.APPLICATION_JSON).build();
    }

}
