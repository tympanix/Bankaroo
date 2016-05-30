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

@ApplicationPath("rest")
@Path("/test")
public class HelloWorld extends Application {

//    @Resource(name = "jdbc/exampleDS")
//    DataSource ds1;

    @GET
    public Response getMessage() throws IOException, SQLException {
        return Response.ok(DB.getEmployees(), MediaType.APPLICATION_JSON).build();
    }
}
