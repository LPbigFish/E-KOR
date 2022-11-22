package org.LPbigFish.Components;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private final List<Block> chain = new ArrayList<>();

    public Blockchain() {
        chain.add(new Block(0, System.currentTimeMillis(), "0", "0", "0", 0));
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Block block) {
        chain.add(block);
        isValid();
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
}
