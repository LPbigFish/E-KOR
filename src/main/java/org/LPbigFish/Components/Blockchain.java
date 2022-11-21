package org.LPbigFish.Components;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain = new ArrayList<>();

    public Blockchain() {
        chain.add(new Block(0, System.currentTimeMillis(), "0", "0", "0", 0));
    }
}
