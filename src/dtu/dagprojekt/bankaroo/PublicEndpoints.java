package dtu.dagprojekt.bankaroo;

import dtu.dagprojekt.bankaroo.database.DB;
import dtu.dagprojekt.bankaroo.models.Credentials;
import dtu.dagprojekt.bankaroo.models.User;
import dtu.dagprojekt.bankaroo.util.Token;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationPath("api")
@Path("/")
public class PublicEndpoints extends Application {

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
            e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
