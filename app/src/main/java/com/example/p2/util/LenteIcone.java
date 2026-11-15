package com.example.p2.util;

import com.example.p2.R;
import com.example.p2.domain.model.Lente;

public final class LenteIcone {
    private LenteIcone() {
    }

    public static int drawable(Lente lente) {
        switch (lente) {
            case ROTEIRO_DE_VIRADA:
                return R.drawable.ic_lente_roteiro;
            case PLANO_SEQUENCIA_AFETIVO:
                return R.drawable.ic_lente_afetivo;
            case CORTE_SECO_ADRENALINA:
                return R.drawable.ic_lente_adrenalina;
            case FORA_DE_QUADRO:
                return R.drawable.ic_lente_fora_de_quadro;
            default:
                throw new IllegalArgumentException();
        }
    }
}
