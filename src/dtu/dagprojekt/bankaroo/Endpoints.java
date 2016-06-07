package dtu.dagprojekt.bankaroo;

import com.sun.xml.internal.ws.client.ResponseContext;
import dtu.dagprojekt.bankaroo.models.User;
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
public class Endpoints extends Application {

    @Context
    ResponseContext context;

//    @Resource(name = "jdbc/exampleDS")
//    DataSource ds1;

    @GET
    @Path("/exchange")
    public Response getExchange() throws IOException, SQLException {
        return Response.ok(DB.getExchanges().toJson(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/accounttypes")
    public Response getAccountTypes() throws IOException, SQLException {
        return Response.ok(DB.getAccountType().toJson(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Credentials credentials) {
        try {
            User user = DB.login(credentials);
            return Token.tokenResponse(user);
        } catch (Exception e){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
