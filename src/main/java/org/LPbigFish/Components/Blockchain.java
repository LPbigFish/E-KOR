package org.LPbigFish.Components;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Blockchain {

    private static long difficulty = 4;

    private final List<Block> chain = new ArrayList<>();

    public Blockchain() {
        addBlock(new Block(0, System.currentTimeMillis(), "0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000", "0", 0, 0, 60));
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Block block) {
        chain.add(block);
        isValid();
        adjustDiff();

    }

    public boolean isValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);
            if (!currentBlock.isValid() || !currentBlock.previousHash().equals(previousBlock.hash()) || currentBlock.index() != previousBlock.index() + 1 || currentBlock.timestamp() <= previousBlock.timestamp()) {
                return false;
            }
        }
        return true;
    }

    private void adjustDiff() {
        if (chain.size() % 200 == 0) {
            Block latestBlock = getLatestBlock();
            Block previousBlock = chain.get(chain.size() - 200);
            difficulty = difficulty * (latestBlock.blockTime() / previousBlock.blockTime());
        }
    }

    private void mine(SubBlock block) {
        Block newBlock = null;
        Mine mine = new Mine(block, difficulty);
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<Block> futureBlock = pool.submit(mine);
        try {
            newBlock = futureBlock.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addBlock(newBlock);
    }


}
