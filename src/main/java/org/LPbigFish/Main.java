package org.LPbigFish;

import org.LPbigFish.Components.Blockchain;
import org.LPbigFish.Security.Converter;
import org.LPbigFish.Security.Hasher;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random r = new Random();
        Hasher.init();

        Blockchain blockchain = new Blockchain();
    }
}