package com.example.p2.data.remote;

import com.example.p2.domain.model.Avaliacao;
import com.example.p2.domain.model.AvaliacaoAtualizarRequest;
import com.example.p2.domain.model.AvaliacaoRequest;
import com.example.p2.domain.model.Filme;
import com.example.p2.domain.model.RecomendacaoRequest;
import com.example.p2.domain.model.ResultadoRecomendacao;
import com.example.p2.domain.model.Usuario;
import com.example.p2.domain.model.UsuarioRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("usuarios")
    Call<Usuario> criarOuObterUsuario(@Body UsuarioRequest request);

    @GET("filmes")
    Call<List<Filme>> listarFilmes(@Query("q") String q);

    @POST("recomendar")
    Call<ResultadoRecomendacao> recomendar(@Body RecomendacaoRequest request);

    @POST("avaliacoes")
    Call<Avaliacao> criarAvaliacao(@Body AvaliacaoRequest request);

    @GET("usuarios/{usuario_id}/avaliacoes")
    Call<List<Avaliacao>> listarAvaliacoes(@Path("usuario_id") int usuarioId);

    @PUT("avaliacoes/{avaliacao_id}")
    Call<Avaliacao> atualizarAvaliacao(
            @Path("avaliacao_id") int avaliacaoId,
            @Body AvaliacaoAtualizarRequest request
    );

    @DELETE("avaliacoes/{avaliacao_id}")
    Call<Void> excluirAvaliacao(@Path("avaliacao_id") int avaliacaoId);
}
