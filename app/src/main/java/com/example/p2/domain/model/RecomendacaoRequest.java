package com.example.p2.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecomendacaoRequest {
    @SerializedName("usuario_id")
    private final int usuarioId;
    private final Lente momento;
    private final Lente clima;
    private final List<Lente> prioridades;
    @SerializedName("modo_lado_b")
    private final boolean modoLadoB;

    public RecomendacaoRequest(
            int usuarioId,
            Lente momento,
            Lente clima,
            List<Lente> prioridades,
            boolean modoLadoB
    ) {
        this.usuarioId = usuarioId;
        this.momento = momento;
        this.clima = clima;
        this.prioridades = prioridades;
        this.modoLadoB = modoLadoB;
    }
}
