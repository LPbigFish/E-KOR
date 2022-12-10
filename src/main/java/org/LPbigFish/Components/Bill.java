package org.LPbigFish.Components;

public record Bill (String identifier, long timeStamp, String hash, String data, String signature, String printerAddress, String witnessKey, String witnessSignature) {

    public Bill(String identifier, long timeStamp, String hash, String data, String signature, String printerAddress, String witnessKey, String witnessSignature) {
        this.identifier = identifier;
        this.timeStamp = timeStamp;
        this.hash = hash;
        this.data = data;
        this.signature = signature;
        this.printerAddress = printerAddress;
        this.witnessKey = witnessKey;
        this.witnessSignature = witnessSignature;
    }

    @Override
    public String identifier() {
        return identifier;
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
