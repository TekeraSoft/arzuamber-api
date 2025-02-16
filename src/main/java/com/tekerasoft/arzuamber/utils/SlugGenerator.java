package com.tekerasoft.arzuamber.utils;

import java.util.Locale;
import java.util.Random;

public class SlugGenerator {
    public static String generateSlug(String input) {
        String slug = input.toLowerCase(Locale.forLanguageTag("tr"));

        // Türkçe karakterleri dönüştürme
        slug = slug
                .replace("ç", "c")
                .replace("ğ", "g")
                .replace("ı", "i")
                .replace("ö", "o")
                .replace("ş", "s")
                .replace("ü", "u");

        // Harf, rakam ve boşluk dışındaki karakterleri kaldır
        slug = slug.replaceAll("[^a-z0-9\\s-]", "");

        // Birden fazla boşluğu tek bir "-" ile değiştir
        slug = slug.replaceAll("\\s+", "-");

        // Birden fazla "-" karakterini teke indir
        slug = slug.replaceAll("-+", "-");

        // Başı ve sonundaki boşlukları kırp
        slug = slug.trim();

        // Rastgele sayı ekleme
        Random random = new Random();
        int randomNumber = 10000 + random.nextInt(90000); // 10000 ile 99999 arasında rastgele sayı
        slug += "-" + randomNumber;

        return slug;
    }
}
