package com.example.p2.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class UrlTrailer {
    private static final String BASE = "https://www.youtube.com/results?search_query=trailer+";

    private UrlTrailer() {
    }

    public static String gerar(String titulo) {
        try {
            return BASE + URLEncoder.encode(titulo, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException erro) {
            // UTF-8 é obrigatório na plataforma; a exceção verificada nunca ocorre.
            throw new IllegalStateException(erro);
        }
    }
}
