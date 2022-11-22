package org.LPbigFish.Components;

import org.LPbigFish.Security.Hasher;

import java.util.Objects;

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

    @Override
    public long index() {
        return index;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public String previousHash() {
        return previousHash;
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
    public long nonce() {
        return nonce;
    }

    public boolean isValid() {
        return getBlockHash().equals(hash);
    }

    public String getBlockHash() {
        return Hasher.toString(Objects.requireNonNull(Hasher.doubleKeccak512(Hasher.toString(Hasher.toKeccak512(index + timestamp + previousHash + data)) + nonce)));
    }

}
