package com.example.p2.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.p2.data.remote.ApiErrorParser;
import com.example.p2.data.remote.ApiService;
import com.example.p2.data.remote.RetrofitClient;
import com.example.p2.domain.model.Avaliacao;
import com.example.p2.domain.model.AvaliacaoAtualizarRequest;
import com.example.p2.domain.model.AvaliacaoRequest;
import com.example.p2.util.Resultado;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvaliacaoRepository {
    private final ApiService apiService;

    public AvaliacaoRepository() {
        this(RetrofitClient.getApiService());
    }

    public AvaliacaoRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resultado<Avaliacao>> criar(AvaliacaoRequest request) {
        MutableLiveData<Resultado<Avaliacao>> estado = new MutableLiveData<>();
        estado.setValue(Resultado.carregando());
        apiService.criarAvaliacao(request).enqueue(new Callback<Avaliacao>() {
            @Override
            public void onResponse(Call<Avaliacao> call, Response<Avaliacao> resposta) {
                publicarResposta(estado, resposta);
            }

            @Override
            public void onFailure(Call<Avaliacao> call, Throwable erro) {
                estado.postValue(Resultado.erro(erro.getMessage()));
            }
        });
        return estado;
    }

    public LiveData<Resultado<Avaliacao>> atualizar(
            int avaliacaoId, AvaliacaoAtualizarRequest request
    ) {
        MutableLiveData<Resultado<Avaliacao>> estado = new MutableLiveData<>();
        estado.setValue(Resultado.carregando());
        apiService.atualizarAvaliacao(avaliacaoId, request).enqueue(new Callback<Avaliacao>() {
            @Override
            public void onResponse(Call<Avaliacao> call, Response<Avaliacao> resposta) {
                publicarResposta(estado, resposta);
            }

            @Override
            public void onFailure(Call<Avaliacao> call, Throwable erro) {
                estado.postValue(Resultado.erro(erro.getMessage()));
            }
        });
        return estado;
    }

    public LiveData<Resultado<Void>> excluir(int avaliacaoId) {
        MutableLiveData<Resultado<Void>> estado = new MutableLiveData<>();
        estado.setValue(Resultado.carregando());
        apiService.excluirAvaliacao(avaliacaoId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> resposta) {
                if (resposta.isSuccessful()) {
                    estado.postValue(Resultado.sucesso(null));
                } else {
                    estado.postValue(Resultado.erro(ApiErrorParser.extrairDetail(resposta.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable erro) {
                estado.postValue(Resultado.erro(erro.getMessage()));
            }
        });
        return estado;
    }

    private void publicarResposta(
            MutableLiveData<Resultado<Avaliacao>> estado,
            Response<Avaliacao> resposta
    ) {
        if (resposta.isSuccessful()) {
            estado.postValue(Resultado.sucesso(resposta.body()));
        } else {
            estado.postValue(Resultado.erro(
                    ApiErrorParser.extrairDetail(resposta.errorBody()), resposta.code()));
        }
    }
}
