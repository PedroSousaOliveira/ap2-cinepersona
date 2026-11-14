package com.example.p2.domain.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Filme implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private int ano;
    private String diretor;
    private String genero;
    @SerializedName("lente_alvo")
    private Lente lenteAlvo;
    @SerializedName("is_cult")
    private boolean isCult;
    @SerializedName("motivo_reconhecimento")
    private String motivoReconhecimento;
    private String sinopse;
    @SerializedName("match_percentual")
    private Integer matchPercentual;

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getAno() {
        return ano;
    }

    public String getDiretor() {
        return diretor;
    }

    public String getGenero() {
        return genero;
    }

    public Lente getLenteAlvo() {
        return lenteAlvo;
    }

    public boolean isCult() {
        return isCult;
    }

    public String getMotivoReconhecimento() {
        return motivoReconhecimento;
    }

    public String getSinopse() {
        return sinopse;
    }

    public Integer getMatchPercentual() {
        return matchPercentual;
    }
}
