package org.LPbigFish.Components;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

public class Values {
    private static BigDecimal target = new BigDecimal(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16));

    private static final BigInteger TARGET_MAX = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16);

    private static long difficulty = 500000;

    public static String[] known_Peers = { "10.50.58.46" };




    public static void init() {
        target = target.divide(new BigDecimal(difficulty), 0, RoundingMode.HALF_UP);
    }

    public static void adjustDiff(List<Block> chain) {
        if (chain.size() % 10 == 0) {
            Block latestBlock = chain.get(chain.size() - 1);
            Block previousBlock = chain.get(chain.size() - 10);
            long time = latestBlock.timestamp() - previousBlock.timestamp();
            if (time > 10 * 40) {
                difficulty *= 0.8;
                System.out.println("Difficulty was multiplied by: " + 1.2 + "because of big downfall of time");
            }
        }
        if (chain.size() % 1024 == 0) {
            Block latestBlock = chain.get(chain.size() - 1);
            Block previousBlock = chain.get(chain.size() - 1024);

            double timeDiff = Math.round(((double) (latestBlock.timestamp() - previousBlock.timestamp()) / (40 * 1024)) * 100.0) / 100.0;
            if (timeDiff > 8)
                difficulty /= 8;
            else difficulty /= Math.max(timeDiff, 0.01);
            target = new BigDecimal(TARGET_MAX.divide(BigInteger.valueOf(difficulty)));

        }
         /*if (chain.size() > 1) {
            Block latestBlock = getLatestBlock();
            Block previousBlock = chain.get(chain.size() - 2);
            long prevDiff = TARGET_MAX.divide(new BigInteger(previousBlock.target())).longValue();

            difficulty = (long) (prevDiff + Math.round(prevDiff / 2048d) * Math.max(1 - (Math.round((latestBlock.timestamp() - previousBlock.timestamp())) / 20), -99) + Math.floor(2 ^ Math.round(latestBlock.index() / 100000d) - 2));
            target = new BigDecimal(TARGET_MAX.divide(BigInteger.valueOf(difficulty)));
        }*/
    }

    public static BigInteger getWorkValue(List<Block> chain) {
        BigDecimal work = new BigDecimal(new BigInteger("0"));
        BigInteger maxHash = new BigInteger("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", 16);
        for (Block block: chain) {
            BigInteger temp_target = (new BigInteger(block.target())).add(BigInteger.ONE);
            work = work.add(new BigDecimal(maxHash).divide(new BigDecimal(temp_target), RoundingMode.HALF_UP));
        }

        return work.toBigInteger();
    }

    public static BigDecimal getTarget() {
        return target;
    }

    public static long getDifficulty() {
        return difficulty;
    }
}
