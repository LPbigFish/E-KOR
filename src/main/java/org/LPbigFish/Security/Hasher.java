package org.LPbigFish.Security;
import java.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Hasher {

    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] toKeccak512(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-512", "BC");
            digest.update(input.getBytes());
            return digest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] doubleKeccak512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-512", "BC");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            md.update(digest);
            md.update(input.getBytes());
            return md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toString(byte[] input) {
        StringBuilder sb = new StringBuilder();
        for (byte b : input) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
