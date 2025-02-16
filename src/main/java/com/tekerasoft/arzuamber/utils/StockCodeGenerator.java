package com.tekerasoft.arzuamber.utils;

import java.security.SecureRandom;

public class StockCodeGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateStockCode(int length) {
        StringBuilder stokKodu = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stokKodu.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return stokKodu.toString();
    }

}
