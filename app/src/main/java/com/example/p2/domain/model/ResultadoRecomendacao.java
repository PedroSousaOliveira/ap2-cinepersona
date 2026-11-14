package com.example.p2.domain.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResultadoRecomendacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private Lente lente;
    @SerializedName("nome_lente")
    private String nomeLente;
    @SerializedName("descricao_lente")
    private String descricaoLente;
    @SerializedName("reconhecimento_percentual")
    private int reconhecimentoPercentual;
    private List<Filme> filmes;

    public Lente getLente() {
        return lente;
    }

    public String getNomeLente() {
        return nomeLente;
    }

    public String getDescricaoLente() {
        return descricaoLente;
    }

    public int getReconhecimentoPercentual() {
        return reconhecimentoPercentual;
    }

    public List<Filme> getFilmes() {
        return filmes;
    }
}
