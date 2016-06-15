package dtu.dagprojekt.bankaroo.endpoints;

import dtu.dagprojekt.bankaroo.models.Role;
import dtu.dagprojekt.bankaroo.models.User;
import dtu.dagprojekt.bankaroo.models.Credentials;
import dtu.dagprojekt.bankaroo.database.DB;
import dtu.dagprojekt.bankaroo.security.Secured;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationPath("api")
@Path("/admin")
@Secured({Role.Employee})
public class AdminEndpoints extends Application {

    @GET
    @Path("/customers")
    public Response getCustomers(@DefaultValue("") @QueryParam("name") String name) throws IOException, SQLException {
        return Response.ok(DB.searchUser(name).toJson(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/accounts")
    public Response getAccounts(@QueryParam("id") long cpr) throws IOException, SQLException {
        return Response.ok(DB.getAccountsByUser(cpr).toJson(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/delete/account")
    public Response deleteAccount(@QueryParam("id") int id) {
        try {
            DB.deleteAccount(id);
            return Response.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path("/delete/user")
    public Response deleteUser(@QueryParam("id") int id) {
        try {
            DB.deleteUser(id);
            return Response.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @POST
    @Path("/change/password")
    public Response changePassword(Credentials credentials){
        try {
            DB.changePassword(credentials.getId(), credentials.getPassword());
            return Response.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @POST
    @Path("/update/user")
    public Response updateUser(@QueryParam("id") long id, User user){
        try {
            DB.updateUser(id, user);
            return Response.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @POST
    @Path("/new/user")
    public Response newUser(User user){
        try {
            user.hashPassword();
            DB.insertUser(user);
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
}
