package dtu.dagprojekt.bankaroo.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class Utils {

    final protected static char[] hexArray = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] concat(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c = new byte[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static String newSalt(){
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return bytesToHex(salt);
    }

    public static byte[] sha256(byte[] array) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(array);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String hashPassword(String plainPassword, String stringSalt) {
        byte[] salt = stringSalt.getBytes(StandardCharsets.UTF_8);
        byte[] pass = plainPassword.getBytes(StandardCharsets.UTF_8);
        byte[] saltPass = Utils.concat(pass, salt);
        return Utils.bytesToHex(Utils.sha256(saltPass));
    }
}
