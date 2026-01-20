package com.valdirsantos714.biblia.utils;

import java.text.Normalizer;

public final class TextoUtils {

    private TextoUtils() {}

    public static String normalizar(String texto) {
        if (texto == null) return null;

        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .trim();
    }
}
