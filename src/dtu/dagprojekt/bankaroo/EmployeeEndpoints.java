package dtu.dagprojekt.bankaroo;

import dtu.dagprojekt.bankaroo.models.User;
import dtu.dagprojekt.bankaroo.param.Credentials;
import dtu.dagprojekt.bankaroo.util.DB;
import dtu.dagprojekt.bankaroo.util.Secured;
import dtu.dagprojekt.bankaroo.util.UpdateQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationPath("api")
@Path("/admin")
public class EmployeeEndpoints {

    @GET
    @Secured
    @Path("/customers")
    public Response getCustomers(@DefaultValue("") @QueryParam("name") String name, @DefaultValue("-1") @QueryParam("id") long id) throws IOException, SQLException {
        UpdateQuery out;
        if (id > 0){
            out = DB.getUser(id);
        } else {
            out = DB.getUser(name);
        }
        return Response.ok(out.toJson(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Secured
    @Path("/accounts")
    public Response getAccounts(@QueryParam("id") long id) throws IOException, SQLException {
        return Response.ok(DB.getAccounts(id).toJson(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Secured
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

    @POST
    @Secured
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
    @Secured
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
    @Secured
    @Path("/new/user")
    public Response newUser(User user){
        try {
            DB.insertUser(user);
            return Response.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
}
