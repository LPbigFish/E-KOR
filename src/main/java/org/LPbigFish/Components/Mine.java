package org.LPbigFish.Components;

import java.math.BigInteger;
import java.util.concurrent.Callable;

public class Mine implements Callable<Block> {
    private SubBlock block;

    private final BigInteger difficulty;
    private final long startNonce;
    private final long endNonce;

    public Mine(SubBlock block, BigInteger difficulty, long startNonce, long endNonce) {
        this.block = block;
        this.difficulty = difficulty;
        this.startNonce = startNonce;
        this.endNonce = endNonce;
    }

    @Override
    public Block call() {
        block = new SubBlock(block.index(), block.timestamp(), block.previousHash(), block.hash(), block.data(), startNonce);
        while (new BigInteger(block.getBlockHash(), 16).compareTo(difficulty) >= 0) {
            block = new SubBlock(block.index(), block.timestamp(), block.previousHash(), block.hash(), block.data(), block.nonce() + 1);
            if (block.nonce() == endNonce) {
                return null;
            }
        }
        long startTime = System.currentTimeMillis() / 1000L;
        return new Block(block.index(), System.currentTimeMillis() / 1000L, block.previousHash(), block.getBlockHash(), block.data(), block.nonce(), difficulty.toString(),startTime - block.timestamp());
    }
}
