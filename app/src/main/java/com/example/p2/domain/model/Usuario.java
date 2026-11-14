package com.example.p2.domain.model;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    @SerializedName("lente_atual")
    private Lente lenteAtual;

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Lente getLenteAtual() {
        return lenteAtual;
    }
}
