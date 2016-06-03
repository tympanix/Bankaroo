package dtu.dagprojekt.bankaroo.util;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Map;

public class AuthContext implements SecurityContext {

    private Map<String, Object> map;

    public AuthContext(Map<String, Object> map){
        this.map = map;
    }

    public long getId(){
        return Long.parseLong(map.get("id").toString());
    }

    @Override
    public Principal getUserPrincipal() {
        return new Principal() {
            @Override
            public String getName() {
                return String.valueOf(map.get("id"));
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
