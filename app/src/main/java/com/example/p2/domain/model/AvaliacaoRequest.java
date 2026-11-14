package com.example.p2.domain.model;

import com.google.gson.annotations.SerializedName;

public class AvaliacaoRequest {
    @SerializedName("usuario_id")
    private final int usuarioId;
    @SerializedName("filme_id")
    private final int filmeId;
    private final int nota;
    private final String comentario;

    public AvaliacaoRequest(int usuarioId, int filmeId, int nota, String comentario) {
        this.usuarioId = usuarioId;
        this.filmeId = filmeId;
        this.nota = nota;
        this.comentario = comentario;
    }
}
