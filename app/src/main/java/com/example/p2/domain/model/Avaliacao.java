package com.example.p2.domain.model;

import com.google.gson.annotations.SerializedName;

public class Avaliacao {
    private int id;
    @SerializedName("usuario_id")
    private int usuarioId;
    @SerializedName("filme_id")
    private int filmeId;
    private int nota;
    private String comentario;
    private Filme filme;

    public int getId() {
        return id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public int getFilmeId() {
        return filmeId;
    }

    public int getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public Filme getFilme() {
        return filme;
    }
}
