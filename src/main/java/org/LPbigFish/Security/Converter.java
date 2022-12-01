package org.LPbigFish.Security;

import java.nio.charset.StandardCharsets;

public class Converter {

    private static final char[] base58 = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();

    public static String encode58Base(String input) {
        //https://youtu.be/GedV3S9X89c

        byte[] inputBytes = input.getBytes(StandardCharsets.US_ASCII);
        long baseValue = 0;
        int numberOfDivisions = 0;
        long temp = 0;

        for (int i = 0; i < inputBytes.length; i++)
            if (inputBytes[i] != 0) {
                baseValue += (inputBytes[i] * (Math.pow(2,(inputBytes.length - i - 1) * 8L)));
            }
        temp = baseValue;
        while (temp > 0) {
            temp /= 58;
            numberOfDivisions++;
        }

        byte[] base = new byte[numberOfDivisions];
        for (int i = 0; i < base.length; i++) {
            base[i] = (byte) (baseValue % 58);
            baseValue = baseValue / 58;
        }

        StringBuilder output = new StringBuilder();
        for (byte b : base) {
            output.append(base58[b]);
        }

        return output.toString();
    }


}
