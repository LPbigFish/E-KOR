package org.LPbigFish;

import org.LPbigFish.Security.Hasher;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random r = new Random();
        while (true) {


            Hasher.toString(Hasher.toSha256(r.nextLong() + ""));
            Hasher.toString(Hasher.doubleSha256(r.nextLong() + ""));
        }
    }
}