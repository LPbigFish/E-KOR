package org.LPbigFish.Components;

import org.LPbigFish.Security.Hasher;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Blockchain {

    private static BigDecimal difficulty = new BigDecimal(new BigInteger("27eb851eb851ec000000000000000000000000000000000000000000000", 16));
    private static long avgNonce = 20000000L;
    private final List<Block> chain = new ArrayList<>();

    public Blockchain() {
        addBlock(new Block(0, System.currentTimeMillis() / 1000L, "0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000", "0", 0, difficulty.toBigInteger().toString(), 60));
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

            double timeDiff = Math.round(((double) (latestBlock.timestamp() - previousBlock.timestamp()) / (20 * 60)) * 100.0) / 100.0;
            difficulty = difficulty.multiply(new BigDecimal(timeDiff));

            System.out.println(timeDiff);
        }
    }

    private void adjustAvgNonce() {
        if (chain.size() % 20 == 0) {
            Long[] nonces = new Long[20];
            for (int i = 0; i < 20; i++) {
                nonces[i] = chain.get(chain.size() - 20 + i).nonce();
            }
            long sum = 0;
            for (int i = 0; i < nonces.length; i++) {
                sum += nonces[i];
            }

            avgNonce = Math.round(sum / nonces.length);
        }
    }

    private void mine(SubBlock block) {
        Block newBlock = null;
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
        List<Future<Block>> futures = new ArrayList<>();
        long startNonce = Math.round(avgNonce / Runtime.getRuntime().availableProcessors() - 1);
        for (int i = 0; i < Runtime.getRuntime().availableProcessors() - 1; i++) {
            Mine mine = null;
            if (i == Runtime.getRuntime().availableProcessors() - 2) {
                mine = new Mine(block, difficulty.toBigInteger(), i * startNonce, Long.MAX_VALUE);
            } else {
                mine = new Mine(block, difficulty.toBigInteger(), i * startNonce, (i + 1) * startNonce);
            }
            futures.add(executor.submit(mine));
        }

        while (newBlock == null) {
            for (Future<Block> future : futures) {
                if (future.isDone()) {
                    try {
                        newBlock = future.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for (Future<Block> future : futures) {
            future.cancel(true);
        }
        executor.shutdown();
        addBlock(newBlock);
    }

    public void printTheChain() {
        System.out.println(Hasher.jsonConverter(chain));
    }
}
