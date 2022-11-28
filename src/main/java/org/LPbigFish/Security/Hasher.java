package org.LPbigFish.Security;

import com.google.gson.Gson;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;

public class Hasher {

    private static final Gson gson = new Gson();

    private static final char[] base58 = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] toKeccak256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-256", "BC");
            digest.update(input.getBytes());
            return digest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toRIPEMD160(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("RIPEMD160", "BC");
            digest.update(input.getBytes());
            return digest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] doubleKeccak256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-256", "BC");
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

    public static byte[] hashToByteArray(String input) {
        byte[] bytes = new byte[input.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(input.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static String jsonConverter(Object object) {
        return gson.toJson(object);
    }



    public static String createAddress(String publicKey) {
        String publicKeyHash = toString(toKeccak256(publicKey));
        publicKeyHash = toString(toRIPEMD160(publicKeyHash));

        String address = "00" + publicKeyHash;
        String checksum = toString(toKeccak256(address));
        checksum = toString(toKeccak256(checksum));
        checksum = checksum.substring(0, 8);
        address += checksum;

        return encodeBase58(address);
    }


}
