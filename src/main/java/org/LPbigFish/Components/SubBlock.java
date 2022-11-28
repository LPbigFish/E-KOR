package org.LPbigFish.Components;

import org.LPbigFish.Security.Hasher;

import java.util.Objects;

    public record SubBlock(long index, long timestamp, String previousHash, String hash, String data, long nonce) {

        public SubBlock(long index, long timestamp, String previousHash, String hash, String data, long nonce) {
            this.index = Math.abs(index);
            this.timestamp = timestamp;
            if (previousHash.length() != 64) {
                throw new IllegalArgumentException("Previous hash must be 64 characters long");
            } else {
                this.previousHash = previousHash;
            }
            this.data = data;
            this.nonce = nonce;
            this.hash = hash;
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
            return Hasher.toString(Objects.requireNonNull(Hasher.toKeccak256(Hasher.toString(Objects.requireNonNull(Hasher.doubleKeccak256(previousHash + data))) + nonce)));
        }

    }

