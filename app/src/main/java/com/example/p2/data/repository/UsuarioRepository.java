package com.example.p2.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.p2.data.remote.ApiErrorParser;
import com.example.p2.data.remote.ApiService;
import com.example.p2.data.remote.RetrofitClient;
import com.example.p2.domain.model.Avaliacao;
import com.example.p2.domain.model.Usuario;
import com.example.p2.domain.model.UsuarioRequest;
import com.example.p2.util.Resultado;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepository {
    private final ApiService apiService;

    public UsuarioRepository() {
        this(RetrofitClient.getApiService());
    }

    public UsuarioRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resultado<Usuario>> criarOuObter(UsuarioRequest request) {
        MutableLiveData<Resultado<Usuario>> estado = new MutableLiveData<>();
        estado.setValue(Resultado.carregando());
        apiService.criarOuObterUsuario(request).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> resposta) {
                if (resposta.isSuccessful()) {
                    estado.postValue(Resultado.sucesso(resposta.body()));
                } else {
                    estado.postValue(Resultado.erro(ApiErrorParser.extrairDetail(resposta.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable erro) {
                estado.postValue(Resultado.erro(erro.getMessage()));
            }
        });
        return estado;
    }

    public LiveData<Resultado<List<Avaliacao>>> listarAvaliacoes(int usuarioId) {
        MutableLiveData<Resultado<List<Avaliacao>>> estado = new MutableLiveData<>();
        estado.setValue(Resultado.carregando());
        apiService.listarAvaliacoes(usuarioId).enqueue(new Callback<List<Avaliacao>>() {
            @Override
            public void onResponse(Call<List<Avaliacao>> call, Response<List<Avaliacao>> resposta) {
                if (resposta.isSuccessful()) {
                    estado.postValue(Resultado.sucesso(resposta.body()));
                } else {
                    estado.postValue(Resultado.erro(ApiErrorParser.extrairDetail(resposta.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<List<Avaliacao>> call, Throwable erro) {
                estado.postValue(Resultado.erro(erro.getMessage()));
            }
        });
        return estado;
    }
}
