package org.LPbigFish.Components;


import com.starkbank.ellipticcurve.PrivateKey;
import org.LPbigFish.Security.Hasher;
import org.LPbigFish.Security.RSA;

public class Wallet {

    private final String privateKey;
    private final String publicKey;

    public Wallet() {
        this.privateKey = RSA.generatePrivateKey();
        this.publicKey = RSA.generatePublicKey(PrivateKey.fromString(this.privateKey));
    }

    public String getAddress() {
        return Hasher.createAddress(this.publicKey);
    }
}
