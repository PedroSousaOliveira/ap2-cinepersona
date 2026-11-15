package com.example.p2.util;

public final class TextoCompartilhamento {
    private TextoCompartilhamento() {
    }

    public static String gerar(String titulo, int matchPercentual) {
        return "Meu match no CinePersona: " + titulo + " (" + matchPercentual + "%)";
    }
}
