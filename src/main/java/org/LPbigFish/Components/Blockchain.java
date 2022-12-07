package org.LPbigFish.Components;

import org.LPbigFish.Security.Hasher;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Blockchain {
    private static long difficulty = 500000;
    private static BigDecimal target = new BigDecimal(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16));

    private static final BigInteger TARGET_MAX = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16);
    private static long avgNonce = 20000000L;
    private final List<Block> chain = new ArrayList<>();



    public Blockchain() {
        target = target.divide(new BigDecimal(difficulty), 0, RoundingMode.HALF_UP);
        addBlock(mine(new SubBlock(0, System.currentTimeMillis() / 1000L, "0000000000000000000000000000000000000000000000000000000000000000", "0", "Genesis Block", 0)));
        run();
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    private void addBlock(Block block) {
        block.printBlock();
        chain.add(block);
        adjustDiff();
        System.out.println("Work Value: " + getWorkValue()); //73967685 na indexu 10
    }

    public void run() {
        while (true) {
            addBlock(mine(new SubBlock(getLatestBlock().index() + 1, System.currentTimeMillis() / 1000L, getLatestBlock().hash(), "0000000000000000000000000000000000000000000000000000000000000000", Math.random() + "", 0)));
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

    private void adjustDiff() {
        /*if (chain.size() % 20 == 0) {
            Block latestBlock = getLatestBlock();
            Block previousBlock = chain.get(chain.size() - 20);

            double timeDiff = Math.round(((double) (latestBlock.timestamp() - previousBlock.timestamp()) / (300 * 20)) * 100.0) / 100.0;
            difficulty /= timeDiff;
            target = new BigDecimal(TARGET_MAX.divide(BigInteger.valueOf(difficulty)));
            System.out.println("Difficulty was multiplied by: " + timeDiff);
            adjustAvgNonce();
        }*/
         if (chain.size() > 1) {
            Block latestBlock = getLatestBlock();
            Block previousBlock = chain.get(chain.size() - 2);
            long prevDiff = TARGET_MAX.divide(new BigInteger(previousBlock.target())).longValue();

            difficulty = (long) (prevDiff + Math.round(prevDiff / 2048d) * Math.max(1 - (Math.round((latestBlock.timestamp() - previousBlock.timestamp())) / 10), -99) + Math.floor(2 ^ Math.round(latestBlock.index() / 100000d) - 2));
            target = new BigDecimal(TARGET_MAX.divide(BigInteger.valueOf(difficulty)));
        }
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

            avgNonce = Math.round(sum / (double) nonces.length);
        }
    }

    private Block mine(SubBlock block) {
        Block newBlock = null;
        int cores = Runtime.getRuntime().availableProcessors() - 2;
        ExecutorService executor = Executors.newFixedThreadPool(cores);
        List<Future<Block>> futures = new ArrayList<>();
        long startNonce = Math.round(avgNonce / (double) cores);
        /*for (int i = 0; i < cores; i++) {
            Mine mine;
            if (i == cores - 1) {
                mine = new Mine(block, difficulty.toBigInteger(), i * startNonce, Long.MAX_VALUE);
            } else {
                mine = new Mine(block, difficulty.toBigInteger(), i * startNonce, (i + 1) * startNonce);
            }
            futures.add(executor.submit(mine));
        }*/

        futures.add(executor.submit(new Mine(block, target.toBigInteger(), 0, Long.MAX_VALUE)));
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
        if(chain.size() != 0)
            newBlock = new Block(newBlock.index(), newBlock.timestamp(), newBlock.previousHash(), newBlock.hash(), newBlock.data(), newBlock.nonce(), newBlock.target(), newBlock.timestamp() - getLatestBlock().timestamp());
        return newBlock;
    }

    public BigInteger getWorkValue() {
        BigDecimal work = new BigDecimal(new BigInteger("0"));
        BigInteger maxHash = new BigInteger("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", 16);
        for (Block block: chain) {
            BigInteger target = new BigInteger(block.target()).add(BigInteger.ONE);
            work = work.add(new BigDecimal(maxHash).divide(new BigDecimal(target), RoundingMode.HALF_UP));
        }

        return work.toBigInteger();
    }

    public void printTheChain() {
        System.out.println(Hasher.jsonConverter(chain));
    }
}
