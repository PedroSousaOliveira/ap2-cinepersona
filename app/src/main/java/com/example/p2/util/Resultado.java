package com.example.p2.util;

public class Resultado<T> {
    public enum Estado {
        CARREGANDO,
        SUCESSO,
        ERRO
    }

    private final Estado estado;
    private final T valor;
    private final String mensagem;

    private Resultado(Estado estado, T valor, String mensagem) {
        this.estado = estado;
        this.valor = valor;
        this.mensagem = mensagem;
    }

    public static <T> Resultado<T> carregando() {
        return new Resultado<>(Estado.CARREGANDO, null, null);
    }

    public static <T> Resultado<T> sucesso(T valor) {
        return new Resultado<>(Estado.SUCESSO, valor, null);
    }

    public static <T> Resultado<T> erro(String mensagem) {
        return new Resultado<>(Estado.ERRO, null, mensagem);
    }

    public Estado getEstado() {
        return estado;
    }

    public T getValor() {
        return valor;
    }

    public String getMensagem() {
        return mensagem;
    }
}
