package org.LPbigFish.Components;

import org.LPbigFish.Security.Hasher;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Blockchain {
    private final List<Block> chain = new ArrayList<>();

    public static Block blockBuffer = null;
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);

    public static boolean synced = true;

    public Blockchain() {
        Hasher.init();
        Values.init();
        addBlock(mine(new SubBlock(0, System.currentTimeMillis() / 1000L, "0000000000000000000000000000000000000000000000000000000000000000", "0", "Genesis Block", 0)));
        run();
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    private void addBlock(Block block) {
        block.printBlock();
        chain.add(block);
        Values.adjustDiff(chain);
        System.out.println("Work Value: " + Values.getWorkValue(chain)); //73967685 na indexu 10
    }

    public void run() {
        while (synced) {
            addBlock(mine(new SubBlock(getLatestBlock().index() + 1, System.currentTimeMillis() / 1000L, getLatestBlock().hash(), "0000000000000000000000000000000000000000000000000000000000000000", Math.random() + "", 0)));
            if (!isValid()) {
                synced = false;
                System.out.println("Blockchain is not valid! Please restart the program to resynchronise!");
            }
        }

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

    private void adjustAvgNonce() {
        if (chain.size() % 20 == 0) {
            Long[] nonces = new Long[20];
            for (int i = 0; i < 20; i++) {
                nonces[i] = chain.get(chain.size() - 20 + i).nonce();
            }
            long sum = 0;
            for (Long nonce : nonces) {
                sum += nonce;
            }

            long avgNonce = Math.round(sum / (double) nonces.length);
        }
    }

    private Block mine(SubBlock block) {
        Block newBlock = null;
        int cores = Runtime.getRuntime().availableProcessors() - 1;
        List<Future<Block>> futures = new ArrayList<>();
        //long startNonce = Math.round(avgNonce / (double) cores);
        /*for (int i = 0; i < cores; i++) {
            Mine mine;
            if (i == cores - 1) {
                mine = new Mine(block, Values.getTarget().toBigInteger().divide(BigInteger.valueOf(16)), i * startNonce, Long.MAX_VALUE);
            } else {
                mine = new Mine(block, Values.getTarget().toBigInteger().divide(BigInteger.valueOf(16)), i * startNonce, (i + 1) * startNonce);
            }
            futures.add(executor.submit(mine));
        }*/

        futures.add(executor.submit(new Mine(block, Values.getTarget().toBigInteger().divide(BigInteger.valueOf(cores)), 0, Long.MAX_VALUE)));
        while (newBlock == null || blockBuffer != null) {
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
        if(chain.size() != 0) {
            newBlock = new Block(newBlock.index(), newBlock.timestamp(), newBlock.previousHash(), newBlock.hash(), newBlock.data(), newBlock.nonce(), newBlock.target(), newBlock.timestamp() - getLatestBlock().timestamp());
        }
        if (blockBuffer != null) {
            newBlock = blockBuffer;
            blockBuffer = null;
        }
        return newBlock;
    }

    public void printTheChain() {
        System.out.println(Hasher.jsonConverter(chain));
    }
}
