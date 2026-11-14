package com.example.p2.domain.model;

public class UsuarioRequest {
    private final String nome;
    private final String email;

    public UsuarioRequest(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }
}
