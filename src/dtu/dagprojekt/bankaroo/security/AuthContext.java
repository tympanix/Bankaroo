package dtu.dagprojekt.bankaroo.security;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public class AuthContext implements SecurityContext {

    private Map<String, Object> payload;
    private List permissions;

    public AuthContext(Map<String, Object> payload){
        this.payload = payload;
        this.permissions = (List) payload.get("permissions");
    }

    public long getId(){
        return Long.parseLong(payload.get("id").toString());
    }

    @Override
    public Principal getUserPrincipal() {
        return new Principal() {
            @Override
            public String getName() {
                return String.valueOf(payload.get("id"));
            }
        };
    }

    @Override
    public boolean isUserInRole(String s) {
        return permissions.contains(s);
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
