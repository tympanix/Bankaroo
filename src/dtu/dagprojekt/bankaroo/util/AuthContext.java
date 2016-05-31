package dtu.dagprojekt.bankaroo.util;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class AuthContext implements SecurityContext {

    private int id;

    public AuthContext(int id){
        this.id = id;
    }

    @Override
    public Principal getUserPrincipal() {
        return new Principal() {
            @Override
            public String getName() {
                return String.valueOf(id);
            }
        };
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
