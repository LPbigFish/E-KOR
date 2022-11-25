package org.LPbigFish.Components;

import org.LPbigFish.Security.Hasher;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private static BigInteger difficulty = new BigInteger("0001F30000000000000000000000000000000000000000000000000000000000", 16);
    private final List<Block> chain = new ArrayList<>();

    public Blockchain() {
        addBlock(new Block(0, System.currentTimeMillis() / 1000L, "0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000", "0", 0, "0", 60));
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Block block) {
        block.printBlock();
        chain.add(block);
        isValid();
        adjustDiff();
        mine(new SubBlock(block.index() + 1, System.currentTimeMillis() / 1000L, block.hash(), "0000000000000000000000000000000000000000000000000000000000000000", "0", 0));
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
        if (chain.size() % 20 == 0) {
            Block latestBlock = getLatestBlock();
            Block previousBlock = chain.get(chain.size() - 20);

            double timeDiff = (latestBlock.timestamp() - previousBlock.timestamp()) / (20 * 60);
            difficulty = difficulty.multiply(new BigDecimal(timeDiff).toBigInteger());

            System.out.println(difficulty);
        }
    }

    private void mine(SubBlock block) {
        Block newBlock = null;
        Mine mine = new Mine(block, difficulty);

        try {
            newBlock = mine.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addBlock(newBlock);
    }

    public void printTheChain() {
        System.out.println(Hasher.jsonConverter(chain));
    }
}
