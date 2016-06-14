package dtu.dagprojekt.bankaroo.util;

import com.sun.jersey.core.util.Priority;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class Authentication implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the HTTP Authorization header from the request
        String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present
        if (token == null) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Authenticate token
        try {
            Map<String, Object> map = Token.authenticate(token);
            requestContext.setSecurityContext(new AuthContext(map));
        } catch (Exception e) {
            Response abort = Response.status(Response.Status.UNAUTHORIZED).build();
            requestContext.abortWith(abort);
        }
    }
}
