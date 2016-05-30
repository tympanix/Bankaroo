import dtu.dagprojekt.bankaroo.util.Utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordTest {

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String password = "hey";

        byte[] pass = password.getBytes(StandardCharsets.UTF_8);
        System.out.println("Pass length:" + pass.length);

        byte[] salt = Utils.newSalt();

        System.out.println("Salt lenght: " + salt.length);

        byte[] saltpass = Utils.concat(pass, salt);
        System.out.println("Saltpass length " + saltpass.length);

        String hey = Utils.bytesToHex(Utils.sha256(saltpass));

        System.out.println(hey.length());
        System.out.println(hey);
    }
}
