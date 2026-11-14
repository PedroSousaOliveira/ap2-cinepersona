package com.example.p2.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.p2.data.remote.ApiErrorParser;
import com.example.p2.data.remote.ApiService;
import com.example.p2.data.remote.RetrofitClient;
import com.example.p2.domain.model.Filme;
import com.example.p2.util.Resultado;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmeRepository {
    private final ApiService apiService;

    public FilmeRepository() {
        this(RetrofitClient.getApiService());
    }

    public FilmeRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resultado<List<Filme>>> listar(String q) {
        MutableLiveData<Resultado<List<Filme>>> estado = new MutableLiveData<>();
        estado.setValue(Resultado.carregando());
        apiService.listarFilmes(q).enqueue(new Callback<List<Filme>>() {
            @Override
            public void onResponse(Call<List<Filme>> call, Response<List<Filme>> resposta) {
                if (resposta.isSuccessful()) {
                    estado.postValue(Resultado.sucesso(resposta.body()));
                } else {
                    estado.postValue(Resultado.erro(ApiErrorParser.extrairDetail(resposta.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<Filme>> call, Throwable erro) {
                estado.postValue(Resultado.erro(erro.getMessage()));
            }
        });
        return estado;
    }
}
