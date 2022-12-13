package org.LPbigFish.Components;

import org.LPbigFish.Security.Hasher;

public record SubBill(String authorityHash, String data, String signature, long nonce, String printerAddress) {
    public SubBill(String authorityHash, String data, String signature, long nonce, String printerAddress) {
        this.data = data;
        this.signature = signature;
        this.printerAddress = printerAddress;
        this.nonce = nonce;
        this.authorityHash = authorityHash();
    }

    @Override
    public String data() {
        return data;
    }

    @Override
    public String signature() {
        return signature;
    }

    @Override
    public String printerAddress() {
        return printerAddress;
    }

    @Override
    public long nonce() {
        return nonce;
    }

    @Override
    public String authorityHash() {
        return Hasher.toString(Hasher.toKeccak512(Hasher.toString(Hasher.doubleKeccak256(data + signature + nonce)) + printerAddress));
    }
}
