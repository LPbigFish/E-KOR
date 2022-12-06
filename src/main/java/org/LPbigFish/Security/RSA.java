package org.LPbigFish.Security;


import com.starkbank.ellipticcurve.Ecdsa;
import com.starkbank.ellipticcurve.PrivateKey;
import com.starkbank.ellipticcurve.PublicKey;
import com.starkbank.ellipticcurve.Signature;
import com.starkbank.ellipticcurve.utils.ByteString;

public class RSA {

    public static String generatePrivateKey() {
        PrivateKey privateKey = new PrivateKey();
        return privateKey.toString();
    }

    public static String generatePublicKey(PrivateKey privateKey) {
        PublicKey publicKey = privateKey.publicKey();
        return publicKey.toPem();
    }

    public static ByteString sign(String message, String privateKey) {
        return Ecdsa.sign(message, PrivateKey.fromString(privateKey)).toDer();
    }

    public static boolean verify(String message, ByteString signature, String publicKey) {
        return Ecdsa.verify(message, Signature.fromBase64(signature), PublicKey.fromPem(publicKey));
    }

    public static String convertByteStringToBase64(ByteString byteString) {
        return byteString.toString();
    }

    public static ByteString convertBase64ToByteString(String base64) {
        return new ByteString(base64.getBytes());
    }

}
