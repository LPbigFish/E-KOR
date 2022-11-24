package org.LPbigFish.Components;

import org.LPbigFish.Security.Hasher;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class Mine implements Callable<Block> {
    private SubBlock block;
    private final String difficulty;

    public Mine(SubBlock block, String difficulty) {
        this.block = block;
        this.difficulty = difficulty;
    }
    @Override
    public Block call() {
        long timestamp = System.currentTimeMillis() * 1000;
        while (!(Arrays.compare(Hasher.hashToByteArray(block.hash()), Hasher.hashToByteArray(difficulty)) >= 0)) {
            block = new SubBlock(block.index(), block.timestamp(), block.previousHash(), block.hash(), block.data(), block.nonce() + 1);
        }
        return new Block(block.index(), block.timestamp(), block.previousHash(), block.hash(), block.data(), block.nonce(), difficulty, block.timestamp() - timestamp);
    }
}
