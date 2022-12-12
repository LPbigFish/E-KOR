package org.LPbigFish.Components;

import org.LPbigFish.Security.Hasher;

public record Bill (String identifier, long timeStamp, String hash, String data, String signature, String printerAddress, String witnessKey, String witnessSignature) {

    public Bill(String identifier, long timeStamp, String hash, String data, String signature, String printerAddress, String witnessKey, String witnessSignature) {

        this.timeStamp = timeStamp;
        this.hash = hash;
        this.data = data;
        this.signature = signature;
        this.printerAddress = printerAddress;
        this.witnessKey = witnessKey;
        this.witnessSignature = witnessSignature;
        this.identifier = identifier();
    }

    @Override
    public String identifier() {
        return Hasher.toString(Hasher.toKeccak512(witnessKey + Hasher.toString(Hasher.doubleKeccak256(timeStamp + hash + data + signature)) + printerAddress + witnessSignature));
    }

    @Override
    public long timeStamp() {
        return timeStamp;
    }

    @Override
    public String hash() {
        return hash;
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
    public String witnessKey() {
        return witnessKey;
    }

    @Override
    public String witnessSignature() {
        return witnessSignature;
    }
}
