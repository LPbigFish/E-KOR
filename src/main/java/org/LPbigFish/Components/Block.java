package org.LPbigFish.Components;

import org.LPbigFish.Security.Hasher;

import java.util.Objects;

public record Block(long index, long timestamp, String previousHash, String hash, String data, long nonce, String target, long blockTime) {

    public Block(long index, long timestamp, String previousHash, String hash, String data, long nonce, String target, long blockTime) {
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
        this.target = target;
        this.blockTime = blockTime;
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

    @Override
    public String target() {
        return target;
    }

    public boolean isValid() {
        return getBlockHash().equals(hash);
    }

    public String getBlockHash() {
        return Hasher.toString(Objects.requireNonNull(Hasher.doubleKeccak256(Hasher.toString(Hasher.toKeccak256(timestamp + previousHash + data)) + nonce)));
    }

    public void printBlock() {
        System.out.println(Hasher.jsonConverter(this));
    }

}
