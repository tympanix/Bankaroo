package dtu.dagprojekt.bankaroo;

import dtu.dagprojekt.bankaroo.util.AuthContext;
import dtu.dagprojekt.bankaroo.util.DB;
import dtu.dagprojekt.bankaroo.util.Secured;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
    public Response getHistory(@Context AuthContext s) throws IOException, SQLException {
        return Response.ok(DB.getAccounts(s.getId()), MediaType.APPLICATION_JSON).build();
    }

}
