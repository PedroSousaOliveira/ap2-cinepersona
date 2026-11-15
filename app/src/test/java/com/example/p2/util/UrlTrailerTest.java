package com.example.p2.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UrlTrailerTest {
    private static final String BASE = "https://www.youtube.com/results?search_query=trailer+";

    @Test
    public void codificaEspacosComoMais() {
        assertEquals(BASE + "Um+Sonho+de+Liberdade", UrlTrailer.gerar("Um Sonho de Liberdade"));
    }

    @Test
    public void codificaAcentosComoUtf8PercentEncoding() {
        assertEquals(BASE + "Amn%C3%A9sia", UrlTrailer.gerar("Amnésia"));
        assertEquals(BASE + "%C3%80+Procura+da+Felicidade",
                UrlTrailer.gerar("À Procura da Felicidade"));
    }

    @Test
    public void codificaDoisPontos() {
        assertEquals(BASE + "Rocky%3A+Um+Lutador", UrlTrailer.gerar("Rocky: Um Lutador"));
    }
}
