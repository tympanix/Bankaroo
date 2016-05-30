import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

public class JWTTest {

    final String secret = "my secret";

    @Test
    public void test0(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiTWF0aGlhcyIsImV4cCI6MTQ2NDYyMDE0MX0.9hPpUJeBGveTPk3dOIQjMXmWEWu_08sY4t6X9wiwIxI";
        try {
            Map<String, Object> hey = new JWTVerifier(secret).verify(token);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (JWTVerifyException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1(){
        JWTSigner jwt = new JWTSigner(secret);
        JWTSigner.Options options = new JWTSigner.Options();
        options.setExpirySeconds(10);

        HashMap<String, Object> payload = new HashMap<String, Object>();
        payload.put("name", "Mathias");
        String token = jwt.sign(payload, options);

        System.out.println("Token: " + token);


        try {
            Map<String, Object> hey = new JWTVerifier(secret).verify(token);
            System.out.println("It worked: " + hey.get("name"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.err.println("Invalid Token! " + e);
            e.printStackTrace();
        } catch (SignatureException e) {
            System.err.println("Invalid signature!");
            e.printStackTrace();
        } catch (JWTVerifyException e) {
            e.printStackTrace();
        }
    }



}
