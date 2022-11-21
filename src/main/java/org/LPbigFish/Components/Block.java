package org.LPbigFish.Components;

public record Block(long index, long timestamp, String previousHash, String hash, String data, long nonce) {

    public Block(long index, long timestamp, String previousHash, String hash, String data, long nonce) {
        this.index = Math.abs(index);
        this.timestamp = timestamp;
        if (previousHash.length() != 64) {
            throw new IllegalArgumentException("Previous hash must be 64 characters long");
        } else {
            this.previousHash = previousHash;
        }
        if (hash.length() != 64) {
            throw new IllegalArgumentException("Hash must be 64 characters long");
        } else {
            this.hash = hash;
        }
        this.data = data;
        this.nonce = nonce;
    }

    public Block getBlock() {
        return this;
    }

    public boolean isValid() {
        return true;
    }

}
