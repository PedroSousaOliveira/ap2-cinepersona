package com.example.p2.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.p2.data.remote.ApiErrorParser;
import com.example.p2.data.remote.ApiService;
import com.example.p2.data.remote.RetrofitClient;
import com.example.p2.domain.model.RecomendacaoRequest;
import com.example.p2.domain.model.ResultadoRecomendacao;
import com.example.p2.util.Resultado;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecomendacaoRepository {
    private final ApiService apiService;

    public RecomendacaoRepository() {
        this(RetrofitClient.getApiService());
    }

    public RecomendacaoRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resultado<ResultadoRecomendacao>> recomendar(RecomendacaoRequest request) {
        MutableLiveData<Resultado<ResultadoRecomendacao>> estado = new MutableLiveData<>();
        estado.setValue(Resultado.carregando());
        apiService.recomendar(request).enqueue(new Callback<ResultadoRecomendacao>() {
            @Override
            public void onResponse(
                    Call<ResultadoRecomendacao> call,
                    Response<ResultadoRecomendacao> resposta
            ) {
                if (resposta.isSuccessful()) {
                    estado.postValue(Resultado.sucesso(resposta.body()));
                } else {
                    estado.postValue(Resultado.erro(ApiErrorParser.extrairDetail(resposta.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<ResultadoRecomendacao> call, Throwable erro) {
                estado.postValue(Resultado.erro(erro.getMessage()));
            }
        });
        return estado;
    }
}
