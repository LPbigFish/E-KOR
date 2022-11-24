package org.LPbigFish.Components;

import java.util.concurrent.Callable;

public class Mine implements Callable<Block> {
    private SubBlock block;
    private final long difficulty;

    public Mine(SubBlock block, long difficulty) {
        this.block = block;
        this.difficulty = difficulty;
    }
    @Override
    public Block call() {
        long timestamp = System.currentTimeMillis() * 1000;
        while (!(Long.parseLong(block.getBlockHash()) < difficulty)) {
            block = new SubBlock(block.index(), block.timestamp(), block.previousHash(), block.hash(), block.data(), block.nonce() + 1);
        }
        return new Block(block.index(), block.timestamp(), block.previousHash(), block.hash(), block.data(), block.nonce(), difficulty, block.timestamp() - timestamp);
    }
}
