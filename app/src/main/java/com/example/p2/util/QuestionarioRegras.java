package com.example.p2.util;

import com.example.p2.domain.model.Lente;

public final class QuestionarioRegras {
    private static final Lente[] LENTES = Lente.values();

    private QuestionarioRegras() {
    }

    public static Lente lenteDaPosicao(int posicao) {
        if (posicao < 0 || posicao >= LENTES.length) {
            throw new IllegalArgumentException();
        }
        return LENTES[posicao];
    }

    public static Lente lenteDoClima(int posicaoSpinner) {
        if (posicaoSpinner == 0) {
            throw new IllegalArgumentException();
        }
        return lenteDaPosicao(posicaoSpinner - 1);
    }

    public static boolean botaoHabilitado(int posicaoMomento, int posicaoClima) {
        return posicaoValida(posicaoMomento) && posicaoClima > 0
                && posicaoClima <= LENTES.length;
    }

    private static boolean posicaoValida(int posicao) {
        return posicao >= 0 && posicao < LENTES.length;
    }
}
