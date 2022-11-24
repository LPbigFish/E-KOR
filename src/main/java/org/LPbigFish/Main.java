package org.LPbigFish;

import org.LPbigFish.Components.Blockchain;
import org.LPbigFish.Security.Hasher;

import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random r = new Random();
        Hasher.init();

        String difficulty = "0001F30000000000000000000000000000000000000000000000000000000000";


        //testing a Big Number parser for multiplication

        int zeros = 0;
        Double diff_compressed = null;
        for (int i = difficulty.length(); i > 0; i--) {
            if (difficulty.charAt(i - 1) == '0') {
                zeros++;
            } else {
                diff_compressed = (double) Long.parseLong(difficulty.substring(0, i - 1), 16);


                diff_compressed *= 0.33333333333333333333333333333333;

                //round diff_compressed to the nearest 0.001
                diff_compressed = Math.round(diff_compressed * 1000.0) / 1000.0;

                break;

                //make the difficulty adapt to deviation
            }
        }
        System.out.println(diff_compressed);
    }
}