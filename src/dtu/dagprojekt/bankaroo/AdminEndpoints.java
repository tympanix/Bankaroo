package dtu.dagprojekt.bankaroo;

import dtu.dagprojekt.bankaroo.models.Role;
import dtu.dagprojekt.bankaroo.models.User;
import dtu.dagprojekt.bankaroo.models.Credentials;
import dtu.dagprojekt.bankaroo.database.DB;
import dtu.dagprojekt.bankaroo.security.Secured;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationPath("api")
@Path("/admin")
public class AdminEndpoints extends Application {

    @GET
    @Path("/search/customers")
    @Secured({Role.Employee})
    public Response getCustomers(@DefaultValue("") @QueryParam("search") String name) throws IOException, SQLException {
        return Response.ok(DB.searchUser(name).toJson(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/customers")
    @Secured({Role.Employee})
    public Response getCustomers(@DefaultValue("-1") @QueryParam("id") long cpr) {
        try {
            if (String.valueOf(cpr).length() != 10) throw new Exception();
            return Response.ok(DB.getUserByCPR(cpr).toJson(), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path("/accounts")
    @Secured({Role.Employee})
    public Response getAccounts(@QueryParam("id") long cpr) throws IOException, SQLException {
        return Response.ok(DB.getAccountsByUser(cpr).toJson(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/delete/account")
    @Secured({Role.Employee})
    public Response deleteAccount(@QueryParam("id") int id, @QueryParam("transfer") int transfer) {
        try {
            DB.closeAccount(id, transfer);
            return Response.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path("/delete/user")
    @Secured({Role.Employee})
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
    @Secured({Role.Employee})
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
    @Secured({Role.Employee})
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
    @Secured({Role.Employee})
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
