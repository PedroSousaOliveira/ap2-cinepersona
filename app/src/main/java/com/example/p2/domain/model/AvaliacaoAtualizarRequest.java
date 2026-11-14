package com.example.p2.domain.model;

public class AvaliacaoAtualizarRequest {
    private final int nota;
    private final String comentario;

    public AvaliacaoAtualizarRequest(int nota, String comentario) {
        this.nota = nota;
        this.comentario = comentario;
    }
}
