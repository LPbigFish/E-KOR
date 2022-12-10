package org.LPbigFish.Components;

public record SubBill(String hash, String data, String Signature, String printerAddress) {
    public SubBill(String hash, String data, String Signature, String printerAddress) {
        this.data = data;
        this.Signature = Signature;
        this.printerAddress = printerAddress;
        this.hash = "";
    }

    @Override
    public String data() {
        return data;
    }

    @Override
    public String Signature() {
        return Signature;
    }

    @Override
    public String printerAddress() {
        return printerAddress;
    }
}
