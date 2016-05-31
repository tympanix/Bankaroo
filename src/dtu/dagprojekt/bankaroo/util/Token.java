package dtu.dagprojekt.bankaroo.util;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import dtu.dagprojekt.bankaroo.models.Customer;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

public class Token {

    private final static String SECRET = "YMETTJyBLY2WYX8kEtlNKHogUK78A0qktJuPl3MxuMFn7pwN4IYYo8luZf5fmVbc";
    private final static int EXPIRE = 3600;

    public static Response tokenResponse(Customer customer) {
        JWTSigner signer = new JWTSigner(SECRET);
        JWTSigner.Options options = new JWTSigner.Options();
        options.setExpirySeconds(EXPIRE);

        HashMap<String, Object> payload = new HashMap<String, Object>();
        payload.put("id", customer.getCpr());
        String token = signer.sign(payload, options);

        return Response.ok("{\"token\": \"" + token + "\"}", MediaType.APPLICATION_JSON).build();
    }

    public static Map<String, Object> authenticate(String token) throws SignatureException, NoSuchAlgorithmException, JWTVerifyException, InvalidKeyException, IOException {
        System.out.println(token);
        return new JWTVerifier(SECRET).verify(token);
    }
}
