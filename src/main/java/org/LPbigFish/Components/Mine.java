package org.LPbigFish.Components;

import org.LPbigFish.Security.Hasher;

import java.math.BigInteger;

public class Mine {
    private SubBlock block;
    private final BigInteger difficulty;

    public Mine(SubBlock block, BigInteger difficulty) {
        this.block = block;
        this.difficulty = difficulty;
    }

    public Block call() {

        while (new BigInteger(block.getBlockHash(), 16).compareTo(difficulty) >= 0) {
            block = new SubBlock(block.index(), block.timestamp(), block.previousHash(), block.hash(), block.data(), block.nonce() + 1);
        }
        long startTime = System.currentTimeMillis() / 1000L;
        return new Block(block.index(), block.timestamp(), block.previousHash(), block.getBlockHash(), block.data(), block.nonce(), difficulty.toString(), startTime - block.timestamp());
    }
}
